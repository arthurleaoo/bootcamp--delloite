package com.sistema.usuarios.entities;


import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "tb_usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String nome;

    @Column(unique = true)
    public String cpf;

    @Column(unique = true)
    public String email;

    public String senha;


}
