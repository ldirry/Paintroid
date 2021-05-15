package org.catrobat.paintroid.command.implementation

import android.graphics.Bitmap
import android.graphics.Canvas
import org.catrobat.paintroid.command.Command
import org.catrobat.paintroid.common.CommonFactory
import org.catrobat.paintroid.contract.LayerContracts
import org.catrobat.paintroid.model.Layer

class AddLayerCommand(private val commonFactory: CommonFactory) : Command {

    override fun run(canvas: Canvas, layerModel: LayerContracts.Model) {
        val layer = Layer(commonFactory.createBitmap(layerModel.width, layerModel.height,
                Bitmap.Config.ARGB_8888))
        layerModel.addLayerAt(0, layer)
        layerModel.currentLayer = layer
    }

    override fun freeResources() {
    }
}