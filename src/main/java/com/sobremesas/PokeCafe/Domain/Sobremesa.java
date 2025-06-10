package com.sobremesas.PokeCafe.Domain; // Pacote onde a classe Sobremesa está localizada.
// A organização do projeto em pastas como 'domain' (ou 'model') para classes de dados
// é uma boa prática demonstrada no slide 14 da Aula 10 - Spring MVC.

import jakarta.persistence.*; // Importa anotações JPA para mapeamento objeto-relacional.
// A JPA (Java Persistence API) é mencionada no slide 2 da Aula 10 - Spring MVC.
import jakarta.validation.constraints.*; // Importa anotações para validação de dados.
// A biblioteca 'spring-boot-starter-validation' inclui essas anotações e é explicada nos slides 39 e 40 da Aula 10 - Spring MVC.
import lombok.Data; // Importa anotação do Lombok para geração automática de código.
// O Lombok é introduzido no slide 2 da Aula 10 - Spring MVC para "criação de código automático de getters, setters, construtores, etc.".
import lombok.NoArgsConstructor; // Importa anotação do Lombok para construtor sem argumentos.
// Faz parte das funcionalidades do Lombok.
import lombok.AllArgsConstructor; // Importa anotação do Lombok para construtor com todos os argumentos.
// Faz parte das funcionalidades do Lombok.
import java.util.Date; // Importa a classe Date para o atributo isDeleted.
// O uso de tipos de dados básicos do Java, como Date, é implícito em exemplos de classes de modelo.

@Data // Esta anotação do Lombok gera automaticamente métodos como getters e setters para todos os atributos,
// toString(), equals() e hashCode(), simplificando o código.
@NoArgsConstructor // Esta anotação do Lombok gera um construtor sem argumentos, que é necessário para a JPA e para frameworks como o Spring.
@AllArgsConstructor // Esta anotação do Lombok gera um construtor com todos os argumentos da classe, útil para inicializar objetos.
@Entity // Esta anotação do pacote `jakarta.persistence` marca a classe `Sobremesa` como uma entidade JPA.
// Isso significa que ela pode ser mapeada para uma tabela em um banco de dados relacional.
@Table(name = "sobremesa") // Mapeia esta entidade para a tabela "sobremesa" no banco de dados.
// Embora '@Table' não esteja explicitamente em um slide, ela faz parte do mapeamento JPA,
// um conceito coberto pelo Spring Data JPA.
public class Sobremesa {

    @Id // Esta anotação marca o atributo `id` como a chave primária da entidade no banco de dados.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Esta anotação configura a estratégia de geração automática de valores para o `id`.
    // `GenerationType.IDENTITY` indica que o banco de dados se encarregará de gerar o ID (geralmente para colunas de auto-incremento).
    private Long id; // Atributo obrigatório solicitado na Questão 1 da prova.

    // Atributo para soft delete, solicitado na Questão 1 e detalhado na Questão 3 da prova.
    // Permite marcar um registro como "deletado" logicamente, sem removê-lo fisicamente do banco de dados.
    private Date isDeleted;

    @NotBlank(message = "A URL da imagem é obrigatória") // Esta anotação de validação garante que a string `imgUrl` não seja nula nem contenha apenas espaços em branco.
    // A mensagem personalizada é exibida se a validação falhar.
    private String imgUrl; // Atributo obrigatório solicitado na Questão 1 da prova.

    @NotBlank(message = "O nome da sobremesa é obrigatório") // Garante que o campo `nome` não seja nulo nem vazio.
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres") // Garante que o comprimento da string `nome` esteja entre 3 e 100 caracteres.
    private String nome;

    @NotBlank(message = "A descrição é obrigatória") // Garante que o campo `descricao` não seja nulo nem vazio.
    @Size(min = 10, max = 255, message = "A descrição deve ter entre 10 e 255 caracteres") // Garante que o comprimento da string `descricao` esteja entre 10 e 255 caracteres.
    private String descricao;

    @NotNull(message = "O preço é obrigatório") // Garante que o campo `preco` não seja nulo.
    @DecimalMin(value = "0.01", message = "O preço mínimo é R$ 0,01") // Garante que o valor decimal de `preco` seja no mínimo 0.01.
    // A anotação '@Min' para valores numéricos é mencionada nos slides, e '@DecimalMin' é uma variação similar para decimais.
    private Double preco;

    @NotBlank(message = "A categoria é obrigatória") // Garante que o campo `categoria` não seja nulo nem vazio.
    private String categoria; // Utilizado para classificar a sobremesa (ex: 'bolo', 'fruta', 'drink').
    // Este é um dos atributos adicionais para cumprir o requisito de "pelo menos 7 atributos".

    // Total de atributos na classe: 7 (id, isDeleted, imgUrl, nome, descricao, preco, categoria).
    // Isso atende ao requisito da Questão 1 de ter "pelo menos 7 atributos".
}