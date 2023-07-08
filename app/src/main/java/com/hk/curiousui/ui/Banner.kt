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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hk.curiousui.R
import kotlinx.coroutines.delay


@Composable
fun FoundationBanner(
    drawableResources: List<Int>,
    bannerWidth: Dp = Dp.Unspecified,
    bannerHeight: Dp = DEFAULT_BANNER_HEIGHT,
    bannerIndicatorType: BannerIndicatorType = BannerIndicatorType.CIRCLE,
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

    Card(
        modifier = Modifier
            .padding(horizontal = 24.dp)
    ) {
        Box(
            modifier = Modifier
                .width(bannerWidth)
                .height(bannerHeight)
        ) {
            HorizontalPager(
                state = pagerState,
                pageCount = drawableResources.size,
                beyondBoundsPageCount = 1,
            ) { page ->
                Image(
                    painter = painterResource(id = drawableResources[page]),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.clickable(interactionSource, null) {
                        onPageClicked(page)
                    }
                )
            }

            if (bannerIndicatorType == BannerIndicatorType.TEXT) TextBannerIndicator(
                pagerState.currentPage,
                drawableResources.size
            )
        }

        if (bannerIndicatorType == BannerIndicatorType.CIRCLE) CircleBannerIndicator(
            pagerState.currentPage,
            drawableResources.size
        )
    }
}

enum class BannerIndicatorType {
    CIRCLE, TEXT
}

@Composable
private fun CircleBannerIndicator(currentIndex: Int, itemCount: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
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
private fun BoxScope.TextBannerIndicator(currentIndex: Int, itemCount: Int) {
    Row(
        modifier = Modifier
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

private val DEFAULT_BANNER_HEIGHT = 150.dp