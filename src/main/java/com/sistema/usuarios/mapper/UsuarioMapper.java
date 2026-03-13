package com.sistema.usuarios.mapper;

import com.sistema.usuarios.dto.UsuarioRequestDto;
import com.sistema.usuarios.dto.UsuarioResponseDto;
import com.sistema.usuarios.entities.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    // pega RequestDto e transforma na entidade Usuario
    Usuario toEntity(UsuarioRequestDto dto);

    // pega entidade usuário e transforma no ResponseDto
    UsuarioResponseDto toResponseDto(Usuario usuario);


    //metodo para pegar os dados do UsuarioRequestDTO e sobrescrever os campos no objeto usuario
    void atualizaUsuarioComDto(UsuarioRequestDto usuarioRequestDto, @MappingTarget Usuario usuario);
}