package uz.mahmudxon.currency.ui.component.chart.renderer.line

import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope

interface LineDrawer {
  fun drawLine(
    drawScope: DrawScope,
    canvas: Canvas,
    linePath: Path
  )
}