package com.prueba.main.controller;

import com.prueba.main.entity.Charge;
import com.prueba.main.entity.Pallet;
import com.prueba.main.service.PalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pallets")
public class PalletController {

    @Autowired
    private PalletService palletService;

    @Operation(summary = "Get all pallets", description = "Retrieve a list of all pallets (Admin access only).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    @RolesAllowed("ADMIN")
    public ResponseEntity<List<Pallet>> getAllPallets() {
        List<Pallet> pallets = palletService.getAllPallets();
        return ResponseEntity.ok(pallets);
    }

    @Operation(summary = "Get pallet by ID", description = "Retrieve a specific pallet by its ID (Admin access only).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Pallet not found"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    @RolesAllowed("ADMIN")
    public ResponseEntity<Optional<Pallet>> getPalletById(@PathVariable Long id) {
        Optional<Pallet> pallet = palletService.getPalletById(id);
        return ResponseEntity.ok(pallet);
    }

    @Operation(summary = "Create a new pallet", description = "Create a new pallet in the system (Open to all users).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pallet created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<Pallet> createPallet(@Valid @RequestBody Pallet pallet) {
        Pallet newPallet = palletService.createPallet(pallet);
        return ResponseEntity.ok(newPallet);
    }

    @Operation(summary = "Get charges by pallet ID", description = "Retrieve all charges associated with a specific pallet (Open to all users).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Charges retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Pallet not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{palletId}/charges")
    public ResponseEntity<List<Charge>> getChargesByPallet(@PathVariable Long palletId) {
        List<Charge> charges = palletService.getChargesByPallet(palletId);
        return ResponseEntity.ok(charges);
    }

    @Operation(summary = "Delete a pallet", description = "Delete a pallet by its ID (Admin access only).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pallet deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Pallet not found"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    @RolesAllowed("ADMIN")
    public ResponseEntity<Void> deletePallet(@PathVariable Long id) {
        palletService.deletePallet(id);
        return ResponseEntity.noContent().build();
    }
}
