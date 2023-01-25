package com.vholodynskyi.assignment.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.squareup.picasso.Picasso
import com.vholodynskyi.assignment.databinding.FragmentDetailsBinding
import com.vholodynskyi.assignment.db.contacts.DbContact
import com.vholodynskyi.assignment.di.GlobalFactory
import kotlinx.coroutines.launch

open class DetailsFragment : Fragment() {
    var binding: FragmentDetailsBinding? = null

    private val detailsViewModel by viewModels<DetailsViewModel> { GlobalFactory } // why GlobalFactory is written
    private val args: DetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentDetailsBinding.inflate(layoutInflater, container, false)
            .also { binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        detailsViewModel.getContactInfoFromDb(args.id)
        lifecycleScope.launch {
            detailsViewModel.contact.observe(viewLifecycleOwner) { setData(it) }
        }
        deleteContact()
    }

    private fun setData(contact: DbContact) {
        binding?.apply {
            email.text = contact.email
            name.text =
                StringBuilder().append(contact.firstName).append(BLANK).append(contact.lastName)
            Picasso.get().load(contact.photo).into(photo)
        }
    }

    private fun deleteContact() {
        binding?.delete?.setOnClickListener {
            detailsViewModel.deleteContact(args.id) {
                findNavController().popBackStack()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    companion object {
        const val BLANK = " "
    }
}
