package com.grupoocho.gestor_deudas.service;

import com.grupoocho.gestor_deudas.model.Usuario;
import com.grupoocho.gestor_deudas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {


    private final UsuarioRepository usuarioRepository;
    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario registrarUsuario(Usuario usuario) {
        // Verificar si el nombre de usuario ya existe
        if (usuarioRepository.findByUsername(usuario.getUsername()).isPresent()) {
            throw new IllegalArgumentException("El nombre de usuario ya existe");
        }
        // Guardar el usuario sin validaciones adicionales
        return usuarioRepository.save(usuario);
    }

    public Usuario loginUsuario(String username, String contrasena) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findByUsername(username);
        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();
            if (usuario.getContrasena() != null && usuario.getContrasena().equals(contrasena)) {
                return usuario;
            } else {
                throw new IllegalArgumentException("Contraseña incorrecta");
            }
        } else {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
    }
}
