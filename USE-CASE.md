# Testar o MCP com uma LLM

Inicie a API e o MCP e conecte o OpenCode conforme o [README](README.md). Envie as mensagens abaixo em ordem, na mesma conversa, mantendo a API iniciada durante o teste.

Use sempre os IDs retornados nesta execução. Um HTTP 404 indica que o registro não foi encontrado; para verificar se um DELETE alterou dados, confira a existência dos registros antes e depois da chamada.

## 1. Listar os registros

```text
Use as ferramentas do MCP partituras para executar este teste. Guarde nesta conversa os IDs retornados nos próximos cadastros e use esses IDs nas operações seguintes. Mostre os resultados das ferramentas em cada etapa.

Para começar, liste os compositores, as partituras e as sessões de estudo, mostrando seus IDs.
```

Ferramentas: `listar_compositores`, `listar_partituras` e `listar_sessoes_estudo`.

## 2. Cadastrar compositor e partitura

```text
Cadastre um compositor chamado "Compositor Teste MCP".

Depois, usando o ID retornado, cadastre uma partitura com estes dados:
- Título: Estudo Teste MCP
- Instrumento: Piano
- Dificuldade: Iniciante
- Status: Em andamento
- Caminho do PDF: nulo

Mostre os IDs do compositor e da partitura criados.
```

Ferramentas: `cadastrar_compositor` e `cadastrar_partitura`.

## 3. Registrar estudos

```text
Registre duas sessões de estudo para a partitura que acabamos de cadastrar:

1. Data 2026-10-29, duração de 20 minutos, observação "Estudo das mãos separadas".
2. Data 2026-10-30, duração de 25 minutos, observação "Revisão da peça completa".

Mostre o ID de cada sessão e guarde qual corresponde ao primeiro estudo.
```

Ferramenta: `registrar_estudo`, chamada duas vezes.

## 4. Buscar por ID

```text
Busque por ID o compositor, a partitura e a primeira sessão de estudo que criamos. Mostre os dados retornados, incluindo os relacionamentos entre eles.
```

Ferramentas: `buscar_compositor`, `buscar_partitura` e `buscar_sessao_estudo`.

Esperado: a partitura vinculada ao compositor criado e a sessão vinculada à partitura, com 20 minutos.

## 5. Consultar os estudos e o total de minutos

```text
Use buscar_estudos_por_compositor para consultar as sessões do compositor que criamos.

Depois, use total_minutos_por_partitura para consultar o tempo total estudado da partitura que criamos.
```

Esperado: duas sessões e **45 minutos** no total. Essas ferramentas usam, respectivamente, as consultas com `JOIN` e `SUM` da API.

## 6. Atualizar os registros

```text
Atualize os registros que criamos, usando os mesmos IDs:

1. Compositor: altere o nome para "Compositor Teste MCP Revisado".
2. Partitura: use o título "Estudo Teste MCP Revisado", o mesmo compositor, instrumento Piano, dificuldade Iniciante, status Concluída e caminho do PDF nulo.
3. Primeira sessão de estudo: mantenha a mesma partitura e a data 2026-10-29, altere a duração para 30 minutos e a observação para "Prática com metrônomo".

Depois, busque os três registros por ID para conferir as alterações e consulte novamente o total de minutos da partitura pela ferramenta correspondente.
```

Ferramentas de atualização: `atualizar_compositor`, `atualizar_partitura` e `atualizar_estudo`.

Esperado: mesmos IDs, dados atualizados, segunda sessão mantida em 25 minutos e total de **55 minutos**. A atualização substitui os dados do registro; por isso a mensagem informa também os campos que serão mantidos.

## 7. Conferir o bloqueio de exclusão (opcional)

```text
Primeiro, busque por ID o compositor, a partitura e as duas sessões que criamos e confira os vínculos. Se algum deles retornar 404, pare este passo e informe que o conjunto de teste está incompleto.

Com os registros confirmados, tente excluir o compositor enquanto ele ainda possui a partitura. Depois, tente excluir essa partitura enquanto ela ainda possui as sessões de estudo.

Mostre os erros retornados pela API e consulte novamente os registros para conferir que continuam presentes. Mantenha os registros vinculados, sem tentar removê-los para contornar o erro.
```

Esperado: erro **HTTP 409** nas duas tentativas, com os registros preservados.

## 8. Excluir os registros do teste

```text
Exclua os registros criados nesta conversa, usando os IDs que guardamos, nesta ordem:

1. As duas sessões de estudo.
2. A partitura.
3. O compositor.

Depois, liste os compositores, as partituras e as sessões para confirmar que os IDs do nosso teste desapareceram. Preserve os demais registros do banco.
```

Ferramentas de exclusão: `excluir_estudo`, `excluir_partitura` e `excluir_compositor`.

Esperado: os registros do teste removidos, sem conflito de relacionamento.
