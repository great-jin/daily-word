package xyz.ibudai.dailyword.server.service;

import xyz.ibudai.dailyword.model.mongo.SubjectContent;

import java.util.List;

public interface MongoService {

    /**
     * Gets correct count.
     *
     * @param matchId     the match id
     * @param contentList the content list
     * @return the correct count
     */
    int getCorrectCount(Integer matchId, List<SubjectContent> contentList);

}
