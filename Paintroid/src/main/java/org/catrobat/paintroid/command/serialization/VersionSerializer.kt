package org.catrobat.paintroid.command.serialization

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.KryoException
import com.esotericsoftware.kryo.Serializer
import com.esotericsoftware.kryo.io.Input

abstract class VersionSerializer<T>(val version: Int): Serializer<T>() {

    protected fun handleVersions(serializer: VersionSerializer<T>, kryo: Kryo, input: Input, type: Class<out T>): T {
        return when(version) {
            1 -> serializer.readV1(serializer, kryo, input, type)
            2 -> serializer.readV2(serializer, kryo, input, type)
            3 -> serializer.readV3(serializer, kryo, input, type)
            CommandSerializationUtilities.CURRENT_IMAGE_VERSION -> serializer.readCurrentVersion(kryo, input, type)
            else -> throw KryoException()
        }
    }

    protected open fun readV1(serializer: VersionSerializer<T>, kryo: Kryo, input: Input, type: Class<out T>): T {
        return serializer.readV2(serializer, kryo, input, type)
    }

    protected open fun readV2(serializer: VersionSerializer<T>, kryo: Kryo, input: Input, type: Class<out T>): T {
        return serializer.readV3(serializer, kryo, input, type)
    }

    protected open fun readV3(serializer: VersionSerializer<T>, kryo: Kryo, input: Input, type: Class<out T>): T {
        return serializer.readCurrentVersion(kryo, input, type)
    }

    abstract fun readCurrentVersion(kryo: Kryo, input: Input, type: Class<out T>): T
}