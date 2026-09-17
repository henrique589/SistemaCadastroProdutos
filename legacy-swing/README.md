# Versão original (Java Swing)

Código da primeira versão do sistema: aplicação desktop com interface Swing, organizada em `model` / `view` / `controle`, que gravava os produtos em um arquivo texto.

Mantida aqui como referência histórica. A versão atual do projeto (API Spring Boot + Angular) está na raiz do repositório e reaproveita o mesmo modelo de domínio (`Produto` abstrata, `ProdutoFisico`, `ProdutoDigital` e as exceções de validação).

Para executar (JDK 17+):

```bash
cd legacy-swing
javac -d out $(find src -name "*.java")
java -cp out sistema_cadastro_produto.Programa
```
