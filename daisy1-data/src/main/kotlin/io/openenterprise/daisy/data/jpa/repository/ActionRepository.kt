package io.openenterprise.daisy.data.jpa.repository

import io.openenterprise.daisy.data.jpa.domain.Action
import io.openenterprise.daisy.data.repository.AuditableRepository
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.UUID

@Repository
interface ActionRepository: AuditableRepository<Action, UUID, String, Instant>