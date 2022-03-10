package com.zinc.berrybucket.presentation.detail

import android.app.ActionBar
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.zinc.berrybucket.R
import com.zinc.berrybucket.databinding.FragmentCommentOptionDialogBinding

class CommentOptionDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentCommentOptionDialogBinding
    private lateinit var commentId: String

    override fun onResume() {
        super.onResume()
        val dialogWidth = resources.getDimensionPixelSize(R.dimen.popupWidth)
        val dialogHeight = ActionBar.LayoutParams.WRAP_CONTENT
        dialog?.window!!.setLayout(dialogWidth, dialogHeight)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_comment_option_dialog,
            container,
            false
        )

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.setCanceledOnTouchOutside(true)

        return binding.root
    }

    fun setCommentId(commentId: String) {
        this.commentId = commentId
    }
}