package com.zinc.berrybucket.presentation.detail

import android.util.Log
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
//
//    private val _bucketDetailInfo = MutableLiveData<List<DetailDescType>>()
//    val bucketDetailInfo: LiveData<List<DetailDescType>> get() = _bucketDetailInfo

    private val _bucketDetailInfo = MutableLiveData<DetailInfo>()
    val bucketDetailInfo: LiveData<DetailInfo> get() = _bucketDetailInfo

    private val _originCommentTaggableList = MutableLiveData<List<CommentTagInfo>>()
    val originCommentTaggableList: LiveData<List<CommentTagInfo>> = _originCommentTaggableList

    private val _commentTaggedList = MutableLiveData<List<CommentTagInfo>>()
    val commentTaggedList: LiveData<List<CommentTagInfo>> get() = _commentTaggedList

    private val _commentEditString = MutableLiveData<String>()
    val commentEditString: LiveData<String> = _commentEditString

    private var commentEditOriginString = ""


    fun getBucketDetail(id: String) {
//        viewModelScope.launch {
//            loadBucketDetail(id)
//        }

        if (id == "open") {
            _bucketDetailInfo.value = detailInfo1
        } else {
            _bucketDetailInfo.value = detailInfo1
        }
    }

    fun getCommentTaggableList() {
        val tagableNickName = listOf(
            CommentTagInfo("A", "승빈"),
            CommentTagInfo("A", "승빈2"),
            CommentTagInfo("B", "카시이")
        )
        _originCommentTaggableList.value = tagableNickName
    }


    fun addCommentTaggedItem(
        commentTagInfo: CommentTagInfo,
        cursorStartIndex: Int,
        cursorEndIndex: Int
    ) {
        val taggedList = commentTaggedListToArray()
        taggedList.add(commentTagInfo)
        _commentTaggedList.value = taggedList

        Log.e("ayhan", "cursorStartIndex :$cursorStartIndex cursorEndIndex : $cursorEndIndex")
        addCommentTaggedItemToEditString(commentTagInfo, cursorStartIndex, cursorEndIndex)
    }

    fun removeCommentTaggedItem(commentTagInfo: CommentTagInfo) {
        val taggedList = commentTaggedListToArray()
        taggedList.remove(commentTagInfo)
        _commentTaggedList.value = taggedList
        removeCommentTaggedItemToEditString(commentTagInfo)
    }

    private fun commentTaggedListToArray(): ArrayList<CommentTagInfo> {
        val taggedList = _commentTaggedList.value
        return if (taggedList.isNullOrEmpty()) {
            arrayListOf()
        } else {
            taggedList as ArrayList
        }
    }

    private fun addCommentTaggedItemToEditString(
        commentTagInfo: CommentTagInfo,
        cursorStartIndex: Int, cursorEndIndex: Int
    ) {
        var editText = commentEditOriginString
        editText = editText.replaceRange(
            startIndex = cursorStartIndex,
            endIndex = cursorEndIndex,
            replacement = "@${commentTagInfo.nickName}"
        )
        _commentEditString.value = editText
    }

    private fun removeCommentTaggedItemToEditString(commentTagInfo: CommentTagInfo) {
        var editString = commentEditOriginString
        if (editString.isBlank()) return
        editString = editString.removePrefix(commentTagInfo.nickName)
        _commentEditString.value = editString
    }

    fun updateCommentEditString(editString: String) {
        commentEditOriginString = editString
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

    private val detailInfo1 =
        DetailInfo(
            imageInfo = ImageInfo(
                imageList = listOf("A", "B", "C")
            ),
            profileInfo = ProfileInfo(
                profileImage = "",
                badgeImage = "",
                titlePosition = "멋쟁이 여행가",
                nickName = "한아크크룽삐옹"
            ),
            descInfo = CommonDetailDescInfo(
                dDay = "D+201",
                tagList = listOf("여행", "강남"),
                title = "가나다라마바사",
            ),
            memoInfo = MemoInfo(
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
            commentInfo = CommentInfo(
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
}