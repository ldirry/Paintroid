package org.catrobat.paintroid.command.serialization

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.Input
import com.esotericsoftware.kryo.io.Output
import org.catrobat.paintroid.command.implementation.ResetCommand

class ResetCommandSerializer(version: Int): VersionSerializer<ResetCommand>(version) {
    override fun write(kryo: Kryo, output: Output, command: ResetCommand) {
    }

    override fun read(kryo: Kryo, input: Input, type: Class<out ResetCommand>): ResetCommand {
        return super.handleVersions(this, kryo, input, type)
    }

    override fun readCurrentVersion(kryo: Kryo, input: Input, type: Class<out ResetCommand>): ResetCommand {
        return ResetCommand()
    }
}