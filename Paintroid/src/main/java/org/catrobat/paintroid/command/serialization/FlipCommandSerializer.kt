package org.catrobat.paintroid.command.serialization

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.Input
import com.esotericsoftware.kryo.io.Output
import org.catrobat.paintroid.command.implementation.FlipCommand

class FlipCommandSerializer(version: Int): VersionSerializer<FlipCommand>(version) {
    override fun write(kryo: Kryo, output: Output, command: FlipCommand) {
        output.writeInt(command.flipDirection.ordinal)
    }

    override fun read(kryo: Kryo, input: Input, type: Class<out FlipCommand>): FlipCommand {
        return super.handleVersions(this, kryo, input, type)
    }

    override fun readCurrentVersion(kryo: Kryo, input: Input, type: Class<out FlipCommand>): FlipCommand {
        val flipDirection = FlipCommand.FlipDirection.values()[input.readInt()]
        return FlipCommand(flipDirection)
    }
}