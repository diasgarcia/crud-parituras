"""Servidor MCP via HTTP na porta 8000, consumindo a API Spring na porta 8080."""

import os
from typing import Literal, TypedDict

import httpx
from fastmcp import FastMCP
from fastmcp.exceptions import ToolError
from pydantic import TypeAdapter, ValidationError


class Compositor(TypedDict):
    id: int
    nome: str


class Partitura(TypedDict):
    id: int
    titulo: str
    instrumento: str | None
    dificuldade: str | None
    status: str | None
    caminhoPdf: str | None
    compositor: Compositor


class SessaoEstudo(TypedDict):
    id: int
    partitura: Partitura
    data: str
    minutos: int
    observacoes: str | None


API_URL = os.getenv("PARTITURAS_API_URL", "http://localhost:8080").rstrip("/")

mcp = FastMCP("partituras")


def requisicao[T](
    metodo: Literal["GET", "POST", "PUT", "DELETE"],
    caminho: str,
    tipo_resposta: type[T],
    dados: dict[str, object] | None = None,
) -> T:
    """Faz uma requisição à API Spring e valida o JSON conforme o tipo esperado."""
    try:
        resposta = httpx.request(
            metodo,
            f"{API_URL}{caminho}",
            json=dados,
            timeout=10,
        )
        resposta.raise_for_status()
    except httpx.HTTPStatusError as erro:
        raise ToolError(
            f"A API retornou HTTP {erro.response.status_code}: {erro.response.text}"
        ) from erro
    except httpx.RequestError as erro:
        raise ToolError(
            f"Não foi possível acessar a API em {API_URL}. Verifique se o Spring está rodando."
        ) from erro

    conteudo = b"null" if resposta.status_code == 204 else resposta.content
    try:
        return TypeAdapter[T](tipo_resposta).validate_json(conteudo, strict=True)
    except ValidationError as erro:
        raise ToolError("A API retornou uma resposta em formato inesperado.") from erro


@mcp.tool()
def listar_compositores() -> list[Compositor]:
    """Lista todos os compositores cadastrados."""
    return requisicao("GET", "/compositores", list[Compositor])


@mcp.tool()
def buscar_compositor(compositor_id: int) -> Compositor:
    """Busca um compositor pelo ID."""
    return requisicao("GET", f"/compositores/{compositor_id}", Compositor)


@mcp.tool()
def cadastrar_compositor(nome: str) -> Compositor:
    """Cadastra um compositor e retorna o registro com o ID gerado."""
    return requisicao("POST", "/compositores", Compositor, {"nome": nome})


@mcp.tool()
def atualizar_compositor(compositor_id: int, nome: str) -> Compositor:
    """Atualiza o nome de um compositor existente."""
    return requisicao("PUT", f"/compositores/{compositor_id}", Compositor, {"nome": nome})


@mcp.tool()
def excluir_compositor(compositor_id: int) -> str:
    """Exclui um compositor. A API bloqueia a exclusão se ele tiver partituras."""
    requisicao("DELETE", f"/compositores/{compositor_id}", type(None))
    return "Compositor excluído."


@mcp.tool()
def listar_partituras() -> list[Partitura]:
    """Lista todas as partituras com seus compositores."""
    return requisicao("GET", "/partituras", list[Partitura])


@mcp.tool()
def buscar_partitura(partitura_id: int) -> Partitura:
    """Busca uma partitura pelo ID."""
    return requisicao("GET", f"/partituras/{partitura_id}", Partitura)


@mcp.tool()
def cadastrar_partitura(
    titulo: str,
    compositor_id: int,
    instrumento: str | None = None,
    dificuldade: str | None = None,
    status: str | None = None,
    caminho_pdf: str | None = None,
) -> Partitura:
    """Cadastra uma partitura vinculada a um compositor existente.

    Para preencher caminho_pdf, identifique a obra pelo título e compositor:
    - Se a obra não puder ser identificada com segurança, use None.
    - Se a obra foi identificada e o usuário enviou um link, salve esse link
      exatamente como foi informado.
    - Se a obra foi identificada e o usuário não enviou um link, use a pesquisa
      e navegação disponíveis no cliente para encontrar um PDF ou link de download
      em um repositório de partituras públicas, preferencialmente o IMSLP.
      Confira se o link corresponde à obra e ao compositor antes de salvá-lo.
    - Se o usuário pedir para deixar sem PDF, respeite o pedido e use None.
      Se não houver pesquisa disponível ou um link confirmado, use None e informe
      que não foi possível obter o PDF. Nunca invente um endereço.
    """
    return requisicao("POST", "/partituras", Partitura, {
        "titulo": titulo,
        "compositor": {"id": compositor_id},
        "instrumento": instrumento,
        "dificuldade": dificuldade,
        "status": status,
        "caminhoPdf": caminho_pdf,
    })


