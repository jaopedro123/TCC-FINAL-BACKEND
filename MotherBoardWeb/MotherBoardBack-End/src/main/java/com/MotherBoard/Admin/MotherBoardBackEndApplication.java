package com.MotherBoard.Admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.MotherBoard.entidade.comum", "com.MotherBoard.admin.user"})
public class MotherBoardBackEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(MotherBoardBackEndApplication.class, args);
	}

}
