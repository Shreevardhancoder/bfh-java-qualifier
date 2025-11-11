package com.bfh.webhookapp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@SpringBootApplication
public class WebhookappApplication implements CommandLineRunner {

    private final RestTemplate restTemplate = new RestTemplate();

    public static void main(String[] args) {
        SpringApplication.run(WebhookappApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // 1️⃣ Generate webhook
        String url = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

        Map<String, String> requestBody = Map.of(
            "name", "John Doe",      // <-- replace with your real name
            "regNo", "REG12347",     // <-- replace with your real reg number
            "email", "john@example.com" // <-- replace with your email
        );

        ResponseEntity<Map> response = restTemplate.postForEntity(url, requestBody, Map.class);
        String webhookUrl = (String) response.getBody().get("webhook");
        String token = (String) response.getBody().get("accessToken");

        System.out.println("✅ Webhook URL: " + webhookUrl);
        System.out.println("✅ Token: " + token);

        // 2️⃣ Your SQL solution query (replace with your real query)
        String finalQuery = "SELECT * FROM employees;";

        // 3️⃣ Submit result
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", token);

        Map<String, String> body = Map.of("finalQuery", finalQuery);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

        restTemplate.postForEntity(webhookUrl, entity, String.class);

        System.out.println("✅ Submitted successfully!");
    }
}
