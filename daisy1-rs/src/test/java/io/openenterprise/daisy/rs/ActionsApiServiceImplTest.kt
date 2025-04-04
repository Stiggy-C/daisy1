package io.openenterprise.daisy.rs

import io.openenterprise.daisy.data.jpa.domain.Action
import io.openenterprise.daisy.data.jpa.service.ActionService
import io.openenterprise.daisy.data.jpa.service.ActionServiceImpl
import io.openenterprise.daisy.data.service.AuditableService
import io.openenterprise.daisy.mvel2.Mvel2Operation
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.core.SecurityContext
import org.apache.commons.lang3.StringUtils
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.function.ThrowingSupplier
import org.mockito.ArgumentMatcher
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.Instant
import java.util.Optional
import java.util.UUID

@ExtendWith(SpringExtension::class)
@Import(ActionsApiServiceImplTest.Configuration::class)
class ActionsApiServiceImplTest {

    @Autowired
    lateinit var actionsApiServiceImpl: ActionsApiServiceImpl

    @Autowired
    lateinit var actionService: ActionService

    @Test
    fun deleteActionById() {
        val securityContext = Mockito.mock(SecurityContext::class.java)
        val uuid = UUID.randomUUID()

        var response: Response = Assertions.assertDoesNotThrow(object : ThrowingSupplier<Response> {
            override fun get(): Response? {
                return actionsApiServiceImpl.deleteActionById(uuid, securityContext)
            }
        })

        Assertions.assertNotNull(response)
        Assertions.assertEquals(HttpStatus.NO_CONTENT.value(), response.status)
    }

    @Test
    fun getActionById() {
        val action = Action()
        val securityContext = Mockito.mock(SecurityContext::class.java)
        val uuid = UUID.randomUUID()

        Mockito.`when`(actionService.findById(Mockito.eq(uuid))).thenReturn(action)

        var response: Response = Assertions.assertDoesNotThrow(object : ThrowingSupplier<Response> {
            override fun get(): Response? {
                return actionsApiServiceImpl.getActionById(uuid, securityContext)
            }
        })

        Assertions.assertNotNull(response)
        Assertions.assertEquals(HttpStatus.OK.value(), response.status)
    }

    @Test
    fun headActionById() {
        val action = Action()
        val securityContext = Mockito.mock(SecurityContext::class.java)
        val uuid = UUID.randomUUID()

        Mockito.`when`(actionService.findById(Mockito.eq(uuid))).thenReturn(action)

        var response: Response = Assertions.assertDoesNotThrow(object : ThrowingSupplier<Response> {
            override fun get(): Response? {
                return actionsApiServiceImpl.headActionById(uuid, securityContext)
            }
        })

        Assertions.assertNotNull(response)
        Assertions.assertEquals(HttpStatus.NO_CONTENT.value(), response.status)
    }

    @Test
    fun postAction() {
        val action = io.openenterprise.daisy.rs.model.Action()
        action.operation = Mvel2Operation::class.java.toString()

        val securityContext = Mockito.mock(SecurityContext::class.java)

        var response: Response = Assertions.assertDoesNotThrow(object : ThrowingSupplier<Response> {
            override fun get(): Response? {
                return actionsApiServiceImpl.postAction(action, securityContext)
            }
        })

        Assertions.assertNotNull(response)
        Assertions.assertEquals(HttpStatus.CREATED.value(), response.status)

        Mockito.verify(actionService, Mockito.times(1)).save(Mockito.argThat(object : ArgumentMatcher<Action> {
            override fun matches(p0: Action?): Boolean {
                return StringUtils.equals(action.operation, p0!!.operation)
            }
        }))
    }

    @TestConfiguration
    class Configuration {

        @Bean
        fun actionService(): ActionService {
            return Mockito.mock(ActionServiceImpl::class.java)
        }

        @Bean
        fun actionsApiService(): ActionsApiService {
            return ActionsApiServiceImpl(Optional.of<AuditableService<Action, UUID, String, Instant>>(actionService()))
        }
    }
}