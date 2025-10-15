package com.group3.realestate.seeder

import com.group3.realestate.models.*
import com.group3.realestate.models.enums.*
import com.group3.realestate.repository.*
import net.datafaker.Faker
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import kotlin.random.Random

@Component
@Profile("dev")
class DataSeeder(
    private val ownerRepository: OwnerRepository,
    private val tenantRepository: TenantRepository,
    private val agentRepository: AgentRepository,
    private val propertyRepository: PropertyRepository,
    private val leaseRepository: LeaseRepository,
    private val paymentRepository: PaymentRepository,
    private val maintenanceRequestRepository: MaintenanceRequestRepository
) : CommandLineRunner {

    private val faker = Faker()

    override fun run(vararg args: String) {
        if (ownerRepository.count() > 0) {
            println("Data already exists. Skipping seeding.")
            return
        }

        println("Seeding database with fake data...")

        // Create Owners
        val owners = (1..10).map {
            Owner(
                firstName = faker.name().firstName(),
                lastName = faker.name().lastName(),
                email = faker.internet().emailAddress(),
                phoneNumber = faker.phoneNumber().cellPhone(),
                mailingAddress = faker.address().fullAddress(),
                createdAt = Instant.now(),
                updatedAt = Instant.now()
            )
        }.let { ownerRepository.saveAll(it) }

        // Create Tenants
        val tenants = (1..20).map {
            Tenant(
                firstName = faker.name().firstName(),
                lastName = faker.name().lastName(),
                email = faker.internet().emailAddress(),
                phoneNumber = faker.phoneNumber().cellPhone(),
                createdAt = Instant.now(),
                updatedAt = Instant.now()
            )
        }.let { tenantRepository.saveAll(it) }

        // Create Agents
        val agents = (1..5).map {
            Agent(
                firstName = faker.name().firstName(),
                lastName = faker.name().lastName(),
                email = faker.internet().emailAddress(),
                phoneNumber = faker.phoneNumber().cellPhone(),
                hireDate = LocalDate.now().minusDays(Random.nextLong(365, 1825)),
                createdAt = Instant.now(),
                updatedAt = Instant.now()
            )
        }.let { agentRepository.saveAll(it) }

        // Create Properties
        val properties = (1..25).map {
            val ownersList = owners.toList()
            val agentsList = agents.toList()
            Property(
                owner = ownersList.random(),
                addressLine1 = faker.address().streetAddress(),
                addressLine2 = if (Random.nextBoolean()) faker.address().secondaryAddress() else null,
                city = faker.address().city(),
                stateProvince = faker.address().state(),
                postalCode = faker.address().zipCode(),
                country = faker.address().country(),
                propertyType = PropertyType.entries.random(),
                status = PropertyStatus.entries.random(),
                sizeSqft = BigDecimal(Random.nextInt(500, 5000)),
                numBedrooms = Random.nextInt(1, 6),
                numBathrooms = Random.nextInt(1, 4),
                yearBuilt = Random.nextInt(1950, 2024),
                description = faker.lorem().paragraph(),
                managingAgents = listOf(agentsList.random()),
                createdAt = Instant.now(),
                updatedAt = Instant.now()
            )
        }.let { propertyRepository.saveAll(it) }

        // Create Leases
        val leases = (1..15).map {
            val startDate = LocalDate.now().minusDays(Random.nextLong(0, 365))
            val endDate = startDate.plusYears(1)
            val propertiesList = properties.toList()
            val tenantsList = tenants.toList()

            Lease(
                property = propertiesList.random(),
                tenant = tenantsList.random(),
                startDate = startDate,
                endDate = endDate,
                monthlyRent = BigDecimal(Random.nextInt(500, 3000)),
                securityDeposit = BigDecimal(Random.nextInt(500, 2000)),
                status = LeaseStatus.entries.random(),
                leaseDocumentUrl = if (Random.nextBoolean()) faker.internet().url() else null,
                createdAt = Instant.now(),
                updatedAt = Instant.now()
            )
        }.let { leaseRepository.saveAll(it) }

        // Create Payments
        val payments = leases.flatMap { lease ->
            (1..Random.nextInt(3, 12)).map { month ->
                Payment(
                    lease = lease,
                    paymentDate = lease.startDate.plusMonths(month.toLong()),
                    amount = lease.monthlyRent,
                    paymentMethod = listOf("Credit Card", "Bank Transfer", "Check", "Cash").random(),
                    notes = if (Random.nextBoolean()) faker.lorem().sentence() else null,
                    createdAt = Instant.now()
                )
            }
        }.let { paymentRepository.saveAll(it) }

        // Create Maintenance Requests
        val maintenanceRequests = (1..30).map {
            val reportedDate = LocalDate.now().minusDays(Random.nextLong(0, 180))
            val status = MaintenanceStatus.entries.random()
            val propertiesList = properties.toList()
            val tenantsList = tenants.toList()

            MaintenanceRequest(
                property = propertiesList.random(),
                tenant = if (Random.nextBoolean()) tenantsList.random() else null,
                reportedDate = reportedDate,
                description = faker.lorem().paragraph(),
                status = status,
                completedDate = if (status == MaintenanceStatus.COMPLETED)
                    reportedDate.plusDays(Random.nextLong(1, 14)) else null,
                notes = if (Random.nextBoolean()) faker.lorem().sentence() else null,
                createdAt = Instant.now(),
                updatedAt = Instant.now()
            )
        }.let { maintenanceRequestRepository.saveAll(it) }

        println("Database seeding completed!")
        println("Created: ${owners.size} owners, ${tenants.size} tenants, ${agents.size} agents")
        println("Created: ${properties.size} properties, ${leases.size} leases")
        println("Created: ${payments.size} payments, ${maintenanceRequests.size} maintenance requests")
    }
}
