package xyz.ibudai.dailyword.socket.adaptor.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import xyz.ibudai.dailyword.basic.encrypt.AESUtil;
import xyz.ibudai.dailyword.model.base.ResponseData;
import xyz.ibudai.dailyword.model.dto.TaskWordDTO;
import xyz.ibudai.dailyword.model.vo.match.MatchVo;
import xyz.ibudai.dailyword.model.dto.RoomDTO;
import xyz.ibudai.dailyword.repository.service.MatchRecordService;
import xyz.ibudai.dailyword.server.service.WordService;
import xyz.ibudai.dailyword.socket.adaptor.ChannelAdaptor;
import xyz.ibudai.dailyword.socket.consts.BeanConst;
import xyz.ibudai.dailyword.socket.enums.Protocol;
import xyz.ibudai.dailyword.socket.manager.ChannelManager;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static xyz.ibudai.dailyword.model.enums.socket.SocketStatus.*;

@Slf4j
@ChannelHandler.Sharable
@Component(BeanConst.Handler.RANK)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RankHandler extends ChannelAdaptor {

    private final ObjectMapper objectMapper;

    private final WordService wordService;
    private final MatchRecordService matchRecordService;


    private final static Cache<String, Set<Integer>> RANK_POOL = Caffeine.newBuilder()
            // 五分钟未匹配上失效
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .initialCapacity(12)
            .maximumSize(1024)
            .build();


    @Override
    public Protocol getProtocol() {
        return Protocol.RANK;
    }

    /**
     * 接收消息事件
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame frame) throws Exception {
        String receiveText = frame.text();
        if (StringUtils.isBlank(receiveText)) {
            return;
        }
        // 数据需解密
        receiveText = AESUtil.desEncrypt(receiveText).trim();

        this.matchGame(ctx, receiveText);
    }

    private synchronized void matchGame(ChannelHandlerContext ctx, String receiveText) throws JsonProcessingException {
        Channel channel = ctx.channel();
        String key = UUID.nameUUIDFromBytes(receiveText.getBytes()).toString();
        Integer uid = ChannelManager.getUid(channel.id().asLongText());
        Set<Integer> users = RANK_POOL.getIfPresent(key);
        if (CollectionUtils.isEmpty(users)) {
            // 第一个建房间，存入信息并返回匹配中
            log.info("create rank match, userId: {}", uid);
            this.waiting(key, channel, users);
            return;
        }
        if (users.contains(uid)) {
            // 同一用户重复提交
            log.info("user {} already submit", uid);
            super.response(channel, RANK_MATCHING);
            return;
        }
        users.add(uid);
        RoomDTO roomDTO = objectMapper.readValue(receiveText, RoomDTO.class);
        if (users.size() != roomDTO.getRoomSize()) {
            // 人数不足，放入缓存继续等待
            this.waiting(key, channel, users);
            return;
        }

        // 广播对应匹配的用户组
        List<Channel> channelList = new ArrayList<>();
        for (Integer matchedUid : users) {
            Channel matchedChannel = ChannelManager.getChannel(Protocol.RANK, matchedUid);
            if (Objects.isNull(matchedChannel)) {
                continue;
            }
            channelList.add(matchedChannel);
        }
        log.info("match success, userId: {}, channel size: {}", uid, channelList.size());
        ChannelManager.batchSend(channelList, this.handlerSuccess(uid, users, roomDTO));

        // 成功后移除匹配
        RANK_POOL.asMap().computeIfPresent(key, (k, v) -> {
            v.removeAll(users);
            return v;
        });
        log.info("match success, after remove key: {}, users: {}", key, RANK_POOL.getIfPresent(key));
    }

    private String handlerSuccess(Integer uid, Set<Integer> users, RoomDTO roomDTO) throws JsonProcessingException {
        log.info("match success, userId: {}, list: {}", uid, users);
        String matchId = UUID.randomUUID().toString();

        // 记录匹配信息
        matchRecordService.initRecord(matchId, users, roomDTO);

        // 返回对局数据内容
        ResponseData res = new ResponseData(RANK_MATCHED.getCode());
        List<TaskWordDTO> taskWords = wordService.getTaskContent(roomDTO.getCatalogue(), roomDTO.getSize());
        res.setData(new MatchVo(matchId, taskWords));
        return objectMapper.writeValueAsString(res);
    }

    private void waiting(String key, Channel channel, Set<Integer> users) throws JsonProcessingException {
        users = Objects.isNull(users) ? new HashSet<>() : users;
        users.add(ChannelManager.getUid(channel.id().asLongText()));
        RANK_POOL.put(key, users);

        // 发送消息
        super.response(channel, RANK_MATCHING);
    }
}
