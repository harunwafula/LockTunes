package com.astrofittechnologies.intsimbi;

import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.astrofittechnologies.intsimbi.models.Song;
import com.astrofittechnologies.intsimbi.services.MediaService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Boolean isPlaying = false;
    private FirebaseFirestore db;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        MediaService mediaService = MediaService.getInstance(requireContext());

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.track_list_rv);
        ImageView playPause = view.findViewById(R.id.play_toggle);
        playPause.setOnClickListener(v -> {

            if(mediaService.getAwake()){
                mediaService.togglePause();
            }else {
                try {
                    mediaService.streamSong("https://firebasestorage.googleapis.com/v0/b/locktunes.appspot.com/o/backtrapping.mp3?alt=media&token=65e86764-1203-4851-9907-5e4c2d67a9c1");

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            isPlaying = !isPlaying;
            if(isPlaying){
                playPause.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24);

            }else{
                playPause.setImageResource(R.drawable.ic_baseline_play_circle_filled_24);

            }

        });

        db.collection("Songs").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    List<Song> songs = new ArrayList<>();
                    List<String> songList = new ArrayList<>();

                    for(QueryDocumentSnapshot document : task.getResult()){
                        Song song = document.toObject(Song.class);
                        songs.add(song);
                        songList.add(song.getName());
                    }


                    TrackListAdapter listAdapter = new TrackListAdapter(songList);
                    listAdapter.setOnClickListener(new TrackListAdapter.OnClickListener() {
                        @Override
                        public void onSongPick(View view, int position) {
                            playPause.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24);
                            isPlaying = true;
                            String url = "";
                            if(position == 0){
                                url = "https://firebasestorage.googleapis.com/v0/b/locktunes.appspot.com/o/backtrapping.mp3?alt=media&token=65e86764-1203-4851-9907-5e4c2d67a9c1";

                            }else {
                                url = "https://firebasestorage.googleapis.com/v0/b/locktunes.appspot.com/o/afterchristmas.mp3?alt=media&token=a1d81366-2fb8-4197-a7a0-a49b0f494720";
                            }

                            try {
                                mediaService.streamSong(url);


                            }catch (IOException exception) {
                                Log.e("Play error","Error playing music");
                            }
                        }
                    });
                    recyclerView.setAdapter(listAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                }else{
                    Toast.makeText(requireContext(), "Read issues ", Toast.LENGTH_LONG).show();

                }
            }
        });


        return view;
    }


}