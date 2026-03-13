package com.sistema.usuarios.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sistema.usuarios.dto.UsuarioRequestDto;
import com.sistema.usuarios.dto.UsuarioResponseDto;
import com.sistema.usuarios.exception.UsuarioNotFoundException;
import com.sistema.usuarios.service.UsuarioService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(UsuarioController.class)

class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsuarioService service;

    @Autowired
    private ObjectMapper objectMapper; // Para converter objeto em JSON String

    @Test
    @DisplayName("Deve criar um usuário e retornar 200 (ou 201 se você ajustar o controller)")
    void deveCriarUsuarioComSucesso() throws Exception {
        // Arrange
        UsuarioRequestDto request = new UsuarioRequestDto("Arthur", "12345678901", "arthur@email.com", "senha123!Ab");
        UsuarioResponseDto response = new UsuarioResponseDto(1L, "Arthur", "12345678901", "arthur@email.com");

        when(service.criarUsuario(any(UsuarioRequestDto.class))).thenReturn(response);

        mockMvc.perform(post("/usuarios/criar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Arthur"))
                .andExpect(jsonPath("$.cpf").value("12345678901"))
                .andExpect(jsonPath("$.email").value("arthur@email.com"));

    }

    @Test
    @DisplayName("Deve buscar usuário por ID com sucesso")
    void deveBuscarUsuarioPorIdComSucesso() throws Exception {

        Long id = 1L;
        UsuarioResponseDto response = new UsuarioResponseDto(id, "Arthur", "12345678901", "arthur@email.com");
        when(service.buscarUsuarioId(id)).thenReturn(response);


        mockMvc.perform(get("/usuarios/find/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Arthur"))
                .andExpect(jsonPath("$.cpf").value("12345678901"))
                .andExpect(jsonPath("$.email").value("arthur@email.com"));
    }

    @Test
    @DisplayName("Deve retornar 404 quando o usuário não existir")
    void deveRetornar404QuandoUsuarioNaoExiste() throws Exception {

        Long id = 99L;

        UsuarioNotFoundException exception = new UsuarioNotFoundException(id);

        when(service.buscarUsuarioId(id)).thenThrow(exception);


        mockMvc.perform(get("/usuarios/find/" + id))
                .andExpect(status().isNotFound()) // Verifica se o status é 404
                .andExpect(jsonPath("$.erro").value(exception.getMessage()))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("Deve retornar 204 ao deletar usuário")
    void deveRetornar204AoDeletar() throws Exception {

        Long id = 1L;
        doNothing().when(service).deletarUsuarioID(id);


        mockMvc.perform(delete("/usuarios/delete/" + id))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve listar todos os usuários com sucesso")
    void deveListarTodosUsuariosComSucesso() throws Exception {

        UsuarioResponseDto usuario = new UsuarioResponseDto(1L, "Arthur", "12345678901", "arthur@email.com");
        when(service.listarUsuarios()).thenReturn(List.of(usuario));


        mockMvc.perform(get("/usuarios/find")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Arthur"))
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    @DisplayName("Deve atualizar um usuário com sucesso")
    void deveAtualizarUsuarioComSucesso() throws Exception {
        Long id = 1L;
        UsuarioRequestDto request = new UsuarioRequestDto("Arthur Silva", "12345678901", "arthur.novo@email.com", "senha123A1b!");
        UsuarioResponseDto response = new UsuarioResponseDto(id, "Arthur Silva", "12345678901", "arthur.novo@email.com");

        when(service.atualizarUsuarioId(eq(id), any(UsuarioRequestDto.class))).thenReturn(response);

        mockMvc.perform(put("/usuarios/update/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Arthur Silva"))
                .andExpect(jsonPath("$.cpf").value("12345678901"))
                .andExpect(jsonPath("$.email").value("arthur.novo@email.com"));
    }


}