package com.vholodynskyi.assignment.repository

import com.vholodynskyi.assignment.db.contacts.ContactsDao
import com.vholodynskyi.assignment.db.contacts.DbContact

class ContactsRepositoryImpl(private val database: ContactsDao) : ContactsRepository {
    override suspend fun addAllContacts(contact: List<DbContact>) {
        database.addAll(contact)
    }

    override suspend fun deleteAll() {
        database.deleteAll()
    }

    override suspend fun deleteById(id: Int) {
        database.deleteById(id)
    }

    override suspend fun getContacts(): List<DbContact> {
        return database.getContacts()
    }

    override suspend fun getContactById(id: Int): DbContact {
        return database.getContactById(id)
    }
}
