package com.vholodynskyi.assignment.data.datasource

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vholodynskyi.assignment.data.datasource.ContactsDao
import com.vholodynskyi.assignment.domain.model.DbContact

@Database(entities = [DbContact::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): ContactsDao
}
