package com.covid.vaccine;

import com.covid.vaccine.service.Bot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.net.URISyntaxException;

@SpringBootApplication
public class VaccineApplication {

    public static void main(String[] args) throws InterruptedException, IOException, URISyntaxException {
        ApplicationContext applicationContext = SpringApplication.run(VaccineApplication.class, args);
        Bot bot = applicationContext.getBean(Bot.class);
		bot.run();
    }
}
