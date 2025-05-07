package com.recommand;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.recommand.scheduler.QuoteBatchJob;

@EnableScheduling
@SpringBootApplication
public class RecommendApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(RecommendApplication.class, args);
//		
//		 try {
//	            QuoteBatchJob job = context.getBean(QuoteBatchJob.class);
//	            job.run();
//	        } catch (Exception e) {
//	            e.printStackTrace();
//	        }

	        //SpringApplication.exit(context);
	}

}
