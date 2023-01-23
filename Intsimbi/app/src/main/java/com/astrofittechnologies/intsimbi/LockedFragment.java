package com.astrofittechnologies.intsimbi;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.astrofittechnologies.intsimbi.models.Song;
import com.astrofittechnologies.intsimbi.models.SongList;
import com.astrofittechnologies.intsimbi.services.MediaService;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LockedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LockedFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private SongList songs;
    private String mParam2;

    private FirebaseFirestore db;

    public LockedFragment() {
        // Required empty public constructor
    }



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param songs Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LockedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LockedFragment newInstance(SongList songs, String param2) {
        LockedFragment fragment = new LockedFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, songs);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            songs = (SongList) getArguments().getSerializable(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        db = FirebaseFirestore.getInstance();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_locked, container, false);
        MediaService mediaService = MediaService.getInstance(requireContext());
        RecyclerView recyclerView = view.findViewById(R.id.track_list_rv);

        if(songs != null){
            TrackListAdapter listAdapter = new TrackListAdapter(songs.getSongs(), requireContext());
            listAdapter.setOnClickListener(new TrackListAdapter.OnClickListener() {
                @Override
                public void onSongPick(View view, int position) {

                    Song song = songs.getSongs().get(position);
                    mediaService.setCurrSong(song.getName(), song.getUrl());
                    String url = song.getUrl();

                    try {
                        mediaService.streamSong(url);


                    }catch (IOException exception) {
                        Log.e("Play error","Error playing music");
                    }
                }
            });
            recyclerView.setAdapter(listAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        }



        return view;
    }
}