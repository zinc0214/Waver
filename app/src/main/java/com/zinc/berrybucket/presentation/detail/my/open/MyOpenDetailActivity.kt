package com.zinc.berrybucket.presentation.detail.my.open

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zinc.berrybucket.R
import com.zinc.berrybucket.databinding.FragmentBucketDetailBinding
import com.zinc.berrybucket.model.*
import com.zinc.berrybucket.presentation.detail.CommentOptionDialogFragment
import com.zinc.berrybucket.presentation.detail.DetailOptionDialogFragment
import com.zinc.berrybucket.presentation.detail.DetailViewModel
import com.zinc.berrybucket.util.onTextChanged
import com.zinc.berrybucket.util.setVisible
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MyOpenDetailActivity : AppCompatActivity() {

    private lateinit var binding: FragmentBucketDetailBinding
    private val viewModel by viewModels<DetailViewModel>()

    private lateinit var detailListAdapter: MyOpenDetailListViewAdapter
    private lateinit var imm: InputMethodManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_bucket_detail)
        setUpViews()
    }

    private fun setUpViews() {
        imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        detailListAdapter = MyOpenDetailListViewAdapter(detailList,
            successClicked = {
                // Success Button Clicked!
            },
            commentLongClicked = {
                showCommentOptionDialog(it)
            })

        binding.detailListView.adapter = detailListAdapter
        binding.optionButton.setOnClickListener { showPopup(binding.optionButton) }
        setUpScrollChangedListener(detailList)
        setUpEditText()
        setUpKeyBoard()
    }

//    private fun setUpImageView() {
//        binding.imageComposeView.apply {
//            setViewCompositionStrategy(
//                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
//            )
//            setContent {
//                BaseTheme {
//
//                }
//            }
//        }
//
//    }

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

    private fun setUpScrollChangedListener(detailList: List<DetailType>) {
        var isToolbarShown = false
        val lastIndex = detailList.lastIndex
        val buttonPosition = detailList.indexOfFirst { it is DetailType.Button }
        val titleInfoPosition = detailList.indexOfFirst { it is DetailDescInfo }

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
                        lastCompleteVisible >= lastIndex - 1 || lastVisible >= buttonPosition
                    detailListAdapter.updateSuccessButton(showEditLayout)
                    showEditView = showEditLayout

                    // 타이틀이 상단 appBar 에 노출되어야 하는 경우
                    val showTitleAppBar = firstVisible > titleInfoPosition
                    titleTextView.text =
                        if (showTitleAppBar) (detailList[titleInfoPosition] as DetailDescInfo).title else ""
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

    private fun showPopup(v: View) {
        DetailOptionDialogFragment().show(supportFragmentManager, "showPopup")
    }

    private val detailList = listOf(
        OpenImageInfo(
            imageList = listOf("A", "B", "C")
        ),
        ProfileInfo(
            profileImage = "",
            badgeImage = "",
            titlePosition = "멋쟁이 여행가",
            nickName = "한아크크룽삐옹"
        ),
        DetailDescInfo(
            dDay = "D+201",
            tagList = listOf("여행", "강남"),
            title = "가나다라마바사",
        ),
        MemoInfo(
            memo = "▶ 첫째날\n" +
                    "도두해안도로 - 도두봉키세스존 - 이호테우해변 - 오설록티뮤지엄 \n" +
                    "\n" +
                    "▶ 둘째날\n" +
                    " 쇠소깍 - 크엉해안경승지 - 이승악오름\n " +
                    "▶ 첫째날\n" +
                    "도두해안도로 - 도두봉키세스존 - 이호테우해변 - 오설록티뮤지엄 \n" +
                    "\n" +
                    "▶ 둘째날\n" +
                    " 쇠소깍 - 크엉해안경승지 - 이승악오름\n" + "▶ 첫째날\n" +
                    "도두해안도로 - 도두봉키세스존 - 이호테우해변 - 오설록티뮤지엄 \n" +
                    "\n" +
                    "▶ 둘째날\n" +
                    " 쇠소깍 - 크엉해안경승지 - 이승악오름\n" +
                    "▶ 첫째날\n" +
                    "도두해안도로 - 도두봉키세스존 - 이호테우해변 - 오설록티뮤지엄 \n" +
                    "\n" +
                    "▶ 둘째날\n" +
                    " 쇠소깍 - 크엉해안경승지 - 이승악오름\n"
        ),
        DetailType.Button,
        CommentInfo(
            commentCount = "10",
            listOf(
                Commenter(
                    "1", "A", "아연이 내꺼지 너무너무 이쁘지", "@귀염둥이 이명선 베리버킷 댓글입니다.\n" +
                            "베리버킷 댓글입니다."
                ),
                Commenter(
                    "2", "B",
                    "Contrary to popular belief",
                    "Lorem Ipsum is not simply random text. It has roots in a piece of classical Latin literature from 45 BC, making it over 2000 years old. Richard McClintock, "
                ),
            )
        )
    )
}
