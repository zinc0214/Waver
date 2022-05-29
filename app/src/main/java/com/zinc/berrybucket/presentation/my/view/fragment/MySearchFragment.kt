package com.zinc.berrybucket.presentation.my.view.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.zinc.berrybucket.R
import com.zinc.berrybucket.compose.ui.my.MySearchBottomScreen
import com.zinc.berrybucket.model.MySearchClickEvent
import com.zinc.berrybucket.model.MyTabType
import com.zinc.berrybucket.presentation.my.viewModel.MyViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MySearchFragment : BottomSheetDialogFragment() {

    lateinit var tabType: MyTabType
    private val viewModel by viewModels<MyViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.NonClosableBottomSheetDialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MySearchBottomScreen(
                    currentTabType = tabType,
                    clickEvent = {
                        if (it == MySearchClickEvent.CloseClicked) {
                            dismiss()
                        }
                    },
                    searchWord = { tabType, word ->
                        viewModel.searchList(tabType, word)
                    },
                    result = viewModel.searchResult.observeAsState()
                )
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            val parentLayout = bottomSheetDialog.findViewById<View>(R.id.design_bottom_sheet)
            parentLayout?.let { it ->
                val behaviour = BottomSheetBehavior.from(it)
                setupFullHeight(it)
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return dialog
    }

    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }

    companion object {
        @JvmStatic
        fun newInstance(tabType: MyTabType) = MySearchFragment().apply {
            this.tabType = tabType
        }
    }
}