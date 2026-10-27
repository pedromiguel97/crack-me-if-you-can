package io.github.pedromiguel97.crackme;

import org.springframework.boot.SpringApplication;

public class TestCrackMeIfYouCanApplication {

    public static void main(String[] args) {
        SpringApplication.from(CrackMeIfYouCanApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
