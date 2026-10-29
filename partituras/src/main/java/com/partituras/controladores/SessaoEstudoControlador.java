package com.partituras.controladores;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.partituras.entidades.Partitura;
import com.partituras.entidades.SessaoEstudo;
import com.partituras.repositorios.CompositorRepositorio;
import com.partituras.repositorios.PartituraRepositorio;
import com.partituras.repositorios.SessaoEstudoRepositorio;

@RestController
@RequestMapping("/sessoes-estudo")
public class SessaoEstudoControlador {

    private final SessaoEstudoRepositorio sessaoEstudoRepositorio;
    private final PartituraRepositorio partituraRepositorio;
    private final CompositorRepositorio compositorRepositorio;

    public SessaoEstudoControlador(SessaoEstudoRepositorio sessaoEstudoRepositorio, PartituraRepositorio partituraRepositorio, CompositorRepositorio compositorRepositorio) {

        this.sessaoEstudoRepositorio = sessaoEstudoRepositorio;
        this.partituraRepositorio = partituraRepositorio;
        this.compositorRepositorio = compositorRepositorio;
    }

    @GetMapping
    public List<SessaoEstudo> listar() {

        return sessaoEstudoRepositorio.findAll();
    }

    @GetMapping("/{id}")
    public SessaoEstudo buscar(@PathVariable Long id) {

        return sessaoEstudoRepositorio.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sessão de estudo não encontrada"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SessaoEstudo inserir(@RequestBody SessaoEstudo sessao) {

        Partitura partitura = validarEObterPartitura(sessao);

        SessaoEstudo nova = SessaoEstudo.builder()
                .partitura(partitura)
                .data(sessao.getData())
                .minutos(sessao.getMinutos())
                .observacoes(sessao.getObservacoes())
                .build();

        return sessaoEstudoRepositorio.save(nova);
    }

    @PutMapping("/{id}")
    public SessaoEstudo atualizar(@PathVariable Long id, @RequestBody SessaoEstudo sessao) {

        SessaoEstudo existente = buscar(id);

        Partitura partitura = validarEObterPartitura(sessao);

        SessaoEstudo atualizada = SessaoEstudo.builder()
                .id(existente.getId())
                .partitura(partitura)
                .data(sessao.getData())
                .minutos(sessao.getMinutos())
                .observacoes(sessao.getObservacoes())
                .build();

        return sessaoEstudoRepositorio.save(atualizada);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {

        sessaoEstudoRepositorio.delete(buscar(id));
    }

    @GetMapping("/compositor/{compositorId}")
    public List<SessaoEstudo> buscarPorCompositor(@PathVariable Long compositorId) {

        if (!compositorRepositorio.existsById(compositorId)) {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Compositor não encontrado");
        }

        return sessaoEstudoRepositorio.buscarPorCompositor(compositorId);
    }

    private Partitura validarEObterPartitura(SessaoEstudo sessao) {

        if (sessao.getData() == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe a data do estudo");
        }

        if (sessao.getMinutos() == null || sessao.getMinutos() <= 0) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Os minutos devem ser maiores que zero");
        }

        if (sessao.getObservacoes() != null && sessao.getObservacoes().length() > 1000) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "As observações devem ter até 1000 caracteres");
        }

        if (sessao.getPartitura() == null || sessao.getPartitura().getId() == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe o ID da partitura");
        }

        return partituraRepositorio.findById(sessao.getPartitura().getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Partitura não encontrada"));
    }
}
