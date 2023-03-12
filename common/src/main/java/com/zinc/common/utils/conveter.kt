package com.zinc.common.utils

import com.zinc.common.models.YesOrNo

fun Boolean.toYn() = if (this) YesOrNo.Y else YesOrNo.N