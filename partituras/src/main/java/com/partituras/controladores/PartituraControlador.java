package com.partituras.controladores;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.partituras.entidades.Partitura;
import com.partituras.repositorios.PartituraRepositorio;

@RestController
@RequestMapping ("/partituras")
public class PartituraControlador {

     private final PartituraRepositorio partituraRepositorio;

     public PartituraControlador(PartituraRepositorio partituraRepositorio) {
        this.partituraRepositorio = partituraRepositorio;
     }

    @PostMapping
    @ResponseStatus (HttpStatus.CREATED)
    public Partitura inserir(@RequestBody Partitura partitura) {

        return partituraRepositorio.save(partitura);
    }


}
