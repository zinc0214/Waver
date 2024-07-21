package com.zinc.waver.util

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText

fun View.setVisible(isVisible: Boolean) {
    if (isVisible) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}

fun EditText.onTextChanged(changed: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            // Do Nothing
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            changed(p0.toString())
        }

        override fun afterTextChanged(p0: Editable?) {
            // Do Nothing
        }

    })
}

fun EditText.afterTextChanged(changed: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            // Do Nothing
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            // Do Nothing
        }

        override fun afterTextChanged(p0: Editable?) {
            changed(p0.toString())
        }
    })
}