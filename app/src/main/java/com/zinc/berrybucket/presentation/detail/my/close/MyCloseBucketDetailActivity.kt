package com.zinc.berrybucket.presentation.detail.my.close

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.zinc.berrybucket.compose.ui.detail.CloseDetailLayer
import com.zinc.berrybucket.model.DetailClickEvent
import com.zinc.berrybucket.model.DetailType
import com.zinc.berrybucket.presentation.detail.DetailOptionDialogFragment
import com.zinc.berrybucket.presentation.detail.DetailViewModel
import com.zinc.berrybucket.util.nonNullObserve
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyCloseBucketDetailActivity : AppCompatActivity() {
    private val viewModel by viewModels<DetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpViewModels()
    }


    private fun setUpViewModels() {
        viewModel.bucketDetailInfo.nonNullObserve(this) {
            setContent {
//                CloseDetailLayer(detailId = "test", clickEvent = {
//                    when (it) {
//                        DetailClickEvent.CloseClicked -> {
//                            finish()
//                        }
//                        DetailClickEvent.MoreOptionClicked -> {
//                            fshowDetailOptionPopup()
//                        }
//                        DetailClickEvent.SuccessClicked -> {
//                            // TODO
//                        }
//                        else -> {
//                            // Do Nothing
//                        }
//                    }
//                })
            }
        }

        viewModel.getBucketDetail("close")
    }

    private fun showDetailOptionPopup() {
        DetailOptionDialogFragment().apply {
            setDetailType(DetailType.MY_CLOSE)
        }.show(supportFragmentManager, "showPopup")
    }


}