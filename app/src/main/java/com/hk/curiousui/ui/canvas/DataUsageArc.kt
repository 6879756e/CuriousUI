package com.hk.curiousui.ui.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt


@Composable
fun DataUsageArc(
    remainingProportion: Float,
    totalData: Float,
) {
    Column(
        modifier = Modifier.size(200.dp, 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProportionArc(
            remainingProportion = remainingProportion,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )

        val leftoverData = String.format("%.2f", remainingProportion * totalData)

        Text("Data remaining", style = MaterialTheme.typography.titleLarge)
        Text(
            "${leftoverData}GB",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
    }
}

@Composable
fun ProportionArc(
    remainingProportion: Float,
    modifier: Modifier,
    strokeWidth: Float = 30.dp.value
) {
    Canvas(modifier = modifier) {
        val height = this.size.height
        val width = this.size.width

        val fullPath = Path().apply {
            moveTo(strokeWidth / 2, height - strokeWidth / 2)
            quadraticBezierTo(
                width / 2,
                -height + (strokeWidth * 1.5).roundToInt(),
                width - strokeWidth / 2,
                height - strokeWidth / 2
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
                0.0f to Color(0xFF3976C4),
                0.4f to Color(0xFF63C4C1)
            ),
            path = remainingPath,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )
    }

}

@Preview
@Composable
fun DataUsageArcPreview() {
    DataUsageArc(0.4f, 10f)
}

@Preview
@Composable
fun ProportionArcPreview() {
    ProportionArc(0.8f, modifier = Modifier.size(300.dp, 200.dp))
}