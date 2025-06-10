package com.sobremesas.PokeCafe.Controller;

import com.sobremesas.PokeCafe.Domain.Usuario;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.ui.Model; // CORRIGIDO: Importe o Model correto do Spring MVC.
import jakarta.servlet.http.HttpSession; // Para gerenciar a sessão HTTP
import com.sobremesas.PokeCafe.Domain.Sobremesa; // Importa a classe de modelo Sobremesa
import com.sobremesas.PokeCafe.Service.UsuarioService;
import com.sobremesas.PokeCafe.Service.SobremesaService; // Importa o SobremesaService
import org.springframework.beans.factory.annotation.Autowired; // Para injeção de dependência
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.ArrayList; // Para o carrinho de compras
import java.util.List; // Para a lista de sobremesas
import java.util.Optional;

@Controller // Anotação que indica que esta classe é um controlador Spring MVC.
public class PokeCafeController {

	private final SobremesaService sobremesaService; // Injeta o SobremesaService
	private final LocalValidatorFactoryBean validator;
	private final PasswordEncoder passwordEncoder;
	private final UsuarioService usuarioService;

	@Autowired // Injeta a dependência do serviço
	public PokeCafeController(SobremesaService sobremesaService,  LocalValidatorFactoryBean validator, PasswordEncoder passwordEncoder, UsuarioService usuarioService) {
		this.sobremesaService = sobremesaService;
		this.validator = validator;
		this.passwordEncoder = passwordEncoder;
		this.usuarioService = usuarioService;
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
			List<Sobremesa> carrinho = (List<Sobremesa>) session.getAttribute("carrinho"); // Obtém o atributo 'carrinho' da sessão
			if (carrinho == null) {
				carrinho = new ArrayList<>(); // Se o carrinho não existe na sessão, cria um novo ArrayList
			}
			carrinho.add(sobremesa); // Adiciona a sobremesa ao carrinho
			session.setAttribute("carrinho", carrinho); // Atualiza o carrinho na sessão
		}
		return "redirect:/index"; // Redireciona para a página inicial
	}

	@GetMapping("/verCarrinho") // Rota para ver o carrinho
	public String verCarrinho(Model model, HttpServletRequest request) {
		HttpSession session = request.getSession(false);

		List<Sobremesa> carrinho = null;
		if (session != null) {
			carrinho = (List<Sobremesa>) session.getAttribute("carrinho");
		}

		if (carrinho == null || carrinho.isEmpty()) {
			model.addAttribute("mensagemCarrinho", "Não existem itens no carrinho.");
			return "redirect:/index";
		}

		// NOVO: Calcular o total do carrinho no controlador (Questão 10)
		// Use BigDecimal para precisão com dinheiro.
		BigDecimal totalCarrinho = BigDecimal.ZERO;
		for (Sobremesa item : carrinho) {
			if (item.getPreco() != null) {
				totalCarrinho = totalCarrinho.add(BigDecimal.valueOf(item.getPreco()));
			}
		}
		model.addAttribute("totalCarrinho", totalCarrinho); // Adiciona o total ao modelo

		model.addAttribute("carrinho", carrinho); // Adiciona os itens do carrinho ao modelo
		return "verCarrinho"; // Retorna o template verCarrinho.html
	}

	@GetMapping("/finalizarCompra") // Rota para finalizar compra (Questão 11)
	public String finalizarCompra(HttpSession session, RedirectAttributes redirectAttributes) { // Adicionar RedirectAttributes
		session.invalidate(); // Invalida a sessão existente
		redirectAttributes.addFlashAttribute("mensagemSucesso", "Compra finalizada com sucesso! Seu carrinho foi esvaziado."); // Adiciona mensagem
		return "redirect:/index"; // Redireciona para index
	}

	// As rotas abaixo (login, admin, editar, deletar, restaurar, cadastro) serão implementadas nas próximas questões.
	@GetMapping({ "/login"})
	public String login() {
		return "login";
	}

	@GetMapping("/admin") // Mapeia requisições GET para a rota /admin
	public String admin(Model model) { // Recebe um Model para passar dados para a view
		// Busca TODAS as sobremesas do banco de dados (ativas e deletadas)
		List<Sobremesa> todasSobremesas = sobremesaService.findAll(); // Usa o novo método do serviço
		model.addAttribute("todasSobremesas", todasSobremesas); // Adiciona a lista ao modelo

		return "admin"; // Retorna o nome do template Thymeleaf a ser renderizado
	}

	@GetMapping("/editar/{id}") // Mapeia requisições GET para a rota /editar/{id}
	public String editar(@PathVariable Long id, Model model) { // Recebe o ID da URL e um Model
		// Busca a sobremesa pelo ID usando o serviço
		Optional<Sobremesa> sobremesaOptional = sobremesaService.findById(id); // Usa o método findById do serviço

		if (sobremesaOptional.isPresent()) { // Verifica se a sobremesa foi encontrada
			model.addAttribute("sobremesa", sobremesaOptional.get()); // Adiciona o objeto Sobremesa encontrado ao modelo.
			return "editar"; // Retorna o nome do template Thymeleaf a ser renderizado
		} else {
			// Se a sobremesa não for encontrada, redireciona para a página admin
			// Poderíamos adicionar uma mensagem de erro aqui, mas a prova não exige.
			return "redirect:/admin";
		}
	}

	@GetMapping("/deletar/{id}") // Mapeia requisições GET para a rota /deletar/{id}
	public String deletar(@PathVariable Long id, RedirectAttributes redirectAttributes) { // Recebe o ID da URL e RedirectAttributes
		sobremesaService.softDelete(id); // Chama o método de soft delete no serviço
		redirectAttributes.addFlashAttribute("mensagemSucesso", "Sobremesa deletada com sucesso!"); // Adiciona a mensagem flash
		return "redirect:/index"; // Redireciona para a página inicial
	}

	@GetMapping("/restaurar/{id}") // Mapeia requisições GET para a rota /restaurar/{id}
	public String restaurar(@PathVariable Long id, RedirectAttributes redirectAttributes) { // Recebe o ID da URL e RedirectAttributes
		sobremesaService.restore(id); // Chama o método de restaurar no serviço
		redirectAttributes.addFlashAttribute("mensagemSucesso", "Sobremesa restaurada com sucesso!"); // Adiciona a mensagem flash
		return "redirect:/index"; // Redireciona para a página inicial
	}

	@GetMapping("/cadastro") // Mapeia requisições GET para a rota /cadastro
	public String cadastro(Model model) { // Recebe um Model para passar dados para a view
		model.addAttribute("sobremesa", new Sobremesa()); // Adiciona um novo objeto Sobremesa vazio ao modelo para o formulário.
		return "cadastro"; // Retorna o nome do template Thymeleaf a ser renderizado
	}

	@GetMapping("/cadusuario")
	public String cadUsuario(Model model) {
		model.addAttribute("usuario", new Usuario()); // Passa um novo objeto Usuario vazio para o formulário
		return "cadusuario"; // Retorna o template cadusuario.html
	}

	@PostMapping("/salvarusuario")
	public String salvarUsuario(@ModelAttribute @Valid Usuario usuario, BindingResult result, RedirectAttributes redirectAttributes) {
		// Validação
		if (result.hasErrors()) {
			return "cadusuario"; // Retorna para o formulário com erros
		}

		// Criptografa a senha antes de salvar
		usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

		// Salva o usuário no banco de dados
		// Você precisará de um UsuarioService para isso, que criaremos no próximo passo se ainda não tiver.
		// Mas, por simplicidade, podemos usar o UsuarioRepository diretamente ou adicionar ao SobremesaService
		// uma lógica para Usuario. Para manter a organização, criaremos um UsuarioService.
		usuarioService.save(usuario); // ERRO AQUI: sobremesaService.save não salva Usuario. Precisamos de UsuarioService.
		// Para resolver isso, vamos criar UsuarioService e injetá-lo, ou adicionar um método saveUsuario no SobremesaService.
		// Vou assumir que você criará um UsuarioService.

		// --- CORREÇÃO: Você precisará injetar um UsuarioService no Controller ---
		// Se já tiver UsuarioService:
		// usuarioService.save(usuario); // Chama o save do UsuarioService
		// Se não tiver, crie um UsuarioService e injete-o.

		// Temporariamente, vou usar o save do sobremesaService para ilustrar, mas você vai querer um UsuarioService dedicado.
		// Vamos criar um UsuarioService e injetá-lo. (Vá para o Passo 8)
		redirectAttributes.addFlashAttribute("mensagemSucesso", "Usuário cadastrado com sucesso!");
		return "redirect:/login"; // Redireciona para a página de login
	}

	@PostMapping("/salvar")
	public String salvar(@ModelAttribute Sobremesa sobremesa, BindingResult result, RedirectAttributes redirectAttributes) { // @Valid REMOVIDO AQUI

		// Tratamento para cadastro vs. edição (Questão 7) - FAZER ISSO ANTES DA VALIDAÇÃO MANUAL
		if (sobremesa.getId() == null) { // Se o ID é nulo, é um novo cadastro
			// Seleciona aleatoriamente uma imagem da pasta static (Questão 7)
			String randomImgUrl = sobremesaService.getRandomImageUrl();
			sobremesa.setImgUrl(randomImgUrl);
			sobremesa.setIsDeleted(null); // Garante que um novo item não seja marcado como deletado
		} else {
			// Se o ID não é nulo, é uma edição.
			// Nenhuma lógica de imagem aleatória para edição (a imagem original permanece).
			// Precisamos obter a imgUrl original para que a validação não falhe se não foi um campo de form.
			// AQUI É FUNDAMENTAL: Se o form de edição não envia a imgUrl (como é o caso),
			// você PRECISA carregar a Sobremesa original do banco para ter a imgUrl.
			Optional<Sobremesa> original = sobremesaService.findById(sobremesa.getId());
			if (original.isPresent()) {
				// Copia a imgUrl original para o objeto que será validado e salvo.
				sobremesa.setImgUrl(original.get().getImgUrl());
				sobremesa.setIsDeleted(original.get().getIsDeleted()); // Mantenha o status isDeleted original
			}
		}

		// Validação manual dos atributos do modelo (Questão 7) - AGORA AQUI
		validator.validate(sobremesa, result); // Executa a validação manualmente após preencher imgUrl

		if (result.hasErrors()) { // Verifica se há erros de validação
			// Se houver erros, retorna para o formulário de cadastro ou edição, mantendo os dados preenchidos e exibindo os erros.
			if (sobremesa.getId() == null) {
				return "cadastro"; // Retorna para o formulário de cadastro com erros
			} else {
				return "editar"; // Retorna para o formulário de edição com erros
			}
		}

		// Salva ou atualiza o item no banco de dados
		sobremesaService.save(sobremesa);

		// Redireciona para /admin com mensagem de sucesso (Questão 7)
		redirectAttributes.addFlashAttribute("mensagemSucesso", "Operação realizada com sucesso!");
		return "redirect:/admin";
	}
}