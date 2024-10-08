package com.prueba.main.controller;

import com.prueba.main.entity.Charge;
import com.prueba.main.service.ChargeService;
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
@RequestMapping("/api/charges")
public class ChargeController {

    @Autowired
    private ChargeService chargeService;

    @Operation(summary = "Get all charges", description = "Retrieve a list of all charges (Admin access only).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    @RolesAllowed("ADMIN")
    public ResponseEntity<List<Charge>> getAllCharges() {
        List<Charge> charges = chargeService.getAllCharges();
        return ResponseEntity.ok(charges);
    }

    @Operation(summary = "Get a charge by ID", description = "Retrieve a specific charge by its ID (Admin access only).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Charge not found"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    @RolesAllowed("ADMIN")
    public ResponseEntity<Optional<Charge>> getChargeById(@PathVariable Long id) {
        Optional<Charge> charge = chargeService.getChargeById(id);
        return ResponseEntity.ok(charge);
    }

    @Operation(summary = "Create a new charge", description = "Create a new charge in the system (Open to all users).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Charge created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<Charge> createCharge(@Valid @RequestBody Charge charge) {
        Charge newCharge = chargeService.createCharge(charge);
        return ResponseEntity.ok(newCharge);
    }

    @Operation(summary = "Assign charge to pallet", description = "Assign a charge to a specific pallet (Available for Carriers and Admins).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Charge assigned to pallet successfully"),
            @ApiResponse(responseCode = "404", description = "Charge or pallet not found"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{chargeId}/assign/{palletId}")
    @RolesAllowed({"CARRIER", "ADMIN"})
    public ResponseEntity<Charge> assignChargeToPallet(@PathVariable Long chargeId, @PathVariable Long palletId) {
        Charge charge = chargeService.assignChargeToPallet(chargeId, palletId);
        return ResponseEntity.ok(charge);
    }

    @Operation(summary = "Delete a charge", description = "Delete a charge by its ID (Admin access only).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Charge deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Charge not found"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    @RolesAllowed("ADMIN")
    public ResponseEntity<Void> deleteCharge(@PathVariable Long id) {
        chargeService.deleteCharge(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update charge state", description = "Update the state of a charge (Available for Carriers and Admins).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Charge state updated successfully"),
            @ApiResponse(responseCode = "404", description = "Charge not found"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}/updatestate")
    @RolesAllowed({"CARRIER", "ADMIN"})
    public ResponseEntity<String> updateState(@PathVariable Long id, @RequestBody String newState) {
        chargeService.updateState(id, newState);
        return ResponseEntity.ok("Charge state updated successfully.");
    }

    @Operation(summary = "Report damage on a charge", description = "Report damage on a specific charge (Available for Carriers and Admins).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Damage reported successfully"),
            @ApiResponse(responseCode = "404", description = "Charge not found"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}/reportdamage")
    @RolesAllowed({"CARRIER", "ADMIN"})
    public ResponseEntity<String> reportDamage(@PathVariable Long id) {
        chargeService.reportDamage(id);
        return ResponseEntity.ok("Damage reported for the charge.");
    }

    @Operation(summary = "Get charges assigned to a carrier", description = "Retrieve all charges assigned to a specific carrier (Carriers only).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Charges retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Carrier not found"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/carrier/{carrierId}/charges")
    @RolesAllowed("CARRIER")
    public ResponseEntity<List<Charge>> getChargesByCarrier(@PathVariable Long carrierId) {
        List<Charge> charges = chargeService.getChargesByCarrier(carrierId);
        return ResponseEntity.ok(charges);
    }

    @Operation(summary = "Get charges assigned to a pallet", description = "Retrieve all charges assigned to a specific pallet (Available for Carriers and Admins).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Charges retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Pallet not found"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/pallet/{palletId}/charges")
    @RolesAllowed({"CARRIER", "ADMIN"})
    public ResponseEntity<List<Charge>> getChargesByPallet(@PathVariable Long palletId) {
        List<Charge> charges = chargeService.getChargesByPallet(palletId);
        return ResponseEntity.ok(charges);
    }
}
