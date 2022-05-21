package com.zinc.berrybucket.presentation.feed.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment

class FeedFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                ShowContent()
            }
        }
    }

    @Composable
    private fun ShowContent() {
        val recommendClicked = remember {
            mutableStateOf(false)
        }

    }


    companion object {
        @JvmStatic
        fun newInstance() = FeedFragment()
    }
}