package com.hk.curiousui.ui.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Preview
@Composable
fun DataUsageArc(
    remainingProportion: Float = 0.8f,
    strokeWidth: Float = 30.dp.value,
) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
    ) {
        val h = drawContext.size.height
        val w = drawContext.size.width
        val y = h / 3

        val fullPath = Path().apply {
            moveTo(strokeWidth / 2, y)
            quadraticBezierTo(
                w / 2, 0f, w - strokeWidth / 2, y
            )
        }

        val remainingPath = Path()

        val pathMeasure = PathMeasure()

        pathMeasure.setPath(fullPath, forceClosed = false)

        pathMeasure.getSegment(
            startDistance = 0f,
            stopDistance = pathMeasure.length * remainingProportion,
            destination = remainingPath, startWithMoveTo = true,
        )

        drawPath(
            color = Color(0xFFAAAAAA),
            path = fullPath,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )

        drawPath(
            brush = Brush.linearGradient(
                0.0f to Color(0xFF3976c4),
                0.4f to Color(0xFF63c4c1)
            ),
            path = remainingPath,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )
    }

}