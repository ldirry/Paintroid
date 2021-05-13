package org.catrobat.paintroid.command.serialization

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.Input
import com.esotericsoftware.kryo.io.Output
import org.catrobat.paintroid.command.implementation.AddLayerCommand
import org.catrobat.paintroid.common.CommonFactory

class AddLayerCommandSerializer(version: Int): VersionSerializer<AddLayerCommand>(version) {
    override fun write(kryo: Kryo, output: Output, command: AddLayerCommand) {
    }

    override fun read(kryo: Kryo, input: Input, type: Class<out AddLayerCommand>): AddLayerCommand {
        return super.handleVersions(this, kryo, input, type)
    }

    override fun readCurrentVersion(kryo: Kryo, input: Input, type: Class<out AddLayerCommand>): AddLayerCommand {
        return AddLayerCommand(CommonFactory())
    }
}