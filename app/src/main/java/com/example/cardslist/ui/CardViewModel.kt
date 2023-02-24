package com.example.cardslist.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cardslist.data.managers.CardsManager
import com.example.cardslist.data.model.Card
import com.example.cardslist.data.model.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardViewModel @Inject constructor(private val cardsManager: CardsManager) :
    ViewModel() {
    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    init {
        fetchCards()
    }

    fun fetchCards() {
        viewModelScope.launch {
            cardsManager.getCardFilteredList().collect { result ->
                when(result.status){
                    Result.Status.SUCCESS -> {
                        _state.update {
                            it.copy(cardList = result.data as? List<Card>, inProgress = false)
                        }
                    }
                    Result.Status.IN_PROGRESS -> {
                        _state.update {
                            it.copy(inProgress = true)
                        }
                    }
                    Result.Status.ERROR -> {
                        _state.update {
                            it.copy(inProgress = false, errorMessage = result.error?.status_message ?: result.message)
                        }
                    }
                }
            }
        }
    }


    data class UiState(
        val cardList: List<Card>? = null,
        val inProgress: Boolean? = null,
        val errorMessage: String? = null
    )
}