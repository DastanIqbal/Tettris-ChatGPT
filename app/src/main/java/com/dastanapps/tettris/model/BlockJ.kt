package com.dastanapps.tettris.model

class BlockJ : ITetrisShape() {

    override var shapeType = TetrominoShape.J

    override fun rotate0(): List<Block> {
        return listOf(
            Block(0, 0),
            Block(0, 1),
            Block(1, 1),
            Block(2, 1)
        )
    }

    override fun rotate90(): List<Block> {
        return listOf(
            Block(0, 0),
            Block(1, 0),
            Block(0, 1),
            Block(0, 2)
        )
    }

    override fun rotate180(): List<Block> {
        return listOf(
            Block(0, 0),
            Block(1, 0),
            Block(2, 0),
            Block(2, 1)
        )
    }

    override fun rotate270(): List<Block> {
        return listOf(
            Block(1, 0),
            Block(1, 1),
            Block(1, 2),
            Block(0, 2)
        )
    }
}