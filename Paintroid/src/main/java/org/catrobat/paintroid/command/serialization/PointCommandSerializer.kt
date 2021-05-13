package org.catrobat.paintroid.command.serialization

import android.graphics.Paint
import android.graphics.PointF
import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.Input
import com.esotericsoftware.kryo.io.Output
import org.catrobat.paintroid.command.implementation.PointCommand

class PointCommandSerializer(version: Int): VersionSerializer<PointCommand>(version) {
    override fun write(kryo: Kryo, output: Output, command: PointCommand) {
        with(kryo) {
            writeObject(output, command.getPoint())
            writeObject(output, command.getPaint())
        }
    }

    override fun read(kryo: Kryo, input: Input, type: Class<out PointCommand>): PointCommand {
        return super.handleVersions(this, kryo, input, type)
    }

    override fun readCurrentVersion(kryo: Kryo, input: Input, type: Class<out PointCommand>): PointCommand {
        return with(kryo) {
            val point = readObject(input, PointF::class.java)
            val paint = readObject(input, Paint::class.java)
            PointCommand(paint, point)
        }
    }
}