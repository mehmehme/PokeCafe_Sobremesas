package com.sobremesas.PokeCafe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.util.concurrent.TimeUnit; // Para CacheControl

@SpringBootApplication
public class PokeCafeApplication implements WebMvcConfigurer { // Implementa WebMvcConfigurer

    public static void main(String[] args) {
        SpringApplication.run(PokeCafeApplication.class, args);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) { // Configura o tratamento de recursos estáticos
        registry.addResourceHandler("/images/**") // Acessível via URL /images/
                .addResourceLocations("classpath:/static/images/") // Onde os arquivos estão localizados
                .setCacheControl(org.springframework.http.CacheControl.maxAge(2, TimeUnit.HOURS).cachePublic()); // Configuração de cache
    }
}