package com.ultipoll

import android.graphics.Color
import android.os.Bundle
import android.provider.CalendarContract.Colors
import android.text.Html
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentTransaction
import com.ultipoll.databinding.FragmentPollSetUpBinding
import com.ultipoll.databinding.FragmentSplashBinding


class PollSetUpFragment : Fragment() {


    private lateinit var binding: FragmentPollSetUpBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPollSetUpBinding.inflate( inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.AddOptionBtn.setOnClickListener{
            val option = binding.Option.text.toString()
            if(option.isBlank()) return@setOnClickListener;
            val latoTf = ResourcesCompat.getFont(requireContext(), R.font.lato_bold)
            val textView = TextView(requireContext()).apply {
                text = option
                setTextColor(Color.BLACK)
                typeface = latoTf
                setTextSize(TypedValue.COMPLEX_UNIT_SP,22f)
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f
                )
            }
            val imgBtn = ImageButton(requireContext()).apply {
                setImageResource(R.drawable.icon_minus)
                background=null
                setPadding(0, 0, 0, 0)
                scaleType = ImageView.ScaleType.FIT_CENTER
                layoutParams = ViewGroup.LayoutParams(
                    (25 * context.resources.displayMetrics.density).toInt(),
                    (25 * context.resources.displayMetrics.density).toInt()
                )
            }

            val itemLayout = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER_VERTICAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(10,10,10,10)
                }
            }
            itemLayout.addView(textView)
            itemLayout.addView(imgBtn)
            binding.OptionsMenu.addView(itemLayout)
            imgBtn.setOnClickListener{
                binding.OptionsMenu.removeView(itemLayout)
            }
            binding.Option.text.clear()
        }
        binding.method.setOnClickListener{
            val transition = parentFragmentManager.beginTransaction()
            val methodPickerFragment = MethodPickerFragment()
            transition.replace(R.id.FrameLayout, methodPickerFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit()
        }

        val id = arguments?.getString("id")
        if(id == null) return;

        ApiCall().getFile(id){ content->
            val lines = content.split('\n')

            val titleRegex = Regex("""---Title:\s*(.+)""")
            val ballotTypeRegex = Regex("""---Ballot_Type:\s*(.+)""")
            val descriptionRegex = Regex("""---Description:\s*(.+)""")

            val title = titleRegex.find(lines[0])?.groupValues?.get(1) ?: ""
            val ballotType = ballotTypeRegex.find(lines[1])?.groupValues?.get(1) ?: ""
            val description = descriptionRegex.find(lines[2])?.groupValues?.get(1) ?: ""


            binding.method.text = Html.fromHtml("<u>$title</u>", Html.FROM_HTML_MODE_LEGACY)
            binding.BallotType.text = ballotType
            binding.description.text = description
        }

    }
}