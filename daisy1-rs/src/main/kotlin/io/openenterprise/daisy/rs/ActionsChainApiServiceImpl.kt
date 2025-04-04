package io.openenterprise.daisy.rs

import io.openenterprise.commons.util.UUIDUtils
import io.openenterprise.daisy.InvocationContext
import io.openenterprise.daisy.data.jpa.domain.ActionsChainMapper
import io.openenterprise.daisy.data.jpa.domain.EntityMapper
import io.openenterprise.daisy.data.service.AuditableService
import io.openenterprise.daisy.data.spark.sql.DatasetPage
import io.openenterprise.daisy.domain.Action
import io.openenterprise.daisy.domain.ActionMapper
import io.openenterprise.daisy.http.HttpHeaders
import io.openenterprise.daisy.rs.model.ActionsChain
import io.openenterprise.daisy.service.ActionService
import jakarta.ws.rs.NotFoundException
import jakarta.ws.rs.ServiceUnavailableException
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.core.SecurityContext
import org.apache.commons.lang3.BooleanUtils
import org.apache.commons.lang3.ClassUtils
import org.apache.spark.sql.Dataset
import org.apache.spark.sql.Row
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.Objects
import java.util.Optional
import java.util.UUID
import javax.cache.Cache

@Component
open class ActionsChainApiServiceImpl(
    @Autowired protected val actionService: ActionService,
    @Autowired protected val actionsChainDataServiceOptional: Optional<AuditableService<io.openenterprise.daisy.data.jpa.domain.ActionsChain, UUID, String, Instant>>,
    @Autowired protected val invocationContextCache: Cache<UUID, InvocationContext>,
    @Value("\${daisy.data.pageable.default-page-size:50}") protected val pageSizeDefaultValue: Int,
) : AbstractApiServiceImpl<io.openenterprise.daisy.data.jpa.domain.ActionsChain, UUID, String, Instant, ActionsChain>(
    actionsChainDataServiceOptional
), ActionsChainApiService {

    override val mapper: EntityMapper<io.openenterprise.daisy.data.jpa.domain.ActionsChain, ActionsChain> =
        ActionsChainMapper.INSTANCE

    override fun deleteActionsChainById(
        id: UUID,
        securityContext: SecurityContext
    ): Response {
        super.deleteById(id)

        return Response.status(Response.Status.NO_CONTENT).build()
    }

    override fun getActionsChainById(
        id: UUID,
        securityContext: SecurityContext
    ): Response {
        val model = super.getById(id)
        val responseBuilder = if (Objects.isNull(model))
            Response.status(Response.Status.NOT_FOUND)
        else
            Response.status(Response.Status.OK).entity(model)

        return responseBuilder.build()
    }

    override fun headActionsChainById(
        id: UUID,
        securityContext: SecurityContext
    ): Response {
        val model = super.getById(id)
        val responseBuilder = if (Objects.isNull(model))
            Response.status(Response.Status.NOT_FOUND)
        else
            Response.status(Response.Status.NO_CONTENT)

        return responseBuilder.build()
    }

    override fun invokeActionsChain(
        body: ActionsChain,
        xDaisySessionId: UUID?,
        persist: Boolean?,
        securityContext: SecurityContext
    ): Response {
        if (BooleanUtils.isTrue(persist)) {
            actionsChainDataServiceOptional.orElseThrow { ServiceUnavailableException() }

            super.save(body)
        }

        val entity = ActionsChainMapper.INSTANCE.restModelToEntity(body)
        val invocationContextAndResult = invokeActionsChain(entity!!, xDaisySessionId)
        val invocationContext: InvocationContext = invocationContextAndResult.first
        var result = invocationContextAndResult.second

        if (Objects.nonNull(result) && ClassUtils.isAssignable(result!!::class.java, Dataset::class.java)) {
            @Suppress("UNCHECKED_CAST")
            val dataset = result as Dataset<Row>

            result = DatasetPage(dataset, PageRequest.ofSize(pageSizeDefaultValue))
        }

        val lastInvocationId = invocationContext.previousInvocations.last.id

        try {
            return Response.status(Response.Status.OK)
                .header(HttpHeaders.LAST_INVOCATION_ID, lastInvocationId)
                .entity(result).build()
        } finally {
            invocationContextCache.put(invocationContext.id, invocationContext)
        }
    }

    override fun invokeActionsChainById(
        id: UUID,
        xDaisySessionId: UUID?,
        securityContext: SecurityContext?
    ): Response {
        val entity = super.getEntityById(id);

        if (Objects.isNull(entity)) {
            throw NotFoundException()
        }

        val invocationContextAndResult = invokeActionsChain(entity!!, xDaisySessionId)
        val invocationContext: InvocationContext = invocationContextAndResult.first
        var result = invocationContextAndResult.second

        if (Objects.nonNull(result) && ClassUtils.isAssignable(result!!::class.java, Dataset::class.java)) {
            @Suppress("UNCHECKED_CAST")
            val dataset = result as Dataset<Row>

            result = DatasetPage(dataset, PageRequest.ofSize(pageSizeDefaultValue))
        }

        val lastInvocationId = invocationContext.previousInvocations.last.id

        try {
            return Response.status(Response.Status.OK)
                .header(HttpHeaders.LAST_INVOCATION_ID, lastInvocationId)
                .entity(result).build()
        } finally {
            invocationContextCache.put(invocationContext.id, invocationContext)
        }
    }

    override fun postActionsChain(
        body: ActionsChain,
        securityContext: SecurityContext?
    ): Response {
        val saved = super.save(body)

        return Response.status(Response.Status.CREATED).entity(saved).build()
    }

    protected fun invokeActionsChain(
        entity: io.openenterprise.daisy.data.jpa.domain.ActionsChain,
        xDaisySessionId: UUID?
    ): Pair<InvocationContext, Any?> {
        val actions = entity.actions.stream()
            .map { it -> ActionMapper.INSTANCE.entityToModel(it) }
            .toList()

        val invocationContext: InvocationContext = if (Objects.isNull(xDaisySessionId)) {
            InvocationContext.Builder().withId(UUIDUtils.randomUUIDv7()).build()
                .also { it -> invocationContextCache.put(it.id, it) }
        } else {
            invocationContextCache.get(xDaisySessionId)
        }

        var result = actionService.invoke(invocationContext, actions as List<Action<*>>)
        return Pair(invocationContext, result)
    }
}