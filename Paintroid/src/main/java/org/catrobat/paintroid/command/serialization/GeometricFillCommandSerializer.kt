package org.catrobat.paintroid.command.serialization

import android.graphics.Paint
import android.graphics.RectF
import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.Input
import com.esotericsoftware.kryo.io.Output
import org.catrobat.paintroid.command.implementation.GeometricFillCommand
import org.catrobat.paintroid.tools.drawable.*

class GeometricFillCommandSerializer(version: Int): VersionSerializer<GeometricFillCommand>(version) {
    override fun write(kryo: Kryo, output: Output, command: GeometricFillCommand) {
        with(kryo) {
            with(output) {
                writeClassAndObject(output, command.shapeDrawable)
                writeInt(command.pointX)
                writeInt(command.pointY)
                writeObject(output, command.boxRect)
                writeFloat(command.boxRotation)
                writeObject(output, command.paint)
            }
        }
    }

    override fun read(kryo: Kryo, input: Input, type: Class<out GeometricFillCommand>): GeometricFillCommand {
        return super.handleVersions(this, kryo, input, type)
    }

    override fun readCurrentVersion(kryo: Kryo, input: Input, type: Class<out GeometricFillCommand>): GeometricFillCommand {
        return with(kryo) {
            with(input) {
                val shape = readClassAndObject(input) as ShapeDrawable
                val pointX = readInt()
                val pointY = readInt()
                val rect = readObject(input, RectF::class.java)
                val rotation = readFloat()
                val paint = readObject(input, Paint::class.java)
                GeometricFillCommand(shape, pointX, pointY, rect, rotation, paint)
            }
        }
    }

    class HeartDrawableSerializer(version: Int): VersionSerializer<HeartDrawable>(version) {
        override fun write(kryo: Kryo, output: Output, command: HeartDrawable) {
        }

        override fun read(kryo: Kryo, input: Input, type: Class<out HeartDrawable>): HeartDrawable {
            return super.handleVersions(this, kryo, input, type)
        }

        override fun readCurrentVersion(kryo: Kryo, input: Input, type: Class<out HeartDrawable>): HeartDrawable {
            return HeartDrawable()
        }
    }

    class OvalDrawableSerializer(version: Int): VersionSerializer<OvalDrawable>(version) {
        override fun write(kryo: Kryo, output: Output, command: OvalDrawable) {
        }

        override fun read(kryo: Kryo, input: Input, type: Class<out OvalDrawable>): OvalDrawable {
            return super.handleVersions(this, kryo, input, type)
        }

        override fun readCurrentVersion(kryo: Kryo, input: Input, type: Class<out OvalDrawable>): OvalDrawable {
            return OvalDrawable()
        }
    }

    class RectangleDrawableSerializer(version: Int): VersionSerializer<RectangleDrawable>(version) {
        override fun write(kryo: Kryo, output: Output, command: RectangleDrawable) {
        }

        override fun read(kryo: Kryo, input: Input, type: Class<out RectangleDrawable>): RectangleDrawable {
            return super.handleVersions(this, kryo, input, type)
        }

        override fun readCurrentVersion(kryo: Kryo, input: Input, type: Class<out RectangleDrawable>): RectangleDrawable {
            return RectangleDrawable()
        }
    }

    class StarDrawableSerializer(version: Int): VersionSerializer<StarDrawable>(version) {
        override fun write(kryo: Kryo, output: Output, command: StarDrawable) {
        }

        override fun read(kryo: Kryo, input: Input, type: Class<out StarDrawable>): StarDrawable {
            return super.handleVersions(this, kryo, input, type)
        }

        override fun readCurrentVersion(kryo: Kryo, input: Input, type: Class<out StarDrawable>): StarDrawable {
            return StarDrawable()
        }
    }
}