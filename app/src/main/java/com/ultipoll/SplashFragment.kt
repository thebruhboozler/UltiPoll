package com.ultipoll

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
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
        binding.setUpPollBtn.setOnClickListener{
            val bundle = Bundle()
            bundle.putString("id", "f839e79bd054717ac890d65352f66fc847825358")

            val transition = parentFragmentManager.beginTransaction()
            val pollSetUpFragment = PollSetUpFragment()
            pollSetUpFragment.arguments = bundle
            transition.replace(R.id.FrameLayout,pollSetUpFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()
        }
    }
}