package com.vholodynskyi.assignment.domain.repository

import com.vholodynskyi.assignment.domain.model.DbContact

interface ContactsRepository {
    suspend fun addAllContacts(contact: List<DbContact>)
    suspend fun deleteAll()
    suspend fun deleteById(id: Int)
    suspend fun getContacts(): List<DbContact>
    suspend fun getContactById(id: Int): DbContact
}
