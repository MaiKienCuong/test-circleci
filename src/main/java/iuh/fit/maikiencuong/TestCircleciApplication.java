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
        return "<h4>test circle ci with docker successfully</h4> " +
                "<br> " +
                "<h4>test cherry pick, test push commit during creating pull request</>";
    }

}
