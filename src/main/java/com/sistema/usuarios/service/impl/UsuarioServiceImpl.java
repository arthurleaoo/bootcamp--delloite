package com.sistema.usuarios.service.impl;

import com.sistema.usuarios.dto.UsuarioRequestDto;
import com.sistema.usuarios.dto.UsuarioResponseDto;
import com.sistema.usuarios.entities.Usuario;
import com.sistema.usuarios.exception.ListaUsuariosVaziaException;
import com.sistema.usuarios.exception.UsuarioNotFoundException;
import com.sistema.usuarios.mapper.UsuarioMapper;
import com.sistema.usuarios.repository.UsuarioRepository;
import com.sistema.usuarios.service.UsuarioService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository repository;
    private final UsuarioMapper usuarioMapper;

    public UsuarioServiceImpl(UsuarioRepository repository, UsuarioMapper usuarioMapper) {
        this.repository = repository;
        this.usuarioMapper = usuarioMapper;
    }

    @Override
    public UsuarioResponseDto criarUsuario(UsuarioRequestDto usuarioRequestDto) {

        if (repository.existsByEmail(usuarioRequestDto.email())) {
            throw new RuntimeException("Email já cadastrado");
        }
        if (repository.existsByCpf((usuarioRequestDto.cpf()))) {
            throw new RuntimeException("CPF já cadastrado");
        }

        Usuario usuario = usuarioMapper.toEntity(usuarioRequestDto);

        repository.save(usuario);

        return usuarioMapper.toResponseDto(usuario);
    }

    @Override
    public UsuarioResponseDto buscarUsuarioId(Long id) {

        Usuario usuario = repository.findById(id)
                .orElseThrow(()-> new UsuarioNotFoundException(id));

        return usuarioMapper.toResponseDto(usuario);
    }

    @Override
    public List<UsuarioResponseDto> listarUsuarios() {

        List<Usuario> usuarios = repository.findAll();

        if (usuarios.isEmpty()) {
            throw new ListaUsuariosVaziaException();
        }

        return usuarios.stream()
                .map(usuarioMapper::toResponseDto)
                .toList();
    }

    @Override
    public void deletarUsuarioID(Long id) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException(id));

        repository.delete(usuario);

    }

    @Override
    public UsuarioResponseDto atualizarUsuarioId(Long id, UsuarioRequestDto usuarioRequestDto) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException(id));

        usuarioMapper.updateEntityFromDto(usuarioRequestDto , usuario);

        Usuario usuarioAtualizado = repository.save(usuario);
        return usuarioMapper.toResponseDto(usuarioAtualizado);
    }


}
