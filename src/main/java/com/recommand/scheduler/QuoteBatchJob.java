package com.recommand.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.recommand.mapper.ContMapper;
import com.recommand.service.QuoteService;
import com.recommand.vo.ContVo;

import java.io.IOException;

@Component
public class QuoteBatchJob {

    private final QuoteService quoteService;
    private final ContMapper contMapper;

    public QuoteBatchJob(QuoteService quoteService, ContMapper contMapper) {
        this.quoteService = quoteService;
        this.contMapper = contMapper;
    }

    @Scheduled(cron = "0 0 * * * *")
    public void run() throws IOException {
        String rawQuote = quoteService.requestQuote();

        JsonObject contentJson = JsonParser.parseString(rawQuote).getAsJsonObject();
        String title = contentJson.get("title").getAsString();
        String quote = contentJson.get("quote").getAsString();
        
//        String[] parts = rawQuote.split("\"|");
//        String quote = parts.length > 1 ? parts[1].trim() : rawQuote;
//        String author = rawQuote.contains("-") ? rawQuote.split("-")[1].split("\n")[0].trim() : "Unknown";
//        String translation = rawQuote.split("\n").length > 1 ? rawQuote.split("\n")[1].trim() : "";

        String imagePrompt = quoteService.requestImagePrompt(quote);
        //String imagePrompt = "";
        String imageUrl = quoteService.downloadImageFromPexels(imagePrompt);
        //String imageUrl = "";

        ContVo vo = new ContVo();
        vo.setUserId("system");
//      vo.setContTitle(quote + " - " + author);
//      vo.setContDetail(translation);
        vo.setContTitle("- "+title+" -");
        vo.setContDetail(quote);
        //vo.setContImg("http://localhost:8081"+imageUrl);
        vo.setContImg("http://121.125.94.188:8000"+imageUrl);
        vo.setCreatedBy("system");
        vo.setUpdatedBy("system");

        contMapper.insertContent(vo);
        System.out.println("배치 저장 완료: " + vo.getContTitle());
    }
}
