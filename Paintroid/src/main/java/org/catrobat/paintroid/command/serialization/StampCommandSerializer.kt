package org.catrobat.paintroid.command.serialization

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.KryoException
import com.esotericsoftware.kryo.io.Input
import com.esotericsoftware.kryo.io.Output
import org.catrobat.paintroid.FileIO
import org.catrobat.paintroid.command.implementation.StampCommand

class StampCommandSerializer(version: Int): VersionSerializer<StampCommand>(version) {
    override fun write(kryo: Kryo, output: Output, command: StampCommand) {
        with(kryo) {
            with(output) {
                var bitmap = command.getFileToStoredBitmap()?.let {
                    FileIO.getBitmapFromFile(command.getFileToStoredBitmap())
                }
                bitmap = bitmap ?: command.getBitmap() ?: throw KryoException()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, output)
                writeObject(output, command.coordinates)
                writeFloat(command.boxWidth)
                writeFloat(command.boxHeight)
                writeFloat(command.boxRotation)
            }
        }
    }

    override fun read(kryo: Kryo, input: Input, type: Class<out StampCommand>): StampCommand {
        return super.handleVersions(this, kryo, input, type)
    }

    override fun readCurrentVersion(kryo: Kryo, input: Input, type: Class<out StampCommand>): StampCommand {
        return with(kryo) {
            with(input) {
                val bitmap = BitmapFactory.decodeStream(input)
                val coordinates = readObject(input, Point::class.java)
                val width = readFloat()
                val height = readFloat()
                val rotation = readFloat()
                StampCommand(bitmap, coordinates, width, height, rotation)
            }
        }
    }
}