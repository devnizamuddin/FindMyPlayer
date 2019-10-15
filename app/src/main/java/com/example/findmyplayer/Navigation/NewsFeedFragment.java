package com.example.findmyplayer.Navigation;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.findmyplayer.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFeedFragment extends Fragment {

    private Context context;

    public NewsFeedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_news_feed, container, false);

        return view;
    }

}
