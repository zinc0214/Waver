package com.zinc.berrybucket.ui_feed.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zinc.berrybucket.CommonViewModel
import com.zinc.berrybucket.ui_feed.models.UIFeedKeyword
import com.zinc.berrybucket.ui_feed.models.parseUI
import com.zinc.berrybucket.util.SingleLiveEvent
import com.zinc.common.models.FeedInfo
import com.zinc.datastore.login.LoginPreferenceDataStoreModule
import com.zinc.domain.usecases.feed.LoadFeedItems
import com.zinc.domain.usecases.feed.LoadFeedKeyWords
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val loadFeedKeyWords: LoadFeedKeyWords,
    private val loadFeedItems: LoadFeedItems,
    loginPreferenceDataStoreModule: LoginPreferenceDataStoreModule
) : CommonViewModel(loginPreferenceDataStoreModule) {
    private val _isKeyWordSelected = MutableLiveData<Boolean>()
    val isKeyWordSelected: LiveData<Boolean> get() = _isKeyWordSelected

    private val _feedKeyWords = MutableLiveData<List<UIFeedKeyword>>()
    val feedKeyWords: LiveData<List<UIFeedKeyword>> get() = _feedKeyWords

    private val _feedItems = MutableLiveData<List<FeedInfo>>()
    val feedItems: LiveData<List<FeedInfo>> get() = _feedItems

    private val _loadFail = SingleLiveEvent<Boolean>()
    val loadFail: LiveData<Boolean> get() = _loadFail

    fun loadKeyWordSelected() {
        _isKeyWordSelected.value = false
    }

    fun loadFeedKeyWords() {
        viewModelScope.launch(CoroutineExceptionHandler { coroutineContext, throwable ->
            _loadFail.value = true
        }) {
            runCatching {
                loadFeedKeyWords.invoke().apply {
                    Log.e("ayhan", "feed response : $this")
                    if (success) {
                        _feedKeyWords.value = data.parseUI()
                        _loadFail.value = false
                    } else {
                        _loadFail.value = true
                    }
                }
            }.getOrElse {
                _loadFail.value = true
            }
        }
    }

    fun loadFeedItems() {
        viewModelScope.launch {
            kotlin.runCatching {
                loadFeedItems.invoke().apply {
                    _feedItems.value = this
                }
            }.getOrElse { }

        }
    }

    fun setKeyWordSelected() {
        _isKeyWordSelected.value = true
    }
}
