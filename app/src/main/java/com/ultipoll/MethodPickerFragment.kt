package com.ultipoll

import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentTransaction
import com.ultipoll.databinding.FragmentMethodPickerBinding

class MethodPickerFragment : Fragment() {


    private lateinit var binding: FragmentMethodPickerBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMethodPickerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val latoTf = ResourcesCompat.getFont(requireContext(), R.font.lato_bold)
        ApiCall().getFileDescriptors{ files ->
            for (file in files) {
                var path = file.path.toString().split('/')
                if (path[0] != "scripts" || path.size == 1) continue;

                val textView = TextView(requireContext()).apply {
                    text = sanitizeName(path[1])
                    setTextColor(Color.BLACK)
                    typeface = latoTf
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, 22f)
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                }

                var id = file.url.split("/").toMutableList().last()
                textView.setOnClickListener {
                    val bundle = Bundle()
                    bundle.putString(
                        "id",
                        id
                    )

                    val transition = parentFragmentManager.beginTransaction()
                    val pollSetUpFragment = PollSetUpFragment()
                    pollSetUpFragment.arguments = bundle
                    transition.replace(R.id.FrameLayout, pollSetUpFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit()
                }

                binding.methods.addView(textView)
            }
        }

    }

    fun sanitizeName(snakeCase: String): String {
        var words = snakeCase.split('_', '.').toMutableList()
        words.removeAt(words.size - 1)
        var final = ""
        for (word in words) {
            final += "$word "
        }
        return final
    }
}