package com.dastanapps.tettris.model.blocks

import com.dastanapps.tettris.model.Block
import com.dastanapps.tettris.model.ITetrisShape
import com.dastanapps.tettris.model.TetrominoShape

class BlockZ : ITetrisShape() {

    override var shapeType = TetrominoShape.Z

    override fun rotate0(): List<Block> {
        return listOf(
            Block(0, 0),
            Block(1, 0),
            Block(1, 1),
            Block(2, 1)
        )
    }

    override fun rotate90(): List<Block> {
        return listOf(
            Block(1, 0),
            Block(0, 1),
            Block(1, 1),
            Block(0, 2)
        )
    }

    override fun rotate180() = rotate0()

    override fun rotate270() = rotate90()
}