package com.group3.realestate.controller

import com.group3.realestate.models.Owner
import com.group3.realestate.repository.OwnerRepository
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/owners")
@Tag(name = "Owners", description = "Property owner management endpoints")
class OwnerController(
    private val ownerRepository: OwnerRepository
) {

    @GetMapping
    @Operation(summary = "Get all owners", description = "Retrieve a list of all property owners")
    fun getAllOwners(): ResponseEntity<List<Owner>> {
        val owners = ownerRepository.findAll()
        return ResponseEntity.ok(owners)
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get owner by ID", description = "Retrieve a specific owner by their ID")
    fun getOwnerById(@PathVariable id: Long): ResponseEntity<Owner> {
        val owner = ownerRepository.findById(id).orElse(null)
            ?: throw NoSuchElementException("Owner not found with id: $id")
        return ResponseEntity.ok(owner)
    }

    @PostMapping
    @Operation(summary = "Create new owner", description = "Register a new property owner")
    fun createOwner(@RequestBody owner: Owner): ResponseEntity<Owner> {
        val savedOwner = ownerRepository.save(owner)
        return ResponseEntity.status(HttpStatus.CREATED).body(savedOwner)
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update owner", description = "Update an existing owner's information")
    fun updateOwner(
        @PathVariable id: Long,
        @RequestBody updatedOwner: Owner
    ): ResponseEntity<Owner> {
        if (!ownerRepository.existsById(id)) {
            throw NoSuchElementException("Owner not found with id: $id")
        }
        updatedOwner.ownerId = id
        val saved = ownerRepository.save(updatedOwner)
        return ResponseEntity.ok(saved)
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete owner", description = "Delete an owner by ID")
    fun deleteOwner(@PathVariable id: Long): ResponseEntity<Void> {
        if (!ownerRepository.existsById(id)) {
            throw NoSuchElementException("Owner not found with id: $id")
        }
        ownerRepository.deleteById(id)
        return ResponseEntity.noContent().build()
    }
}
