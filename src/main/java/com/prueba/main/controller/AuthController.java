package com.prueba.main.controller;


import com.prueba.main.dtos.reponse.AuthResponse;
import com.prueba.main.dtos.reponse.CarrierResponseDTO;
import com.prueba.main.dtos.request.AuthRequest;
import com.prueba.main.dtos.request.CarrierCreateDTO;
import com.prueba.main.entity.Carrier;
import com.prueba.main.security.JwtTokenProvider;
import com.prueba.main.service.CarrierService;
import com.prueba.main.util.enums.CustomDatails.CustomUserDetails;
import com.prueba.main.util.enums.Role;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {


    @Autowired
    private CarrierService carrierService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;


    @Operation(summary = "Create new user", description = "this enpoint create a new user in database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/register")
    public ResponseEntity<CarrierResponseDTO> registerUser(@RequestBody CarrierCreateDTO carrierCreateDTO) {
        // Crear un nuevo usuario
        Carrier carrier = new Carrier();
        carrier.setName(carrierCreateDTO.getName());
        carrier.setUsername(carrierCreateDTO.getUsername());
        carrier.setPassword(carrierCreateDTO.getPassword());


        // Asignar el rol desde el DTO
        if (carrierCreateDTO.getRole() == null) {
            carrier.setRole(Role.OPERATOR); // Asignar rol USER por defecto si no se especifica
        } else {
            carrier.setRole(carrierCreateDTO.getRole());
        }

        // Crear el usuario a través del servicio
        Carrier createdCarrier = carrierService.createUser(carrier);

        // Construir la respuesta
        CarrierResponseDTO responseCarrierDTO = new CarrierResponseDTO();
        responseCarrierDTO.setId(createdCarrier.getId());
        responseCarrierDTO.setName(createdCarrier.getName());
        responseCarrierDTO.setRole(createdCarrier.getRole().name());

        return ResponseEntity.ok(responseCarrierDTO);
    }

    @Operation(summary = "Login", description = "This enpoint is in charge of authenticating the start of the section in the api")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        try {
            // Autenticación
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(),authRequest.getPassword()));

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Generar el token JWT
            String token = jwtTokenProvider.generateToken(userDetails.getUsername(), ((CustomUserDetails) userDetails).getRole());

            // Retornar el token en la respuesta
            return ResponseEntity.ok((AuthResponse) new AuthResponse(token));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body((AuthResponse) new AuthResponse("Credenciales incorrectas"));
        }
    }




}
