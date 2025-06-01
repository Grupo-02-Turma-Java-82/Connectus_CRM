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
  public OpenAPI connectUsCrmOpenAPI() {
    String descricao = "A API ConnectUs CRM é uma solução backend robusta, desenvolvida em Java com Spring Boot, " +
        "projetada para otimizar e gerenciar interações e relacionamentos com clientes.\n\n" +
        "Esta API RESTful oferece um conjunto de endpoints para facilitar a integração com sistemas frontend " +
        "e outras aplicações corporativas, centralizando as informações e processos de CRM.\n\n" +
        "**Principais Funcionalidades da API:**\n" +
        "* **Gerenciamento de Clientes (CRUD):** Cadastro, consulta, atualização, exclusão e busca de clientes (pessoas físicas e jurídicas).\n"
        +
        "* **Gestão de Oportunidades de Venda (CRUD):** Criação, acompanhamento, atualização de status (Aberta, Fechada, Perdida) e exclusão de oportunidades.\n"
        +
        "* **Gerenciamento de Usuários do Sistema:** Cadastro, consulta e atualização de usuários internos.\n" +
        "* **Relacionamentos:** Associação de oportunidades a clientes e usuários responsáveis.\n" +
        "* **Qualificação de Leads:** Cálculo e atribuição de Lead Score para clientes.\n";

    return new OpenAPI()
        .info(new Info()
            .title("ConnectUs CRM API")
            .description(descricao)
            .version("v0.0.1")
            .license(new License()
                .name("Generation Brasil / Grupo 02 - Turma Java 82")
                .url("https://brazil.generation.org/"))
            .contact(new Contact()
                .name("Grupo 02 - Turma Java 82 (Projeto ConnectUs CRM)")
                .url("https://github.com/Grupo-02-Turma-Java-82/Connectus_CRM")
                .email("grupo02turmajava82@hotmail.com")))
        .externalDocs(new ExternalDocumentation()
            .description("Repositório do Projeto no Github")
            .url("https://github.com/Grupo-02-Turma-Java-82/Connectus_CRM"));
  }

  @Bean
  OpenApiCustomizer customerGlobalHeaderOpenApiCustomiser() {
    return openApi -> {
      openApi.getPaths().values().forEach(pathItem -> pathItem.readOperations().forEach(operation -> {
        ApiResponses apiResponses = operation.getResponses();
        apiResponses.addApiResponse("200", createApiResponse("Sucesso!"));
        apiResponses.addApiResponse("201", createApiResponse("Objeto Criado/Persistido!"));
        apiResponses.addApiResponse("204",
            createApiResponse("Operação bem-sucedida, sem conteúdo de retorno (Ex: Objeto Excluído)."));
        apiResponses.addApiResponse("400", createApiResponse("Erro na Requisição (Ex: Dados inválidos)."));
        apiResponses.addApiResponse("401", createApiResponse("Acesso Não Autorizado (Requer autenticação)."));
        apiResponses.addApiResponse("403", createApiResponse("Acesso Proibido (Autenticado, mas sem permissão)."));
        apiResponses.addApiResponse("404", createApiResponse("Recurso Não Encontrado."));
        apiResponses.addApiResponse("500", createApiResponse("Erro Interno na Aplicação."));
      }));
    };
  }

  private ApiResponse createApiResponse(String message) {
    return new ApiResponse().description(message);
  }
}