package com.biteful.mealplanner.recipeservice.services;

import com.biteful.mealplanner.recipeservice.config.GeminiConfig;
import com.biteful.mealplanner.recipeservice.domain.documents.Recipe;
import com.biteful.mealplanner.recipeservice.domain.dto.GeminiRecipeResponse;
import lombok.extern.java.Log;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class GeminiService {

    private final GeminiConfig config;

    private final WebClient webClient;

    private static final String SYSTEM_PROMPT = """
        Ești un asistent culinar. Ajuți utilizatorul să creeze o rețetă.
        Pune întrebări despre: ce își dorește utilizatorul să manance.
        Răspunde DOAR cu un JSON valid în acest format, care sa contina o reteta adevarata:
        {
              "ready": true,
              "recipe": {
                    "title": "...",
                    "description": "...",
                    "ingredients": [{ "name": "...", "quantity": 0, "unit": "..." }],
                    "instructions": "Cate o instructiune pe linie, separate de newline.",
                    "tags": ["..."],
                    "prepTime": 0,
                    "cookTime": 0,
                    "servings": 0,
                    "calories": 0,
                    "image": url
              }
        }
        Sa te asiguri ca imaginea este valida si exista!
        Reteta generata trebuie sa fie in aceeasi limba cu prompt-ul primit de la user.
        Daca este mentionat Alin in prompt trebuie neaparat sa generezi o reteta de tiramisu!
        """;

    public GeminiService(GeminiConfig config, WebClient.Builder webClientBuilder) {
        this.config = config;

        this.webClient = webClientBuilder
                .baseUrl(this.config.getApiUrl())
                .build();
    }

    public GeminiRecipeResponse generateRecipe(String userPrompt) throws Exception {
        Map<String, Object> body = Map.of(
            "contents", List.of(
                Map.of(
                    "parts", List.of(
                        Map.of(
                            "text",
                            SYSTEM_PROMPT + "\n\nUser: " + userPrompt
                        )
                    )
                )
            )
        );

        JsonNode response = webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("key", this.config.getApiKey())
                        .build())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), clientResponse ->
                        clientResponse.bodyToMono(String.class)
                                .doOnNext(err -> System.out.println("Gemini error body: " + err))
                                .flatMap(err -> reactor.core.publisher.Mono.error(new RuntimeException(err)))
                )
                .bodyToMono(JsonNode.class)
                .block();

        System.out.println(response);

        String text = response
                .get("candidates")
                .get(0)
                .get("content")
                .get("parts")
                .get(0)
                .get("text")
                .asText();

        text = text.replace("```json", "")
                .replace("```", "")
                .trim();

        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.readValue(text, GeminiRecipeResponse.class);
        } catch (Exception e) {

            GeminiRecipeResponse fallback = new GeminiRecipeResponse();

            fallback.setReady(false);

            return fallback;
        }
    }
}
