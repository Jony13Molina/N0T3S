package com.example.jonny.n0t3s.tabView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.jonny.n0t3s.R;

public class Messages extends Fragment {
    public Messages() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.messages_xmlfragment, container, false);
    }
}

