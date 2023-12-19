package com.example.rhythmix.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rhythmix.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChoosePlaylistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChoosePlaylistFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    // TODO: Rename and change types of parameters

    public ChoosePlaylistFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChoosePlaylistFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChoosePlaylistFragment newInstance(String param1, String param2) {
        ChoosePlaylistFragment fragment = new ChoosePlaylistFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_choose_playlist, container, false);
    }
}