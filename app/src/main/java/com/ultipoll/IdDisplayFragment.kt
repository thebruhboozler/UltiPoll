package com.ultipoll

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ultipoll.databinding.FragmentIdDisplayBinding

class IdDisplayFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentIdDisplayBinding

    private lateinit var pollId:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pollId = requireArguments().getString(ARG_ID).toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentIdDisplayBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.idDisplay.text = pollId;

        binding.confirm.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.FrameLayout,BoothFragment.newInstance(pollId)).commit()
            dismiss()
        }
    }

    companion object {
        private const val ARG_ID = "arg_id"

        fun newInstance(id: String): IdDisplayFragment {
            return IdDisplayFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_ID, id)
                }
            }
        }
    }
}