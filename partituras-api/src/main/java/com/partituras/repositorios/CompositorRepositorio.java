package com.partituras.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.partituras.entidades.Compositor;

public interface CompositorRepositorio extends JpaRepository<Compositor, Long> {
}
