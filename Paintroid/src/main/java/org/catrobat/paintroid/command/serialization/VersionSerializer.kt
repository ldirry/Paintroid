/*
 * Paintroid: An image manipulation application for Android.
 * Copyright (C) 2010-2021 The Catrobat Team
 * (<http://developer.catrobat.org/credits>)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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