package com.dastanapps.tettris.model.blocks

import com.dastanapps.tettris.model.Block
import com.dastanapps.tettris.model.ITetrisShape
import com.dastanapps.tettris.model.TetrominoShape

class BlockO : ITetrisShape() {

    override var shapeType = TetrominoShape.O

    override fun rotate0(): List<Block> {
        return listOf(
            Block(0, 0),
            Block(0, 1),
            Block(1, 0),
            Block(1, 1)
        )
    }

    override fun rotate90() = rotate0()

    override fun rotate180() = rotate0()

    override fun rotate270() = rotate0()
}