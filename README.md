
---

````markdown
# EcoTech - API (Spring Boot)

API backend construída em **Spring Boot (Java 21)** para o projeto EcoTech.  
Responsável pela **autenticação JWT**, **gestão de usuários**, e integração com os módulos do aplicativo mobile (**Flutter**) e do painel administrativo (**Angular**).  

O backend é o **núcleo da solução**, garantindo segurança, escalabilidade e centralização dos dados.

![Java](https://img.shields.io/badge/Java-21-red) ![SpringBoot](https://img.shields.io/badge/SpringBoot-3.x-brightgreen) ![Swagger](https://img.shields.io/badge/Swagger-UI-orange) ![License](https://img.shields.io/badge/License-Academic-lightgrey)

---

## ✨ Funcionalidades

- **Autenticação JWT**  
  - Registro e login de usuários  
  - Proteção de rotas por perfil  

- **Gestão de usuários**  
  - CRUD básico  
  - Conversão de entidades para DTOs  

- **Documentação automática com Swagger UI**  
  - Disponível em `/swagger-ui.html`  

- **Banco de dados**  
  - **H2 Database** (ambiente de desenvolvimento)  
  - **PostgreSQL** (produção)  

---

## 🛠️ Tecnologias Utilizadas

- **Java 21**  
- **Spring Boot 3.x**  
- **Spring Security + JWT**  
- **Spring Data JPA**  
- **H2 Database**  
- **PostgreSQL**  
- **Swagger UI**  

---

## 🚀 Como Executar o Projeto

```bash
# Clone o repositório
git clone https://github.com/ThiagoAlvesFeitosa/ecotech_api.git

# Acesse a pasta
cd ecotech_api

# Execute a aplicação (necessário JDK 21)
./mvnw spring-boot:run
````

Após rodar, acesse no navegador:
👉 [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## 📌 Status do Projeto

✅ Autenticação JWT implementada
✅ Estrutura modular (models, controllers, services, repositories)
✅ Documentação com Swagger UI
🚧 Expansão de endpoints (coletas, pontos, recompensas, impacto ambiental)
🚧 Deploy em ambiente cloud

---

## 📖 Contexto Acadêmico

Este repositório corresponde ao **backend do EcoTech**, parte do **Enterprise Challenge FIAP 2025**.
O projeto busca alinhar **Sociedade 5.0, IoT e Sustentabilidade**, por meio de uma solução integrada:

* **Aplicativo mobile (Flutter)**
* **API (Spring Boot)**
* **Admin Panel (Angular)**

---

```

---

```
