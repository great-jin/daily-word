package xyz.ibudai.dailyword.server.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.ibudai.dailyword.model.vo.*;
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
     * @param wordRequest the word request
     * @return the task content
     */
    @PostMapping("getTaskContent")
    public List<TaskWord> getTaskContent(@RequestBody WordRequest wordRequest) {
        return wordService.getTaskContent(wordRequest);
    }
}
