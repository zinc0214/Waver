package com.zinc.berrybucket.presentation.detail.my.open

import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Html.FROM_HTML_MODE_COMPACT
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.databinding.DataBindingUtil
import com.zinc.berrybucket.R
import com.zinc.berrybucket.databinding.ActivityMyOpenBucketDetailBinding
import com.zinc.berrybucket.model.CommentTagInfo
import com.zinc.berrybucket.model.DetailInfo
import com.zinc.berrybucket.model.DetailType
import com.zinc.berrybucket.presentation.detail.CommentOptionDialogFragment
import com.zinc.berrybucket.presentation.detail.DetailOptionDialogFragment
import com.zinc.berrybucket.presentation.detail.DetailViewModel
import com.zinc.berrybucket.presentation.detail.listview.CommentTagListViewAdapter
import com.zinc.berrybucket.util.nonNullObserve
import com.zinc.berrybucket.util.onTextChanged
import com.zinc.berrybucket.util.setVisible
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MyOpenBucketDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyOpenBucketDetailBinding
    private val viewModel by viewModels<DetailViewModel>()

    private lateinit var commentTagListAdapter: CommentTagListViewAdapter
    private lateinit var imm: InputMethodManager
    private lateinit var commentTaggableList: List<CommentTagInfo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_open_bucket_detail)
        setUpViews()
        setUpViewModels()
    }

    private fun setUpViews() {
        imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        // setUpEditText()
        //  setUpKeyBoard()
    }

    private fun setUpContentInfo(detailInfo: DetailInfo) {
        binding.composeView.apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
            setContent {
//                OpenDetailLayer(
//                    detailInfo = detailInfo,
//                    clickEvent = {
//                        when (it) {
//                            DetailClickEvent.CloseClicked -> {
//                                finish()
//                            }
//                            DetailClickEvent.MoreOptionClicked -> {
//                                showDetailOptionPopup()
//                            }
//                            DetailClickEvent.SuccessClicked -> {
//                                // TODO
//                            }
//                            is CommentLongClicked -> {
//                                showCommentOptionDialog(it.commentId)
//                            }
//                        }
//                    }
//                )
            }
        }
    }

    private fun setUpViewModels() {
        viewModel.bucketDetailInfo.nonNullObserve(this) {
            setUpContentInfo(it)
        }

        viewModel.originCommentTaggableList.nonNullObserve(this) {
            commentTaggableList = it
        }

        viewModel.commentEditString.nonNullObserve(this) {
            changeEditText(it)
            val getLastIndex = binding.commentEditTextView.text.length
            binding.commentEditTextView.setSelection(getLastIndex)
        }

        viewModel.getBucketDetail("open")
        viewModel.getCommentTaggableList()
    }

    private fun setUpKeyBoard() {
        val contentLayout = binding.contentLayout
        contentLayout.viewTreeObserver.addOnGlobalLayoutListener {
            val rect = Rect()
            contentLayout.getWindowVisibleDisplayFrame(rect)
            val screenHeight = contentLayout.rootView.height
            val keypadHeight = screenHeight - rect.bottom
            if (keypadHeight > screenHeight * 0.15) {
                // Keyboard Show
                binding.commentEditTextView.requestFocus()
            } else {
                // Keyboard Hide
                binding.commentEditTextView.clearFocus()
                binding.commentTagLayout.visibility = View.GONE
            }
        }
    }

    private fun setUpEditText() {
        commentTagListAdapter = CommentTagListViewAdapter {
            val editText = binding.commentEditTextView.text.toString()
            val currentBlock = getCurrentEditTextBlock(editText)
            viewModel.addCommentTaggedItem(it, currentBlock.startIndex, currentBlock.endIndex)
        }

        binding.commentTagListView.adapter = commentTagListAdapter

        binding.commentSendButton.isEnabled = false

        binding.commentEditTextView.setOnFocusChangeListener { _, hasFocus ->
            binding.commentSendButton.setVisible(hasFocus)
            binding.commentEditTextView.maxLines = if (hasFocus) 3 else 1
        }

        binding.commentEditTextView.onTextChanged { text ->

            binding.commentSendButton.isEnabled = text.isNotBlank()

            val validTaggableList = getCurrentBlockTaggableList(text)
            if (validTaggableList.isEmpty()) {
                binding.commentTagLayout.visibility = View.GONE
            } else {
                commentTagListAdapter.updateItems(validTaggableList)
                binding.commentTagLayout.visibility = View.VISIBLE
            }

            viewModel.updateCommentEditString(text)
        }

        binding.commentSendButton.setOnClickListener {
            imm.hideSoftInputFromWindow(binding.commentEditTextView.windowToken, 0)
            binding.commentEditTextView.setText("")
            binding.commentEditTextView.clearFocus()
        }
    }

    // 현재 커서가 위치한 EditText 블록에서 태그 가능한 리스트 확인
    private fun getCurrentBlockTaggableList(
        currentEditText: String
    ): List<CommentTagInfo> {
        val currentCursorBlock = getCurrentEditTextBlock(currentEditText).currentBlockText
        if (currentCursorBlock.isEmpty()) return emptyList()

        if (currentCursorBlock.isNotBlank() && currentCursorBlock.first() == '@') {
            val originString = currentCursorBlock.drop(1)
            return if (originString.isNotBlank()) {
                commentTaggableList.filter { commentInfo ->
                    commentInfo.nickName.contains(
                        originString
                    )
                }
            } else {
                commentTaggableList
            }
        }
        return emptyList()
    }

    // 현재 EditText 의 커서가 있는 블록값 확인
    private fun getCurrentEditTextBlock(currentEditText: String): CurrentBlock {
        val splitText = currentEditText.split(" ")
        var splitLength = 0
        var startIndex = 0
        val currentEditTextFocus = binding.commentEditTextView.selectionEnd
        splitText.forEach {
            splitLength += it.length + 1
            if (splitLength > currentEditTextFocus) {
                return CurrentBlock(it, startIndex, startIndex + it.length)
            }
            startIndex += it.length + 1
        }
        return CurrentBlock(currentEditText, 0, 0)
    }

    private fun changeEditText(currentEditText: String) {
        val changedColorText = changeTaggedTextColor(currentEditText)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.commentEditTextView.setText(
                Html.fromHtml(
                    changedColorText,
                    FROM_HTML_MODE_COMPACT
                )
            )
        } else {
            binding.commentEditTextView.setText(Html.fromHtml(changedColorText))
        }
    }

    // 태그된 텍스트와 아닌 텍스트를 각각 색상 채우기
    private fun changeTaggedTextColor(currentEditText: String): String {
        val taggedList = getTaggedOrNotBlockList(currentEditText)
        var changedColorText = ""
        taggedList.forEach {
            changedColorText += if (it.first) {
                getColoredSpanned(it.second)
            } else {
                it.second
            }
            if (taggedList.last() != it) {
                changedColorText += " "
            }
        }
        return changedColorText
    }

    // 태그된 텍스트와 아닌 텍스트를 각각 리스트로 분리
    private fun getTaggedOrNotBlockList(currentEditText: String): List<Pair<Boolean, String>> {
        val list = mutableListOf<Pair<Boolean, String>>()
        currentEditText.split(" ").forEach { text ->
            val isExistTagged = commentTaggableList.firstOrNull { "@${it.nickName}" == text }
            list.add(Pair(isExistTagged != null, text))
        }
        return list
    }


    private fun getColoredSpanned(text: String): String {
        return "<font color=#284f81>$text</font>"
    }

    private fun showCommentOptionDialog(commentId: String) {
        CommentOptionDialogFragment().apply {
            setCommentId(commentId)
        }.show(supportFragmentManager, "CommentOptionDialog")
    }

    private fun showDetailOptionPopup() {
        DetailOptionDialogFragment().apply {
            setDetailType(DetailType.MY_OPEN)
        }.show(supportFragmentManager, "showPopup")
    }

    data class CurrentBlock(
        val currentBlockText: String,
        val startIndex: Int,
        val endIndex: Int
    )
}
