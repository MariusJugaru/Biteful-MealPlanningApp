package com.biteful.mealplanner.recipeservice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootApplication
public class RecipeServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecipeServiceApplication.class, args);
	}

//    @Bean
//    CommandLineRunner init(MongoTemplate mongoTemplate) {
//        return args -> {
//            mongoTemplate.dropCollection("recipes");
//        };
//    }

}
