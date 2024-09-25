package com.MotherBoard.Admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MCControlador {

	@GetMapping("")
		public String Index() {
			return "Index";
		}
	
	@GetMapping("/login")
	public String Login() {
		return "login";
	}
	
	@GetMapping("/inventario")
	public String inventario() {
		return "inventario";
	}
	
	@GetMapping("/SobreNos")
	public String SobreNos() {
		return "SobreNos";
	}
	
	
	@GetMapping("/atendimentoFunc")
	public String atendimentoFunc() {
		return "atendimentoFunc";
	}
	
	@GetMapping("/Politica")
	public String Politica() {
		return "Politica";
	}
}
