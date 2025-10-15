package com.group3.realestate.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.group3.realestate.models.enums.PropertyStatus
import com.group3.realestate.models.enums.PropertyType
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.math.BigDecimal
import java.time.Instant

@Entity
@Table(name = "properties")
@EntityListeners(AuditingEntityListener::class)
data class Property(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var propertyId: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    @JsonIgnoreProperties("properties")
    var owner: Owner?,

    @Column(nullable = false)
    var addressLine1: String,
    var addressLine2: String?,
    @Column(nullable = false)
    var city: String,
    @Column(nullable = false)
    var stateProvince: String,
    @Column(nullable = false)
    var postalCode: String,
    @Column(nullable = false)
    var country: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var propertyType: PropertyType,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: PropertyStatus = PropertyStatus.AVAILABLE,

    var sizeSqft: BigDecimal?,
    var numBedrooms: Int?,
    var numBathrooms: Int?,
    var yearBuilt: Int?,
    var description: String?,

    @ManyToMany(cascade = [CascadeType.MERGE], fetch = FetchType.LAZY)
    @JoinTable(
        name = "property_agents",
        joinColumns = [JoinColumn(name = "property_id")],
        inverseJoinColumns = [JoinColumn(name = "agent_id")]
    )
    @JsonIgnoreProperties("properties")
    val managingAgents: List<Agent> = emptyList(),

    @CreatedDate
    @Column(nullable = false, updatable = false)
    var createdAt: Instant? = null,

    @LastModifiedDate
    @Column(nullable = false)
    var updatedAt: Instant? = null
)