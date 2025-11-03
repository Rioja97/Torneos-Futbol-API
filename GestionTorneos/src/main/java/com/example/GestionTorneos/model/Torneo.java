package com.example.GestionTorneos.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Torneo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column(nullable = false)
    @Size(min = 2, max = 100)
    private String nombre;
    @NotNull
    @Column(nullable = false)
    @Size(min = 2, max = 100)
    private String division;

    @ManyToMany
    @JoinTable(
            name = "torneo_equipos",
            joinColumns = @JoinColumn(name = "torneo_id"),
            inverseJoinColumns = @JoinColumn(name = "equipo_id")
    )
    private List<Equipo> equiposParticipantes = new ArrayList<>();

    @OneToMany(mappedBy = "torneo",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JsonIgnore
    private List<Partido> partidos = new ArrayList<>();

    @NotNull
    @Min(4)
    @Max(30)
    private Integer cupo;

    public Torneo() {
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getDivision() {
        return division;
    }
    public void setDivision(String division) {
        this.division = division;
    }
    public List<Equipo> getEquiposParticipantes() {
        return equiposParticipantes;
    }
    public void setEquiposParticipantes(List<Equipo> equiposParticipantes) {
        this.equiposParticipantes = equiposParticipantes;
    }
    public List<Partido> getPartidos() {
        return partidos;
    }
    public void setPartidos(List<Partido> partidos) {
        this.partidos = partidos;
    }
    public @NotNull @Min(4) @Max(30) Integer getCupo() {
        return cupo;
    }
    public void setCupo(@NotNull @Min(4) @Max(30) Integer cupo) {
        this.cupo = cupo;
    }
}