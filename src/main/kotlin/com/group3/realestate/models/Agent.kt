package com.group3.realestate.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.Instant
import java.time.LocalDate

@Entity
@Table(name = "agents")
data class Agent(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var agentId: Long? = null,

    @Column(nullable = false)
    var firstName: String,

    @Column(nullable = false)
    var lastName: String,

    @Column(nullable = false, unique = true)
    var email: String,

    @Column(nullable = false)
    var phoneNumber: String,

    @Column(nullable = false)
    var hireDate: LocalDate,

    @ManyToMany(mappedBy = "managingAgents", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("managingAgents")
    val properties: List<Property> = emptyList(),

    @CreatedDate
    @Column(nullable = false, updatable = false)
    var createdAt: Instant? = null,

    @LastModifiedDate
    @Column(nullable = false)
    var updatedAt: Instant? = null
)