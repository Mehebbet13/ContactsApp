package com.vholodynskyi.assignment.repository

import com.vholodynskyi.assignment.db.contacts.ContactsDao
import com.vholodynskyi.assignment.db.contacts.DbContact

class ContactsRepositoryImpl(private val database: ContactsDao) : ContactsRepository {
    override suspend fun addAllContacts(contact: List<DbContact>) {
        database.addAll(contact)
    }
}
