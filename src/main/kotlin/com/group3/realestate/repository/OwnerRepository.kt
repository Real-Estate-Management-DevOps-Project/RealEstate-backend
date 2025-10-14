package com.group3.realestate.repository

import com.group3.realestate.models.Owner
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OwnerRepository : JpaRepository<Owner, Long>
