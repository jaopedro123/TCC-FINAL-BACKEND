package com.MotherBoard.Site;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MCControle {

	@GetMapping("")
	public String Index() {
		return "index";
	}
	
}
