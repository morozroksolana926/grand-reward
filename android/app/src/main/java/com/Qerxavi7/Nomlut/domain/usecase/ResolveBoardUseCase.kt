package com.Qerxavi7.Nomlut.domain.usecase

import com.Qerxavi7.Nomlut.domain.model.BoardState
import com.Qerxavi7.Nomlut.domain.model.TokenType
import kotlin.random.Random

class ResolveBoardUseCase {

    fun create(cols: Int, rows: Int, random: Random): BoardState {
        val types = TokenType.entries
        val cells = List<TokenType?>(cols * rows) { types[random.nextInt(types.size)] }
        return BoardState(cols, rows, cells)
    }

    fun clear(board: BoardState, group: Set<Int>): BoardState {
        val cells = board.cells.toMutableList()
        for (index in group) {
            if (index in cells.indices) cells[index] = null
        }
        return board.copy(cells = cells)
    }

    operator fun invoke(board: BoardState, random: Random): BoardState {
        val cells = board.cells.toMutableList()
        val counts = HashMap<TokenType, Int>()
        for (type in TokenType.entries) {
            counts[type] = cells.count { it == type }
        }
        for (col in 0 until board.cols) {
            val stack = ArrayList<TokenType>(board.rows)
            for (row in board.rows - 1 downTo 0) {
                val token = cells[row * board.cols + col]
                if (token != null) stack.add(token)
            }
            for (row in board.rows - 1 downTo 0) {
                val fillIndex = board.rows - 1 - row
                val token = if (fillIndex < stack.size) stack[fillIndex] else pick(counts, random)
                cells[row * board.cols + col] = token
            }
        }
        return board.copy(cells = cells)
    }

    fun shuffle(board: BoardState, random: Random): BoardState {
        val present = board.cells.filterNotNull().shuffled(random)
        if (present.size != board.cells.size) return board
        return board.copy(cells = present)
    }

    fun hasMoves(board: BoardState): Boolean {
        for (row in 0 until board.rows) {
            for (col in 0 until board.cols) {
                val token = board.tokenAt(col, row) ?: continue
                if (board.tokenAt(col + 1, row) == token) return true
                if (board.tokenAt(col, row + 1) == token) return true
            }
        }
        return false
    }

    private fun pick(counts: HashMap<TokenType, Int>, random: Random): TokenType {
        val types = TokenType.entries
        val maxCount = counts.values.maxOrNull() ?: 0
        val weights = types.map { maxCount - (counts[it] ?: 0) + 2 }
        val total = weights.sum()
        var roll = random.nextInt(total)
        for (index in types.indices) {
            roll -= weights[index]
            if (roll < 0) {
                val chosen = types[index]
                counts[chosen] = (counts[chosen] ?: 0) + 1
                return chosen
            }
        }
        val fallback = types[types.size - 1]
        counts[fallback] = (counts[fallback] ?: 0) + 1
        return fallback
    }
}
