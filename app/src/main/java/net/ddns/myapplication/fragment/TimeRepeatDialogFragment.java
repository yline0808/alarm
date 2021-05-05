package net.ddns.myapplication.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import net.ddns.myapplication.R;

public class TimeRepeatDialogFragment extends DialogFragment {
    public interface OnCompleteListener{
        void onInputedData(String minute, String count);
    }

    private OnCompleteListener mCallback;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try{
            mCallback = (OnCompleteListener) context;
        }catch (Exception e){
            Log.e("onAttach", e.toString());
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_repeat_count, null);
        builder.setView(view);

        final NumberPicker npMinute = (NumberPicker)view.findViewById(R.id.npMinute);
        final NumberPicker npCount = (NumberPicker)view.findViewById(R.id.npCount);
        final Button btnOk = (Button)view.findViewById(R.id.btnOk);
        final Button btnCancel = (Button)view.findViewById(R.id.btnCancel);

        npMinute.setMinValue(1);
        npMinute.setMaxValue(5);
        npMinute.setWrapSelectorWheel(false);
        npMinute.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return formatMinute(value);
            }
        });

        npCount.setMinValue(1);
        npCount.setMaxValue(4);
        npCount.setWrapSelectorWheel(false);
        npCount.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return formatCount(value);
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                mCallback.onInputedData(formatMinute(npMinute.getValue()), formatCount(npCount.getValue()));
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return builder.create();
    }

    private String formatMinute(int value){
        switch(value){
            case 1:
                return "5";
            case 2:
                return "10";
            case 3:
                return "15";
            case 4:
                return "30";
            case 5:
                return "60";
            default:
                return "-1";
        }
    }

    private String formatCount(int value){
        switch(value){
            case 1:
                return "3";
            case 2:
                return "5";
            case 3:
                return "10";
            case 4:
                return getResources().getString(R.string.repeat_infinite);
            default:
                return "-1";
        }
    }
}
