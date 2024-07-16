package io.github.casl0.mediauploader.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.casl0.mediauploader.models.DomainUpdateHistory
import io.github.casl0.mediauploader.repository.UpdateHistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * UI状態
 *
 * @property updateHistory メディア更新履歴
 */
internal data class HomeUiState(
    val updateHistory: List<DomainUpdateHistory> = listOf(),
)

/**
 * ホーム画面のビジネスロジックを扱うViewModel
 *
 * @property updateHistoryRepository メディア更新履歴のリポジトリ
 * @property _uiState UI状態（可変）
 * @property uiState UI状態（非可変）
 */
@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val updateHistoryRepository: UpdateHistoryRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())

    val uiState: Flow<HomeUiState> get() = _uiState

    init {
        viewModelScope.launch {
            updateHistoryRepository.getAllHistory().collect { newValue ->
                _uiState.update { it.copy(updateHistory = newValue) }
            }
        }
    }
}
