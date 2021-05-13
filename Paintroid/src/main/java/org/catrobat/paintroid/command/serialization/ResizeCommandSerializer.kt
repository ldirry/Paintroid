package org.catrobat.paintroid.command.serialization

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.Input
import com.esotericsoftware.kryo.io.Output
import org.catrobat.paintroid.command.implementation.ResizeCommand

class ResizeCommandSerializer(version: Int): VersionSerializer<ResizeCommand>(version) {
    override fun write(kryo: Kryo, output: Output, command: ResizeCommand) {
        with(output) {
            writeInt(command.newWidth)
            writeInt(command.newHeight)
        }
    }

    override fun read(kryo: Kryo, input: Input, type: Class<out ResizeCommand>): ResizeCommand {
        return super.handleVersions(this, kryo, input, type)
    }

    override fun readCurrentVersion(kryo: Kryo, input: Input, type: Class<out ResizeCommand>): ResizeCommand {
        return with(input) {
            val width = readInt()
            val height =  readInt()
            ResizeCommand(width, height)
        }
    }
}