package xyz.ibudai.dailyword.server.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.ibudai.dailyword.model.dto.TaskWordDTO;
import xyz.ibudai.dailyword.model.enums.Catalogue;
import xyz.ibudai.dailyword.model.vo.*;
import xyz.ibudai.dailyword.model.vo.match.MatchVo;
import xyz.ibudai.dailyword.model.vo.word.Word;
import xyz.ibudai.dailyword.server.service.WordService;

import java.util.List;

/**
 * The type Word resource.
 */
@RestController
@RequestMapping("/api/server/word")
public class WordResource {

    @Autowired
    private WordService wordService;


    /**
     * Gets dict detail.
     *
     * @return the dict detail
     */
    @GetMapping("getDictDetail")
    public List<DictDetail> getDictDetail() {
        return wordService.getDictDetail();
    }

    /**
     * Translation word.
     *
     * @param word the word
     * @return the word
     */
    @GetMapping("translation")
    public Word translation(@RequestParam("word") String word) {
        return wordService.translation(word);
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
        return wordService.starTask(catalogue, size);
    }

    /**
     * Gets answer.
     *
     * @param matchId the match id
     * @return the answer
     */
    @GetMapping("getAnswer")
    public List<TaskWordDTO> getAnswer(@RequestParam("matchId") String matchId) {
        return wordService.getAnswer(matchId);
    }
}
