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
    private  var participate: Boolean =false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pollId = requireArguments().getString(ARG_ID).toString()
        participate = requireArguments().getBoolean(ARG_PARTICIPATE)
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
            if(participate)
                parentFragmentManager.beginTransaction().replace(R.id.FrameLayout,
                    BoothFragment.newInstance(pollId)).commit()
            else
                parentFragmentManager.beginTransaction().replace(R.id.FrameLayout,
                    WaitingRoomFragment.newInstance(pollId)).commit()
            dismiss()
        }
    }

    companion object {
        private const val ARG_ID = "arg_id"
        private const val ARG_PARTICIPATE = "participate"

        fun newInstance(id: String , participate: Boolean): IdDisplayFragment {
            return IdDisplayFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_ID, id)
                    putBoolean(ARG_PARTICIPATE ,participate)
                }
            }
        }
    }
}