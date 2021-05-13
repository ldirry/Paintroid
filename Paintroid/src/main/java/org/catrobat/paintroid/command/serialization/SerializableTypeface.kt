package org.catrobat.paintroid.command.serialization

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.Input
import com.esotericsoftware.kryo.io.Output

class SerializableTypeface(val font: String, val bold: Boolean, val underline: Boolean, val italic: Boolean, val textSize: Float, val textSkewX: Float) {

    class TypefaceSerializer(version: Int): VersionSerializer<SerializableTypeface>(version) {
        override fun write(kryo: Kryo, output: Output, typeface: SerializableTypeface) {
            with(output) {
                writeString(typeface.font)
                writeBoolean(typeface.bold)
                writeBoolean(typeface.underline)
                writeBoolean(typeface.italic)
                writeFloat(typeface.textSize)
                writeFloat(typeface.textSkewX)
            }
        }

        override fun read(kryo: Kryo, input: Input, type: Class<out SerializableTypeface>): SerializableTypeface {
            return super.handleVersions(this, kryo, input, type)
        }

        override fun readCurrentVersion(kryo: Kryo, input: Input, type: Class<out SerializableTypeface>): SerializableTypeface {
            return with(input) {
                SerializableTypeface(readString(), readBoolean(), readBoolean(), readBoolean(), readFloat(), readFloat())
            }
        }
    }
}


