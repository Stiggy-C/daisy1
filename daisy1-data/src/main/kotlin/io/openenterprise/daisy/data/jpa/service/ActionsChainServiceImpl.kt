package io.openenterprise.daisy.data.jpa.service

import io.openenterprise.daisy.data.jpa.domain.ActionsChain
import io.openenterprise.daisy.data.service.AbstractAuditableServiceImpl
import jakarta.inject.Named
import java.time.Instant
import java.util.UUID

@Named
class ActionsChainServiceImpl: AbstractAuditableServiceImpl<ActionsChain, UUID, String, Instant>(),
    ActionsChainService