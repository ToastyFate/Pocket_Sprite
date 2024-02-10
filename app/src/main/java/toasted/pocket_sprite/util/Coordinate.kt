package toasted.pocket_sprite.util

class Coordinate(var x: Int, var y: Int) {

    fun setCoordinate(coordinate: Coordinate) {
        this.x = coordinate.x
        this.y = coordinate.y
    }
}