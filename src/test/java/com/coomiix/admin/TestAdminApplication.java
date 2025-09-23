package com.coomiix.admin;

import org.springframework.boot.SpringApplication;

public class TestAdminApplication {

	public static void main(String[] args) {
		SpringApplication.from(AdminApplication::main).with(TestAdminApplication.class).run(args);
	}

}
