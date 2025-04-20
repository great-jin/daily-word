package xyz.ibudai.dailyword.server.resource;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.ibudai.dailyword.model.enums.Catalogue;
import xyz.ibudai.dailyword.model.vo.*;
import xyz.ibudai.dailyword.model.vo.match.MatchVo;
import xyz.ibudai.dailyword.model.vo.word.Word;
import xyz.ibudai.dailyword.server.service.MatchRecordService;
import xyz.ibudai.dailyword.server.service.TaskWordService;

/**
 * The type Word resource.
 */
@RestController
@RequestMapping("/api/server/word")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TaskWordResource {

    private final TaskWordService taskWordService;
    private final MatchRecordService matchRecordService;

    /**
     * Translation word.
     *
     * @param word the word
     * @return the word
     */
    @GetMapping("translation")
    public Word translation(@RequestParam("word") String word) {
        return taskWordService.translation(word);
    }

    /**
     * Gets task content.
     *
     * @param catalogue the catalogue
     * @param size      the size
     * @return task content
     */
    @PostMapping("startTask")
    public MatchVo startTask(@RequestParam("catalogue") Catalogue catalogue,
                             @RequestParam("size") Integer size) {
        return taskWordService.starTask(catalogue, size);
    }

    /**
     * Gets answer.
     *
     * @param matchId the match id
     * @return the answer
     */
    @GetMapping("getAnswer")
    public AnswerVo getAnswer(@RequestParam("matchId") Integer matchId) {
        Boolean finished = matchRecordService.checkTaskFinished(matchId);
        if (!finished) {
            throw new IllegalStateException("对局进行中，无法查看答案！");
        }

        return taskWordService.getAnswer(matchId);
    }
}
