package com.sobremesas.PokeCafe.Controller;

import ch.qos.logback.core.model.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PokeCafeApplication {

	// Página inicial
	@GetMapping({"/", "/index"})
	public String index() {
		return "index";
	}

	@GetMapping({ "/login"})
	public String login() {
		return "login";
	}

	// Adicionar ao carrinho
	@GetMapping("/adicionar")
	public String adicionarCarrinho() {
		return "adicionarCarrinho";
	}

	// Ver carrinho
	@GetMapping("/carrinho")
	public String verCarrinho() {
		return "verCarrinho";
	}

	// Página Admin
	@GetMapping("/admin")
	public String admin() {
		return "admin";
	}

	// Editar item (popup)
	@GetMapping("/editar/{id}")
	public String editar(@PathVariable Long id, Model model) {
		return "editar";
	}

	// Deletar item
	@GetMapping("/deletar/{id}")
	public String deletar(@PathVariable Long id) {
		// lógica pra deletar aqui
		return "redirect:/admin";
	}

	// Restaurar item
	@GetMapping("/restaurar/{id}")
	public String restaurar(@PathVariable Long id) {
		// lógica pra restaurar aqui
		return "redirect:/admin";
	}

	// Cadastro de item (popup)
	@GetMapping("/cadastro")
	public String cadastro() {
		return "cadastro";
	}
}
