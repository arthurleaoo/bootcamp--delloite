package com.sistema.usuarios.service.impl;

import com.sistema.usuarios.dto.UsuarioRequestDto;
import com.sistema.usuarios.dto.UsuarioResponseDto;
import com.sistema.usuarios.entities.Usuario;
import com.sistema.usuarios.exception.ListaUsuariosVaziaException;
import com.sistema.usuarios.exception.UsuarioNotFoundException;
import com.sistema.usuarios.mapper.UsuarioMapper;
import com.sistema.usuarios.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {

    @InjectMocks
    private UsuarioServiceImpl service;

    @Mock
    private UsuarioMapper usuarioMapper;

    @Mock
    private UsuarioRepository repository;

    @Test
    @DisplayName("Deve criar usuário com sucesso")
    void deveCriarUsuarioComSucesso() {

        UsuarioRequestDto request = new UsuarioRequestDto(
                "Arthur",
                "12345678901",
                "arthur@email.com",
                "123456"
        );

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Arthur");
        usuario.setEmail("arthur@email.com");

        UsuarioResponseDto responseDto = new UsuarioResponseDto(
                1L,
                "Arthur",
                "12345678910",
                "arthur@email.com"
        );

        when(usuarioMapper.toEntity(any())).thenReturn(usuario);
        when(repository.save(any())).thenReturn(usuario);
        when(usuarioMapper.toResponseDto(any())).thenReturn(responseDto);

        UsuarioResponseDto response = service.criarUsuario(request);

        assertNotNull(response);
        assertEquals("Arthur", response.nome());
    }

    @Test
    @DisplayName("Deve dar erro de duplicidade de Email")
    void deveDarErroDeEmailDuplicado(){

        UsuarioRequestDto userRequestDto = new UsuarioRequestDto(
                "Arthur",
                "12345678901",
                "arthur@email.com",
                "123456"
        );

        when(repository.existsByEmail("arthur@email.com"))
                .thenReturn(true);

        assertThrows(
                RuntimeException.class,
                () -> service.criarUsuario(userRequestDto)
        );
    }

    @Test
    @DisplayName("Deve dar erro de duplicidade de CPF")
    void deveDarErroDeCpfDuplicado(){

        UsuarioRequestDto userRequestDto = new UsuarioRequestDto(
                "Arthur",
                "12345678901",
                "arthur@email.com",
                "123456"
        );

        when(repository.existsByCpf("12345678901"))
                .thenReturn(true);

        assertThrows(
                RuntimeException.class,
                () -> service.criarUsuario(userRequestDto)
        );
    }

    @Test
    @DisplayName("Deve buscar usuário por id com sucesso")
    void deveBuscarUsuarioPorIdComSucesso() {

        Long id = 1L;

        Usuario usuario = new Usuario();
        usuario.setId(id);

        UsuarioResponseDto responseDto = new UsuarioResponseDto(
                id,
                "Arthur",
                "12345678901",
                "arthur@email.com"
        );

        when(repository.findById(id))
                .thenReturn(Optional.of(usuario));

        when(usuarioMapper.toResponseDto(usuario))
                .thenReturn(responseDto);

        UsuarioResponseDto response = service.buscarUsuarioId(id);

        assertNotNull(response);
    }

    @Test
    @DisplayName("Deve lançar erro quando usuário não existir")
    void deveLancarErroQuandoUsuarioNaoEncontrado(){

        when(repository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                UsuarioNotFoundException.class,
                () -> service.buscarUsuarioId(1L)
        );
    }

    @Test
    @DisplayName("Deve listar usuários com sucesso")
    void deveListarUsuariosComSucesso(){

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Arthur");
        usuario.setCpf("12345678910");
        usuario.setEmail("arthur@email.com");

        UsuarioResponseDto responseDto = new UsuarioResponseDto(
                1L,
                "Arthur",
                "12345678910",
                "arthur@email.com"
        );

        when(repository.findAll())
                .thenReturn(List.of(usuario));

        when(usuarioMapper.toResponseDto(usuario))
                .thenReturn(responseDto);

        List<UsuarioResponseDto> response = service.listarUsuarios();

        assertNotNull(response);
        assertEquals(1, response.size());
    }

    @Test
    @DisplayName("Deve lançar erro quando lista de usuários estiver vazia")
    void deveLancarErroQuandoListaUsuariosVazia(){

        when(repository.findAll())
                .thenReturn(Collections.emptyList());

        assertThrows(ListaUsuariosVaziaException.class,
                ()->service.listarUsuarios());

    }

    @Test
    @DisplayName("Deve deletar usuário com sucesso")
    void deveDeletarUsuarioComSucesso(){

        Long id = 1L;

        Usuario usuario = new Usuario();
        usuario.setId(id);

        when(repository.findById(id))
                .thenReturn(Optional.of(usuario));

        service.deletarUsuarioID(id);

        verify(repository, times(1)).delete(usuario);
    }

    @Test
    @DisplayName("Deve lançar erro ao tentar deletar usuário inexistente")
    void deveLancarErroAoDeletarUsuarioInexistente(){

        Long id = 99L;

        when(repository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                UsuarioNotFoundException.class,
                () -> service.deletarUsuarioID(id)
        );

        verify(repository, never()).delete(any());
    }
    @Test
    @DisplayName("Deve atualizar usuário com sucesso")
    void deveAtualizarUsuarioComSucesso(){

        Long id = 1L;

        UsuarioRequestDto request = new UsuarioRequestDto(
                "Arthur Atualizado",
                "12345678901",
                "arthur@email.com",
                "123456"
        );

        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setNome("Arthur");

        UsuarioResponseDto responseDto = new UsuarioResponseDto(
                id,
                "Arthur Atualizado",
                "00011122234",
                "arthur@email.com"
        );

        when(repository.findById(id))
                .thenReturn(Optional.of(usuario));

        when(repository.save(usuario))
                .thenReturn(usuario);

        when(usuarioMapper.toResponseDto(usuario))
                .thenReturn(responseDto);

        UsuarioResponseDto response = service.atualizarUsuarioId(id, request);

        assertNotNull(response);
        assertEquals(id, response.id());

        verify(repository, times(1)).save(usuario);
    }

    @Test
    @DisplayName("Deve lançar erro ao atualizar usuário inexistente")
    void deveLancarErroAoAtualizarUsuarioInexistente(){

        Long id = 99L;

        UsuarioRequestDto request = new UsuarioRequestDto(
                "Arthur",
                "12345678901",
                "arthur@email.com",
                "123456"
        );

        when(repository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                UsuarioNotFoundException.class,
                () -> service.atualizarUsuarioId(id, request)
        );

        verify(repository, never()).save(any());
    }

}