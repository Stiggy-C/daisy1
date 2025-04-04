package io.openenterprise.daisy.data.jpa.repository

import io.openenterprise.daisy.data.jpa.domain.ActionsChain
import io.openenterprise.daisy.data.repository.AuditableRepository
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.UUID

@Repository
interface ActionsRepository: AuditableRepository<ActionsChain, UUID, String, Instant>