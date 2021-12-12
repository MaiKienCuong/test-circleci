package iuh.fit.maikiencuong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
@RequestMapping("/test")
public class TestCircleciApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestCircleciApplication.class, args);
    }

    @GetMapping
    public String test() {
        return "test circle ci with docker successfully";
    }

}
