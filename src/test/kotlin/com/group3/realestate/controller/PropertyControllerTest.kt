package com.group3.realestate.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.group3.realestate.config.TestConfig
import com.group3.realestate.models.Owner
import com.group3.realestate.models.Property
import com.group3.realestate.models.enums.PropertyStatus
import com.group3.realestate.models.enums.PropertyType
import com.group3.realestate.repository.OwnerRepository
import com.group3.realestate.repository.PropertyRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.Instant

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(properties = ["spring.profiles.active=test"])
@Import(TestConfig::class)
class PropertyControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var propertyRepository: PropertyRepository

    @Autowired
    private lateinit var ownerRepository: OwnerRepository

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private lateinit var testOwner: Owner
    private lateinit var testProperty: Property

    @BeforeEach
    fun setup() {
        propertyRepository.deleteAll()
        ownerRepository.deleteAll()

        testOwner = ownerRepository.save(
            Owner(
                firstName = "Test",
                lastName = "Owner",
                email = "testowner@example.com",
                phoneNumber = "555-0001",
                mailingAddress = "123 Test St",
                createdAt = Instant.now(),
                updatedAt = Instant.now()
            )
        )

        testProperty = propertyRepository.save(
            Property(
                owner = testOwner,
                addressLine1 = "100 Test Ave",
                addressLine2 = "Main Street",
                city = "Test City",
                stateProvince = "TC",
                postalCode = "12345",
                country = "USA",
                propertyType = PropertyType.APARTMENT,
                status = PropertyStatus.AVAILABLE,
                sizeSqft = BigDecimal("1000"),
                numBedrooms = 2,
                numBathrooms = 1,
                yearBuilt = 2020,
                description = "Test property",
                createdAt = Instant.now(),
                updatedAt = Instant.now()
            )
        )
    }

    @Test
    @WithMockUser
    fun `should get all properties`() {
        mockMvc.perform(get("/api/properties"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$[0].addressLine1").value("100 Test Ave"))
    }

    @Test
    @WithMockUser
    fun `should get property by id`() {
        mockMvc.perform(get("/api/properties/${testProperty.propertyId}"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.addressLine1").value("100 Test Ave"))
            .andExpect(jsonPath("$.city").value("Test City"))
    }

    @Test
    @WithMockUser
    fun `should return 404 when property not found`() {
        mockMvc.perform(get("/api/properties/99999"))
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.error").value("Not Found"))
    }

    @Test
    @WithMockUser
    fun `should create new property`() {
        val newProperty = mapOf(
            "owner" to mapOf("ownerId" to testOwner.ownerId),
            "addressLine1" to "200 New St",
            "addressLine2" to null,
            "city" to "New City",
            "stateProvince" to "NC",
            "postalCode" to "54321",
            "country" to "USA",
            "propertyType" to "HOUSE",
            "status" to "AVAILABLE",
            "sizeSqft" to 1500,
            "numBedrooms" to 3,
            "numBathrooms" to 2,
            "yearBuilt" to 2021,
            "description" to "New property",
            "managingAgents" to emptyList<Map<String, Any>>()
        )

        mockMvc.perform(
            post("/api/properties")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newProperty))
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.addressLine1").value("200 New St"))
            .andExpect(jsonPath("$.city").value("New City"))
    }

    @Test
    @WithMockUser
    fun `should update property`() {
        val updatedData = mapOf(
            "owner" to mapOf("ownerId" to testOwner.ownerId),
            "addressLine1" to "Updated Address",
            "addressLine2" to "Main Street",
            "city" to "Updated City",
            "stateProvince" to "TC",
            "postalCode" to "12345",
            "country" to "USA",
            "propertyType" to "APARTMENT",
            "status" to "AVAILABLE",
            "sizeSqft" to 1000,
            "numBedrooms" to 2,
            "numBathrooms" to 1,
            "yearBuilt" to 2020,
            "description" to "Updated property",
            "managingAgents" to emptyList<Map<String, Any>>()
        )

        mockMvc.perform(
            put("/api/properties/${testProperty.propertyId}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedData))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.addressLine1").value("Updated Address"))
            .andExpect(jsonPath("$.city").value("Updated City"))
    }

    @Test
    @WithMockUser
    fun `should delete property`() {
        val propertyId = testProperty.propertyId

        mockMvc.perform(delete("/api/properties/$propertyId"))
            .andExpect(status().isNoContent)

        mockMvc.perform(get("/api/properties/$propertyId"))
            .andExpect(status().isNotFound)
    }
}
