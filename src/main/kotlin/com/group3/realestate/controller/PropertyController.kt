package com.group3.realestate.controller

import com.group3.realestate.models.Property
import com.group3.realestate.repository.PropertyRepository
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/properties")
@Tag(name = "Properties", description = "Property management endpoints")
class PropertyController(
    private val propertyRepository: PropertyRepository
) {

    @GetMapping
    @Operation(summary = "Get all properties", description = "Retrieve a list of all properties")
    fun getAllProperties(): ResponseEntity<List<Property>> {
        val properties = propertyRepository.findAll()
        return ResponseEntity.ok(properties)
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get property by ID", description = "Retrieve a specific property by its ID")
    fun getPropertyById(@PathVariable id: Long): ResponseEntity<Property> {
        val property = propertyRepository.findById(id)
            .orElseThrow { NoSuchElementException("Property not found with id: $id") }
        return ResponseEntity.ok(property)
    }

    @PostMapping
    @Operation(summary = "Create new property", description = "Create a new property listing")
    fun createProperty(@RequestBody property: Property): ResponseEntity<Property> {
        val savedProperty = propertyRepository.save(property)
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProperty)
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update property", description = "Update an existing property")
    fun updateProperty(
        @PathVariable id: Long,
        @RequestBody updatedProperty: Property
    ): ResponseEntity<Property> {
        val property = propertyRepository.findById(id)
            .orElseThrow { NoSuchElementException("Property not found with id: $id") }

        updatedProperty.propertyId = id
        val saved = propertyRepository.save(updatedProperty)
        return ResponseEntity.ok(saved)
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete property", description = "Delete a property by ID")
    fun deleteProperty(@PathVariable id: Long): ResponseEntity<Void> {
        propertyRepository.deleteById(id)
        return ResponseEntity.noContent().build()
    }
}
