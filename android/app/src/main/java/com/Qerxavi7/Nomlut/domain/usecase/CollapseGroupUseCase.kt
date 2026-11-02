package com.Qerxavi7.Nomlut.domain.usecase

import com.Qerxavi7.Nomlut.domain.model.BoardState

class CollapseGroupUseCase {

    operator fun invoke(board: BoardState, col: Int, row: Int): Set<Int> {
        val seed = board.tokenAt(col, row) ?: return emptySet()
        val visited = LinkedHashSet<Int>()
        val pending = ArrayDeque<Int>()
        pending.addLast(board.indexOf(col, row))
        while (pending.isNotEmpty()) {
            val index = pending.removeLast()
            if (!visited.add(index)) continue
            val currentCol = index % board.cols
            val currentRow = index / board.cols
            val neighbours = listOf(
                currentCol - 1 to currentRow,
                currentCol + 1 to currentRow,
                currentCol to currentRow - 1,
                currentCol to currentRow + 1
            )
            for ((nextCol, nextRow) in neighbours) {
                if (board.tokenAt(nextCol, nextRow) != seed) continue
                val nextIndex = board.indexOf(nextCol, nextRow)
                if (!visited.contains(nextIndex)) pending.addLast(nextIndex)
            }
        }
        return visited
    }

    fun largestGroup(board: BoardState): Set<Int> {
        var best: Set<Int> = emptySet()
        val seen = HashSet<Int>()
        for (row in 0 until board.rows) {
            for (col in 0 until board.cols) {
                val index = board.indexOf(col, row)
                if (index in seen) continue
                val group = invoke(board, col, row)
                seen.addAll(group)
                if (group.size > best.size) best = group
            }
        }
        return if (best.size >= 2) best else emptySet()
    }
}
