package com.vholodynskyi.assignment.ui.contactslist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vholodynskyi.assignment.api.contacts.ApiContactResponse
import com.vholodynskyi.assignment.db.contacts.DbContact
import com.vholodynskyi.assignment.di.GlobalFactory
import com.vholodynskyi.assignment.repository.ContactsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ContactsListViewModel(
    private val repository: ContactsRepository
) : ViewModel() {

//    private val _uiState
//        get() = MutableStateFlow(ApiContactResponse(emptyList()))  when write in this was emails not loaded :(
//    val uiState: StateFlow<ApiContactResponse> = _uiState.asStateFlow()

    private val _uiState = MutableStateFlow(ApiContactResponse(emptyList()))
    val uiState: StateFlow<ApiContactResponse> = _uiState.asStateFlow()

    fun getContactList() {
        viewModelScope.launch {
            _uiState.value = GlobalFactory.service.getContacts()
        }
    }

    fun addAllContactsDatabase(contact: List<DbContact>) {
        viewModelScope.launch {
            try {
                repository.deleteAll()
                repository.addAllContacts(contact)
//                GlobalFactory.db.userDao().addAll(contact) // what if write this way without repository
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
