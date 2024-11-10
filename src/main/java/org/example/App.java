package org.example;

import org.example.controller.BookController;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class App 
{
    public static void main( String[] args )
    {
        SpringApplication.run(App.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            BookController bookController = ctx.getBean(BookController.class);
            bookController.saveBooksFromAPI();
            bookController.showStoredBooks();
        };
    }

}
