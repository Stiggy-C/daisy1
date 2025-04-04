package io.openenterprise.daisy.data.jpa.service

import io.openenterprise.daisy.data.jpa.domain.Action
import io.openenterprise.daisy.data.service.AuditableService
import jakarta.inject.Named
import java.time.Instant
import java.util.UUID

@Named
interface ActionService: AuditableService<Action, UUID, String, Instant>
