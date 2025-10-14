package com.group3.realestate.controller

import com.group3.realestate.models.Tenant
import com.group3.realestate.repository.TenantRepository
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/tenants")
@Tag(name = "Tenants", description = "Tenant management endpoints")
class TenantController(
    private val tenantRepository: TenantRepository
) {

    @GetMapping
    @Operation(summary = "Get all tenants", description = "Retrieve a list of all tenants")
    fun getAllTenants(): ResponseEntity<List<Tenant>> {
        val tenants = tenantRepository.findAll()
        return ResponseEntity.ok(tenants)
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get tenant by ID", description = "Retrieve a specific tenant by their ID")
    fun getTenantById(@PathVariable id: Long): ResponseEntity<Tenant> {
        val tenant = tenantRepository.findById(id)
            .orElseThrow { NoSuchElementException("Tenant not found with id: $id") }
        return ResponseEntity.ok(tenant)
    }

    @PostMapping
    @Operation(summary = "Create new tenant", description = "Register a new tenant")
    fun createTenant(@RequestBody tenant: Tenant): ResponseEntity<Tenant> {
        val savedTenant = tenantRepository.save(tenant)
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTenant)
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update tenant", description = "Update an existing tenant's information")
    fun updateTenant(
        @PathVariable id: Long,
        @RequestBody updatedTenant: Tenant
    ): ResponseEntity<Tenant> {
        val tenant = tenantRepository.findById(id)
            .orElseThrow { NoSuchElementException("Tenant not found with id: $id") }

        updatedTenant.tenantId = id
        val saved = tenantRepository.save(updatedTenant)
        return ResponseEntity.ok(saved)
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete tenant", description = "Delete a tenant by ID")
    fun deleteTenant(@PathVariable id: Long): ResponseEntity<Void> {
        tenantRepository.deleteById(id)
        return ResponseEntity.noContent().build()
    }
}
