package com.dastanapps.tettris.model.blocks

import com.dastanapps.tettris.model.Block
import com.dastanapps.tettris.model.ITetrisShape
import com.dastanapps.tettris.model.TetrominoShape

class BlockI : ITetrisShape() {

    override var shapeType = TetrominoShape.I

    override fun rotate0(): List<Block> {
        return listOf(
            Block(0, 0),
            Block(1, 0),
            Block(2, 0),
            Block(3, 0)
        )
    }

    override fun rotate90(): List<Block> {
        return listOf(
            Block(0, 0),
            Block(0, 1),
            Block(0, 2),
            Block(0, 3)
        )
    }

    override fun rotate180() = rotate0()

    override fun rotate270() = rotate90()
}