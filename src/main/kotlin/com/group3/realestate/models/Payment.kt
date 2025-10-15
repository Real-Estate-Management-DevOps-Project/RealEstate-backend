package com.group3.realestate.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate

@Entity
@Table(name = "payments")
@EntityListeners(AuditingEntityListener::class)
data class Payment(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var paymentId: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lease_id", nullable = false)
    @JsonIgnoreProperties("payments")
    var lease: Lease,

    @Column(nullable = false)
    var paymentDate: LocalDate,

    @Column(nullable = false)
    var amount: BigDecimal,

    var paymentMethod: String?,
    var notes: String?,

    @CreatedDate
    @Column(nullable = false, updatable = false)
    var createdAt: Instant? = null
)