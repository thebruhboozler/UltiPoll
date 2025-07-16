package com.ultipoll

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ultipoll.databinding.FragmentResultBinding
import com.ultipoll.databinding.FragmentWaitingRoomBinding


class ResultFragment : Fragment() {


    private lateinit var binding: FragmentResultBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResultBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.winner.text= arguments?.getString(WINNER)
        binding.home.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.FrameLayout, SplashFragment()).commit()
        }
    }


    companion object {
        private const val WINNER = "WINNER"

        fun newInstance(winner: String): ResultFragment {
            return ResultFragment().apply {
                arguments = Bundle().apply {
                    putString(WINNER, winner)
                }
            }
        }
    }
}