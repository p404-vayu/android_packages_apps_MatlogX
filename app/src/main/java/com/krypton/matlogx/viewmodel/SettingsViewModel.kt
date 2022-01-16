/*
 * Copyright (C) 2021 AOSP-Krypton Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.krypton.matlogx.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.krypton.matlogx.repo.SettingsRepository

import dagger.hilt.android.lifecycle.HiltViewModel

import javax.inject.Inject

import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
) : ViewModel() {

    private var _logcatBuffers = MutableLiveData<String>()
    val logcatBuffers: LiveData<String> = _logcatBuffers

    private var _logcatSizeLimit = MutableLiveData<Int>()
    val logcatSizeLimit: LiveData<Int> = _logcatSizeLimit

    init {
        viewModelScope.launch {
            settingsRepository.getLogcatBuffers().collectLatest {
                _logcatBuffers.value = it
            }
        }
        viewModelScope.launch {
            settingsRepository.getLogcatSizeLimit().collectLatest {
                _logcatSizeLimit.value = it
            }
        }
    }

    fun setLogcatBuffers(buffers: String) {
        viewModelScope.launch {
            settingsRepository.setLogcatBuffers(buffers)
        }
    }

    fun setLogcatSizeLimit(limit: Int) {
        viewModelScope.launch {
            settingsRepository.setLogcatSizeLimit(limit)
        }
    }
}