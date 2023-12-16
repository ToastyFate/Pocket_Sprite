package toasted.pocket_sprite.ui.components


import androidx.compose.ui.graphics.Color

class GridItem(color: Color, x: Int, y: Int) {


    private var color: Color = Color.White
    private var x: Int = 0
    private var y: Int = 0

    init{
        this.color = color
        this.x = x
        this.y = y
    }

    fun setColor(color: Color) {
        this.color = color
    }

    fun getColor(): Color {
        return color
    }

    fun setXY(x: Int, y: Int) {
        this.x= x
        this.y = y
    }

    fun getXY(): Pair<Int, Int> {
        return Pair(x, y)
    }

    fun copy(color: Color): GridItem {
        return GridItem(color, x, y)
    }



}