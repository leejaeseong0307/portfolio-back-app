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

    @Scheduled(cron = "0 0 5 * * *")
    public void run() throws IOException {
        String rawQuote = quoteService.requestQuote();

        JsonObject contentJson = JsonParser.parseString(rawQuote).getAsJsonObject();
        String title = contentJson.get("title").getAsString();
        String quote = contentJson.get("quote").getAsString();
        
        String imageUrl = quoteService.downloadImageFromPexels(title);

        ContVo vo = new ContVo();
        vo.setUserId("system");
        vo.setContTitle("- "+title+" -");
        vo.setContDetail(quote);
        //vo.setContImg(imageUrl);
        vo.setContImg("http://localhost:8081"+imageUrl);
        vo.setCreatedBy("system");
        vo.setUpdatedBy("system");

        contMapper.insertContent(vo);
        System.out.println("배치 저장 완료: " + vo.getContTitle());
    }
}
