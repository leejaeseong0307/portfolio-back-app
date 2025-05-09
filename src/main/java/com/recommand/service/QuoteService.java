package com.recommand.service;

import com.google.gson.*;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service  // ❗ 꼭 필요
public class QuoteService {

	private final OkHttpClient client = new OkHttpClient.Builder()
		    .connectTimeout(60, TimeUnit.SECONDS)
		    .readTimeout(60, TimeUnit.SECONDS)
		    .writeTimeout(60, TimeUnit.SECONDS)
		    .build();

    @Value("${openai.api.key}")
    private String apiKey;
    
    @Value("${pexels.api.key}")
    private String pexelsApiKey;

    @Value("${image.save.dir}")
    private String imageSaveDir;
    
    @Value("${file.upload-dir}")
	private String uploadDir;

    public String requestQuote() throws IOException {
        //return requestGpt("외국 위인의 명언 하나를 영어 원문과 번역, 인물 이름 포함해서 알려줘.");
    	return requestGpt("공백포함 최대 150자 내에서 영어로 된 희망 및 동기부여 관련 글 하나를 title과 quote를 나누어서 json 형태로 알려줘.");
    }

    public String requestImagePrompt(String quote) throws IOException {
        return requestGpt("이 영화에 어울리는 배경 이미지를 영어로 묘사해줘: " + quote);
    }

    public String downloadImageFromPexels(String query) throws IOException {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host("api.pexels.com")
                .addPathSegments("v1/search")
                .addQueryParameter("query", query)
                .addQueryParameter("per_page", "1")
                .addQueryParameter("orientation", "landscape")
                .build();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", pexelsApiKey)
                .get()
                .build();

        String imageUrl;
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Pexels 응답 실패: " + response.code());
            }

            String resBody = response.body().string();
            JsonObject json = JsonParser.parseString(resBody).getAsJsonObject();
            imageUrl = json.getAsJsonArray("photos")
                    .get(0).getAsJsonObject()
                    .getAsJsonObject("src")
                    .get("large").getAsString();
        }

        Request imgRequest = new Request.Builder().url(imageUrl).build();
        byte[] imageBytes;
        try (Response imgResponse = client.newCall(imgRequest).execute()) {
            imageBytes = imgResponse.body().bytes();
        }

        String fileName = UUID.randomUUID() + ".png";
        String absolutePath = uploadDir.replace("/", "").replace("file:", "");
        Path savePath = Paths.get(absolutePath, fileName);
        Files.createDirectories(savePath.getParent());
        Files.write(savePath, imageBytes);

        return "/" + imageSaveDir + "/" + fileName;
    }

    private String requestGpt(String prompt) throws IOException {
        JsonArray messages = new JsonArray();
        JsonObject userMessage = new JsonObject();
        userMessage.addProperty("role", "user");
        userMessage.addProperty("content", prompt);
        messages.add(userMessage);

        JsonObject body = new JsonObject();
        body.addProperty("model", "gpt-4");
        body.add("messages", messages);

        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .addHeader("Authorization", "Bearer " + apiKey)
                .post(RequestBody.create(MediaType.parse("application/json"), body.toString()))
                .build();

        try (Response response = client.newCall(request).execute()) {
            String resBody = response.body().string();
            JsonObject json = JsonParser.parseString(resBody).getAsJsonObject();
            return json.getAsJsonArray("choices")
                    .get(0).getAsJsonObject()
                    .get("message").getAsJsonObject()
                    .get("content").getAsString();
        }
    }
}
