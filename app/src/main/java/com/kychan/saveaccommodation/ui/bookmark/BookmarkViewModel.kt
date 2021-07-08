package com.kychan.saveaccommodation.ui.bookmark

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kychan.saveaccommodation.data.AccommodationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val accommodationRepository: AccommodationRepository
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _bookmarkList = MutableLiveData<List<BookmarkItem>>()
    val bookmarkList: LiveData<List<BookmarkItem>>
        get() = _bookmarkList

    init {
        compositeDisposable.add(
            accommodationRepository
                .fetchBookmarkList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _bookmarkList.value = it
                    Log.d("AccommodationViewModel", it.toString())
                }, {
                    Log.d("AccommodationViewModel", it.toString())
                })
        )
    }

    fun deleteAccommodation(id: Int) {
        Thread {
            accommodationRepository.deleteAccommodation(id)
        }.start()
    }
}