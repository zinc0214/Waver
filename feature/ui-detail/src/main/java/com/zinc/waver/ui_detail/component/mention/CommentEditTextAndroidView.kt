package com.zinc.waver.ui_detail.component.mention

import android.content.Context
import android.text.Editable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import com.zinc.waver.ui_detail.databinding.LayoutMentionTagViewBinding
import com.zinc.waver.ui_detail.model.OpenDetailEditTextViewEvent
import com.zinc.waver.ui_detail.model.TaggedTextInfo

class CommentEditTextAndroidView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    val originText: String,
    val commentEvent: (OpenDetailEditTextViewEvent) -> Unit
) : FrameLayout(context, attrs, defStyleAttr) {

    private lateinit var binding: LayoutMentionTagViewBinding
    private var savedText: String = originText
    private var _spannableString = mutableMapOf<String, TaggedTextInfo>()
    private var changeStartIndex = 0

    init {
        setUpViews()
    }

    private fun setUpViews() {

        binding = LayoutMentionTagViewBinding.inflate(LayoutInflater.from(context), this, true)

        binding.commentSendButton.isEnabled = false
        binding.commentSendButton.setOnClickListener {
            commentEvent.invoke(OpenDetailEditTextViewEvent.SendComment(savedText))
            hideKeyboard()
            binding.commentEditTextView.clearFocus()
            binding.commentEditTextView.setText("")
        }

        binding.commentEditTextView.setText(originText)
        binding.commentEditTextView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {


            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString() != savedText) {

                    /// TODO: 스페이스 두 번 클릭시 . 으로 바뀌면서 강제로 글자수가 줄어드는 현상 해결 필요
                    changeStartIndex = binding.commentEditTextView.selectionEnd

                    val taggedString = _spannableString.values.firstOrNull {
                        changeStartIndex in it.startIndex..it.endIndex
                    }

                    if (taggedString != null) {
                        if (s.toString().length > savedText.length) {
                            binding.commentEditTextView.setText(savedText)
                            binding.commentEditTextView.setSelection(taggedString.endIndex)
                            updateText(getMakeTaggedText(savedText), isForceUpdate = true)
                        } else if (s.toString().length < savedText.length) {
                            _spannableString.values.remove(taggedString)
                            val removeText = savedText.removeRange(
                                taggedString.startIndex,
                                taggedString.endIndex + 1
                            )
                            updateTaggedString(taggedString, false)
                            updateText(getMakeTaggedText(removeText), isForceUpdate = true)
                        }
                    } else {
                        updateTaggedString(s.toString())
                        savedText = s.toString()
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
                commentEvent.invoke(
                    OpenDetailEditTextViewEvent.TextChanged(
                        savedText,
                        changeStartIndex,
                        _spannableString.values.toList()
                    )
                )

                binding.commentSendButton.isEnabled = savedText.isNotEmpty()
            }
        })
    }
