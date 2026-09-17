# Sistema de Cadastro de Produtos

[![CI](https://github.com/henrique589/SistemaCadastroProdutos/actions/workflows/ci.yml/badge.svg)](https://github.com/henrique589/SistemaCadastroProdutos/actions/workflows/ci.yml)

Aplicação web para cadastro de produtos **físicos** e **digitais**, com API REST em **Java 17 / Spring Boot 3** e interface em **Angular 17**, persistindo em **PostgreSQL**.

O projeto nasceu como uma aplicação desktop em Java Swing (preservada em [`legacy-swing/`](legacy-swing/)) e foi evoluído para uma arquitetura web em camadas, mantendo o mesmo domínio: uma hierarquia `Produto → ProdutoFisico | ProdutoDigital` com regras de validação (nome obrigatório, valores não negativos).

```
┌──────────────┐   HTTP/JSON   ┌──────────────────┐   JPA/JDBC   ┌────────────┐
│  Angular 17  │ ────────────► │  Spring Boot 3   │ ───────────► │ PostgreSQL │
│  (frontend)  │ ◄──────────── │  (backend/api)   │ ◄─────────── │    16      │
└──────────────┘  RFC 7807     └──────────────────┘   Flyway     └────────────┘
```

## Funcionalidades

- CRUD completo de produtos, com campos específicos por tipo (peso/dimensões para físicos; tamanho do arquivo para digitais)
- Listagem paginada com filtro por nome e por tipo
- Validação em duas camadas: Bean Validation na borda da API e invariantes no domínio (as regras do sistema original, agora impossíveis de burlar)
- Erros padronizados no formato **RFC 7807** (`application/problem+json`), consumidos pelo frontend para exibir mensagens amigáveis
- Documentação interativa da API com **Swagger UI** (`/swagger-ui.html`)
- Schema versionado com **Flyway**; Hibernate roda em modo `validate`
- Testes automatizados nas duas pontas e pipeline de **CI no GitHub Actions**
- Empacotamento com **Docker** (multi-stage) e orquestração com **docker-compose**

## Stack

| Camada    | Tecnologias |
|-----------|-------------|
| Backend   | Java 17, Spring Boot 3.3 (Web, Data JPA, Validation, Actuator), Hibernate 6, Flyway, springdoc-openapi, Maven |
| Frontend  | Angular 17 (standalone components, signals, reactive forms, control flow `@if/@for`), Bootstrap 5, RxJS |
| Banco     | PostgreSQL 16 (H2 em memória apenas nos testes) |
| Testes    | JUnit 5, Mockito, AssertJ, MockMvc · Jasmine, Karma, HttpTestingController |
| Infra     | Docker, docker-compose, nginx, GitHub Actions |

## Como executar

### Opção 1 — tudo com Docker (recomendado)

Pré-requisito: Docker Desktop.

```bash
docker compose up --build
```

| Serviço     | URL |
|-------------|-----|
| Frontend    | http://localhost |
| API         | http://localhost:8080/api/produtos |
| Swagger UI  | http://localhost:8080/swagger-ui.html |
| Postgres    | `localhost:5433` (usuário/senha `cadastro`) |

> A porta do Postgres no host é **5433** por padrão (para não colidir com uma instalação local na 5432). Altere com `DB_HOST_PORT=5432 docker compose up`.

### Opção 2 — desenvolvimento local

Pré-requisitos: JDK 17, Node 20, Docker (só para o banco).

```bash
# 1) Banco
docker compose up -d db

# 2) Backend (porta 8080) — o wrapper baixa o Maven na primeira execução
cd backend
DB_PORT=5433 ./mvnw spring-boot:run        # Windows: set DB_PORT=5433 && mvnw.cmd spring-boot:run

# 3) Frontend (porta 4200, com proxy /api -> 8080)
cd frontend
npm install
npm start
```

Variáveis de ambiente do backend (todas com padrão): `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD`, `SERVER_PORT`, `CORS_ORIGINS`.

## Testes

```bash
# Backend: 20 testes (domínio, serviço com Mockito e integração da API com MockMvc + H2 + Flyway)
cd backend && ./mvnw test

# Frontend: 12 testes (serviço HTTP, formulário reativo e componente raiz)
cd frontend && npx ng test --watch=false --browsers=ChromeHeadless
```

## API

| Método | Rota                  | Descrição |
|--------|-----------------------|-----------|
| GET    | `/api/produtos`       | Lista paginada. Parâmetros: `tipo` (`FISICO`/`DIGITAL`), `nome`, `page`, `size`, `sort` |
| GET    | `/api/produtos/{id}`  | Busca por id |
| POST   | `/api/produtos`       | Cria (retorna `201` + header `Location`) |
| PUT    | `/api/produtos/{id}`  | Atualiza (não permite trocar o tipo) |
| DELETE | `/api/produtos/{id}`  | Remove (`204`) |

Exemplo de criação:

```json
POST /api/produtos
{ "tipo": "FISICO", "nome": "Teclado mecânico", "preco": 349.90, "pesoKg": 0.85, "dimensoesCm": 44 }
```

Exemplo de erro (RFC 7807):

```json
HTTP 400
{
  "type": "about:blank",
  "title": "Regra de negócio violada",
  "status": 400,
  "detail": "O campo 'pesoKg' deve ser preenchido.",
  "instance": "/api/produtos",
  "timestamp": "2026-09-17T03:32:45Z"
}
```

## Estrutura do repositório

```
backend/
  src/main/java/com/henrique/cadastroprodutos/
    domain/       entidades JPA (Produto abstrata + subclasses, SINGLE_TABLE) e regras de negócio
    repository/   Spring Data + Specifications para filtros dinâmicos
    service/      casos de uso e transações
    dto/          records de entrada/saída da API
    web/          controller REST e handler global de exceções (ProblemDetail)
    config/       CORS e OpenAPI
  src/main/resources/db/migration/   migrações Flyway
  src/test/java/                      testes unitários e de integração
frontend/
  src/app/produtos/   modelo, serviço HTTP, lista e formulário
  src/app/shared/     notificações e tratamento de erros da API
legacy-swing/         versão original em Java Swing (referência histórica)
docker-compose.yml
.github/workflows/ci.yml
```

## Decisões de projeto

- **Herança `SINGLE_TABLE`** no JPA: os subtipos têm poucos campos próprios, então uma tabela única evita *joins* e mantém as consultas simples. A coluna `tipo` é o discriminador e tem `CHECK` no banco.
- **Validação no domínio, não só no DTO**: o sistema Swing original tinha validação nos *setters*, mas o construtor não os chamava — o bug clássico de invariante furada. Aqui o construtor delega aos *setters*, então não existe caminho para criar um produto inválido.
- **Specifications** em vez de JPQL com parâmetros nulos: filtros opcionais combináveis sem depender de como cada banco trata `:param is null`.
- **`ProblemDetail` (RFC 7807)**: um único formato de erro para 400/404/500, o que simplifica o tratamento no Angular (`shared/erro-api.ts`).
- **Tipo imutável após criação**: trocar um produto de físico para digital mudaria o conjunto de campos obrigatórios; a API rejeita com 400 e o formulário desabilita o campo na edição.
- **H2 apenas em testes**, em modo PostgreSQL, rodando as mesmas migrações Flyway — os testes de integração exercitam o schema real sem exigir Docker no CI.

## Próximos passos

- Autenticação/autorização com Spring Security + JWT
- Categorias de produto e upload de imagem
- Testes end-to-end com Playwright
- Observabilidade: métricas Prometheus via Actuator

## Autor

**Henrique Azevedo Andrade Silva** — [LinkedIn](https://www.linkedin.com/in/henriqueaandradesilva) · [GitHub](https://github.com/henrique589)
