package com.vholodynskyi.assignment.ui.contactslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.vholodynskyi.assignment.api.contacts.toDbContact
import com.vholodynskyi.assignment.databinding.FragmentContactsListBinding
import com.vholodynskyi.assignment.di.GlobalFactory
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

open class ContactsListFragment : Fragment() {

    private val viewModel: ContactsListViewModel by viewModels<ContactsListViewModel> {
        GlobalFactory
    }

    private val contactAdapter: ContactAdapter by lazy {
        ContactAdapter(
            requireActivity(),
            this::onContactClicked
        )
    }

    private fun onContactClicked(id: Int) {
        findNavController()
            .navigate(ContactsListFragmentDirections.actionContactListToDetails(id))
    }

    private var binding: FragmentContactsListBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Creates a vertical Layout Manager
        return FragmentContactsListBinding.inflate(layoutInflater, container, false)
            .apply {
                contactList.layoutManager = LinearLayoutManager(context)
                contactList.adapter = contactAdapter
            }
            .also {
                binding = it
            }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getContactList()
        setData()
        binding?.swipeRefresh?.setOnRefreshListener {
            viewModel.getContactList()
            setData()
        }

        setAdapterData()
    }

    private fun setData() {
        lifecycleScope.launch {
            viewModel.uiState.collect { contact ->
                val dbContacts = contact.results?.map { it.toDbContact() }
                dbContacts?.let { viewModel.addAllContactsDatabase(it) }
                binding?.progress?.visibility = View.GONE
                binding?.swipeRefresh?.isRefreshing = false
            }
        }
    }

    private fun setAdapterData() {
        lifecycleScope.launch {
            viewModel.contacts.collect {
                contactAdapter.items = it
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
