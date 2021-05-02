package net.ddns.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.ddns.myapplication.R;
import net.ddns.myapplication.table.Song;

import java.util.ArrayList;

public class SoundListAdapter extends RecyclerView.Adapter<SoundListAdapter.SoundListViewHolder> implements Filterable {
    ArrayList<Song> songs;
    ArrayList<Song> songsAll;
    private static int lastCheckedPos = -1;

    public SoundListAdapter(ArrayList<Song> songs) {
        this.songs = songs;
        this.songsAll = new ArrayList<>(songs);
    }

    private onSongListener mListener;
    public void setOnClickListener(onSongListener listener){
        mListener = listener;
    }

//    public void dataSetChanged(ArrayList<Song> exampleList){
//        songs = exampleList;
//        notifyDataSetChanged();
//    }

    @NonNull
    @Override
    public SoundListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_sound, parent, false);

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

        public SoundListViewHolder(Context context, View itemView){
            super(itemView);
            this.itemView = itemView;

            textView = itemView.findViewById(R.id.txtvSound);
            radioButton = itemView.findViewById(R.id.radioBtnSound);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastCheckedPos = getAdapterPosition();
                    notifyDataSetChanged();
                    radioButton.setChecked(true);
                    String strText = textView.getText().toString();
                    Toast.makeText(context, strText, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public interface onSongListener{
        void onSongClicked(int position);
    }
}
