package dk.dtu.compute.se.pisd.roborally.webApplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringAPIApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringAPIApplication.class, args);
    }

    public static void startSpring(){
        SpringApplication.run(SpringAPIApplication.class);
    }
}
