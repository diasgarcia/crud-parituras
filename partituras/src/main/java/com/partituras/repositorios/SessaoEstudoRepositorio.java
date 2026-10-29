package com.partituras.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.partituras.entidades.SessaoEstudo;

public interface SessaoEstudoRepositorio extends JpaRepository<SessaoEstudo, Long> {

    boolean existsByPartituraId(Long partituraId);
}
