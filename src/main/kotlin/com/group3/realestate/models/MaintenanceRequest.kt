package com.group3.realestate.models

import com.group3.realestate.models.enums.MaintenanceStatus
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
import jakarta.persistence.Table
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.Instant
import java.time.LocalDate

@Entity
@Table(name = "maintenance_requests")
data class MaintenanceRequest(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var requestId: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    var property: Property,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id")
    var tenant: Tenant?,

    @Column(nullable = false)
    var reportedDate: LocalDate,

    @Column(nullable = false)
    var description: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: MaintenanceStatus = MaintenanceStatus.OPEN,

    var completedDate: LocalDate?,
    var notes: String?,

    @CreatedDate
    @Column(nullable = false, updatable = false)
    var createdAt: Instant? = null,

    @LastModifiedDate
    @Column(nullable = false)
    var updatedAt: Instant? = null
)