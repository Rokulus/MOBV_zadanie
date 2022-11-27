package com.example.mobv_zadanie_procka.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mobv_zadanie_procka.R
import com.example.mobv_zadanie_procka.databinding.FragmentPubDetailBinding
import com.example.mobv_zadanie_procka.helpers.Injection
import com.example.mobv_zadanie_procka.helpers.PreferenceData
import com.example.mobv_zadanie_procka.ui.viewmodels.DetailViewModel

class PubDetailFragment : Fragment() {
    private lateinit var binding: FragmentPubDetailBinding
    private lateinit var viewModel: DetailViewModel
    private val navigationArgs: PubDetailFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(
            this,
            Injection.provideViewModelFactory(requireContext())
        ).get(DetailViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPubDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val x = PreferenceData.getInstance().getUserItem(requireContext())
        if ((x?.uid ?: "").isBlank()) {
            Navigation.findNavController(view).navigate(R.id.action_to_login)
            return
        }

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            model = viewModel
        }.also { bnd ->
            bnd.back.setOnClickListener { it.findNavController().popBackStack() }

            bnd.mapButton.setOnClickListener {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(
                            "geo:0,0,?q=" +
                                    "${viewModel.pub.value?.lat ?: 0}," +
                                    "${viewModel.pub.value?.lon ?: 0}" +
                                    "(${viewModel.pub.value?.name ?: ""}"
                        )
                    )
                )
            }
        }

        viewModel.loadPub(navigationArgs.id)
    }

}