package io.openenterprise.graalvm.polyglot.python

import io.openenterprise.graalvm.polyglot.ContextBuilder
import io.openenterprise.graalvm.polyglot.SupportedLanguage
import io.openenterprise.graalvm.polyglot.SupportedLanguage.PYTHON
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Runnable
import org.apache.commons.lang3.StringUtils
import org.graalvm.polyglot.Context
import org.graalvm.polyglot.Engine
import org.graalvm.polyglot.io.IOAccess
import org.graalvm.python.embedding.utils.VirtualFileSystem
import org.springframework.beans.factory.annotation.Value
import javax.annotation.Nonnull

class ContextBuilder : ContextBuilder {

    companion object {

        @JvmStatic
        val ENGINE: Engine = Engine.newBuilder(PYTHON.languageId).build();

    }

    @Value("\${io.openenterprise.daisy.graalvm.python.executable:''}")
    lateinit var pythonExecutableUriAsString: String

    @Nonnull
    override fun createContext(): Context {
        var builder =
            Context.newBuilder(PYTHON.languageId).allowAllAccess(true).allowCreateThread(true).allowNativeAccess(true)
                .engine(ENGINE).option("python.PosixModuleBackend", "native")

        builder = if (StringUtils.isBlank(pythonExecutableUriAsString)) builder.allowIO(
            IOAccess.newBuilder().fileSystem(
                VirtualFileSystem.newBuilder().allowHostIO(
                    VirtualFileSystem.HostIO.READ_WRITE
                ).build()
            ).build()
        )
        else builder.allowIO(IOAccess.ALL).option("python.Executable", pythonExecutableUriAsString)
            .option("python.ForceImportSite", "true")

        val context = builder.build()

        Dispatchers.IO.dispatch(CoroutineScope(Dispatchers.IO).coroutineContext, Runnable {
            context.initialize(PYTHON.languageId)
        })

        return context
    }

    override fun getSupportedLanguage(): String? {
        return PYTHON.languageId
    }
}
