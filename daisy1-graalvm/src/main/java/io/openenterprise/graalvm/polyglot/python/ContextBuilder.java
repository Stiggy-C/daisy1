package io.openenterprise.graalvm.polyglot.python;

import jakarta.annotation.Nonnull;
import kotlinx.coroutines.Dispatchers;
import org.apache.commons.lang3.StringUtils;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Engine;
import org.graalvm.polyglot.io.IOAccess;
import org.graalvm.python.embedding.utils.VirtualFileSystem;
import org.springframework.beans.factory.annotation.Value;

import static io.openenterprise.daisy.graalvm.SupportedLanguage.PYTHON;
import static kotlinx.coroutines.CoroutineScopeKt.CoroutineScope;

public class ContextBuilder implements io.openenterprise.graalvm.polyglot.ContextBuilder {

    protected static final Engine ENGINE = Engine.newBuilder(PYTHON.getLanguageId()).build();

    @Value("${python.executable:''}")
    protected String pythonExecutableUriAsString;

    @Nonnull
    @Override
    public Context createContext() {
        var builder = Context.newBuilder(PYTHON.getLanguageId())
                .allowAllAccess(true)
                .allowCreateThread(true)
                .allowNativeAccess(true)
                .engine(ENGINE)
                .option("python.PosixModuleBackend", "native");
        var context = StringUtils.isBlank(pythonExecutableUriAsString) ?
                builder.allowIO(IOAccess.newBuilder().fileSystem(VirtualFileSystem.newBuilder().allowHostIO(
                        VirtualFileSystem.HostIO.READ_WRITE).build()).build()).build() :
                builder.allowIO(IOAccess.ALL).option("python.Executable", pythonExecutableUriAsString).option(
                        "python.ForceImportSite", "true").build();

        try {
            return context;
        } finally {
            Dispatchers.getIO().dispatch(CoroutineScope(Dispatchers.getIO()).getCoroutineContext(),
                    () -> context.initialize(PYTHON.getLanguageId()));
        }
    }

    @Override
    public String getSupportedLanguage() {
        return PYTHON.getLanguageId();
    }
}
