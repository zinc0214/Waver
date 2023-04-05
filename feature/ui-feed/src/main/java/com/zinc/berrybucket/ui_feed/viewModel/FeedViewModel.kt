package com.zinc.berrybucket.ui_feed.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zinc.berrybucket.CommonViewModel
import com.zinc.common.models.FeedInfo
import com.zinc.common.models.FeedKeyWord
import com.zinc.datastore.login.LoginPreferenceDataStoreModule
import com.zinc.domain.usecases.feed.LoadFeedItems
import com.zinc.domain.usecases.feed.LoadFeedKeyWords
import dagger.hilt.android.lifecycle.HiltViewModel
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

    private val _feedKeyWords = MutableLiveData<List<FeedKeyWord>>()
    val feedKeyWords: LiveData<List<FeedKeyWord>> get() = _feedKeyWords

    private val _feedItems = MutableLiveData<List<FeedInfo>>()
    val feedItems: LiveData<List<FeedInfo>> get() = _feedItems

    fun loadKeyWordSelected() {
        _isKeyWordSelected.value = false
    }

    fun loadFeedKeyWords() {
        viewModelScope.launch {
            runCatching {
                loadFeedKeyWords.invoke().apply {
                    _feedKeyWords.value = this
                }
            }.getOrElse {

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
