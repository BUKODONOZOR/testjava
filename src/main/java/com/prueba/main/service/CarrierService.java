package com.prueba.main.service;

import com.prueba.main.entity.Carrier;
import com.prueba.main.repository.CarrierRepository;
import com.prueba.main.util.enums.CustomDatails.CustomUserDetails;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class CarrierService implements UserDetailsService { // Implementa UserDetailsService

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private CarrierRepository carrierRepository;

    public Optional<Carrier> getUsByUsername(String username) {
        return carrierRepository.findByUsername(username);
    }


    // Valida la fortaleza de la contraseña (ejemplo básico, puedes añadir más reglas)
    private boolean isStrongPassword(String password) {
        return password.matches(".*[A-Z].*") && // Al menos una mayúscula
                password.matches(".*[a-z].*") && // Al menos una minúscula
                password.matches(".*\\d.*") &&   // Al menos un número
                password.matches(".*[!@#$%^&*].*"); // Al menos un carácter especial
    }


    public Carrier createUser(Carrier carrierCreateDTO) {
        // Crear un nuevo usuario y establecer sus atributos
        Carrier carrier = new Carrier();
        carrier.setName(carrierCreateDTO.getName());
        carrier.setUsername(carrierCreateDTO.getUsername());
        carrier.setPassword(carrierCreateDTO.getPassword());
        carrier.setRole(carrierCreateDTO.getRole()); // Establecer el rol desde el DTO


        // Validar si el usuario ya existe
        if (carrierRepository.existsByUsername(carrier.getUsername())) {
            throw new IllegalArgumentException("El usuario con ese username ya existe.");
        }

        // Validación de la contraseña
        if (!StringUtils.hasText(carrier.getPassword()) || carrier.getPassword().length() < 8) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres.");
        }

        if (!isStrongPassword(carrier.getPassword())) {
            throw new IllegalArgumentException("La contraseña debe ser más fuerte.");
        }

        // Encriptar la contraseña
        carrier.setPassword(passwordEncoder.encode(carrier.getPassword()));

        // Intentar guardar el usuario en la base de datos
        try {
            return carrierRepository.save(carrier);
        } catch (ConstraintViolationException e) {
            throw new IllegalArgumentException("Error al guardar el usuario: " + e.getMessage());
        }
    }


    public Carrier getCarrierById(Long id) {
        return carrierRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }

    public void deleteCarrier(Long id) {
        if (carrierRepository.existsById(id)) {
            carrierRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("User not found with id: " + id);
        }
    }

    // Implementación del método loadUserByUsername de UserDetailsService
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Carrier carrier = carrierRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // Construir CustomUserDetails con rol
        return new CustomUserDetails(
                carrier.getId(),
                carrier.getUsername(),
                carrier.getPassword(),
                carrier.getRole(), // Usar el rol del usuario
                getAuthorities(carrier) // Obtener las autoridades
        );
    }
    // Método para obtener las autoridades del usuario
    private Collection<? extends GrantedAuthority> getAuthorities(Carrier user) {
        return List.of(new SimpleGrantedAuthority(user.getRole().name())); // Asumiendo que role es un enum
    }
}
