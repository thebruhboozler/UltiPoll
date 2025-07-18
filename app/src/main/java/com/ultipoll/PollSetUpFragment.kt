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
import androidx.core.view.children
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import com.google.firebase.database.FirebaseDatabase
import com.ultipoll.databinding.FragmentPollSetUpBinding
import com.ultipoll.databinding.FragmentSplashBinding
import kotlin.random.Random
import kotlin.random.nextInt


class PollSetUpFragment : Fragment() {

    val database =
        FirebaseDatabase.getInstance("https://ultipoll-default-rtdb.europe-west1.firebasedatabase.app")
    val ref = database.getReference("polls")
    val options = mutableListOf<String>()
    private val vm: PollViewModel by activityViewModels()
    private lateinit var script: String


    private var participate: Boolean = false;
    private lateinit var binding: FragmentPollSetUpBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPollSetUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.AddOptionBtn.setOnClickListener { addOption() }
        binding.method.setOnClickListener {
            val transition = parentFragmentManager.beginTransaction()
            val methodPickerFragment = MethodPickerFragment()
            transition.replace(R.id.FrameLayout, methodPickerFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit()
        }

        val id = arguments?.getString("id")
        if (id == null) return;
        ApiCall().getFile(id) { content ->
            script = content
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

        binding.participateBtn.setOnClickListener {
            if (participate) {
                binding.participateBtn.setBackgroundResource(R.drawable.check_box)
            } else {
                binding.participateBtn.setBackgroundResource(R.drawable.check_box_filled)
            }
            participate = !participate
        }

        binding.finish.setOnClickListener { onFinishClicked() }
    }

    fun addOption() {
        val option = binding.Option.text.toString()
        if (option.isBlank()) return;
        val latoTf = ResourcesCompat.getFont(requireContext(), R.font.lato_bold)
        val textView = TextView(requireContext()).apply {
            text = option
            setTextColor(Color.BLACK)
            typeface = latoTf
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 22f)
            layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            )
        }
        val imgBtn = ImageButton(requireContext()).apply {
            setImageResource(R.drawable.icon_minus)
            background = null
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
                setMargins(10, 10, 10, 10)
            }
        }
        itemLayout.addView(textView)
        itemLayout.addView(imgBtn)
        binding.OptionsMenu.addView(itemLayout)
        imgBtn.setOnClickListener {
            binding.OptionsMenu.removeView(itemLayout)
            options.remove(option)
        }
        options.add(option)
        binding.Option.text.clear()
    }

    fun onFinishClicked() {
        fun generateRandomID(onIdGenerated: (String) -> Unit) {
            fun genRandId(len: Int): String {
                var res = "";
                val acceptableChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
                for (i in 1..len) {
                    res += acceptableChars[Random.nextInt(0, acceptableChars.length)];
                }
                return res;
            }
            ref.get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val polls = snapshot.children.map { it.key }
                    var id = "";
                    do {
                        id = genRandId(5);
                    } while (polls.contains(id))
                    onIdGenerated(id)
                }
            }
        }

        generateRandomID { id ->
            val ballotType = binding.BallotType.text.toString()
            val basePoll = mapOf(
                "title" to binding.Title.text.toString(),
                "type" to ballotType,
                "winner" to -1,
                "participants" to binding.participantNum.text.toString().toInt(),
                "options" to options.filterNotNull(),
                "numOfVotesCast" to 0
            )
            val poll: Map<String, Any>;


            if (ballotType == "Ranked" || ballotType == "Point") {
                var votes = mutableMapOf<String, Any?>()
                val participantCount = binding.participantNum.text.toString().toInt()
                for (i in 0..participantCount) {
                    votes.put(i.toString(), "No vote present yet");
                }
                poll = basePoll + mapOf<String, Any>("votes" to votes.toMap())
            } else {
                val optionEntries: Map<String, Any> =
                    options.filterNotNull().associate { it to 0 }
                poll = basePoll + mapOf<String, Any>("votes" to optionEntries)
            }


            ref.get().addOnSuccessListener {
                val newKey = "poll_$id"
                ref.child(newKey).setValue(poll);
            }
            vm.startLuaRunner(id,script)
            IdDisplayFragment.newInstance(id,participate).show(parentFragmentManager, "IdDisplay")

        }
    }

}