package com.sistema.usuarios.repository;

import com.sistema.usuarios.entities.Usuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository repository;

    @Test
    @DisplayName("Deve retornar true quando o email já existe no banco")
    void deveRetornarTrueQuandoEmailExiste() {

        String email = "arthur@email.com";
        Usuario usuario = new Usuario();
        usuario.setNome("Arthur");
        usuario.setEmail(email);
        usuario.setCpf("12345678901");
        usuario.setSenha("123456");

        repository.save(usuario);

        boolean existe = repository.existsByEmail(email);

        assertTrue(existe);
    }

    @Test
    @DisplayName("Deve retornar false quando o email não existe")
    void deveRetornarFalseQuandoEmailNaoExiste() {

        String email = "arthurinexistente@email.com";

        boolean existe = repository.existsByEmail(email);

        assertFalse(existe);
    }

    @Test
    @DisplayName("Deve retornar true quando o cpf já existe no banco")
    void deveRetornarTrueQuandoCpfExiste() {

        String cpf = "12345678901";
        Usuario usuario = new Usuario();
        usuario.setNome("Arthur");
        usuario.setEmail("outro@email.com");
        usuario.setCpf(cpf);
        usuario.setSenha("123456");

        repository.save(usuario);

        boolean existe = repository.existsByCpf(cpf);

        assertTrue(existe);
    }

    @Test
    @DisplayName("Deve retornar false quando o Cpf não existe")
    void deveRetornarFalseQuandoCpfNaoExiste() {

        String cpf = "00099988876";

        boolean existe = repository.existsByEmail(cpf);

        assertFalse(existe);
    }
}