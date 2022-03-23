package com.zinc.berrybucket.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zinc.berrybucket.model.*
import com.zinc.domain.usecases.detail.LoadBucketDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val loadBucketDetail: LoadBucketDetail
) : ViewModel() {

    private val _bucketDetailInfo = MutableLiveData<List<DetailDescType>>()
    val bucketDetailInfo: LiveData<List<DetailDescType>> get() = _bucketDetailInfo

    fun getBucketDetail(id: String) {
//        viewModelScope.launch {
//            loadBucketDetail(id)
//        }

        if (id == "open") {
            _bucketDetailInfo.value = detailList1
        } else {
            _bucketDetailInfo.value = detailList2
        }

    }

    private val detailList1 = listOf(
        ImageInfo(
            imageList = listOf("A")
        ),
        ProfileInfo(
            profileImage = "",
            badgeImage = "",
            titlePosition = "멋쟁이 여행가",
            nickName = "한아크크룽삐옹"
        ),
        CommonDetailDescInfo(
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
        InnerSuccessButton(
            isVisible = false
        ),
        CommentInfo(
            commentCount = 2,
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

    private val detailList2 = listOf(
        CloseDetailDescInfo(
            CommonDetailDescInfo(
                dDay = "D+201",
                tagList = listOf("여행", "강남"),
                title = "가나다라마바사",
            ),
            goalCount = 1,
            userCount = 0
        ),
        ImageInfo(
            imageList = listOf("A", "B", "C")
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
        )
    )
}