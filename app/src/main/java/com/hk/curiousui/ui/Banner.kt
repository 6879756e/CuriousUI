@file:OptIn(ExperimentalFoundationApi::class)

package com.hk.curiousui.ui

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hk.curiousui.R
import com.hk.curiousui.ui.util.converter.toPx
import kotlinx.coroutines.delay


@Composable
fun FoundationBanner(
    drawableResources: List<Int>,
    modifier: Modifier = Modifier,
    bannerIndicatorType: BannerIndicatorType = BannerIndicatorType.AUTO,
    contentScale: ContentScale = ContentScale.Crop,
    onPageClicked: (Int) -> Unit = {},
) {
    val pagerState = rememberPagerState(0)
    val pagerIsDragged by pagerState.interactionSource.collectIsDraggedAsState()

    val interactionSource = remember { MutableInteractionSource() }
    val interaction by interactionSource.interactions.collectAsState(initial = null)

    LaunchedEffect(key1 = interaction, key2 = pagerIsDragged) {
        if (!pagerIsDragged && interaction !is PressInteraction.Press) {
            delay(2000)
            while (true) {
                delay(2000)
                pagerState.animateScrollToPage((pagerState.currentPage + 1) % drawableResources.size)
            }
        }
    }

    BoxWithConstraints(modifier = modifier) {
        val indicatorType = if (bannerIndicatorType == BannerIndicatorType.AUTO) {
            getIndicatorType(drawableResources)
        } else {
            bannerIndicatorType
        }

        Card {
            Box(modifier = Modifier.weight(1f)) {
                HorizontalPager(
                    state = pagerState,
                    pageCount = drawableResources.size,
                    beyondBoundsPageCount = 1,
                ) { page ->
                    Image(painter = painterResource(id = drawableResources[page]),
                        contentDescription = null,
                        contentScale = contentScale,
                        modifier = Modifier.clickable(interactionSource, null) {
                            onPageClicked(page)
                        })
                }
                if (indicatorType == BannerIndicatorType.TEXT) {
                    this@BoxWithConstraints.TextBannerIndicator(
                        pagerState.currentPage,
                        drawableResources.size
                    )
                }
            }
            if (indicatorType == BannerIndicatorType.CIRCLE) {
                CircleBannerIndicator(pagerState.currentPage, drawableResources.size)
            }
        }
    }
}

@Composable
private fun BoxWithConstraintsScope.getIndicatorType(
    drawableResources: List<Int>
): BannerIndicatorType {
    val circleIndicatorMinWidth = (drawableResources.size * 12 + 4).dp.toPx()

    return if (constraints.maxWidth > circleIndicatorMinWidth) {
        BannerIndicatorType.CIRCLE
    } else {
        BannerIndicatorType.TEXT
    }
}

enum class BannerIndicatorType {
    CIRCLE, TEXT, AUTO
}

@Composable
private fun CircleBannerIndicator(
    currentIndex: Int,
    itemCount: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .padding(start = 4.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(itemCount) {
            val indicator = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .border(1.dp, Color.Black, CircleShape)

            if (it == currentIndex) {
                Box(modifier = indicator.background(Color.Black))
            } else {
                Box(modifier = indicator)
            }

            Spacer(modifier = Modifier.width(4.dp))
        }
    }
}

@Composable
private fun BoxScope.TextBannerIndicator(
    currentIndex: Int,
    itemCount: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .offset(x = (-8).dp, y = (-8).dp)
            .clip(CircleShape)
            .align(Alignment.BottomEnd)
            .background(Color.White)
            .border(BorderStroke(1.dp, Color.Black), shape = CircleShape)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(text = "${currentIndex + 1} / $itemCount", style = MaterialTheme.typography.labelSmall)
    }
}

@Preview(name = "Light mode")
@Preview(name = "Dark mode", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun FoundationBannerPreview() {
    val drawableResources = listOf(
        R.drawable.naver_banner,
        R.drawable.kakao_banner,
        R.drawable.line_banner,
        R.drawable.coupang_banner,
        R.drawable.baemin_banner,
        R.drawable.karrot_banner,
        R.drawable.toss_banner,
    )

    FoundationBanner(drawableResources)
}