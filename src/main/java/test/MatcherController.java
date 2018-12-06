package test;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MatcherController {

    @RequestMapping("/match")
    public MatchMatrix match(@RequestParam(value="filename", defaultValue="тест.txt") String filename) {
        return new MatchMatrix(filename);
    }
}
