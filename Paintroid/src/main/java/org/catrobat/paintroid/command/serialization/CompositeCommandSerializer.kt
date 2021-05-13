package org.catrobat.paintroid.command.serialization

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.Input
import com.esotericsoftware.kryo.io.Output
import org.catrobat.paintroid.command.Command
import org.catrobat.paintroid.command.implementation.CompositeCommand

class CompositeCommandSerializer(version: Int): VersionSerializer<CompositeCommand>(version) {
    override fun write(kryo: Kryo, output: Output, command: CompositeCommand) {
        output.writeInt(command.commands.size)
        command.commands.forEach { cmd ->
            kryo.writeClassAndObject(output, cmd)
        }
    }

    override fun read(kryo: Kryo, input: Input, type: Class<out CompositeCommand>): CompositeCommand {
        return super.handleVersions(this, kryo, input, type)
    }

    override fun readCurrentVersion(kryo: Kryo, input: Input, type: Class<out CompositeCommand>): CompositeCommand {
        val size = input.readInt()
        return CompositeCommand().apply {
            for (i in 1..size) {
                addCommand(kryo.readClassAndObject(input) as Command)
            }
        }
    }
}