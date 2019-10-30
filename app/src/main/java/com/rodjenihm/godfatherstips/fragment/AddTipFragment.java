package com.rodjenihm.godfatherstips.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.rodjenihm.godfatherstips.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddTipFragment extends Fragment {


    public AddTipFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_tip, container, false);

        Button buttonAddTip = view.findViewById(R.id.button_add_tip);
        buttonAddTip.setOnClickListener(v -> {

        });

        return view;
    }

}
