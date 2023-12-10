package toasted.pocket_sprite

import androidx.compose.ui.graphics.Color

class GridItem(name: String, color: Color, xPixels: Int, yPixels: Int) {

    private var name: String = "None"
    private var color: Color = Color.White
    private var xPixels: Int = 0
    private var yPixels: Int = 0

    init{
        this.name = name
        this.color = color
        this.xPixels = xPixels
        this.yPixels = yPixels
    }

    fun setColor(color: Color) {
        this.color = color
    }

    fun getColor(): Color {
        return color
    }

    fun setXY(x: Int, y: Int) {
        xPixels = x
        yPixels = y
    }

    fun getXY(): Pair<Int, Int> {
        return Pair(xPixels, yPixels)
    }


}