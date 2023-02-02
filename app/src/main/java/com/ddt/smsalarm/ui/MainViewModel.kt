package com.ddt.smsalarm.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddt.smsalarm.data.MainRepository
import com.ddt.smsalarm.data.model.Setting
import com.ddt.smsalarm.data.db.FilterEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    private val _filters = MutableStateFlow<List<FilterEntity>>(emptyList())
    val filters = _filters.asStateFlow()

    private val _setting = MutableStateFlow<Setting>(Setting())
    val setting = _setting.asStateFlow()

    private fun getFilters() = viewModelScope.launch(Dispatchers.IO) {
        repository.getFilters().collect {
            _filters.emit(it)
        }
    }

    fun insertFilter(filterEntity: FilterEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertFilter(filterEntity)
    }

    fun updateFilter(filterEntity: FilterEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateFilter(filterEntity)
    }

    fun deleteFilter(filterEntity: FilterEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteFilter(filterEntity)
    }


    private fun getSetting() = viewModelScope.launch(Dispatchers.IO) {
        repository.getSetting().collect {
            _setting.emit(it)
        }
    }

    fun saveSetting(setting: Setting) = viewModelScope.launch(Dispatchers.IO) {
        repository.storeISetting(setting)
    }
}