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

import com.partituras.entidades.Compositor;
import com.partituras.repositorios.CompositorRepositorio;
import com.partituras.repositorios.PartituraRepositorio;

@RestController
@RequestMapping("/compositores")
public class CompositorControlador {

    private final CompositorRepositorio compositorRepositorio;
    private final PartituraRepositorio partituraRepositorio;

    public CompositorControlador(CompositorRepositorio compositorRepositorio, PartituraRepositorio partituraRepositorio) {

        this.compositorRepositorio = compositorRepositorio;
        this.partituraRepositorio = partituraRepositorio;
    }

    @GetMapping
    public List<Compositor> listar() {

        return compositorRepositorio.findAll();
    }

    @GetMapping("/{id}")
    public Compositor buscar(@PathVariable Long id) {

        return compositorRepositorio.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Compositor não encontrado"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Compositor inserir(@RequestBody Compositor compositor) {

        validar(compositor);

        Compositor novo = Compositor.builder()
                .nome(compositor.getNome())
                .build();

        return compositorRepositorio.save(novo);
    }

    @PutMapping("/{id}")
    public Compositor atualizar(@PathVariable Long id, @RequestBody Compositor compositor) {

        Compositor existente = buscar(id);

        validar(compositor);

        Compositor atualizado = Compositor.builder()
                .id(existente.getId())
                .nome(compositor.getNome())
                .build();

        return compositorRepositorio.save(atualizado);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {

        Compositor compositor = buscar(id);

        if (partituraRepositorio.existsByCompositorId(id)) {

            throw new ResponseStatusException(HttpStatus.CONFLICT, "Exclua ou transfira as partituras deste compositor antes de excluí-lo");
        }

        compositorRepositorio.delete(compositor);
    }

    private void validar(Compositor compositor) {

        if (compositor.getNome() == null || compositor.getNome().isBlank() || compositor.getNome().length() > 255) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe um nome com até 255 caracteres");
        }
    }
}
