package org.catrobat.paintroid.command.serialization

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.Input
import com.esotericsoftware.kryo.io.Output
import org.catrobat.paintroid.command.implementation.LoadBitmapListCommand

class LoadBitmapListCommandSerializer(version: Int): VersionSerializer<LoadBitmapListCommand>(version) {
    override fun write(kryo: Kryo, output: Output, command: LoadBitmapListCommand) {
        output.writeInt(command.loadedImageList.size)
        command.loadedImageList.forEach {
            it?.compress(Bitmap.CompressFormat.PNG, 100, output)
        }
    }

    override fun read(kryo: Kryo, input: Input, type: Class<out LoadBitmapListCommand>): LoadBitmapListCommand {
        return super.handleVersions(this, kryo, input, type)
    }

    override fun readCurrentVersion(kryo: Kryo, input: Input, type: Class<out LoadBitmapListCommand>): LoadBitmapListCommand {
        val size = input.readInt()
        val bitmapList = ArrayList<Bitmap>()
        for (i in 1..size) {
            bitmapList.add(BitmapFactory.decodeStream(input))
        }
        return LoadBitmapListCommand(bitmapList)
    }
}