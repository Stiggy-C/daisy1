package io.openenterprise.daisy.data.jpa.service

import io.openenterprise.daisy.data.jpa.domain.ActionsChain
import io.openenterprise.daisy.data.service.AuditableService
import java.time.Instant
import java.util.UUID

interface ActionsChainService: AuditableService<ActionsChain, UUID, String, Instant>