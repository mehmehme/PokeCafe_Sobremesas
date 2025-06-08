package com.sobremesas.PokeCafe.domain; // Pacote onde a classe ItemCafe está localizada

import jakarta.persistence.*; // Importa anotações JPA para mapeamento objeto-relacional
import jakarta.validation.constraints.*; // Importa anotações para validação de dados
import lombok.Data; // Importa anotação do Lombok para geração automática de código
import lombok.NoArgsConstructor; // Importa anotação do Lombok para construtor sem argumentos
import lombok.AllArgsConstructor; // Importa anotação do Lombok para construtor com todos os argumentos
import java.util.Date; // Importa a classe Date para o atributo isDeleted

@Data //  Esta anotação do Lombok gera automaticamente métodos como getters e setters para todos os atributos, toString(), equals() e hashCode(). Isso simplifica o código, evitando a necessidade de escrevê-los manualmente.
@NoArgsConstructor //  Esta anotação do Lombok gera um construtor sem argumentos. É necessário para a JPA e para frameworks como o Spring.
@AllArgsConstructor //  Esta anotação do Lombok gera um construtor com todos os argumentos da classe. Útil para criar objetos com todos os valores de uma vez.
@Entity //  Esta anotação do pacote `jakarta.persistence` marca a classe `ItemCafe` como uma entidade JPA (Java Persistence API). Isso significa que ela pode ser mapeada para uma tabela em um banco de dados relacional.

public class ItemCafe {

    // Atributos obrigatórios conforme a prova: ID, isDeleted, imageUrl

    @Id //  Esta anotação marca o atributo `id` como a chave primária da entidade no banco de dados.
    @GeneratedValue(strategy = GenerationType.IDENTITY) //  Esta anotação configura a estratégia de geração automática de valores para o `id`. `GenerationType.IDENTITY` indica que o banco de dados se encarregará de gerar o ID (geralmente para colunas de auto-incremento).
    private Long id; // Atributo obrigatório: ID (Long)

    private Date isDeleted; // Atributo obrigatório: isDeleted (Date). Utilizado para a técnica de "soft delete", onde os registros não são realmente apagados do banco, mas marcados como deletados.

    @NotBlank(message = "A URL da imagem é obrigatória") //  Esta anotação de validação (`jakarta.validation.constraints`) garante que a string `imgUrl` não seja nula nem contenha apenas espaços em branco. Se a validação falhar, a mensagem "A URL da imagem é obrigatória" será exibida.
    private String imgUrl; // Atributo obrigatório: imageUrl (String). Armazenará o caminho da imagem do item.

    // Atributos específicos do tema PokeCafe (totalizando 7+ atributos, aqui temos 9 no total)

    @NotBlank(message = "O nome da sobremesa é obrigatório") //  Garante que o campo `nome` não seja nulo nem vazio.
    @Size(min = 3, max = 50, message = "O nome deve ter entre 3 e 50 caracteres") //  Garante que o comprimento da string `nome` esteja entre 3 e 50 caracteres.
    private String nome;

    @NotNull(message = "O preço é obrigatório") //  Garante que o campo `preco` não seja nulo.
    @DecimalMin(value = "0.01", message = "O preço mínimo é R$ 0,01") // Garante que o valor decimal de `preco` seja no mínimo 0.01. (Nota: `DecimalMin` não foi explicitamente detalhado nos slides, mas o conceito de `@Min` para valores numéricos  está presente, e `DecimalMin` é uma variação específica para números decimais.)
    private Double preco;

    @NotBlank(message = "A descrição é obrigatória") //  Garante que o campo `descricao` não seja nulo nem vazio.
    @Size(min = 10, max = 255, message = "A descrição deve ter entre 10 e 255 caracteres") //  Garante que o comprimento da string `descricao` esteja entre 10 e 255 caracteres.
    private String descricao;

    @NotBlank(message = "A categoria é obrigatória") //  Garante que o campo `categoria` não seja nulo nem vazio.
    private String categoria; // Ex: 'Sobremesa', 'Bebida', 'Lanche'.

    @NotNull(message = "O tempo de preparo é obrigatório") //  Garante que o campo `tempoPreparo` não seja nulo.
    @Min(value = 1, message = "O tempo mínimo é 1 minuto") //  Garante que o valor de `tempoPreparo` seja no mínimo 1.
    private Integer tempoPreparo; // em minutos

    @NotNull(message = "O status de disponibilidade é obrigatório") //  Garante que o campo `disponivel` não seja nulo.
    private Boolean disponivel; // Indica se o item está disponível para venda.

    // A validação customizada mencionada no seu código pode ser implementada com outras anotações ou lógica de validação mais complexa, mas para o básico da questão 1, as anotações acima são suficientes.
}