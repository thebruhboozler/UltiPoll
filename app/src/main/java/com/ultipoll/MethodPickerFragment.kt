package com.ultipoll

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ultipoll.databinding.FragmentMethodPickerBinding

class MethodPickerFragment : Fragment() {


    private lateinit var binding: FragmentMethodPickerBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMethodPickerBinding.inflate(inflater, container,false)
        return binding.root
    }


}