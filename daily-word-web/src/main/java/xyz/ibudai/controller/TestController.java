package xyz.ibudai.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/web")
public class TestController {

    @GetMapping("demo1")
    public String demo1() {
        return "hello";
    }
}
