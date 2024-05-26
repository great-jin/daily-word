package xyz.ibudai.dailyword.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.ibudai.dailyword.model.WordRequest;
import xyz.ibudai.dailyword.service.WordService;
import xyz.ibudai.dailyword.model.DictDetail;
import xyz.ibudai.dailyword.model.TaskWord;
import xyz.ibudai.dailyword.model.Word;

import java.util.List;

@RestController
@RequestMapping("/api/server/word")
public class WordController {

    @Autowired
    private WordService wordService;

    @GetMapping("getDictDetail")
    public List<DictDetail> getDictDetail() {
        return wordService.getDictDetail();
    }

    @GetMapping("translation")
    public Word translation(@RequestParam("word") String word) {
        return wordService.translation(word);
    }

    @PostMapping("getTaskContent")
    public List<TaskWord> getTaskContent(@RequestBody WordRequest wordRequest) {
        return wordService.getTaskContent(wordRequest);
    }
}
