package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.entity.ShoppingItemEntity
import com.example.data.repository.ShoppingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ShoppingListUiState(
    val pendingItems: List<ShoppingItemEntity> = emptyList(),
    val completedItems: List<ShoppingItemEntity> = emptyList(),
    val totalCount: Int = 0
)

class ShoppingViewModel(
    private val shoppingRepository: ShoppingRepository
) : ViewModel() {

    private val _inputText = MutableStateFlow("")
    val inputText: StateFlow<String> = _inputText.asStateFlow()

    private val _showClearDialog = MutableStateFlow(false)
    val showClearDialog: StateFlow<Boolean> = _showClearDialog.asStateFlow()

    val shoppingState: StateFlow<ShoppingListUiState> = shoppingRepository.allItems
        .map { list ->
            val pending = list.filter { !it.isCompleted }
            val completed = list.filter { it.isCompleted }
            ShoppingListUiState(
                pendingItems = pending,
                completedItems = completed,
                totalCount = list.size
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ShoppingListUiState()
        )

    fun onInputTextChange(text: String) {
        _inputText.value = text
    }

    fun addItem() {
        val text = _inputText.value.trim()
        if (text.isNotEmpty()) {
            viewModelScope.launch {
                shoppingRepository.addItem(text)
                _inputText.value = ""
            }
        }
    }

    fun toggleItem(item: ShoppingItemEntity) {
        viewModelScope.launch {
            shoppingRepository.toggleItem(item)
        }
    }

    fun deleteItem(id: Long) {
        viewModelScope.launch {
            shoppingRepository.deleteItem(id)
        }
    }

    fun setShowClearDialog(show: Boolean) {
        _showClearDialog.value = show
    }

    fun clearAll() {
        viewModelScope.launch {
            shoppingRepository.clearAll()
            _showClearDialog.value = false
        }
    }

    fun clearCompleted() {
        viewModelScope.launch {
            shoppingRepository.clearCompleted()
        }
    }

    companion object {
        fun provideFactory(shoppingRepository: ShoppingRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ShoppingViewModel(shoppingRepository) as T
                }
            }
    }
}
