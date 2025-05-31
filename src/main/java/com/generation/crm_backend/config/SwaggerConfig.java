package com.generation.crm_backend.config;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI talentysOpenAPI() {
    String descricao = "O Sistema de Cadastro de RH (Talentys) é uma aplicação backend desenvolvida para gerenciar os principais processos da área de Recursos Humanos.\n\n"
        +
        "Esta API RESTful foi construída com Java e Spring Boot, seguindo boas práticas de arquitetura e segurança. " +
        "Ela fornece endpoints para integração com futuros sistemas frontend ou outras aplicações corporativas.\n\n" +
        "**Principais Funcionalidades da API:**\n" +
        "* Cadastro e gerenciamento de colaboradores\n" +
        "* Controle de recrutamento e seleção\n" +
        "* Registro de jornada de trabalho e controle de ponto\n" +
        "* Geração de dados para folha de pagamento\n" +
        "* Gerenciamento de treinamentos e desenvolvimento";

    return new OpenAPI()
        .info(new Info()
            .title("ConnectUs")
            .description(descricao)
            .version("v0.0.1")
            .license(new License()
                .name("Generations Brasil")
                .url("https://brazil.generation.org/"))
            .contact(new Contact()
                .name("Grupo 02 - Turma Java 82 (Projeto ConnectUs)")
                .url("https://github.com/Grupo-02-Turma-Java-82/Connectus_CRM")
                .email("grupo02turmajava82@hotmail.com")))
        .externalDocs(new ExternalDocumentation()
            .description("Github do Projeto")
            .url("https://github.com/Grupo-02-Turma-Java-82/Connectus_CRM"));
  }

  @Bean
  OpenApiCustomizer customerGlobalHeaderOpenApiCustomiser() {

    return openApi -> {
      openApi.getPaths().values().forEach(pathItem -> pathItem.readOperations().forEach(operation -> {

        ApiResponses apiResponses = operation.getResponses();

        apiResponses.addApiResponse("200", createApiResponse("Sucesso!"));
        apiResponses.addApiResponse("201", createApiResponse("Objeto Persistido!"));
        apiResponses.addApiResponse("204", createApiResponse("Objeto Excluído!"));
        apiResponses.addApiResponse("400", createApiResponse("Erro na Requisição!"));
        apiResponses.addApiResponse("401", createApiResponse("Acesso Não Autorizado!"));
        apiResponses.addApiResponse("403", createApiResponse("Acesso Proibido!"));
        apiResponses.addApiResponse("404", createApiResponse("Objeto Não Encontrado!"));
        apiResponses.addApiResponse("500", createApiResponse("Erro na Aplicação!"));

      }));
    };
  }

  private ApiResponse createApiResponse(String message) {

    return new ApiResponse().description(message);

  }
}