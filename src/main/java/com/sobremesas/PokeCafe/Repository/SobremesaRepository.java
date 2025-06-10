package com.sobremesas.PokeCafe.Repository;

import com.sobremesas.PokeCafe.Domain.Sobremesa; // Importa a sua classe de modelo Sobremesa
import org.springframework.data.jpa.repository.JpaRepository; // Importa a interface JpaRepository
import org.springframework.stereotype.Repository; // Importa a anotação @Repository

import java.util.List; // Para retornar listas de Sobremesa

@Repository // Anotação que indica que esta interface é um componente de repositório Spring.
// O conceito de @Repository é uma especialização de @Component e é mencionado no slide 34 da Aula 02 - Framework Spring.
public interface SobremesaRepository extends JpaRepository<Sobremesa, Long> {
    // JpaRepository<T, ID> fornece métodos CRUD básicos (Create, Read, Update, Delete)
    // para a entidade T (Sobremesa) e o tipo da chave primária ID (Long).
    // Isso é parte do Spring Data JPA, que lida com o mapeamento objeto relacional.

    // Metodo personalizado para buscar sobremesas que não foram deletadas (isDeleted == null).
    // O Spring Data JPA cria a query automaticamente baseada no nome do metodo.
    List<Sobremesa> findByIsDeletedIsNull();
}