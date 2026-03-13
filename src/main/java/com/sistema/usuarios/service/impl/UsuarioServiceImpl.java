package com.sistema.usuarios.service.impl;

import com.sistema.usuarios.dto.UsuarioRequestDto;
import com.sistema.usuarios.dto.UsuarioResponseDto;
import com.sistema.usuarios.entities.Usuario;
import com.sistema.usuarios.exception.ListaUsuariosVaziaException;
import com.sistema.usuarios.exception.UsuarioNotFoundException;
import com.sistema.usuarios.mapper.UsuarioMapper;
import com.sistema.usuarios.repository.UsuarioRepository;
import com.sistema.usuarios.service.UsuarioService;
import com.sistema.usuarios.validation.UsuarioValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository repository;
    @Autowired
    private UsuarioMapper usuarioMapper;
    @Autowired
    private UsuarioValidator usuarioValidator;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public UsuarioResponseDto criarUsuario(UsuarioRequestDto usuarioRequestDto) {

        usuarioValidator.validarDadosUnicos(usuarioRequestDto, null);

        Usuario usuario = usuarioMapper.toEntity(usuarioRequestDto);

        String senhaCriptografada = passwordEncoder.encode(usuarioRequestDto.senha());
        usuario.setSenha(senhaCriptografada);

        Usuario usuarioSalvo = repository.save(usuario);

        return usuarioMapper.toResponseDto(usuarioSalvo);
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

        usuarioValidator.validarDadosUnicos(usuarioRequestDto, id);

        usuarioMapper.atualizaUsuarioComDto(usuarioRequestDto , usuario);

        String senhaCriptografada = passwordEncoder.encode(usuarioRequestDto.senha());
        usuario.setSenha(senhaCriptografada);

        Usuario usuarioAtualizado = repository.save(usuario);
        return usuarioMapper.toResponseDto(usuarioAtualizado);
    }

}
