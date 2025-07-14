package com.ultipoll

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ultipoll.R
import com.ultipoll.databinding.FragmentIdInputBinding

class IdInputFragment : BottomSheetDialogFragment() {


    private lateinit var binding: FragmentIdInputBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIdInputBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.confirm.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.FrameLayout,
                    BoothFragment.newInstance(binding.idInput.text.toString()))
                .commit()
            dismiss()
        }
    }

}