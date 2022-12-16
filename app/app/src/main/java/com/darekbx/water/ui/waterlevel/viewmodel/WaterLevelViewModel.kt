package com.darekbx.water.ui.waterlevel.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.darekbx.data.dto.WaterLevelDto
import com.darekbx.water.repository.local.WaterLevelRepository
import com.darekbx.water.repository.local.StatusRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class WaterLevelViewModel @Inject constructor(
    private val statusRepository: StatusRepository,
    private val levelRepository: WaterLevelRepository
) : ViewModel() {

    fun status() = statusRepository.status().asLiveData()

    fun waterLevels(): LiveData<List<WaterLevelDto>> {
        val date = dateFormat.format(Date())
        return levelRepository.waterLevels(date).asLiveData()
    }

    private val dateFormat by lazy { SimpleDateFormat("YYYYMMdd") }
}