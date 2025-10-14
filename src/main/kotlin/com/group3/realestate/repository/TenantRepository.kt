package com.group3.realestate.repository

import com.group3.realestate.models.Tenant
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TenantRepository : JpaRepository<Tenant, Long>
