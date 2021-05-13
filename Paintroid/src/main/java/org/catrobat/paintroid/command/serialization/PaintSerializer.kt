package org.catrobat.paintroid.command.serialization

import android.content.Context
import android.graphics.Paint
import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.Input
import com.esotericsoftware.kryo.io.Output
import org.catrobat.paintroid.tools.implementation.DefaultToolPaint

class PaintSerializer(version: Int, private val activityContext: Context): VersionSerializer<Paint>(version) {
    override fun write(kryo: Kryo, output: Output, paint: Paint) {
        with(output) {
            writeInt(paint.color)
            writeFloat(paint.strokeWidth)
            writeInt(paint.strokeCap.ordinal)
            writeBoolean(paint.isAntiAlias)
            writeInt(paint.style.ordinal)
            writeInt(paint.strokeJoin.ordinal)
        }
    }

    override fun read(kryo: Kryo, input: Input, type: Class<out Paint>): Paint {
        return super.handleVersions(this, kryo, input, type)
    }

    override fun readCurrentVersion(kryo: Kryo, input: Input, type: Class<out Paint>): Paint {
        val toolPaint = DefaultToolPaint(activityContext).apply {
            with(input) {
                color = readInt()
                strokeWidth = readFloat()
                strokeCap = Paint.Cap.values()[readInt()]
            }
        }
        return toolPaint.paint.apply {
            with(input) {
                isAntiAlias = readBoolean()
                style = Paint.Style.values()[readInt()]
                strokeJoin = Paint.Join.values()[readInt()]
            }
        }
    }
}