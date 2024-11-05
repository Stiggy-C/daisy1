package io.openenterprise.daisy.python

import io.openenterprise.daisy.mvel2.Mvel2Operation
import javax.annotation.Nullable

interface PythonOperation<T>: Mvel2Operation<T> {

    @Nullable
    fun eval(source: String): T?

}