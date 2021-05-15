package org.catrobat.paintroid.command.implementation

import android.graphics.Canvas
import org.catrobat.paintroid.command.Command
import org.catrobat.paintroid.contract.LayerContracts

class CompositeCommand : Command {

    var commands = mutableListOf<Command>(); private set

    fun addCommand(command: Command) {
        commands.add(command)
    }

    override fun run(canvas: Canvas, layerModel: LayerContracts.Model) {
        commands.forEach { command ->
            layerModel.currentLayer?.let { layer ->
                canvas.setBitmap(layer.bitmap)
            }
            command.run(canvas, layerModel)
        }
    }

    override fun freeResources() {
        commands.forEach { command ->
            command.freeResources()
        }
    }
}