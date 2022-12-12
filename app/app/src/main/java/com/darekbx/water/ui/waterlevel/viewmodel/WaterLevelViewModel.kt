package com.darekbx.water.ui.waterlevel.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.darekbx.water.repository.local.LevelRepository
import com.darekbx.water.repository.local.StatusRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WaterLevelViewModel @Inject constructor(
    private val statusRepository: StatusRepository,
    private val levelRepository: LevelRepository
) : ViewModel() {

    val status = statusRepository.statusFlow().asLiveData()
    val levels = levelRepository.levelFlow().asLiveData()
}