package com.MotherBoard.Admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MCControlador {

	@GetMapping("")
		public String Index() {
			return "Index";
		}
	
	
}
