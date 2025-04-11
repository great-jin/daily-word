package xyz.ibudai.dailyword.server.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import xyz.ibudai.dailyword.model.config.SystemConfig;
import xyz.ibudai.dailyword.model.entity.MatchDetail;
import xyz.ibudai.dailyword.model.entity.MatchRecord;
import xyz.ibudai.dailyword.model.entity.RankBoard;
import xyz.ibudai.dailyword.model.entity.UserDetail;
import xyz.ibudai.dailyword.model.vo.board.RankBoardVo;
import xyz.ibudai.dailyword.oss.util.OssServer;
import xyz.ibudai.dailyword.repository.dao.RankBoardDao;
import xyz.ibudai.dailyword.repository.util.SecurityUtil;
import xyz.ibudai.dailyword.server.service.AuthUserService;
import xyz.ibudai.dailyword.server.service.MatchDetailService;
import xyz.ibudai.dailyword.server.service.RankBoardService;
import xyz.ibudai.dailyword.server.service.UserDetailService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * (RankBoard)表服务实现类
 *
 * @author budai
 * @since 2025-03-16 09:26:04
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RankBoardServiceImpl extends ServiceImpl<RankBoardDao, RankBoard> implements RankBoardService {

    private final OssServer ossServer;

    private final AuthUserService authUserService;
    private final UserDetailService userDetailService;
    private final MatchDetailService matchDetailService;


    @Override
    public RankBoard getUserRank(Integer userId) {
        if (Objects.isNull(userId)) {
            return RankBoard.init();
        }
        RankBoard rankBoard = new RankBoard();
        List<RankBoard> list = this.lambdaQuery()
                .eq(RankBoard::getSeason, SystemConfig.getSeason())
                .eq(RankBoard::getUserId, userId)
                .list();
        if (CollectionUtils.isEmpty(list)) {
            return RankBoard.init();
        }

        int score = 0, win = 0, lost = 0;
        for (RankBoard item : list) {
            score += item.getScore();
            win += item.getMatchWin();
            lost += item.getMatchLost();
        }
        rankBoard.setSeason(SystemConfig.getSeason());
        rankBoard.setScore(score);
        rankBoard.setMatchWin(win);
        rankBoard.setMatchLost(lost);
        return rankBoard;
    }

    @Override
    public List<RankBoardVo> listByCatalog(String catalog) {
        Integer userId = SecurityUtil.getLoginUser();
        UserDetail userDetail = userDetailService.getById(userId);

        // 排行榜数据
        int index = 1;
        boolean containBoard = false;
        List<RankBoardVo> list = this.baseMapper.listTopFifty(catalog, SystemConfig.getSeason());
        for (RankBoardVo item : list) {
            if (Objects.equals(item.getUserName(), userDetail.getUserName())) {
                containBoard = true;
            }
            item.setIndex(String.valueOf(index++));
            item.setAvatarUrl(ossServer.signAvatarUrl(item.getAvatarUrl()));
        }
        if (containBoard) {
            return list;
        }

        // 前 50 不包含自身且存在匹配记录
        RankBoard userRank = this.lambdaQuery()
                .eq(RankBoard::getSeason, SystemConfig.getSeason())
                .eq(RankBoard::getCatalog, catalog)
                .eq(RankBoard::getUserId, userId)
                .one();
        if (Objects.isNull(userRank)) {
            return list;
        }

        // 构建 VO 对象
        RankBoardVo boardVo = new RankBoardVo();
        boardVo.setIndex("-");
        boardVo.setUserName(userDetail.getUserName());
        boardVo.setMatchCount(userRank.getMatchCount());
        boardVo.setScore(userRank.getScore());
        boardVo.setAvatarUrl(ossServer.signAvatarUrl(userDetail.getAvatar()));
        list.add(boardVo);
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRankBoard(List<MatchRecord> recordList) {
        if (CollectionUtils.isEmpty(recordList)) {
            return;
        }
        MatchDetail matchDetail = matchDetailService.getById(recordList.get(0).getMatchId());
        String catalog = null;
        Map<Integer, MatchRecord> recordMap = new HashMap<>();
        for (MatchRecord record : recordList) {
            recordMap.put(record.getUserId(), record);
            if (StringUtils.isBlank(catalog)) {
                catalog = matchDetail.getCatalog();
            }
        }

        Map<Integer, String> userMap = authUserService.groupByIds(recordMap.keySet());
        // 查询用户排行榜
        List<RankBoard> userRankList = this.lambdaQuery()
                .eq(RankBoard::getScore, SystemConfig.getSeason())
                .eq(RankBoard::getCatalog, catalog)
                .in(RankBoard::getUserId, recordMap.keySet())
                .list();
        Map<Integer, RankBoard> rankBoardMap = userRankList.stream()
                .collect(Collectors.toMap(RankBoard::getUserId, it -> it, (o, r) -> o));
        // 赛季未匹配过
        Set<Integer> newUserIds = new HashSet<>();
        List<RankBoard> newRankBoardList = new ArrayList<>();
        for (Integer uid : recordMap.keySet()) {
            RankBoard rankBoard = rankBoardMap.get(uid);
            MatchRecord matchRecord = recordMap.get(uid);
            if (Objects.nonNull(rankBoard) || Objects.isNull(matchRecord)) {
                // 存在季赛记录
                continue;
            }

            newUserIds.add(uid);
            rankBoard = RankBoard.initByRecord(matchRecord);
            rankBoard.setUserName(userMap.get(uid));
            newRankBoardList.add(rankBoard);
        }
        // 存在记录更新分数
        for (RankBoard item : userRankList) {
            Integer userId = item.getUserId();
            MatchRecord matchRecord = recordMap.get(userId);
            if (Objects.isNull(matchRecord) || newUserIds.contains(userId)) {
                // 不存在匹配记录 || 赛季首次匹配
                continue;
            }

            // 更新最新数据
            Integer score = matchRecord.getScore();
            item.setScore(item.getScore() + score);
            item.setMatchCount(item.getMatchCount() + 1);
            if (score > 0) {
                item.setMatchWin(item.getMatchWin() + 1);
            } else {
                item.setMatchLost(item.getMatchLost() + 1);
            }
            item.setUpdateTime(LocalDateTime.now());
        }

        // 创建代理对象处理事务
        RankBoardService proxy = (RankBoardService) AopContext.currentProxy();
        if (!CollectionUtils.isEmpty(newRankBoardList)) {
            proxy.saveBatch(newRankBoardList);
        }
        if (!CollectionUtils.isEmpty(userRankList)) {
            proxy.updateBatchById(userRankList);
        }
    }
}

