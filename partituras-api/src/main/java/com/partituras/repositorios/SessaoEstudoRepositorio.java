package com.partituras.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.partituras.entidades.SessaoEstudo;

public interface SessaoEstudoRepositorio extends JpaRepository<SessaoEstudo, Long> {

    boolean existsByPartituraId(Long partituraId);

    @Query(value = """
            SELECT s.*
            FROM sessoes_estudo s
            JOIN partituras p ON p.id = s.partitura_id
            JOIN compositores c ON c.id = p.compositor_id
            WHERE c.id = :compositorId
            """, nativeQuery = true)
    List<SessaoEstudo> buscarPorCompositor(@Param("compositorId") Long compositorId);

    @Query(value = """
            SELECT COALESCE(SUM(s.minutos), 0)
            FROM sessoes_estudo s
            WHERE s.partitura_id = :partituraId
            """, nativeQuery = true)
    Long totalMinutosPorPartitura(@Param("partituraId") Long partituraId);
}
