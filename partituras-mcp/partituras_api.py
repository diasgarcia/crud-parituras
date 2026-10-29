import os

import httpx
from fastmcp.exceptions import ToolError

API_URL = os.getenv("PARTITURAS_API_URL", "http://localhost:8080").rstrip("/")


def requisicao(metodo: str, caminho: str, dados: dict | None = None):
    """Faz uma requisição à API Spring e devolve o JSON da resposta."""
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

    if resposta.status_code == 204:
        return None

    return resposta.json()
