package com.zinc.berrybucket.ui.presentation.common

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.unit.Velocity
import kotlin.math.abs

@Composable
fun rememberNestedScrollConnection(
    onOffsetChanged: (Float) -> Unit,
    maxAppBarHeight: Float,
    minAppBarHeight: Float
) =
    remember {
        var currentHeight = maxAppBarHeight
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                Log.d("ayhan", "available : $available")
                currentHeight = (currentHeight + available.y).coerceIn(
                    minimumValue = minAppBarHeight,
                    maximumValue = maxAppBarHeight
                )
                return if (abs(currentHeight) == maxAppBarHeight || abs(currentHeight) == minAppBarHeight) {
                    super.onPreScroll(available, source)
                } else {
                    onOffsetChanged(currentHeight)
                    available
                }
            }

            override suspend fun onPreFling(available: Velocity): Velocity {
                if (available.y < 0) {
                    onOffsetChanged(minAppBarHeight)
                } else {
                    onOffsetChanged(maxAppBarHeight)
                }
                return super.onPreFling(available)
            }
        }
    }