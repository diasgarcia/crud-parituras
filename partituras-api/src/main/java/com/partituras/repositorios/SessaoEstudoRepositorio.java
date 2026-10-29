package com.partituras.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.partituras.entidades.SessaoEstudo;

public interface SessaoEstudoRepositorio extends JpaRepository<SessaoEstudo, Long> {

    boolean existsByPartituraId(Long partituraId);

    @Query("""
            SELECT s
            FROM SessaoEstudo s
            JOIN s.partitura p
            JOIN p.compositor c
            WHERE c.id = :compositorId
            """)
    List<SessaoEstudo> buscarPorCompositor(@Param("compositorId") Long compositorId);

    @Query("""
            SELECT COALESCE(SUM(s.minutos), 0)
            FROM SessaoEstudo s
            WHERE s.partitura.id = :partituraId
            """)
    Long totalMinutosPorPartitura(@Param("partituraId") Long partituraId);
}
