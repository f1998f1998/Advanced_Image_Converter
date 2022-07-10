package com.aic.advancedimageconverter.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.aic.advancedimageconverter.R
import com.aic.advancedimageconverter.databinding.FragmentHomeBinding
import com.aic.advancedimageconverter.ui.conversions.ConversionsViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        val viewmodel = ConversionsViewModel.instance!!

        binding.start.setOnClickListener {
            viewmodel.convert(context)
        }

        binding.stop.setOnClickListener {
            viewmodel.stop()
        }




        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}