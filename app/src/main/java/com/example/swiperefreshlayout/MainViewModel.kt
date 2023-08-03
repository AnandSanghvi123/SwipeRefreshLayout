package com.example.swiperefreshlayout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {

    private val _data = MutableLiveData<List<String>>()
    val data: LiveData<List<String>> get() = _data

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private var originalData = mutableListOf<String>()

    init {
        originalData = generateData().toMutableList()
        filterData("") // Filter initial data with empty query
    }

    fun loadData() {
        _loading.value = true
        viewModelScope.launch {
            // Simulate a delay for fetching new data
            withContext(Dispatchers.IO) {
                kotlinx.coroutines.delay(2000) // Simulate a 2-second delay
            }

            val newData = generateData().shuffled()

            _data.postValue(newData)
            originalData = newData.toMutableList()

            _loading.postValue(false)
        }
    }

    fun filterData(query: String) {
        val filteredData = originalData.filter { it.contains(query, true) }
        _data.value = filteredData
    }

    private fun generateData(): List<String> {
        val data = mutableListOf<String>()
        for (i in 1..10) {
            data.add("Item $i")
        }
        return data
    }
}
