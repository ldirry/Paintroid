package org.catrobat.paintroid.command.serialization

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.Input
import com.esotericsoftware.kryo.io.Output
import org.catrobat.paintroid.command.implementation.RotateCommand

class RotateCommandSerializer(version: Int): VersionSerializer<RotateCommand>(version) {
    override fun write(kryo: Kryo, output: Output, command: RotateCommand) {
        output.writeInt(command.rotateDirection.ordinal)
    }

    override fun read(kryo: Kryo, input: Input, type: Class<out RotateCommand>): RotateCommand {
        return super.handleVersions(this, kryo, input, type)
    }

    override fun readCurrentVersion(kryo: Kryo, input: Input, type: Class<out RotateCommand>): RotateCommand {
        val rotateDirection = RotateCommand.RotateDirection.values()[input.readInt()]
        return RotateCommand(rotateDirection)
    }
}