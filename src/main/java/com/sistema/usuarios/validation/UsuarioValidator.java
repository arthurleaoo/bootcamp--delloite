package com.sistema.usuarios.validation;

import com.sistema.usuarios.dto.UsuarioRequestDto;
import com.sistema.usuarios.exception.DadosDuplicadosException;
import com.sistema.usuarios.repository.UsuarioRepository;
import org.springframework.stereotype.Component;


@Component
public class UsuarioValidator {

    private final UsuarioRepository repository;

    public UsuarioValidator(UsuarioRepository repository) {
        this.repository = repository;
    }

    public void validarDadosUnicos(UsuarioRequestDto usuarioRequestDto, Long idAtual) {
        // Validação de Email
        repository.findByEmail(usuarioRequestDto.email()).ifPresent(usuario -> {
            if (idAtual == null || !usuario.getId().equals(idAtual)) {
                throw new DadosDuplicadosException("O e-mail " + usuarioRequestDto.email() + " já está em uso.");
            }
        });

        // Validação de CPF
        repository.findByCpf(usuarioRequestDto.cpf()).ifPresent(usuario -> {
            if (idAtual == null || !usuario.getId().equals(idAtual)) {
                throw new DadosDuplicadosException("O CPF " + usuarioRequestDto.cpf() + " já está cadastrado.");
            }
        });
    }
}