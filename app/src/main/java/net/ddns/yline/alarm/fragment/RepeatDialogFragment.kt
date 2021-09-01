package net.ddns.yline.alarm.fragment

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import net.ddns.yline.alarm.R
import net.ddns.yline.alarm.databinding.DialogRepeatCountBinding
import java.lang.Exception

class RepeatDialogFragment : DialogFragment() {
    interface OnCompleteListener {
        fun onInputedData(minute: String?, count: String?)
    }

    private var mCallback: OnCompleteListener? = null
    private lateinit var binding:DialogRepeatCountBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            mCallback = context as OnCompleteListener
        } catch (e: Exception) {
            Log.e("onAttach", e.toString())
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DialogRepeatCountBinding.inflate(inflater, container, false)
        val view = binding.root

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.apply {
            buttonRepeatSet
        }

        return view
    }

//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        val binding = DialogRepeatCountBinding.inflate(LayoutInflater.from(parentFragment.context), parentFragment, false)
//
//        val builder =
//
//        val builder = AlertDialog.Builder(activity)
//        val inflater = activity!!.layoutInflater
//        val view: View = inflater.inflate(R.layout.dialog_repeat_count, null)
//        builder.setView(view)
//        val npMinute = view.findViewById<View>(R.id.npMinute) as NumberPicker
//        val npCount = view.findViewById<View>(R.id.npCount) as NumberPicker
//        val btnOk = view.findViewById<View>(R.id.btnOk) as Button
//        val btnCancel = view.findViewById<View>(R.id.btnCancel) as Button
//        npMinute.minValue = 1
//        npMinute.maxValue = 5
//        npMinute.wrapSelectorWheel = false
//        npMinute.setFormatter { value -> formatMinute(value) }
//        npCount.minValue = 1
//        npCount.maxValue = 4
//        npCount.wrapSelectorWheel = false
//        npCount.setFormatter { value -> formatCount(value) }
//        btnOk.setOnClickListener {
//            dismiss()
//            mCallback!!.onInputedData(formatMinute(npMinute.value), formatCount(npCount.value))
//        }
//        btnCancel.setOnClickListener { dismiss() }
//        return builder.create()
//    }

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
