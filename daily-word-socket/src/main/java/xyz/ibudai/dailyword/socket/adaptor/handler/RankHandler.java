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
import xyz.ibudai.dailyword.model.vo.match.RoomVo;
import xyz.ibudai.dailyword.server.service.WordService;
import xyz.ibudai.dailyword.socket.adaptor.ChannelAdaptor;
import xyz.ibudai.dailyword.socket.consts.BeanConst;
import xyz.ibudai.dailyword.socket.enums.Protocol;
import xyz.ibudai.dailyword.socket.manager.ChannelManager;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static xyz.ibudai.dailyword.model.enums.socket.SocketStatus.*;

@Slf4j
@ChannelHandler.Sharable
@Component(BeanConst.Handler.RANK)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RankHandler extends ChannelAdaptor {

    private final WordService wordService;
    private final ObjectMapper objectMapper;


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
        String key = UUID.nameUUIDFromBytes(receiveText.getBytes()).toString();
        Set<Integer> userList = RANK_POOL.getIfPresent(key);
        if (CollectionUtils.isEmpty(userList)) {
            // 第一个建房间，存入信息并返回匹配中
            this.waiting(key, ctx.channel(), userList);
            return;
        }

        RoomVo roomVo = objectMapper.readValue(receiveText, RoomVo.class);
        if (userList.size() + 1 == roomVo.getRoomSize()) {
            // 用户匹配房间人数，返回数据开始对局
            ResponseData res = new ResponseData(RANK_MATCHED.getCode());
            res.setData(wordService.getTaskContent(roomVo.getCatalogue(), roomVo.getSize()));
            ChannelManager.send(ctx.channel(), objectMapper.writeValueAsString(res));

            // 成功后移除匹配
            RANK_POOL.asMap().computeIfPresent(key, (k, v) -> {
                v.removeAll(userList);
                return v;
            });
            return;
        }

        // 人数不足，放入缓存继续等待
        this.waiting(key, ctx.channel(), userList);
    }


    private void waiting(String key, Channel channel, Set<Integer> userList) throws JsonProcessingException {
        userList = Objects.isNull(userList) ? new HashSet<>() : userList;
        userList.add(ChannelManager.getUid(channel.id().asLongText()));
        RANK_POOL.put(key, userList);

        // 发送消息
        ResponseData res = new ResponseData(RANK_MATCHING.getCode());
        ChannelManager.send(channel, objectMapper.writeValueAsString(res));
    }
}
