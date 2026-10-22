package com.partituras.entidades;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.partituras.entidades.enums.Dificuldade;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Partitura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (nullable = false)
    private String titulo;

    @Column (nullable = false)
    private String compositor;

    private String instrumento;

    private String tonalidade;

    @Enumerated(EnumType.STRING)
    private Dificuldade dificuldade;

    private Integer anoComposicao;

    private String urlPdf;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

}
