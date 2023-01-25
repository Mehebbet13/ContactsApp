package com.vholodynskyi.assignment.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vholodynskyi.assignment.db.contacts.DbContact
import com.vholodynskyi.assignment.repository.ContactsRepository
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val repository: ContactsRepository
) : ViewModel() {
    private val _contact = MutableLiveData<DbContact>()
    val contact: LiveData<DbContact>
        get() = _contact

    fun getContactInfoFromDb(id: Int) {
        viewModelScope.launch {
            try {
                _contact.value = repository.getContactById(id)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteContact(id: Int, callback: () -> Unit) {
        viewModelScope.launch {
            try {
                repository.deleteById(id)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                callback.invoke()
            }
        }
    }
}
