package com.group3.realestate.repository

import com.group3.realestate.models.Tenant
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import java.time.Instant

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class TenantRepositoryTest {

    @Autowired
    private lateinit var tenantRepository: TenantRepository

    @Test
    fun `should save and retrieve tenant`() {
        // Given
        val tenant = Tenant(
            firstName = "Alice",
            lastName = "Williams",
            email = "alice.williams@example.com",
            phoneNumber = "555-0100",
            createdAt = Instant.now(),
            updatedAt = Instant.now()
        )

        // When
        val savedTenant = tenantRepository.save(tenant)

        // Then
        assertNotNull(savedTenant.tenantId)
        assertEquals("Alice", savedTenant.firstName)
        assertEquals("Williams", savedTenant.lastName)
        assertEquals("alice.williams@example.com", savedTenant.email)
    }

    @Test
    fun `should find all tenants`() {
        // Given
        tenantRepository.save(
            Tenant(
                firstName = "Bob",
                lastName = "Brown",
                email = "bob.brown@example.com",
                phoneNumber = "555-0101",
                createdAt = Instant.now(),
                updatedAt = Instant.now()
            )
        )

        tenantRepository.save(
            Tenant(
                firstName = "Carol",
                lastName = "Davis",
                email = "carol.davis@example.com",
                phoneNumber = "555-0102",
                createdAt = Instant.now(),
                updatedAt = Instant.now()
            )
        )

        // When
        val tenants = tenantRepository.findAll()

        // Then
        assertTrue(tenants.size >= 2)
    }

    @Test
    fun `should update tenant`() {
        // Given
        val tenant = tenantRepository.save(
            Tenant(
                firstName = "David",
                lastName = "Miller",
                email = "david.miller@example.com",
                phoneNumber = "555-0103",
                createdAt = Instant.now(),
                updatedAt = Instant.now()
            )
        )

        // When
        tenant.phoneNumber = "555-9999"
        val updatedTenant = tenantRepository.save(tenant)

        // Then
        assertEquals("555-9999", updatedTenant.phoneNumber)
    }

    @Test
    fun `should delete tenant`() {
        // Given
        val tenant = tenantRepository.save(
            Tenant(
                firstName = "Eve",
                lastName = "Wilson",
                email = "eve.wilson@example.com",
                phoneNumber = "555-0104",
                createdAt = Instant.now(),
                updatedAt = Instant.now()
            )
        )

        // When
        tenantRepository.deleteById(tenant.tenantId!!)

        // Then
        assertFalse(tenantRepository.findById(tenant.tenantId!!).isPresent)
    }
}
