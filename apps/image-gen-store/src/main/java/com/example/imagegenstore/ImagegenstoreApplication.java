package com.example.imagegenstore;

import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.client.RestClient;

import java.util.Map;

@SpringBootApplication
public class ImagegenstoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImagegenstoreApplication.class, args);
	}

	@Bean
	RestClient restClient(RestClient.Builder builder) {
		return builder.build();
	}

	@Bean
	ApplicationRunner runner (RestClient rc, ImageModel imageModel, @Value("${AI_PROMPT}") String prompt, @Value("${DB_SERVICE_URL:http://localhost:8080/images}") String dbServiceUrl) {
		return args -> {
			System.out.println("Prompt: " + prompt);
			var response = imageModel.call(new ImagePrompt(prompt));
			var url = response.getResult().getOutput().getUrl();
			System.out.println("URL: " + url);

			var bodilessEntity = rc.post().uri(dbServiceUrl+"?prompt={prompt}&url={url}",Map.of("prompt", prompt, "url", url))
					.retrieve().toBodilessEntity();
			Assert.state(bodilessEntity.getStatusCode().is2xxSuccessful(), "Failed to post to database");
		};
	}


}

// 1. take in a prompt
// 2. call OpenAI to get image/url
// 3. "post" to curl -XPOST -dprompt="cute raccoon242"  -durl="https://i.imgur.com/FLRxq2L.png"  http://localhost:8080/images