package com.group3.realestate.repository

import com.group3.realestate.models.Owner
import com.group3.realestate.models.Property
import com.group3.realestate.models.enums.PropertyStatus
import com.group3.realestate.models.enums.PropertyType
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.time.Instant

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
class PropertyRepositoryTest {

    @Autowired
    private lateinit var propertyRepository: PropertyRepository

    @Autowired
    private lateinit var ownerRepository: OwnerRepository

    @Test
    fun `should save and retrieve property`() {
        // Given
        val owner = Owner(
            firstName = "John",
            lastName = "Doe",
            email = "john.doe@example.com",
            phoneNumber = "123-456-7890",
            mailingAddress = "123 Main St",
            createdAt = Instant.now(),
            updatedAt = Instant.now()
        )
        val savedOwner = ownerRepository.save(owner)

        val property = Property(
            owner = savedOwner,
            addressLine1 = "456 Oak Ave",
            addressLine2 = null,
            city = "Springfield",
            stateProvince = "IL",
            postalCode = "62701",
            country = "USA",
            propertyType = PropertyType.APARTMENT,
            status = PropertyStatus.AVAILABLE,
            sizeSqft = BigDecimal("1200"),
            numBedrooms = 2,
            numBathrooms = 2,
            yearBuilt = 2020,
            description = "Modern apartment",
            createdAt = Instant.now(),
            updatedAt = Instant.now()
        )

        // When
        val savedProperty = propertyRepository.save(property)

        // Then
        assertNotNull(savedProperty.propertyId)
        assertEquals("456 Oak Ave", savedProperty.addressLine1)
        assertEquals("Springfield", savedProperty.city)
        assertEquals(PropertyType.APARTMENT, savedProperty.propertyType)
        assertEquals(PropertyStatus.AVAILABLE, savedProperty.status)
    }

    @Test
    fun `should find all properties`() {
        // Given
        val owner = ownerRepository.save(
            Owner(
                firstName = "Jane",
                lastName = "Smith",
                email = "jane.smith@example.com",
                phoneNumber = "12345678",
                mailingAddress = "456 Main St",
                createdAt = Instant.now(),
                updatedAt = Instant.now()
            )
        )

        propertyRepository.save(
            Property(
                owner = owner,
                addressLine1 = "100 First St",
                addressLine2 = null,
                city = "Chicago",
                stateProvince = "IL",
                postalCode = "60601",
                country = "USA",
                propertyType = PropertyType.HOUSE,
                status = PropertyStatus.AVAILABLE,
                sizeSqft = BigDecimal("2000"),
                numBedrooms = 3,
                numBathrooms = 2,
                yearBuilt = 2018,
                description = "Nice house",
                createdAt = Instant.now(),
                updatedAt = Instant.now()
            )
        )

        // When
        val properties = propertyRepository.findAll()

        // Then
        assertTrue(properties.isNotEmpty())
    }

    @Test
    fun `should delete property`() {
        // Given
        val owner = ownerRepository.save(
            Owner(
                firstName = "Bob",
                lastName = "Johnson",
                email = "bob.johnson@example.com",
                phoneNumber = "555-0001",
                mailingAddress = "789 Oak St",
                createdAt = Instant.now(),
                updatedAt = Instant.now()
            )
        )

        val property = propertyRepository.save(
            Property(
                owner = owner,
                addressLine1 = "200 Second St",
                addressLine2 = null,
                city = "Boston",
                stateProvince = "MA",
                postalCode = "02101",
                country = "USA",
                propertyType = PropertyType.CONDO,
                status = PropertyStatus.AVAILABLE,
                sizeSqft = BigDecimal("1500"),
                numBedrooms = 2,
                numBathrooms = 2,
                yearBuilt = 2019,
                description = "Modern condo",
                createdAt = Instant.now(),
                updatedAt = Instant.now()
            )
        )

        // When
        propertyRepository.deleteById(property.propertyId!!)

        // Then
        assertFalse(propertyRepository.findById(property.propertyId!!).isPresent)
    }
}
