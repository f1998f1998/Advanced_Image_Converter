package com.aic.advancedimageconverter.ui.conversions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.aic.advancedimageconverter.databinding.FragmentConversionsBinding

class ConversionsFragment : Fragment() {

    private var _binding: FragmentConversionsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val conversionsViewModel = ConversionsViewModel.instance!!

        _binding = FragmentConversionsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        conversionsViewModel.total.observe(viewLifecycleOwner) {
            binding.total.text = it.toString()
            binding.progressBar.max = it
        }
        conversionsViewModel.done.observe(viewLifecycleOwner) {
            binding.converted.text = it.toString()
            binding.remaining.text = (conversionsViewModel.total.value!! - it).toString()
            binding.progressBar.progress = it
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}