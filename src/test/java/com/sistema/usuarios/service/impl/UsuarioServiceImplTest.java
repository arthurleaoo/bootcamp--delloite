package com.sistema.usuarios.service.impl;

import com.sistema.usuarios.dto.UsuarioRequestDto;
import com.sistema.usuarios.dto.UsuarioResponseDto;
import com.sistema.usuarios.entities.Usuario;
import com.sistema.usuarios.exception.DadosDuplicadosException;
import com.sistema.usuarios.exception.ListaUsuariosVaziaException;
import com.sistema.usuarios.exception.UsuarioNotFoundException;
import com.sistema.usuarios.mapper.UsuarioMapper;
import com.sistema.usuarios.repository.UsuarioRepository;
import com.sistema.usuarios.validation.UsuarioValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    @Mock
    private UsuarioValidator usuarioValidator;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("Deve criar usuário com sucesso")
    void deveCriarUsuarioComSucesso() {

        UsuarioRequestDto request = new UsuarioRequestDto(
                "Arthur",
                "12345678901",
                "arthur@email.com",
                "123456!Abc"
        );

        Usuario usuario = new Usuario();
        usuario.setNome("Arthur");
        usuario.setCpf("12345678901");
        usuario.setEmail("arthur@email.com");

        Usuario usuarioSalvo = new Usuario();
        usuarioSalvo.setId(1L);
        usuarioSalvo.setNome("Arthur");

        UsuarioResponseDto responseDto = new UsuarioResponseDto(
                1L,
                "Arthur",
                "12345678901",
                "arthur@email.com"
        );

        doNothing().when(usuarioValidator).validarDadosUnicos(request, null);
        when(passwordEncoder.encode(request.senha())).thenReturn("senha_hash");
        when(usuarioMapper.toEntity(request)).thenReturn(usuario);
        when(repository.save(usuario)).thenReturn(usuarioSalvo);
        when(usuarioMapper.toResponseDto(usuarioSalvo)).thenReturn(responseDto);

        UsuarioResponseDto response = service.criarUsuario(request);

        assertNotNull(response);
        assertEquals("Arthur", response.nome());
        assertEquals(1L, response.id());

        verify(usuarioValidator).validarDadosUnicos(request, null);
        verify(passwordEncoder).encode(anyString());
        verify(repository).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve dar erro de duplicidade de Email")
    void deveDarErroDeEmailDuplicado(){

        UsuarioRequestDto userRequestDto = new UsuarioRequestDto(
                "Arthur",
                "12345678901",
                "arthur@email.com",
                "123456A1!bc"
        );

        doThrow(new DadosDuplicadosException("Email já cadastrado"))
                .when(usuarioValidator).validarDadosUnicos(any(), any());

        assertThrows(
                DadosDuplicadosException.class,
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
                "123456A!bc"
        );

        doThrow(new DadosDuplicadosException("Cpf já cadastrado"))
                .when(usuarioValidator).validarDadosUnicos(any(), any());

        assertThrows(
                DadosDuplicadosException.class,
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
    @DisplayName("Deve atualizar usuário com sucesso e cobrir todas as linhas")
    void deveAtualizarUsuarioComSucesso() {
        // Arrange
        Long id = 1L;
        UsuarioRequestDto request = new UsuarioRequestDto(
                "Arthur Atualizado",
                "12345678901",
                "arthur@email.com",
                "123456A!1bc"
        );

        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(id);
        usuarioExistente.setNome("Arthur Antigo");

        UsuarioResponseDto responseDto = new UsuarioResponseDto(
                id,
                "Arthur Atualizado",
                "12345678901",
                "arthur@email.com"
        );


        when(repository.findById(id)).thenReturn(Optional.of(usuarioExistente));


        doNothing().when(usuarioValidator).validarDadosUnicos(request, id);

        when(passwordEncoder.encode(request.senha())).thenReturn("senha_hash");

        when(repository.save(usuarioExistente)).thenReturn(usuarioExistente);

        when(usuarioMapper.toResponseDto(usuarioExistente)).thenReturn(responseDto);


        UsuarioResponseDto response = service.atualizarUsuarioId(id, request);


        assertNotNull(response);
        assertEquals("Arthur Atualizado", response.nome());

        // Verificações que forçam a ferramenta de cobertura a validar a linha
        verify(repository).findById(id);
        verify(usuarioValidator).validarDadosUnicos(request, id);
        verify(passwordEncoder).encode(request.senha());
        verify(repository).save(usuarioExistente);
    }

    @Test
    @DisplayName("Deve lançar erro ao atualizar usuário inexistente")
    void deveLancarErroAoAtualizarUsuarioInexistente(){

        Long id = 99L;

        UsuarioRequestDto request = new UsuarioRequestDto(
                "Arthur",
                "12345678901",
                "arthur@email.com",
                "123456A1bc!"
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