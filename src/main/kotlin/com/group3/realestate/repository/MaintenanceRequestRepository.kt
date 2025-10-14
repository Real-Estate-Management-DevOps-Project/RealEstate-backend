package com.group3.realestate.repository

import com.group3.realestate.models.MaintenanceRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MaintenanceRequestRepository : JpaRepository<MaintenanceRequest, Long>
