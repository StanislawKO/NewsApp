package com.codexample.newsapp.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codexample.newsapp.data.api.NewsRepository
import com.codexample.newsapp.models.NewsResponse
import com.codexample.newsapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: NewsRepository): ViewModel() {

    val searchNewsLiveData: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1

    init {
        getSearchNews("")
    }

    fun getSearchNews(query: String) =
        viewModelScope.launch {
            searchNewsLiveData.postValue(Resource.Loading())
            val response = repository.getSearchNews(query = query, pageNumber = searchNewsPage)
            if (response.isSuccessful) {
                response.body().let {
                    searchNewsLiveData.postValue(Resource.Success(it))
                }
            } else {
                response.body().let {
                    searchNewsLiveData.postValue(Resource.Error(message = response.message()))
                }
            }
        }
}