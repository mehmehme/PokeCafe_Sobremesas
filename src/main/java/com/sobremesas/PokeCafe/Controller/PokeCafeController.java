package com.sobremesas.PokeCafe.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.ui.Model; // CORRIGIDO: Importe o Model correto do Spring MVC.
import jakarta.servlet.http.HttpSession; // Para gerenciar a sessão HTTP
import com.sobremesas.PokeCafe.Domain.Sobremesa; // Importa a classe de modelo Sobremesa
import com.sobremesas.PokeCafe.Service.SobremesaService; // Importa o SobremesaService
import org.springframework.beans.factory.annotation.Autowired; // Para injeção de dependência

import java.util.ArrayList; // Para o carrinho de compras
import java.util.Date; // Para o soft-delete
import java.util.List; // Para a lista de sobremesas

@Controller // Anotação que indica que esta classe é um controlador Spring MVC.
public class PokeCafeApplication {

	private final SobremesaService sobremesaService; // Injeta o SobremesaService

	@Autowired // Injeta a dependência do serviço
	public PokeCafeApplication(SobremesaService sobremesaService) {
		this.sobremesaService = sobremesaService;
	}

	// Página inicial (Questão 3)
	@GetMapping({"/", "/index"}) // Mapeia requisições GET para a URL raiz e /index
	public String index(Model model, HttpSession session) { // Recebe um Model para passar dados para a view e HttpSession para gerenciar o carrinho
		// Busca todas as sobremesas que não estão deletadas logicamente
		List<Sobremesa> sobremesas = sobremesaService.findAllActive(); // Usa o serviço para obter os dados
		model.addAttribute("listaDeSobremesas", sobremesas); // Adiciona a lista ao modelo para ser acessada no HTML

		// Obtém a quantidade de itens no carrinho da sessão HTTP
		// A sessão HTTP é usada para "guardar informação sobre a interação com o cliente".
		List<Sobremesa> carrinho = (List<Sobremesa>) session.getAttribute("carrinho"); // Obtém o atributo 'carrinho' da sessão
		int quantidadeCarrinho = (carrinho != null) ? carrinho.size() : 0;
		model.addAttribute("quantidadeCarrinho", quantidadeCarrinho); // Adiciona a quantidade ao modelo

		return "index"; // Retorna o nome do template Thymeleaf a ser renderizado
	}

	// Rotas do carrinho (Questão 9 e 10)
	@GetMapping("/adicionarCarrinho/{id}") // Recebe o ID do item a ser adicionado ao carrinho
	public String adicionarCarrinho(@PathVariable Long id, HttpSession session) { // Recebe o ID da URL e a sessão
		Sobremesa sobremesa = sobremesaService.findById(id).orElse(null); // Busca a sobremesa pelo ID

		if (sobremesa != null) {
			List<Sobremesa> carrinho = (List<Sobremesa>) session.getAttribute("carrinho");
			if (carrinho == null) {
				carrinho = new ArrayList<>(); // Se o carrinho não existe na sessão, cria um novo ArrayList
			}
			carrinho.add(sobremesa); // Adiciona a sobremesa ao carrinho
			session.setAttribute("carrinho", carrinho); // Atualiza o carrinho na sessão
		}
		return "redirect:/index"; // Redireciona para a página inicial
	}

	@GetMapping("/verCarrinho") // Rota para ver o carrinho
	public String verCarrinho(Model model, HttpSession session) {
		List<Sobremesa> carrinho = (List<Sobremesa>) session.getAttribute("carrinho");
		if (carrinho == null || carrinho.isEmpty()) { // Se o carrinho estiver vazio
			model.addAttribute("mensagemCarrinho", "Não existem itens no carrinho."); // Adiciona mensagem ao modelo
			return "redirect:/index"; // Redireciona para /index
		}
		model.addAttribute("carrinho", carrinho); // Adiciona os itens do carrinho ao modelo
		return "verCarrinho"; // Retorna o template verCarrinho.html
	}

	@GetMapping("/finalizarCompra") // Rota para finalizar compra (Questão 11)
	public String finalizarCompra(HttpSession session) {
		session.invalidate(); // Invalida a sessão existente
		return "redirect:/index"; // Redireciona para index
	}

	// As rotas abaixo (login, admin, editar, deletar, restaurar, cadastro) serão implementadas nas próximas questões.
	@GetMapping({ "/login"})
	public String login() {
		return "login";
	}

	@GetMapping("/admin")
	public String admin() {
		return "admin";
	}

	@GetMapping("/editar/{id}")
	public String editar(@PathVariable Long id, Model model) {
		return "editar";
	}

	@GetMapping("/deletar/{id}")
	public String deletar(@PathVariable Long id) {
		// Lógica para deletar será implementada na Questão 8
		return "redirect:/admin";
	}

	@GetMapping("/restaurar/{id}")
	public String restaurar(@PathVariable Long id) {
		// Lógica para restaurar será implementada na Questão 8
		return "redirect:/admin";
	}

	@GetMapping("/cadastro")
	public String cadastro() {
		return "cadastro";
	}
}