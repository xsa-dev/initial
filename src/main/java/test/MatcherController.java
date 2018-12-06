package test;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class MatcherController {
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/match")
    public MatchMatrix match(@RequestParam(value="filename", defaultValue="тест.txt") String filename) {
        return new MatchMatrix(counter.incrementAndGet(), filename);
    }
}
