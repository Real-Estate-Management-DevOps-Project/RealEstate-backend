package com.group3.realestate.models

import com.group3.realestate.models.enums.LeaseStatus
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate

@Entity
@Table(name = "leases")
data class Lease(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var leaseId: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    var property: Property,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    var tenant: Tenant,

    @Column(nullable = false)
    var startDate: LocalDate,

    @Column(nullable = false)
    var endDate: LocalDate,

    @Column(nullable = false)
    var monthlyRent: BigDecimal,

    var securityDeposit: BigDecimal?,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: LeaseStatus = LeaseStatus.PENDING,

    var leaseDocumentUrl: String?,

    @OneToMany(mappedBy = "lease", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val payments: Set<Payment> = emptySet(),

    @CreatedDate
    @Column(nullable = false, updatable = false)
    var createdAt: Instant? = null,

    @LastModifiedDate
    @Column(nullable = false)
    var updatedAt: Instant? = null
)