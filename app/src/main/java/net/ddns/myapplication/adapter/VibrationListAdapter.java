package net.ddns.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.ddns.myapplication.R;
import net.ddns.myapplication.table.Vibration;

import java.util.ArrayList;

public class VibrationListAdapter extends RecyclerView.Adapter<VibrationListAdapter.VibrationListViewHolder> implements Filterable {
    ArrayList<Vibration> vibrations;
    ArrayList<Vibration> vibrationsAll;
    private static int lastCheckedPos = -1;

    private OnItemClickListener mListener = null;

    public VibrationListAdapter(ArrayList<Vibration> vibrations) {
        this.vibrations = vibrations;
        this.vibrationsAll = new ArrayList<>(vibrations);
        lastCheckedPos = -1;
    }

    public interface OnItemClickListener{
        void onItemClick(View v, int pos, Vibration vib);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

    @NonNull
    @Override
    public VibrationListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_sound_vibration, parent, false);

        VibrationListViewHolder vibrationListViewHolder = new VibrationListViewHolder(context, view);

        return vibrationListViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull VibrationListViewHolder holder, int position) {
        String text = vibrations.get(position).getName();
        holder.textView.setText(text);
        holder.radioButton.setChecked(lastCheckedPos == position);
    }

    @Override
    public int getItemCount() {
        return vibrations.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<Vibration> filteredVibrations = new ArrayList<>();

                if(constraint == null || constraint.length() == 0){
                    filteredVibrations.addAll(vibrationsAll);
                }else{
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for(Vibration vibration:vibrationsAll){
                        if(vibration.getName().toLowerCase().contains(filterPattern)){
                            filteredVibrations.add(vibration);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filteredVibrations;

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                vibrations.clear();
                vibrations.addAll((ArrayList) results.values);
                notifyDataSetChanged();
            }
        };
    }

    public class VibrationListViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public RadioButton radioButton;
        public View itemView;

        Vibration selVibration = new Vibration();

        public VibrationListViewHolder(Context context, View itemView){
            super(itemView);
            this.itemView = itemView;

            textView = itemView.findViewById(R.id.txtvName);
            radioButton = itemView.findViewById(R.id.radioBtn);
            radioButton.setChecked(false);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastCheckedPos = getAdapterPosition();
                    notifyDataSetChanged();
                    radioButton.setChecked(true);
                    selVibration = vibrations.get(lastCheckedPos);

                    if(mListener != null){
                        mListener.onItemClick(v, lastCheckedPos, selVibration);
                    }
                }
            });
        }
    }
}
