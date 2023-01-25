package com.vholodynskyi.assignment.ui.contactslist

import android.R
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vholodynskyi.assignment.api.contacts.toDbContact
import com.vholodynskyi.assignment.databinding.FragmentContactsListBinding
import com.vholodynskyi.assignment.di.GlobalFactory
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
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
                ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                    override fun onMove(
                        v: RecyclerView,
                        h: RecyclerView.ViewHolder,
                        t: RecyclerView.ViewHolder
                    ) = false

                    override fun onSwiped(h: RecyclerView.ViewHolder, dir: Int) =
                        contactAdapter.removeAt(h.absoluteAdapterPosition)

                    override fun onChildDraw(
                        c: Canvas,
                        recyclerView: RecyclerView,
                        viewHolder: RecyclerView.ViewHolder,
                        dX: Float,
                        dY: Float,
                        actionState: Int,
                        isCurrentlyActive: Boolean
                    ) {
                        RecyclerViewSwipeDecorator.Builder(
                            c,
                            recyclerView,
                            viewHolder,
                            dX,
                            dY,
                            actionState,
                            isCurrentlyActive
                        ).addSwipeLeftActionIcon(R.drawable.ic_delete)
                            .create()
                            .decorate()

                        super.onChildDraw(
                            c,
                            recyclerView,
                            viewHolder,
                            dX,
                            dY,
                            actionState,
                            isCurrentlyActive
                        )
                    }
                }).attachToRecyclerView(contactList)
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
                contactAdapter.items = it.reversed()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
