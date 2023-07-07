package com.hk.curiousui.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hk.curiousui.R


/**
 * TODO:
DONE * 1. Make it take in a list of drawable resources, or provide a composable for a given int? (Int) -> @Composable
 * 2. Make it automatically slide every couple of seconds
 * 3. Add circles beneath which indicate the current ad position
 * 4. Make circles clickable which causes the ad to swipe to that position
 * 5. When user is pressing on item, pause number 2.
 */


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FoundationBanner(
    drawableResources: List<Int>,
    bannerWidth: Dp = Dp.Unspecified,
    bannerHeight: Dp = DEFAULT_BANNER_HEIGHT,
) {
    val pagerState = rememberPagerState(0)

    Card(
        modifier = Modifier
            .width(bannerWidth)
            .height(bannerHeight)
            .padding(horizontal = 24.dp)
    ) {
        HorizontalPager(
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
    }
}

@Preview
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