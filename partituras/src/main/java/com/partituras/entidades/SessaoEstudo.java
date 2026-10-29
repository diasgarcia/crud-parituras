package com.partituras.entidades;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "sessoes_estudo")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessaoEstudo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "partitura_id", nullable = false)
    private Partitura partitura;

    @Column(nullable = false)
    private LocalDate data;

    @Column(nullable = false)
    private Integer minutos;

    @Column(length = 1000)
    private String observacoes;
}
