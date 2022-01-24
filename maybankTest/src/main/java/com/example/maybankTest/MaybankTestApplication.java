package com.example.maybankTest;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@MapperScan("com.example.maybankTest.mapper")
@SpringBootApplication
@EnableTransactionManagement(proxyTargetClass=false)
public class MaybankTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(MaybankTestApplication.class, args);
	}
}
