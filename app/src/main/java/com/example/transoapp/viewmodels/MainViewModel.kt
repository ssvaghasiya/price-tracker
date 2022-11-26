package com.example.transoapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.transoapp.pojo.ExampleData
import com.example.transoapp.repository.ExampleRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val repository: ExampleRepository,
) : ViewModel() {

    val exampleLiveData: LiveData<ExampleData?>
        get() = repository.examples

    fun callApi() {
        viewModelScope.launch {
            repository.getExamples()
        }
    }

}

