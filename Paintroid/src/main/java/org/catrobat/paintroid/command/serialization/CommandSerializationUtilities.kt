package org.catrobat.paintroid.command.serialization

import android.content.ContentValues
import android.content.Context
import android.graphics.*
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.Input
import com.esotericsoftware.kryo.io.Output
import org.catrobat.paintroid.command.Command
import org.catrobat.paintroid.command.CommandManager
import org.catrobat.paintroid.command.implementation.*
import org.catrobat.paintroid.common.Constants
import org.catrobat.paintroid.model.CommandManagerModel
import org.catrobat.paintroid.tools.drawable.*
import java.io.*
import java.lang.Exception
import java.util.*
import kotlin.collections.LinkedHashMap

class CommandSerializationUtilities(private val activityContext: Context, private val commandManager: CommandManager) {

    companion object {
        const val CURRENT_IMAGE_VERSION = 1
        const val MAGIC_VALUE = "CATROBAT"
    }

    val kryo = Kryo()
    private val registerMap = LinkedHashMap<Class<*>, VersionSerializer<*>?>()

    init {
        setRegisterMapVersion(CURRENT_IMAGE_VERSION)
        registerClasses()
    }

    private fun setRegisterMapVersion(version: Int) {
        with(registerMap) {
            put(Command::class.java, null)
            put(CompositeCommand::class.java, CompositeCommandSerializer(version))
            put(FloatArray::class.java, null)
            put(PointF::class.java, null)
            put(Point::class.java, null)
            put(CommandManagerModel::class.java, CommandManagerModelSerializer(version))
            put(SetDimensionCommand::class.java, SetDimensionCommandSerializer(version))
            put(SprayCommand::class.java, SprayCommandSerializer(version))
            put(Paint::class.java, PaintSerializer(version, activityContext))
            put(AddLayerCommand::class.java, AddLayerCommandSerializer(version))
            put(SelectLayerCommand::class.java, SelectLayerCommandSerializer(version))
            put(LoadCommand::class.java, LoadCommandSerializer(version))
            put(TextToolCommand::class.java, TextToolCommandSerializer(version, activityContext))
            put(Array<String>::class.java, null)
            put(FillCommand::class.java, FillCommandSerializer(version))
            put(FlipCommand::class.java, FlipCommandSerializer(version))
            put(CropCommand::class.java, CropCommandSerializer(version))
            put(CutCommand::class.java, CutCommandSerializer(version))
            put(ResizeCommand::class.java, ResizeCommandSerializer(version))
            put(RotateCommand::class.java, RotateCommandSerializer(version))
            put(ResetCommand::class.java, ResetCommandSerializer(version))
            put(ReorderLayersCommand::class.java, ReorderLayersCommandSerializer(version))
            put(RemoveLayerCommand::class.java, RemoveLayerCommandSerializer(version))
            put(MergeLayersCommand::class.java, MergeLayersCommandSerializer(version))
            put(PathCommand::class.java, PathCommandSerializer(version))
            put(SerializablePath::class.java, SerializablePath.PathSerializer(version))
            put(SerializablePath.Move::class.java, SerializablePath.PathActionMoveSerializer(version))
            put(SerializablePath.Line::class.java, SerializablePath.PathActionLineSerializer(version))
            put(SerializablePath.Quad::class.java, SerializablePath.PathActionQuadSerializer(version))
            put(SerializablePath.Rewind::class.java, SerializablePath.PathActionRewindSerializer(version))
            put(LoadBitmapListCommand::class.java, LoadBitmapListCommandSerializer(version))
            put(GeometricFillCommand::class.java, GeometricFillCommandSerializer(version))
            put(HeartDrawable::class.java, GeometricFillCommandSerializer.HeartDrawableSerializer(version))
            put(OvalDrawable::class.java, GeometricFillCommandSerializer.OvalDrawableSerializer(version))
            put(RectangleDrawable::class.java, GeometricFillCommandSerializer.RectangleDrawableSerializer(version))
            put(StarDrawable::class.java, GeometricFillCommandSerializer.StarDrawableSerializer(version))
            put(ShapeDrawable::class.java, null)
            put(RectF::class.java, null)
            put(StampCommand::class.java, StampCommandSerializer(version))
            put(SerializableTypeface::class.java, SerializableTypeface.TypefaceSerializer(version))
            put(PointCommand::class.java, PointCommandSerializer(version))
        }
    }

    private fun registerClasses() {
        registerMap.forEach { (classRegister, serializer) ->
            val registration = kryo.register(classRegister)
            serializer?.let {
                registration.serializer = serializer
            }
        }
    }

    fun writeToFile(fileName: String): Uri? {
        var returnUri: Uri? = null
        val contentResolver = activityContext.contentResolver

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
                put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }
            contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)?.let { uri ->
                contentResolver.openOutputStream(uri)?.use { stream ->
                    writeToStream(stream)
                    returnUri = uri
                }
            }
        } else {
            if (!(Constants.MEDIA_DIRECTORY.exists() || Constants.MEDIA_DIRECTORY.mkdirs())) {
                return null
            }
            val imageFile = File(Constants.MEDIA_DIRECTORY, fileName)
            FileOutputStream(imageFile).use { fileStream ->
                writeToStream(fileStream)
                returnUri = Uri.fromFile(imageFile)
            }
        }

        return returnUri
    }

    private fun writeToStream(stream: OutputStream) {
        Output(stream).use { output ->
            output.writeString(MAGIC_VALUE)
            output.writeInt(CURRENT_IMAGE_VERSION)
            kryo.writeObject(output, commandManager.commandManagerModel)
        }
    }

    fun readFromFile(uri: Uri): CommandManagerModel? {
        var commandModel: CommandManagerModel?

        activityContext.contentResolver.openInputStream(uri).use { contentResolverStream ->
            Input(contentResolverStream).use { input ->
                if (!input.readString().equals(MAGIC_VALUE)) {
                    throw NotCatrobatImageException("Magic Value doesn't exist.")
                }
                val imageVersion = input.readInt()
                if (CURRENT_IMAGE_VERSION != imageVersion) {
                    setRegisterMapVersion(imageVersion)
                    registerClasses()
                }
                commandModel = kryo.readObject(input, CommandManagerModel::class.java)
            }
        }

        commandModel?.commands?.reverse()
        return commandModel
    }

    class NotCatrobatImageException(message: String): Exception(message)
}