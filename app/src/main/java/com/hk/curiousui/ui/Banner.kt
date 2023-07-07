@file:OptIn(ExperimentalFoundationApi::class)

package com.hk.curiousui.ui

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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


/**
 * TODO:
DONE * 1. Make it take in a list of drawable resources, or provide a composable for a given int? (Int) -> @Composable
DONE * 2. Make it automatically slide every couple of seconds (scrolling from last page to first page has undesirable animation (backward scroll). Need to work on this!)
DONE * 3. Add circles beneath which indicate the current ad position
REJECTED * 4. Make circles clickable which causes the ad to swipe to that position
 * 5. When user is pressing on item, pause number 2.
 * 6. Make 2 types of indicators, circular and text, but position them differently.
 */


@Composable
fun FoundationBanner(
    drawableResources: List<Int>,
    bannerWidth: Dp = Dp.Unspecified,
    bannerHeight: Dp = DEFAULT_BANNER_HEIGHT,
) {
    val pagerState = rememberPagerState(0)

    LaunchedEffect(key1 = Unit, block = {
        while (true) {
            delay(2000)
            pagerState.animateScrollToPage((pagerState.currentPage + 1) % drawableResources.size)
        }
    })

    Card(
        modifier = Modifier
            .padding(horizontal = 24.dp)
    ) {
        HorizontalPager(
            modifier = Modifier
                .width(bannerWidth)
                .height(bannerHeight),
            state = pagerState,
            pageCount = drawableResources.size,
            beyondBoundsPageCount = 1,
        ) { page ->
            Image(
                painter = painterResource(id = drawableResources[page]),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }

        BannerIndicator(pagerState.currentPage, drawableResources.size)
    }
}

@Composable
private fun BannerIndicator(
    currentIndex: Int,
    itemCount: Int,
) {
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

@Preview
@Composable
fun BannerIndicatorPreview() {
    BannerIndicator(3, 10)
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