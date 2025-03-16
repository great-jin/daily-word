package xyz.ibudai.dailyword.repository.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import xyz.ibudai.dailyword.model.entity.AuthUser;
import xyz.ibudai.dailyword.model.entity.RankBoard;
import xyz.ibudai.dailyword.repository.dao.AuthUserDao;
import xyz.ibudai.dailyword.repository.dao.RankBoardDao;
import xyz.ibudai.dailyword.repository.service.RankBoardService;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * (RankBoard)表服务实现类
 *
 * @author makejava
 * @since 2025-03-16 09:26:04
 */
@Service
public class RankBoardServiceImpl extends ServiceImpl<RankBoardDao, RankBoard> implements RankBoardService {

    @Autowired
    private AuthUserDao authUserDao;


    @Override
    public RankBoard getUserRank(Integer userId) {
        RankBoard rankBoard = new RankBoard();
        List<RankBoard> list = this.lambdaQuery()
                .eq(RankBoard::getSeason, 1)
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
        rankBoard.setSeason(1);
        rankBoard.setScore(score);
        rankBoard.setMatchWin(win);
        rankBoard.setMatchLost(lost);
        return rankBoard;
    }

    @Override
    public List<RankBoard> listByCatalog(Integer userId, String catalog) {
        List<RankBoard> list = this.lambdaQuery()
                .eq(RankBoard::getSeason, 1)
                .eq(RankBoard::getCatalog, catalog)
                .orderByDesc(RankBoard::getScore)
                .last("LIMIT 100")
                .list();
        Set<Integer> userIds = list.stream()
                .map(RankBoard::getUserId)
                .collect(Collectors.toSet());
        int i = 1;
        for (RankBoard item : list) {
            item.setIndex(String.valueOf(i++));
        }
        if (userIds.contains(userId)) {
            return list;
        }

        // 最后一条展示用户
        RankBoard userRank = this.lambdaQuery()
                .eq(RankBoard::getSeason, 1)
                .eq(RankBoard::getCatalog, catalog)
                .eq(RankBoard::getUserId, userId)
                .one();
        if (Objects.isNull(userRank)) {
            userRank = RankBoard.init();
            userRank.setUserId(userId);
            AuthUser authUser = authUserDao.selectById(userId);
            userRank.setUserName(authUser.getUsername());
        }
        userRank.setIndex("-");
        list.add(userRank);
        return list;
    }
}

