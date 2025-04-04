package io.openenterprise.daisy.data.jpa.service

import io.openenterprise.daisy.data.jpa.domain.Action
import io.openenterprise.daisy.data.service.AbstractAuditableServiceImpl
import jakarta.inject.Named
import java.time.Instant
import java.util.UUID

@Named
class ActionServiceImpl: AbstractAuditableServiceImpl<Action, UUID, String, Instant>(), ActionService