//
//    fun setData(
//        originText: String,
//        commentEvent: (OpenDetailEditTextViewEvent) -> Unit
//    ) {
//        this.originText = originText
//        this.commentEvent = commentEvent
//        setUpViews()
//    }

    // 태그된 텍스트 사이로 다른 텍스트가 추가되었을 때, 그 텍스트 만큼 기존의 index 를 업데이트 하기 위함
    private fun updateTaggedString(changeText: String) {
        var size = 0

        // 현재 수정된 Index 를 기반으로 앞쪽에서 가장 가까운 tag text
        val lastTagged = _spannableString.values.lastOrNull { it.endIndex < changeStartIndex }
        // 현재 수정된 Index 를 기반으로 뒤에서 가장 가까운 tag text
        val firstTagged = _spannableString.values.firstOrNull { it.startIndex > changeStartIndex }

        // 태그 사이에 낀 텍스트인 경우
        if (lastTagged != null && firstTagged != null) {

            // 값이 추가된 경우
            if (changeText.length > savedText.length) {
                // 변경된 텍스트
                val betweenText = changeText.slice(lastTagged.endIndex + 1..firstTagged.startIndex)

                // 기존 텍스트
                val currentText =
                    savedText.slice(lastTagged.endIndex + 1 until firstTagged.startIndex)

                size = betweenText.length - currentText.length
            }
            // 값이 삭제된 경우
            else if (changeText.length < savedText.length) {
                // 변경된 텍스트
                val betweenText =
                    changeText.slice(lastTagged.endIndex + 1 until firstTagged.startIndex - 1)

                // 기존 텍스트
                val currentText =
                    savedText.slice(lastTagged.endIndex + 1 until firstTagged.startIndex)

                size = betweenText.length - currentText.length
            }

        }

        // 모든 태그 전에 수정된 텍스트인 경우
        else if (firstTagged != null) {
            // 값이 추가된 경우
            if (changeText.length > savedText.length) {
                // 변경된 텍스트
                val betweenText = changeText.slice(0..firstTagged.startIndex)

                // 기존 텍스트
                val currentText =
                    savedText.slice(0 until firstTagged.startIndex)

                size = betweenText.length - currentText.length
            }
            // 값이 삭제된 경우
            else if (changeText.length < savedText.length) {
                // 변경된 텍스트
                val betweenText =
                    changeText.slice(0 until firstTagged.startIndex)

                // 기존 텍스트
                val currentText =
                    savedText.slice(0..firstTagged.startIndex)

                size = betweenText.length - currentText.length
            }
        }

        _spannableString.values.filter { it.startIndex > changeStartIndex }.map {
            it.copy(startIndex = it.startIndex + size, endIndex = it.endIndex + size)
        }.forEach { modifiedTaggedTextInfo ->
            val id = modifiedTaggedTextInfo.id
            _spannableString[id] = modifiedTaggedTextInfo
        }
    }

    // 앞의 태그가 지워진 경우, 뒤에 있는 태그의 index 값을 수정
    private fun updateTaggedString(changedItem: TaggedTextInfo, isAdd: Boolean) {
        val size = if (isAdd) changedItem.text.length else -(changedItem.text.length)

        if (isAdd) {
            // 추가된 경우
            _spannableString.values.filter { it.startIndex >= changedItem.startIndex }.map {
                it.copy(
                    startIndex = it.startIndex + changedItem.endIndex,
                    endIndex = it.endIndex + changedItem.endIndex
                )
            }.forEach { modifiedTaggedTextInfo ->
                val id = modifiedTaggedTextInfo.id
                _spannableString[id] = modifiedTaggedTextInfo
            }
        } else {
            // 삭제된 경우
            _spannableString.values.filter { it.startIndex > changedItem.endIndex }.map {
                it.copy(
                    startIndex = it.startIndex + size,
                    endIndex = it.endIndex + size
                )
            }.forEach { modifiedTaggedTextInfo ->
                val id = modifiedTaggedTextInfo.id
                _spannableString[id] = modifiedTaggedTextInfo
            }
        }
    }

    // 태그된 텍스트에 컬러를 입히기 위해 강제로 앞뒤에 특수한 문자열을 넣음
    private fun getMakeTaggedText(originText: String): String {
        var makeTaggedText = originText
        val sortedList = _spannableString.values.sortedBy { it.startIndex }

        sortedList.forEachIndexed { index, taggedTextInfo ->

            // 첫번째 아이템이 아닌 경우, 위치가 변경되었을 거라서 값을 추가해야함
            var sliceIndexStart = taggedTextInfo.startIndex
            var sliceIndexEnd = taggedTextInfo.endIndex

            if (index > 0) {
                // 기존 index 값에 계속해서 추가된 텍스트를 더해줌. ("|" 와 "`")
                sliceIndexStart += index + index
                sliceIndexEnd += index + index
            }

            val originSliceText = makeTaggedText.slice(sliceIndexStart..sliceIndexEnd)

            if (originSliceText == taggedTextInfo.text) {
                makeTaggedText =
                    makeTaggedText.replaceRange(
                        sliceIndexStart..sliceIndexEnd,
                        "|${taggedTextInfo.text}`"
                    )
            }
        }

        return makeTaggedText
    }

    fun updateText(
        originText: String,
        newTaggedInfo: TaggedTextInfo? = null,
        isForceUpdate: Boolean = false
    ) {
        if (newTaggedInfo != null) {
            updateTaggedString(newTaggedInfo, true)
            _spannableString[newTaggedInfo.id] = newTaggedInfo
        }

        val reformatText = getMakeTaggedText(originText)

        if (binding.commentEditTextView.text.toString() != originText || isForceUpdate) {

            val startIndexList = mutableListOf<Int>() // 시작 인덱스를 저장할 리스트
            val endIndexList = mutableListOf<Int>() // 끝 인덱스를 저장할 리스트

            var startIndex = reformatText.indexOf("|") // 시작 인덱스 초기값 설정
            while (startIndex != -1) { // 모든 시작 인덱스를 찾을 때까지 반복
                startIndexList.add(startIndex)
                startIndex = reformatText.indexOf("|", startIndex + 1) // 다음 시작 인덱스 찾기
            }

            for (startIndex in startIndexList) {
                val endIndex = reformatText.indexOf("`", startIndex + 1) // 끝 인덱스 찾기
                endIndexList.add(endIndex)
            }

            val spannableString = SpannableStringBuilder(reformatText)
            for (i in 0 until startIndexList.size) {
                val start = startIndexList[i] // @부터 시작
                val end = endIndexList[i] // 태그된 곳 까지만
                spannableString.setSpan(
                    ForegroundColorSpan(context.getColor(com.zinc.waver.ui_common.R.color.main5)), // 변경할 색상 설정
                    start, // 시작 인덱스 설정
                    end, // 끝 인덱스 설정 (+2는 끝 인덱스를 inclusive로 포함하기 위함)
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE // 스팬 적용 범위 설정
                )
            }

            // "@" 문자열을 빈 문자열로 바꿉니다.
            var index = spannableString.indexOf("|")
            while (index >= 0) {
                spannableString.replace(index, index + 1, "")
                index = spannableString.indexOf("|")
            }

            // "!" 문자열을 빈 문자열로 바꿉니다.
            index = spannableString.indexOf("`")
            while (index >= 0) {
                spannableString.replace(index, index + 1, "")
                index = spannableString.indexOf("`")
            }

            val resultText =
                spannableString.toString() // 제거된 텍스트로 업데이트된 SpannableStringBuilder를 다시 String으로 변환

            savedText = spannableString.toString()
            changeStartIndex = resultText.length
            binding.commentEditTextView.text = spannableString
            binding.commentEditTextView.setSelection(resultText.length)

        }
    }

    fun hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}