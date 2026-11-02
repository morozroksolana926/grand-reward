package com.Qerxavi7.Nomlut.domain.model

data class BoardState(
    val cols: Int,
    val rows: Int,
    val cells: List<TokenType?>
) {
    fun indexOf(col: Int, row: Int): Int = row * cols + col

    fun tokenAt(col: Int, row: Int): TokenType? {
        if (col < 0 || col >= cols || row < 0 || row >= rows) return null
        val index = indexOf(col, row)
        if (index < 0 || index >= cells.size) return null
        return cells[index]
    }

    val isEmpty: Boolean get() = cells.isEmpty()

    companion object {
        val Empty = BoardState(0, 0, emptyList())
    }
}
