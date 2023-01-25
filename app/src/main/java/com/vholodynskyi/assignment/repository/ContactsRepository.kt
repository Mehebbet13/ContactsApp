package com.vholodynskyi.assignment.repository

import com.vholodynskyi.assignment.db.contacts.DbContact

interface ContactsRepository {
    suspend fun addAllContacts(contact: List<DbContact>)
    suspend fun deleteAll()
    suspend fun getContacts(): List<DbContact>
    suspend fun getContactById(id: Int): DbContact
}
