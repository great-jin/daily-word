package xyz.ibudai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.config.Task;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.ibudai.model.TaskWord;
import xyz.ibudai.model.Word;
import xyz.ibudai.service.WordService;

import java.util.List;

@RestController
@RequestMapping("/api/word")
public class WordController {

    @Autowired
    private WordService wordService;

    @GetMapping("translation")
    public Word translation(@RequestParam("word") String word) {
        return wordService.translation(word);
    }

    @GetMapping("getTaskContent")
    public List<TaskWord> getTaskContent(@RequestParam("size") Integer size,
                                         @RequestParam("offset") Integer offset) {
        return wordService.getTaskContent(size, offset);
    }
}
