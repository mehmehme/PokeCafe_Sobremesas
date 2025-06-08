package com.sobremesas.PokeCafe.Service;

import com.sobremesas.PokeCafe.Domain.Sobremesa;
import com.sobremesas.PokeCafe.Repository.SobremesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import org.springframework.core.io.ResourceLoader; // Certifique-se que este import está aqui
import org.springframework.core.io.Resource; // Certifique-se que este import está aqui
import java.io.IOException; // Certifique-se que este import está aqui


@Service
public class SobremesaService {

    private final SobremesaRepository repository;
    private final ResourceLoader resourceLoader; // Certifique-se que este está injetado

    @Autowired
    public SobremesaService(SobremesaRepository repository, ResourceLoader resourceLoader) {
        this.repository = repository;
        this.resourceLoader = resourceLoader;
    }

    public List<Sobremesa> findAllActive() { // <--- ESTE MÉTODO DEVE ESTAR AQUI
        return repository.findByIsDeletedIsNull();
    }

    public List<Sobremesa> findAll() { // <--- E ESTE TAMBÉM
        return repository.findAll();
    }

    public Optional<Sobremesa> findById(Long id) {
        return repository.findById(id);
    }

    public Sobremesa save(Sobremesa sobremesa) {
        return repository.save(sobremesa);
    }

    public String getRandomImageUrl() { // <--- E ESTE TAMBÉM
        List<String> imageNames = new ArrayList<>();
        try {
            Resource resource = resourceLoader.getResource("classpath:static/images/");
            if (resource.exists() && resource.getFile().isDirectory()) {
                for (java.io.File file : resource.getFile().listFiles()) {
                    if (file.isFile()) {
                        imageNames.add(file.getName());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao listar imagens estáticas: " + e.getMessage());
        }

        if (!imageNames.isEmpty()) {
            Random random = new Random();
            return imageNames.get(random.nextInt(imageNames.size()));
        }
        return "default.jpg";
    }

    public void softDelete(Long id) {
        // Busca a sobremesa pelo ID
        Optional<Sobremesa> sobremesaOptional = repository.findById(id);
        if (sobremesaOptional.isPresent()) {
            Sobremesa sobremesa = sobremesaOptional.get();
            sobremesa.setIsDeleted(new Date()); // Define isDeleted com a data/hora atual
            repository.save(sobremesa); // Salva a sobremesa atualizada no banco
        }
    }

    public void restore(Long id) {
        // Busca a sobremesa pelo ID
        Optional<Sobremesa> sobremesaOptional = repository.findById(id);
        if (sobremesaOptional.isPresent()) {
            Sobremesa sobremesa = sobremesaOptional.get();
            sobremesa.setIsDeleted(null); // Define isDeleted como null
            repository.save(sobremesa); // Salva a sobremesa atualizada no banco
        }
    }
}