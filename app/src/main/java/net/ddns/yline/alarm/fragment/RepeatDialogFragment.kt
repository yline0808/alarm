package net.ddns.yline.alarm.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import net.ddns.yline.alarm.R
import net.ddns.yline.alarm.databinding.DialogRepeatCountBinding

class RepeatDialogFragment : DialogFragment() {
//    alarm repeat set button listener
    private lateinit var mCallback: OnCompleteListener
//    binding
    private lateinit var binding:DialogRepeatCountBinding

//    alarm repeat set button interfase
    interface OnCompleteListener {
        fun onButtonRepeatSetClicked(minute: String?, count: String?)
    }

//    ===== override fun =====
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DialogRepeatCountBinding.inflate(inflater, container, false)

        binding.apply {
            pickerRepeatSelect.run {
                minValue = 1
                maxValue = 4
                wrapSelectorWheel = false
                setFormatter { formatCount(it) }
            }
            pickerGapSelect.run {
                minValue = 1
                maxValue = 5
                wrapSelectorWheel = false
                setFormatter{ formatMinute(it) }
            }
            buttonRepeatSet.setOnClickListener{
                dismiss()
                mCallback.onButtonRepeatSetClicked(formatMinute(pickerGapSelect.value), formatCount(pickerRepeatSelect.value))
            }
            buttonRepeatCancel.setOnClickListener { dismiss() }
        }

        return binding.root
    }

//    ===== function =====
    fun setButtonClickListener(buttonClickListener:OnCompleteListener) {
        this.mCallback = buttonClickListener
    }

//    minute picker formatter
    private fun formatMinute(value: Int): String {
        return when (value) {
            1 -> "5"
            2 -> "10"
            3 -> "15"
            4 -> "30"
            5 -> "60"
            else -> "-1"
        }
    }

//    repeat count picker formatter
    private fun formatCount(value: Int): String {
        return when (value) {
            1 -> "3"
            2 -> "5"
            3 -> "10"
            4 -> resources.getString(R.string.repeat_infinite)
            else -> "-1"
        }
    }
}
