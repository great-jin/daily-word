package xyz.ibudai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.ibudai.model.TaskWord;
import xyz.ibudai.model.Word;
import xyz.ibudai.model.WordRequest;
import xyz.ibudai.service.WordService;

import java.util.List;

@RestController
@RequestMapping("/api/business/word")
public class WordController {

    @Autowired
    private WordService wordService;

    @GetMapping("translation")
    public Word translation(@RequestParam("word") String word) {
        return wordService.translation(word);
    }

    @PostMapping("getTaskContent")
    public List<TaskWord> getTaskContent(@RequestBody WordRequest wordRequest) {
        return wordService.getTaskContent(wordRequest);
    }
}
