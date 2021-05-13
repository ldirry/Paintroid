package org.catrobat.paintroid.command.serialization

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.Input
import com.esotericsoftware.kryo.io.Output
import org.catrobat.paintroid.command.implementation.LoadCommand

class LoadCommandSerializer(version: Int): VersionSerializer<LoadCommand>(version) {
    override fun write(kryo: Kryo, output: Output, command: LoadCommand) {
        command.loadedImage?.compress(Bitmap.CompressFormat.PNG, 100, output)
    }

    override fun read(kryo: Kryo, input: Input, type: Class<out LoadCommand>): LoadCommand {
        return super.handleVersions(this, kryo, input, type)
    }

    override fun readCurrentVersion(kryo: Kryo, input: Input, type: Class<out LoadCommand>): LoadCommand {
        val bitmap = BitmapFactory.decodeStream(input)
        return LoadCommand(bitmap)
    }
}