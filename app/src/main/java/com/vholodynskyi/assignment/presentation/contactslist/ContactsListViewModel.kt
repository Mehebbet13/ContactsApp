package com.vholodynskyi.assignment.presentation.contactslist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vholodynskyi.assignment.data.remote.dto.ApiContactResponse
import com.vholodynskyi.assignment.domain.model.DbContact
import com.vholodynskyi.assignment.data.datasource.di.GlobalFactory
import com.vholodynskyi.assignment.domain.repository.ContactsRepository
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

    private val _contacts = MutableStateFlow(emptyList<DbContact>())
    val contacts: StateFlow<List<DbContact>> = _contacts.asStateFlow()

    fun getContactList() {
        viewModelScope.launch {
            _uiState.value = GlobalFactory.service.getContacts()
        }
    }

    fun getAllContactsFromDB() {
        viewModelScope.launch {
            try {
                _contacts.value = repository.getContacts()
//                GlobalFactory.db.userDao().addAll(contact) // what if write this way without repository
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun addAllContactsDatabase(contact: List<DbContact>) {
        viewModelScope.launch {
            try {
                repository.addAllContacts(contact)
//                GlobalFactory.db.userDao().addAll(contact) // what if write this way without repository
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                getAllContactsFromDB()
                Log.i("db data", GlobalFactory.db.userDao().getContacts().toString())
            }
        }
    }
}
