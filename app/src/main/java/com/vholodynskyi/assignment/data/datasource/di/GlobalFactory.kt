package com.vholodynskyi.assignment.data.datasource.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.vholodynskyi.assignment.data.remote.ContactsService
import com.vholodynskyi.assignment.data.datasource.AppDatabase
import com.vholodynskyi.assignment.data.repository.ContactsRepositoryImpl
import com.vholodynskyi.assignment.presentation.contactslist.ContactsListViewModel
import com.vholodynskyi.assignment.presentation.details.DetailsViewModel

object GlobalFactory : ViewModelProvider.Factory {

    val service: ContactsService by lazy {
        RetrofitServicesProvider().contactsService
    }

    lateinit var db: AppDatabase

    fun init(context: Context) {
        db = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app-database"
        ).build()
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            ContactsListViewModel::class.java -> ContactsListViewModel(ContactsRepositoryImpl(db.userDao()))
            DetailsViewModel::class.java -> DetailsViewModel(ContactsRepositoryImpl(db.userDao()))
            else -> throw IllegalArgumentException("Cannot create factory for ${modelClass.simpleName}")
        } as T
    }
}
