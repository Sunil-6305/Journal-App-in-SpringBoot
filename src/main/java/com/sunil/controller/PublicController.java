package com.sunil.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/public")
public class PublicController {
	

	@GetMapping("/health-check")
	public String healthCheck() {
		return "Ok";

	}

}
