package com.partituras;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.partituras.controladores.CompositorControlador;
import com.partituras.controladores.PartituraControlador;
import com.partituras.controladores.SessaoEstudoControlador;
import com.partituras.entidades.Compositor;
import com.partituras.entidades.Partitura;
import com.partituras.repositorios.CompositorRepositorio;
import com.partituras.repositorios.PartituraRepositorio;
import com.partituras.repositorios.SessaoEstudoRepositorio;

class ControladoresTests {

    private CompositorRepositorio compositores;
    private PartituraRepositorio partituras;
    private SessaoEstudoRepositorio sessoes;
    private MockMvc mvc;

    @BeforeEach
    void preparar() {
        compositores = mock(CompositorRepositorio.class);
        partituras = mock(PartituraRepositorio.class);
        sessoes = mock(SessaoEstudoRepositorio.class);
        mvc = MockMvcBuilders.standaloneSetup(
                new CompositorControlador(compositores, partituras),
                new PartituraControlador(partituras, compositores, sessoes),
                new SessaoEstudoControlador(sessoes, partituras, compositores)).build();
    }

    @Test
    void cadastroIgnoraIdEnviadoEUsaCompositorDoBanco() throws Exception {
        Compositor compositor = Compositor.builder().id(3L).nome("Beethoven").build();
        when(compositores.findById(3L)).thenReturn(Optional.of(compositor));

        mvc.perform(post("/partituras").contentType(MediaType.APPLICATION_JSON).content("""
                {"id":99,"titulo":"Für Elise","compositor":{"id":3,"nome":"Outro nome"}}
                """))
                .andExpect(status().isCreated());

        ArgumentCaptor<Partitura> salvo = ArgumentCaptor.forClass(Partitura.class);
        verify(partituras).save(salvo.capture());
        assertNull(salvo.getValue().getId());
        assertSame(compositor, salvo.getValue().getCompositor());
    }

    @Test
    void atualizacaoRespeitaIdDaUrl() throws Exception {
        Partitura existente = Partitura.builder().id(1L).build();
        Compositor compositor = Compositor.builder().id(3L).build();
        when(partituras.findById(1L)).thenReturn(Optional.of(existente));
        when(compositores.findById(3L)).thenReturn(Optional.of(compositor));

        mvc.perform(put("/partituras/1").contentType(MediaType.APPLICATION_JSON).content("""
                {"id":99,"titulo":"Novo título","compositor":{"id":3}}
                """))
                .andExpect(status().isOk());

        ArgumentCaptor<Partitura> salvo = ArgumentCaptor.forClass(Partitura.class);
        verify(partituras).save(salvo.capture());
        assertEquals(1L, salvo.getValue().getId());
        assertEquals("Novo título", salvo.getValue().getTitulo());
        assertSame(compositor, salvo.getValue().getCompositor());
    }

    @Test
    void rejeitaPartituraComCompositorInexistente() throws Exception {
        mvc.perform(post("/partituras").contentType(MediaType.APPLICATION_JSON).content("""
                {"titulo":"Für Elise","compositor":{"id":999}}
                """))
                .andExpect(status().isNotFound());
        verify(partituras, never()).save(any());
    }

    @Test
    void rejeitaSessaoComMinutosNegativos() throws Exception {
        mvc.perform(post("/sessoes-estudo").contentType(MediaType.APPLICATION_JSON).content("""
                {"partitura":{"id":1},"data":"2026-10-28","minutos":-5}
                """))
                .andExpect(status().isBadRequest());
        verify(sessoes, never()).save(any());
    }

    @Test
    void impedeExclusaoDeCompositorComPartituras() throws Exception {
        when(compositores.findById(1L)).thenReturn(Optional.of(new Compositor()));
        when(partituras.existsByCompositorId(1L)).thenReturn(true);
        mvc.perform(delete("/compositores/1")).andExpect(status().isConflict());
        verify(compositores, never()).delete(any(Compositor.class));
    }

    @Test
    void impedeExclusaoDePartituraComSessoes() throws Exception {
        when(partituras.findById(1L)).thenReturn(Optional.of(new Partitura()));
        when(sessoes.existsByPartituraId(1L)).thenReturn(true);
        mvc.perform(delete("/partituras/1")).andExpect(status().isConflict());
        verify(partituras, never()).delete(any(Partitura.class));
    }

    @Test
    void recursoInexistenteRetorna404() throws Exception {
        mvc.perform(get("/sessoes-estudo/999")).andExpect(status().isNotFound());
        mvc.perform(delete("/partituras/999")).andExpect(status().isNotFound());
    }

    @Test
    void totalZeroParaPartituraExistenteSemEstudos() throws Exception {
        when(partituras.existsById(1L)).thenReturn(true);
        when(sessoes.totalMinutosPorPartitura(1L)).thenReturn(0L);
        mvc.perform(get("/sessoes-estudo/partitura/1/total-minutos"))
                .andExpect(status().isOk()).andExpect(content().string("0"));
        mvc.perform(get("/sessoes-estudo/partitura/999/total-minutos"))
                .andExpect(status().isNotFound());
    }
}
