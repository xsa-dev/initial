package test;

import org.junit.jupiter.api.Test;

public class MatchMatrixTest {
    @Test
    public void MatcherControllerTest() {
        MatchMatrix mm = new MatchMatrix(0, "тест.txt");
        System.out.println(mm.response);
    }

    @Test
    public void getContent() {
    }

    @Test
    public void getResponse() {
    }


}