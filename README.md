* [ ]

# Partituras MCP

CRUD de compositores, partituras e registros de estudo com Spring Boot, MySQL e um servidor MCP em Python.

## Quickstart

No GitHub, abra o repositório e selecione **Code → Codespaces → Create codespace on main**. Aguarde a preparação do ambiente. Não é necessário instalar Java, Python, Maven ou MySQL no seu computador.

O devcontainer instala Java 21, Maven, Python 3.13 e MySQL 8. Também inicia e configura o banco e executa automaticamente:

```bash
python -m pip install -e ./partituras-mcp
mvn -B -f partituras-api/pom.xml -DskipTests package
```

Com o MySQL iniciado pelo container, execute os comandos abaixo na raiz do projeto, em dois terminais do Codespaces.

**Terminal 1 — API:**

```bash
mvn -f partituras-api/pom.xml spring-boot:run
```

**Terminal 2 — MCP:**

```bash
python partituras-mcp/partituras_mcp.py
```

## Portas e acesso

| Serviço | Porta | Endereço |
| --- | --- | --- |
| API Spring | 8080 | http://localhost:8080 |
| MCP HTTP | 8000 | http://localhost:8000/mcp |
| MySQL | 3306 | localhost:3306 |

MySQL: banco `mcp-partituras`, usuário `rafael`, senha `q1w2e3`.

Os endereços `localhost` são internos ao Codespaces. Para acessar de fora, copie os endereços encaminhados na aba **Ports**: porta 8080 para a API e porta 8000 com `/mcp` no final para o MCP.

Para testar a API, importe `partituras.postman_collection.json` no Postman e configure `api_url` com o endereço encaminhado da porta 8080. Para o MCP, crie uma requisição **MCP → HTTP**, informe o endereço encaminhado da porta 8000 com `/mcp` e clique em **Connect**.

Na configuração atual, iniciar a API recria as tabelas e carrega os exemplos de `import.sql`.

## OpenCode

No OpenCode, com a API e o MCP iniciados, adicione este bloco dentro de `mcp` no seu `opencode.jsonc`:

```json
"partituras": {
  "type": "remote",
  "url": "http://localhost:8000/mcp",
  "enabled": true
}
```

Se o OpenCode estiver fora do Codespaces, use o endereço encaminhado da porta 8000 com `/mcp` no final.