@mcp.tool()
def atualizar_partitura(
    partitura_id: int,
    titulo: str,
    compositor_id: int,
    instrumento: str | None = None,
    dificuldade: str | None = None,
    status: str | None = None,
    caminho_pdf: str | None = None,
) -> Partitura:
    """Substitui os dados da partitura. Campos opcionais omitidos ficam nulos.

    Para preencher caminho_pdf, identifique a obra pelo título e compositor:
    - Se a obra não puder ser identificada com segurança, use None.
    - Se a obra foi identificada e o usuário enviou um link, salve esse link
      exatamente como foi informado.
    - Se a obra foi identificada e o usuário não enviou um link, use a pesquisa
      e navegação disponíveis no cliente para encontrar um PDF ou link de download
      em um repositório de partituras públicas, preferencialmente o IMSLP.
      Confira se o link corresponde à obra e ao compositor antes de salvá-lo.
    - Se o usuário pedir para deixar sem PDF, respeite o pedido e use None.
      Se não houver pesquisa disponível ou um link confirmado, use None e informe
      que não foi possível obter o PDF. Nunca invente um endereço.
    """
    return requisicao("PUT", f"/partituras/{partitura_id}", Partitura, {
        "titulo": titulo,
        "compositor": {"id": compositor_id},
        "instrumento": instrumento,
        "dificuldade": dificuldade,
        "status": status,
        "caminhoPdf": caminho_pdf,
    })


@mcp.tool()
def excluir_partitura(partitura_id: int) -> str:
    """Exclui uma partitura. A API bloqueia a exclusão se ela tiver estudos."""
    requisicao("DELETE", f"/partituras/{partitura_id}", type(None))
    return "Partitura excluída."


@mcp.tool()
def listar_sessoes_estudo() -> list[SessaoEstudo]:
    """Lista todas as sessões de estudo cadastradas."""
    return requisicao("GET", "/sessoes-estudo", list[SessaoEstudo])


@mcp.tool()
def buscar_sessao_estudo(sessao_id: int) -> SessaoEstudo:
    """Busca uma sessão de estudo pelo ID."""
    return requisicao("GET", f"/sessoes-estudo/{sessao_id}", SessaoEstudo)


@mcp.tool()
def registrar_estudo(
    partitura_id: int,
    data: str,
    minutos: int,
    observacoes: str | None = None,
) -> SessaoEstudo:
    """Registra um estudo de uma partitura. Informe a data no formato AAAA-MM-DD."""
    return requisicao("POST", "/sessoes-estudo", SessaoEstudo, {
        "partitura": {"id": partitura_id},
        "data": data,
        "minutos": minutos,
        "observacoes": observacoes,
    })


@mcp.tool()
def atualizar_estudo(
    sessao_id: int,
    partitura_id: int,
    data: str,
    minutos: int,
    observacoes: str | None = None,
) -> SessaoEstudo:
    """Substitui os dados de um estudo. A data deve estar no formato AAAA-MM-DD."""
    return requisicao("PUT", f"/sessoes-estudo/{sessao_id}", SessaoEstudo, {
        "partitura": {"id": partitura_id},
        "data": data,
        "minutos": minutos,
        "observacoes": observacoes,
    })


@mcp.tool()
def excluir_estudo(sessao_id: int) -> str:
    """Exclui uma sessão de estudo pelo ID."""
    requisicao("DELETE", f"/sessoes-estudo/{sessao_id}", type(None))
    return "Sessão de estudo excluída."


@mcp.tool()
def buscar_estudos_por_compositor(compositor_id: int) -> list[SessaoEstudo]:
    """Lista os estudos de um compositor usando a consulta com JOIN da API."""
    return requisicao("GET", f"/sessoes-estudo/compositor/{compositor_id}", list[SessaoEstudo])


@mcp.tool()
def total_minutos_por_partitura(partitura_id: int) -> int:
    """Soma os minutos estudados de uma partitura usando a consulta SUM da API."""
    return requisicao("GET", f"/sessoes-estudo/partitura/{partitura_id}/total-minutos", int)


if __name__ == "__main__":
    mcp.run(transport="http", host="0.0.0.0", port=8000)
