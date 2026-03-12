package com.sistema.usuarios.mapper;

import com.sistema.usuarios.dto.UsuarioRequestDto;
import com.sistema.usuarios.dto.UsuarioResponseDto;
import com.sistema.usuarios.entities.Usuario;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-12T14:44:55-0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class UsuarioMapperImpl implements UsuarioMapper {

    @Override
    public Usuario toEntity(UsuarioRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        Usuario usuario = new Usuario();

        usuario.setNome( dto.nome() );
        usuario.setCpf( dto.cpf() );
        usuario.setEmail( dto.email() );
        usuario.setSenha( dto.senha() );

        return usuario;
    }

    @Override
    public UsuarioResponseDto toResponseDto(Usuario usuario) {
        if ( usuario == null ) {
            return null;
        }

        Long id = null;
        String nome = null;
        String cpf = null;
        String email = null;

        id = usuario.getId();
        nome = usuario.getNome();
        cpf = usuario.getCpf();
        email = usuario.getEmail();

        UsuarioResponseDto usuarioResponseDto = new UsuarioResponseDto( id, nome, cpf, email );

        return usuarioResponseDto;
    }

    @Override
    public void updateEntityFromDto(UsuarioRequestDto usuarioRequestDto, Usuario entity) {
        if ( usuarioRequestDto == null ) {
            return;
        }

        entity.setNome( usuarioRequestDto.nome() );
        entity.setCpf( usuarioRequestDto.cpf() );
        entity.setEmail( usuarioRequestDto.email() );
        entity.setSenha( usuarioRequestDto.senha() );
    }
}
