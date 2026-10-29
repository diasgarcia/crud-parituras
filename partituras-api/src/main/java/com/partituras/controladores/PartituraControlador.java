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
import com.partituras.entidades.Partitura;
import com.partituras.repositorios.CompositorRepositorio;
import com.partituras.repositorios.PartituraRepositorio;
import com.partituras.repositorios.SessaoEstudoRepositorio;

@RestController
@RequestMapping("/partituras")
public class PartituraControlador {

    private final PartituraRepositorio partituraRepositorio;
    private final CompositorRepositorio compositorRepositorio;
    private final SessaoEstudoRepositorio sessaoEstudoRepositorio;

    public PartituraControlador(PartituraRepositorio partituraRepositorio, CompositorRepositorio compositorRepositorio, SessaoEstudoRepositorio sessaoEstudoRepositorio) {

        this.partituraRepositorio = partituraRepositorio;
        this.compositorRepositorio = compositorRepositorio;
        this.sessaoEstudoRepositorio = sessaoEstudoRepositorio;
    }

    @GetMapping
    public List<Partitura> listar() {

        return partituraRepositorio.findAll();
    }

    @GetMapping("/{id}")
    public Partitura buscar(@PathVariable Long id) {

        return partituraRepositorio.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Partitura não encontrada"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Partitura inserir(@RequestBody Partitura partitura) {

        Compositor compositor = validarEObterCompositor(partitura);

        Partitura nova = Partitura.builder()
                .titulo(partitura.getTitulo())
                .instrumento(partitura.getInstrumento())
                .dificuldade(partitura.getDificuldade())
                .status(partitura.getStatus())
                .caminhoPdf(partitura.getCaminhoPdf())
                .compositor(compositor)
                .build();

        return partituraRepositorio.save(nova);
    }

    @PutMapping("/{id}")
    public Partitura atualizar(@PathVariable Long id, @RequestBody Partitura partitura) {

        Partitura existente = buscar(id);

        Compositor compositor = validarEObterCompositor(partitura);

        Partitura atualizada = Partitura.builder()
                .id(existente.getId())
                .titulo(partitura.getTitulo())
                .instrumento(partitura.getInstrumento())
                .dificuldade(partitura.getDificuldade())
                .status(partitura.getStatus())
                .caminhoPdf(partitura.getCaminhoPdf())
                .compositor(compositor)
                .build();

        return partituraRepositorio.save(atualizada);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {

        Partitura partitura = buscar(id);

        if (sessaoEstudoRepositorio.existsByPartituraId(id)) {

            throw new ResponseStatusException(HttpStatus.CONFLICT, "Exclua ou transfira as sessões de estudo desta partitura antes de excluí-la");
        }

        partituraRepositorio.delete(partitura);
    }

    private Compositor validarEObterCompositor(Partitura partitura) {

        if (partitura.getTitulo() == null || partitura.getTitulo().isBlank() || partitura.getTitulo().length() > 255) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe um título com até 255 caracteres");
        }

        if (partitura.getCompositor() == null || partitura.getCompositor().getId() == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe o ID do compositor");
        }

        return compositorRepositorio.findById(partitura.getCompositor().getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Compositor não encontrado"));
    }
}
