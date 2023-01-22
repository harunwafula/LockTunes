package com.astrofittechnologies.intsimbi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TrackListAdapter  extends RecyclerView.Adapter<TrackListAdapter.ViewHolder> {

    private final List<String> songs;
    private TrackListAdapter.OnClickListener onClickListener;

    public interface OnClickListener {
         void onSongPick(View view, int position);
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView songName, songArtist;
        private final ImageView songDp;





        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            songName = (TextView) view.findViewById(R.id.song_name_txt);
            songArtist = (TextView) view.findViewById(R.id.song_artist_txt);
            songDp = view.findViewById(R.id.song_dp_img);
        }

        public TextView getSongName() {
            return songName;
        }

        public TextView getSongArtist() {
            return songArtist;
        }

        public ImageView getSongDp() {return  songDp;}

        public void bind(final String song, final TrackListAdapter.OnClickListener listener, int position) {
            getSongName().setText(song);

//            String photoUrl = item.getDisplayImage();
//
//            CircularProgressDrawable progressDrawable = new CircularProgressDrawable(context);
//            if(photoUrl != null){
//                Glide.with(context).load(photoUrl).placeholder(progressDrawable).into(getDisplayImage());
//            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onSongPick(v, position );
                }
            });
        }
    }


    public TrackListAdapter(List<String> songs) {
        this.songs = songs;

    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public TrackListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.tracklist_item, viewGroup, false);

        return new TrackListAdapter.ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(TrackListAdapter.ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.bind(songs.get(position), onClickListener, position);


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return songs.size();
    }

    public void newData(String name){

        songs.add(name);
        notifyDataSetChanged();
    }

    public void setOnClickListener(TrackListAdapter.OnClickListener listener) {
        this.onClickListener= listener;
    }
    public void removeItem (int position){
        songs.remove(position);
        notifyDataSetChanged();
    }

    public List<String> getSongs() {

        List<String> songs = new ArrayList<>();
        songs.addAll(this.songs);
        return songs;
    }
}
