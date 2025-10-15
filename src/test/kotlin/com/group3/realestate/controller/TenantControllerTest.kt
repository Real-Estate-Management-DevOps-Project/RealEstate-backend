package com.group3.realestate.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.group3.realestate.config.TestConfig
import com.group3.realestate.models.Tenant
import com.group3.realestate.repository.TenantRepository
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
import java.time.Instant

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(properties = ["spring.profiles.active=test"])
@Import(TestConfig::class)
class TenantControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var tenantRepository: TenantRepository

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private lateinit var testTenant: Tenant

    @BeforeEach
    fun setup() {
        tenantRepository.deleteAll()

        testTenant = tenantRepository.save(
            Tenant(
                firstName = "John",
                lastName = "Doe",
                email = "john.doe@example.com",
                phoneNumber = "555-1234",
                createdAt = Instant.now(),
                updatedAt = Instant.now()
            )
        )
    }

    @Test
    @WithMockUser
    fun `should get all tenants`() {
        mockMvc.perform(get("/api/tenants"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$[0].firstName").value("John"))
            .andExpect(jsonPath("$[0].lastName").value("Doe"))
    }

    @Test
    @WithMockUser
    fun `should get tenant by id`() {
        mockMvc.perform(get("/api/tenants/${testTenant.tenantId}"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.firstName").value("John"))
            .andExpect(jsonPath("$.email").value("john.doe@example.com"))
    }

    @Test
    @WithMockUser
    fun `should return 404 when tenant not found`() {
        mockMvc.perform(get("/api/tenants/99999"))
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.error").value("Not Found"))
    }

    @Test
    @WithMockUser
    fun `should create new tenant`() {
        val newTenant = mapOf(
            "firstName" to "Jane",
            "lastName" to "Smith",
            "email" to "jane.smith@example.com",
            "phoneNumber" to "555-5678",
            "leases" to emptyList<Map<String, Any>>()
        )

        mockMvc.perform(
            post("/api/tenants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newTenant))
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.firstName").value("Jane"))
            .andExpect(jsonPath("$.lastName").value("Smith"))
            .andExpect(jsonPath("$.email").value("jane.smith@example.com"))
    }

    @Test
    @WithMockUser
    fun `should update tenant`() {
        val updatedData = mapOf(
            "firstName" to "John",
            "lastName" to "Doe",
            "email" to "john.updated@example.com",
            "phoneNumber" to "555-9999",
            "leases" to emptyList<Map<String, Any>>()
        )

        mockMvc.perform(
            put("/api/tenants/${testTenant.tenantId}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedData))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.phoneNumber").value("555-9999"))
            .andExpect(jsonPath("$.email").value("john.updated@example.com"))
    }

    @Test
    @WithMockUser
    fun `should delete tenant`() {
        val tenantId = testTenant.tenantId

        mockMvc.perform(delete("/api/tenants/$tenantId"))
            .andExpect(status().isNoContent)

        mockMvc.perform(get("/api/tenants/$tenantId"))
            .andExpect(status().isNotFound)
    }
}
