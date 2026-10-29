"""Servidor MCP via HTTP na porta 8000, consumindo a API Spring na porta 8080."""

import sys

# Evita que este arquivo mcp.py esconda o pacote mcp usado pelo FastMCP.
if __name__ == "__main__":
    _pasta_do_script = sys.path.pop(0)

from fastmcp import FastMCP

if __name__ == "__main__":
    sys.path.insert(0, _pasta_do_script)

from partituras_api import requisicao

mcp = FastMCP("partituras")


@mcp.tool()
def listar_compositores() -> list[dict]:
    """Lista todos os compositores cadastrados."""
    return requisicao("GET", "/compositores")


@mcp.tool()
def buscar_compositor(compositor_id: int) -> dict:
    """Busca um compositor pelo ID."""
    return requisicao("GET", f"/compositores/{compositor_id}")


@mcp.tool()
def cadastrar_compositor(nome: str) -> dict:
    """Cadastra um compositor e retorna o registro com o ID gerado."""
    return requisicao("POST", "/compositores", {"nome": nome})


@mcp.tool()
def atualizar_compositor(compositor_id: int, nome: str) -> dict:
    """Atualiza o nome de um compositor existente."""
    return requisicao("PUT", f"/compositores/{compositor_id}", {"nome": nome})


@mcp.tool()
def excluir_compositor(compositor_id: int) -> str:
    """Exclui um compositor. A API bloqueia a exclusão se ele tiver partituras."""
    requisicao("DELETE", f"/compositores/{compositor_id}")
    return "Compositor excluído."


@mcp.tool()
def listar_partituras() -> list[dict]:
    """Lista todas as partituras com seus compositores."""
    return requisicao("GET", "/partituras")


@mcp.tool()
def buscar_partitura(partitura_id: int) -> dict:
    """Busca uma partitura pelo ID."""
    return requisicao("GET", f"/partituras/{partitura_id}")


@mcp.tool()
def cadastrar_partitura(
    titulo: str,
    compositor_id: int,
    instrumento: str | None = None,
    dificuldade: str | None = None,
    status: str | None = None,
    caminho_pdf: str | None = None,
) -> dict:
    """Cadastra uma partitura vinculada a um compositor existente."""
    return requisicao("POST", "/partituras", {
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
) -> dict:
    """Substitui os dados da partitura. Campos opcionais omitidos ficam nulos."""
    return requisicao("PUT", f"/partituras/{partitura_id}", {
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
    requisicao("DELETE", f"/partituras/{partitura_id}")
    return "Partitura excluída."


@mcp.tool()
def listar_sessoes_estudo() -> list[dict]:
    """Lista todas as sessões de estudo cadastradas."""
    return requisicao("GET", "/sessoes-estudo")


@mcp.tool()
def buscar_sessao_estudo(sessao_id: int) -> dict:
    """Busca uma sessão de estudo pelo ID."""
    return requisicao("GET", f"/sessoes-estudo/{sessao_id}")


@mcp.tool()
def registrar_estudo(
    partitura_id: int,
    data: str,
    minutos: int,
    observacoes: str | None = None,
) -> dict:
    """Registra um estudo de uma partitura. Informe a data no formato AAAA-MM-DD."""
    return requisicao("POST", "/sessoes-estudo", {
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
) -> dict:
    """Substitui os dados de um estudo. A data deve estar no formato AAAA-MM-DD."""
    return requisicao("PUT", f"/sessoes-estudo/{sessao_id}", {
        "partitura": {"id": partitura_id},
        "data": data,
        "minutos": minutos,
        "observacoes": observacoes,
    })


@mcp.tool()
def excluir_estudo(sessao_id: int) -> str:
    """Exclui uma sessão de estudo pelo ID."""
    requisicao("DELETE", f"/sessoes-estudo/{sessao_id}")
    return "Sessão de estudo excluída."


@mcp.tool()
def buscar_estudos_por_compositor(compositor_id: int) -> list[dict]:
    """Lista os estudos de um compositor usando a consulta com JOIN da API."""
    return requisicao("GET", f"/sessoes-estudo/compositor/{compositor_id}")


@mcp.tool()
def total_minutos_por_partitura(partitura_id: int) -> int:
    """Soma os minutos estudados de uma partitura usando a consulta SUM da API."""
    return requisicao("GET", f"/sessoes-estudo/partitura/{partitura_id}/total-minutos")


if __name__ == "__main__":
    mcp.run(transport="http", host="0.0.0.0", port=8000)
