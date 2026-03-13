package com.sistema.usuarios.controller;

import com.sistema.usuarios.dto.UsuarioRequestDto;
import com.sistema.usuarios.dto.UsuarioResponseDto;
import com.sistema.usuarios.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuários", description = "Endpoints do usuário")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Operation(summary = "Cria usuários")
    @PostMapping("/criar")
    public UsuarioResponseDto criarUsuario(@RequestBody @Valid UsuarioRequestDto usuarioRequestDtodto){
        return usuarioService.criarUsuario(usuarioRequestDtodto);
    }

    @Operation(summary = "Busca usuários pelo seu ID")
    @GetMapping("/find/{id}")
    public UsuarioResponseDto buscarUsuarioId(@PathVariable Long id){
        return usuarioService.buscarUsuarioId(id);
    }

    @Operation(summary = "Atualiza usuários pelo seu ID")
    @PutMapping("/update/{id}")
    public UsuarioResponseDto atualizarUsuarioId(@PathVariable Long id,
                                                 @RequestBody @Valid  UsuarioRequestDto usuarioRequestDto ){

        return usuarioService.atualizarUsuarioId(id, usuarioRequestDto);
    }

    @Operation(summary = "Lista todos os usuários")
    @GetMapping("/find")
    public List<UsuarioResponseDto> listarUsuarios(){
        return usuarioService.listarUsuarios();
    }

    @Operation(summary = " Deleta o usuário pelo seu ID")
    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarUsuario(@PathVariable Long id){
        usuarioService.deletarUsuarioID(id);
    }
}