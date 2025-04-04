package io.openenterprise.daisy.rs

import io.openenterprise.daisy.data.jpa.domain.ActionMapper
import io.openenterprise.daisy.data.jpa.domain.EntityMapper
import io.openenterprise.daisy.data.service.AuditableService
import io.openenterprise.daisy.rs.model.Action
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.core.SecurityContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.Objects
import java.util.Optional
import java.util.UUID

@Component
open class ActionsApiServiceImpl(
    @Autowired protected val actionDataServiceOptional: Optional<AuditableService<io.openenterprise.daisy.data.jpa.domain.Action, UUID, String, Instant>>
) : AbstractApiServiceImpl<io.openenterprise.daisy.data.jpa.domain.Action, UUID, String, Instant, Action>(
    actionDataServiceOptional
), ActionsApiService {

    override val mapper: EntityMapper<io.openenterprise.daisy.data.jpa.domain.Action, Action> =
        ActionMapper.INSTANCE

    override fun deleteActionById(
        id: UUID, securityContext: SecurityContext
    ): Response {
        super.deleteById(id)

        return Response.status(Response.Status.NO_CONTENT).build()
    }

    override fun getActionById(
        id: UUID, securityContext: SecurityContext
    ): Response {
        val model = super.getById(id)
        val responseBuilder = if (Objects.isNull(model))
            Response.status(Response.Status.NOT_FOUND)
        else
            Response.status(Response.Status.OK).entity(model)

        return responseBuilder.build()
    }

    override fun headActionById(
        id: UUID, securityContext: SecurityContext
    ): Response {
        val model = super.getById(id)
        val responseBuilder = if (Objects.isNull(model))
            Response.status(Response.Status.NOT_FOUND)
        else
            Response.status(Response.Status.NO_CONTENT)

        return responseBuilder.build()
    }

    override fun postAction(
        body: Action, securityContext: SecurityContext
    ): Response {
        val saved = super.save(body)

        return Response.status(Response.Status.CREATED).entity(saved).build()
    }
}