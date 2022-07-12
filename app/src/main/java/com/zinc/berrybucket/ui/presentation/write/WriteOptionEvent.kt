package com.zinc.berrybucket.ui.presentation.write

sealed class WriteOptionEvent {
    object GoToBack : WriteOptionEvent()
    data class MemoClick(
        val originMemo: String,
        val memoChanged: (String) -> Unit,
        val closeClicked: () -> Unit
    ) : WriteOptionEvent()
}