package com.astrofittechnologies.intsimbi;

import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.astrofittechnologies.intsimbi.models.Song;
import com.astrofittechnologies.intsimbi.models.SongList;
import com.astrofittechnologies.intsimbi.services.MediaService;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.UncheckedIOException;
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

    private TextView currSongName;

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

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        MediaService mediaService = MediaService.getInstance(requireContext());


        ImageView playPause = view.findViewById(R.id.play_toggle);
        ImageView currSongDp = view.findViewById(R.id.song_dp_img);
        BottomNavigationView nav = view.findViewById(R.id.bottom_navigation);
        db.collection("Songs").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()){
                    SongList songs = new SongList();
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                        Song song = documentSnapshot.toObject(Song.class);
                        songs.addSong(song);
                    }
                    Fragment content  = new LockedFragment();
                    Bundle args = new Bundle();
                    args.putSerializable(ARG_PARAM1, songs);
                    content.setArguments(args);

                    getChildFragmentManager().beginTransaction().replace(R.id.content_frame, content).commit();


                    nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                            Fragment content;

                            switch (item.getItemId()){
                                case R.id.unlocked:
                                    content = new UnlockedFragment();
                                    getChildFragmentManager().beginTransaction().replace(R.id.content_frame, content).commit();

                                    return true;
                                case R.id.search:
                                    content = new SearchFragment();
                                    getChildFragmentManager().beginTransaction().replace(R.id.content_frame, content).commit();

                                    return true;
                                case R.id.settings:
                                    content = new SettingsFragment();
                                    getChildFragmentManager().beginTransaction().replace(R.id.content_frame, content).commit();

                                    return true;
                                default:
                                    content = new LockedFragment();
                                    Bundle args = new Bundle();
                                    args.putSerializable(ARG_PARAM1, songs);
                                    content.setArguments(args);
                                    getChildFragmentManager().beginTransaction().replace(R.id.content_frame, content).commit();

                                    return true;
                            }



                        }
                    });
                    ConstraintLayout currController = view.findViewById(R.id.currently_playing_song);
                    mediaService.setController(currController);

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

                        //toggle the play pause icon
                        if(  mediaService.getIsPlaying()){
                            playPause.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24);

                        }else{
                            playPause.setImageResource(R.drawable.ic_baseline_play_circle_filled_24);

                        }

                    });
                }else{
                    Toast.makeText(requireContext(), "Read issues ", Toast.LENGTH_LONG).show();

                }
            }
        });





        return view;
    }

    private void switchContent() {

    }


}