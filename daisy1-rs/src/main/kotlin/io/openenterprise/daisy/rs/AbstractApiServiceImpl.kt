package io.openenterprise.daisy.rs

import io.openenterprise.daisy.data.domain.Auditable
import io.openenterprise.daisy.data.jpa.domain.EntityMapper
import io.openenterprise.daisy.data.service.AuditableService
import jakarta.inject.Inject
import jakarta.ws.rs.InternalServerErrorException
import jakarta.ws.rs.NotFoundException
import jakarta.ws.rs.ServiceUnavailableException
import java.io.Serializable
import java.time.temporal.Temporal
import java.util.Optional

abstract class AbstractApiServiceImpl<A: Auditable<I, U, T>, I: Serializable, U: Serializable, T: Temporal, M>(
        @Inject val dataServiceOptional: Optional<AuditableService<A, I, U, T>>) {

    protected abstract val mapper: EntityMapper<A, M>

    fun deleteById(id : I)  {
        val dataService = dataServiceOptional.orElseThrow { ServiceUnavailableException() }

        if (dataService.existsById(id)) {
            try {
                dataService.deleteById(id)
            } catch(e: Exception) {
                throw InternalServerErrorException(e)
            }
        } else {
            throw NotFoundException()
        }
    }

    fun getById(id: I): M? {
        return mapper.entityToRestModel(getEntityById(id))
    }

    fun getEntityById(id: I): A? {
        val dataService = dataServiceOptional.orElseThrow { ServiceUnavailableException() }

        return if (dataService.existsById(id)) {
            dataService.findById(id)
        } else {
            null
        }
    }

    fun save(model: M): M {
        val dataService = dataServiceOptional.orElseThrow { ServiceUnavailableException() }
        val entity = mapper.restModelToEntity(model)

        return mapper.entityToRestModel(dataService.save(entity))
    }
}