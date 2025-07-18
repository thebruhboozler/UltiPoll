package com.ultipoll

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ultipoll.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {

    private lateinit var binding: FragmentSplashBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigation).visibility =
            View.VISIBLE
        binding.setUpPollBtn.setOnClickListener{
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigation).visibility =
                View.GONE
            val bundle = Bundle()
            bundle.putString("id", "387a072a77604a0b0175fd48b10b444b4561ab6e")

            val transition = parentFragmentManager.beginTransaction()
            val pollSetUpFragment = PollSetUpFragment()
            pollSetUpFragment.arguments = bundle
            transition.replace(R.id.FrameLayout,pollSetUpFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()
        }
        binding.participateBtn.setOnClickListener {
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigation).visibility =
                View.GONE
            IdInputFragment().show(parentFragmentManager,"IdInput")
        }
    }
}