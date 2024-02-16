package com.rowland.engineering.rowbank;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.convert.Jsr310Converters;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;



import java.util.TimeZone;

@EnableScheduling
@EnableTransactionManagement
@EntityScan(basePackageClasses = {
		RowBankApplication.class,
		Jsr310Converters.class
})
@SpringBootApplication
public class RowBankApplication {
	@PostConstruct
	void init(){
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}

	public static void main(String[] args) {
		SpringApplication.run(RowBankApplication.class, args);
	}

}
