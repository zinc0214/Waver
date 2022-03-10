package com.zinc.berrybucket.presentation.detail

import android.app.ActionBar
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.zinc.berrybucket.R
import com.zinc.berrybucket.databinding.FragmentDetailOptionDialogBinding

class DetailOptionDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentDetailOptionDialogBinding

    override fun onResume() {
        super.onResume()
        val dialogWidth = ActionBar.LayoutParams.MATCH_PARENT
        val dialogHeight = ActionBar.LayoutParams.MATCH_PARENT
        dialog?.window?.let {
            it.setLayout(dialogWidth, dialogHeight)
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            it.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        }
        setStyle(STYLE_NO_TITLE, R.style.DetailOptionPopup)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_detail_option_dialog,
            container,
            false
        )
        setUpViews()
        return binding.root
    }

    private fun setUpViews() {
        binding.contentLayout.setOnClickListener {
            dismiss()
        }
        binding.hideTextView.setOnClickListener {
            Toast.makeText(requireContext(), "hello", Toast.LENGTH_SHORT).show()
        }
    }
}