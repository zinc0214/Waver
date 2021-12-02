package com.zinc.berrybucket.presentation.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import com.zinc.berrybucket.R
import com.zinc.berrybucket.compose.theme.BaseTheme
import com.zinc.berrybucket.compose.ui.component.ImageViewPagerInsideIndicator
import com.zinc.berrybucket.databinding.FragmentBucketDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: FragmentBucketDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_bucket_detail)
        setUpViews()
    }


    private fun setUpViews() {

        var isToolbarShown = false

        binding.apply {
            // scroll change listener begins at Y = 0 when image is fully collapsed
            scrollView.setOnScrollChangeListener(
                NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, _ ->

                    // User scrolled past image to height of toolbar and the title text is
                    // underneath the toolbar, so the toolbar should be shown.
                    val shouldShowToolbar = scrollY > toolbar.height

                    // The new state of the toolbar differs from the previous state; update
                    // appbar and toolbar attributes.
                    if (isToolbarShown != shouldShowToolbar) {
                        isToolbarShown = shouldShowToolbar

                        // Use shadow animator to add elevation if toolbar is shown
                        appbar.isActivated = shouldShowToolbar

                        // Show the plant name if toolbar is shown
                        toolbarLayout.isTitleEnabled = shouldShowToolbar
                    }
                }
            )

            imageComposeView.apply {
                setViewCompositionStrategy(
                    ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
                )
                setContent {
                    BaseTheme {
                        ImageViewPagerInsideIndicator(
                            modifier = Modifier.fillMaxWidth(),
                            imageList = listOf("A", "B", "C")
                        )
                    }
                }
            }
        }
    }
}
