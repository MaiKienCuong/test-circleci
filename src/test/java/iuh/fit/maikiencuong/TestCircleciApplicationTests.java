package iuh.fit.maikiencuong;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TestCircleciApplicationTests {

    @Test
    void test_add() {
        int sum = 1 + 1;
        Assertions.assertEquals(2, sum);
    }

    @Test
    void test_subtract() {
        int sub = 2 - 1;
        Assertions.assertEquals(1, sub);
    }

    @Test
    void test_multi() {
        int multi = 2 * 2;
        Assertions.assertEquals(4, multi);
    }

}
