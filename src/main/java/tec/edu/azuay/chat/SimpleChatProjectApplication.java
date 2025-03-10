package tec.edu.azuay.chat;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SimpleChatProjectApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SimpleChatProjectApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        System.out.println("Init Simple Chat Project - Success");
    }
}
