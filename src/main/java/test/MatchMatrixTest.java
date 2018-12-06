package test;

import org.junit.jupiter.api.Test;

class MatchMatrixTest {
    @Test
    void MatcherControllerTest() {
        MatchMatrix mm = new MatchMatrix("тест.txt");
        System.out.println(mm.response);
    }
}