package com.zinc.berrybucket.presentation.detail.my.open

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zinc.berrybucket.R
import com.zinc.berrybucket.databinding.ActivityMyOpenBucketDetailBinding
import com.zinc.berrybucket.model.CommonDetailDescInfo
import com.zinc.berrybucket.model.DetailDescType
import com.zinc.berrybucket.model.DetailType
import com.zinc.berrybucket.model.InnerSuccessButton
import com.zinc.berrybucket.presentation.detail.CommentOptionDialogFragment
import com.zinc.berrybucket.presentation.detail.DetailOptionDialogFragment
import com.zinc.berrybucket.presentation.detail.DetailViewModel
import com.zinc.berrybucket.util.nonNullObserve
import com.zinc.berrybucket.util.onTextChanged
import com.zinc.berrybucket.util.setVisible
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MyOpenBucketDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyOpenBucketDetailBinding
    private val viewModel by viewModels<DetailViewModel>()

    private lateinit var detailListAdapter: MyOpenDetailListViewAdapter
    private lateinit var imm: InputMethodManager
    private lateinit var detailList: List<DetailDescType>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_open_bucket_detail)
        setUpViews()
        setUpViewModels()
    }

    private fun setUpViews() {
        imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        binding.optionButton.setOnClickListener { showDetailOptionPopup() }
        setUpEditText()
        setUpKeyBoard()
    }

    private fun setUpViewModels() {
        viewModel.bucketDetailInfo.nonNullObserve(this) {
            detailList = it
            setUpAdapter()
        }
        viewModel.getBucketDetail("open")
    }

    private fun setUpAdapter() {
        detailListAdapter = MyOpenDetailListViewAdapter(
            successClicked = {
                // Success Button Clicked!
            },
            commentLongClicked = {
                showCommentOptionDialog(it)
            }).apply { updateItems(detailList) }

        binding.detailListView.adapter = detailListAdapter
        setUpScrollChangedListener()
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
            }
        }
    }

    private fun setUpScrollChangedListener() {
        var isToolbarShown = false
        val lastIndex = detailList.lastIndex
        val buttonPosition = detailList.indexOfFirst { it is InnerSuccessButton }
        val titleInfoPosition = detailList.indexOfFirst { it is CommonDetailDescInfo }

        binding.apply {
            detailListView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
                val shouldShowToolbar = scrollY > toolbar.height
                if (isToolbarShown != shouldShowToolbar) {
                    isToolbarShown = shouldShowToolbar
                    appbar.isActivated = shouldShowToolbar
                }
            }

            detailListView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val firstVisible = layoutManager.findFirstVisibleItemPosition()
                    val lastVisible = layoutManager.findLastVisibleItemPosition()
                    val lastCompleteVisible = layoutManager.findLastCompletelyVisibleItemPosition()

                    // 하단 댓글 입력 버튼이 노출되어야 하는 경우
                    val showEditLayout =
                        lastCompleteVisible >= lastIndex - 1 || lastVisible >= buttonPosition || !imm.isActive
                    (detailList[buttonPosition] as InnerSuccessButton).isVisible = showEditLayout
                    detailListAdapter.updateItems(detailList)
                    showEditView = showEditLayout

                    // 타이틀이 상단 appBar 에 노출되어야 하는 경우
                    val showTitleAppBar = firstVisible > titleInfoPosition
                    titleTextView.text =
                        if (showTitleAppBar) (detailList[titleInfoPosition] as CommonDetailDescInfo).title else ""
                }
            })
        }
    }

    private fun setUpEditText() {
        binding.commentSendButton.isEnabled = false

        binding.commentEditTextView.setOnFocusChangeListener { _, hasFocus ->
            binding.commentSendButton.setVisible(hasFocus)
            binding.commentEditTextView.maxLines = if (hasFocus) 3 else 1
        }

        binding.commentEditTextView.onTextChanged { text ->
            binding.commentSendButton.isEnabled = text.isNotBlank()
        }

        binding.commentSendButton.setOnClickListener {
            imm.hideSoftInputFromWindow(binding.commentEditTextView.windowToken, 0)
            binding.commentEditTextView.setText("")
            binding.commentEditTextView.clearFocus()
        }
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
}
