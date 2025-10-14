package com.group3.realestate.repository

import com.group3.realestate.models.Agent
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AgentRepository : JpaRepository<Agent, Long>
