package com.group3.realestate.repository

import com.group3.realestate.models.Property
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PropertyRepository : JpaRepository<Property, Long>
