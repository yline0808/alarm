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
import net.ddns.myapplication.table.Song;

import java.util.ArrayList;

public class VibrationListAdapter extends RecyclerView.Adapter<VibrationListAdapter.SoundListViewHolder> implements Filterable {
    ArrayList<Song> songs;
    ArrayList<Song> songsAll;
    private static int lastCheckedPos = -1;

    private OnItemClickListener mListener = null;

    public VibrationListAdapter(ArrayList<Song> songs) {
        this.songs = songs;
        this.songsAll = new ArrayList<>(songs);
    }

    public interface OnItemClickListener{
        void onItemClick(View v, int pos, Song s);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

    @NonNull
    @Override
    public SoundListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_sound_vibration, parent, false);

        SoundListViewHolder soundListViewHolder = new SoundListViewHolder(context, view);

        return soundListViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SoundListViewHolder holder, int position) {
        String text = songs.get(position).getTitle();
        holder.textView.setText(text);
        holder.radioButton.setChecked(lastCheckedPos == position);
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<Song> filteredSongs = new ArrayList<>();

                if(constraint == null || constraint.length() == 0){
                    filteredSongs.addAll(songsAll);
                }else{
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for(Song song:songsAll){
                        if(song.getTitle().toLowerCase().contains(filterPattern)){
                            filteredSongs.add(song);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filteredSongs;

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                songs.clear();
                songs.addAll((ArrayList) results.values);
                notifyDataSetChanged();
            }
        };
    }

    public class SoundListViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public RadioButton radioButton;
        public View itemView;

        Song selSong = new Song();

        public SoundListViewHolder(Context context, View itemView){
            super(itemView);
            this.itemView = itemView;

            textView = itemView.findViewById(R.id.txtvName);
            radioButton = itemView.findViewById(R.id.radioBtn);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastCheckedPos = getAdapterPosition();
                    notifyDataSetChanged();
                    radioButton.setChecked(true);
                    selSong = songs.get(lastCheckedPos);

                    if(mListener != null){
                        mListener.onItemClick(v, lastCheckedPos, selSong);
                    }
                }
            });
        }
    }
}
