package com.ite.aoc

import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.JScrollPane


fun <T> List<List<T>>.visualize(
    refreshDelay: Long = 500,
    cellSize: Int = 5,
    colorMapper: (Pair<Int, Int>, T) -> Color,
): GridVisualizer<T> {
    val copy: List<MutableList<T>> = this.map { it.map { i -> i }.toMutableList() }.toList()
    return GridVisualizer(copy, cellSize, refreshDelay, colorMapper).also {
        javax.swing.SwingUtilities.invokeLater {
            val frame = JFrame("Grid Visualization")
            frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
            val scrollFrame = JScrollPane(it)
            scrollFrame.preferredSize = Dimension(it.preferredSize.width + 50, it.preferredSize.height + 50)
            frame.add(scrollFrame)
            frame.pack()
            frame.isVisible = true
        }
    }
}

class GridVisualizer<T>(
    private val grid: List<MutableList<T>>,
    private val cellSize: Int,
    private val refreshDelay: Long,
    private val colorMapper: (Pair<Int, Int>, T) -> Color,
) : JPanel() {

    init {
        preferredSize = Dimension(grid[0].size * cellSize, grid.size * cellSize)
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)

        // Draw grid
        for (i in grid.indices) {
            for (j in grid[i].indices) {
                val color = colorMapper(i to j, grid[i][j])
                g.color = color
                g.fillRect(j * cellSize, i * cellSize, cellSize, cellSize)
                g.color = Color.LIGHT_GRAY
                g.drawRect(j * cellSize, i * cellSize, cellSize, cellSize)
            }
        }
    }

    fun updateCellIf(pos: Pair<Int, Int>, value: T, condition: (T) -> Boolean) {
        if (condition(grid[pos.first][pos.second])) {
            updateCell(pos, value)
        }
    }

    fun updateCell(pos: Pair<Int, Int>, value: T, wait: Boolean = false, customRefresh: Long? = null) {
        grid[pos.first][pos.second] = value
        revalidate()
        if (wait) Thread.sleep(customRefresh ?: refreshDelay)
        rootPane?.repaint()
    }

    fun <R> updateThenResetCell(
        pos: Pair<Int, Int>,
        value: T,
        wait: Boolean = false,
        customRefresh: Long? = null,
        runnable: () -> R
    ): R {
        val old = grid[pos.first][pos.second]
        updateCell(pos, value, wait, customRefresh)
        try {
            return runnable()
        } finally {
            updateCell(pos, old, wait, customRefresh)
        }
    }
}