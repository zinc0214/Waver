package com.zinc.berrybucket.ui.custom

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.zinc.berrybucket.R
import com.zinc.berrybucket.databinding.WidgetTextAndSwitchBinding

class TextAndSwitchView @JvmOverloads constructor(
    context: Context,
    val attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private lateinit var binding: WidgetTextAndSwitchBinding
    var text: String? = ""
    var isOn = false

    init {
        setUpViews()
    }

    private fun setUpViews() {
        binding = WidgetTextAndSwitchBinding.inflate(LayoutInflater.from(context), this, true)

        val attributeArray: TypedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.textAndSwitchView, 0, 0
        )
        text = attributeArray.getString(R.styleable.textAndSwitchView_text)
        isOn = attributeArray.getBoolean(R.styleable.textAndSwitchView_isOn, false)

        binding.title = text
        binding.switchButton.isChecked = isOn
    }
}


