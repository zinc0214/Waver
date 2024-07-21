package com.zinc.waver.ui.presentation.report

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.databinding.DataBindingUtil
import com.zinc.common.models.ReportItem
import com.zinc.common.models.ReportItems
import com.zinc.waver.R
import com.zinc.waver.databinding.ActivityReportBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReportActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReportBinding

    private lateinit var imm: InputMethodManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_report)
        setUpView()
        setUpKeyBoard()
    }

    private fun setUpView() {
        imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        binding.composeView.apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
            setContent {
//                ReportLayer(reportInfo = reportInfo, reportItems = reportItems, clickEvent = {
//                    when (it) {
//                        is ReportClickEvent.ReportClicked -> {
//                            // go to report
//                        }
//                        is ReportClickEvent.ReportItemSelected -> {
//                            Log.e(
//                                "ayhan",
//                                "it.reportItem :${it.reportItem.text} , ${it.reportItem.text == "기타"}"
//                            )
//                            binding.etcVisible = it.reportItem.text == "기타"
//                        }
//                        ReportClickEvent.BackClicked -> {
//                            finish()
//                        }
//                    }
//                })
            }
        }
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
                binding.etcEditTextView.requestFocus()
            } else {
                // Keyboard Hide
                binding.etcEditTextView.clearFocus()
            }
        }

        binding.etcEditTextView.setOnFocusChangeListener { view, isFocused ->
            binding.keyBoardOpend = isFocused
        }
    }

//    val reportInfo = ReportInfo(
//        writer = "마이버리 귀염둥이 이명선",
//        contents = "버킷리스트가 너무 재미없어요 좀 재밌게 써달란 말이에욥"
//    )

    val reportItems = ReportItems(
        items = listOf(
            ReportItem(
                text = "광고/홍보성",
                id = "1"
            ),
            ReportItem(
                text = "욕설/인신 공격",
                id = "2"
            ),
            ReportItem(
                text = "불법정보",
                id = "3"
            ),
            ReportItem(
                text = "불법정보",
                id = "4"
            ),
            ReportItem(
                text = "음란성/선정성",
                id = "5"
            ),
            ReportItem(
                text = "개인정보 노출",
                id = "6"
            ),
            ReportItem(
                text = "같은 내용 도배",
                id = "7"
            ),
            ReportItem(
                text = "정치적/사회적 의견",
                id = "8"
            ),
            ReportItem(
                text = "기타",
                id = "9"
            )
        )
    )
}