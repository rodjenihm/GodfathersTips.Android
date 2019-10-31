package com.rodjenihm.godfatherstips.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.FirebaseFirestore;
import com.rodjenihm.godfatherstips.R;
import com.rodjenihm.godfatherstips.model.Tip;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddTipFragment extends Fragment {
    private EditText rivalsView ;
    private EditText timeView ;
    private EditText tipView;
    private EditText oddsView;

    public AddTipFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_tip, container, false);

        rivalsView = view.findViewById(R.id.rivals);
        timeView = view.findViewById(R.id.time);
        tipView = view.findViewById(R.id.tip);
        oddsView = view.findViewById(R.id.odds);

        Button buttonAddTip = view.findViewById(R.id.button_add_tip);
        buttonAddTip.setOnClickListener(v -> {
            if (validateAddTipFormData()) {
                addTip(rivalsView.getText().toString(), timeView.getText().toString(), tipView.getText().toString(), oddsView.getText().toString());
            }
        });

        return view;
    }

    private boolean validateAddTipFormData() {
        String rivals = rivalsView.getText().toString();
        String time = timeView.getText().toString();
        String tip = tipView.getText().toString();
        String odds = oddsView.getText().toString();

        boolean isRivalsEmpty = rivals.trim().length() <= 0;
        boolean isTimeEmpty = time.trim().length() <= 0;
        boolean isTipEmpty = tip.trim().length() <= 0;
        boolean isOddsEmpty = odds.trim().length() <= 0;

        if (isRivalsEmpty) {
            rivalsView.setError("Please enter rivals");
        } else {
            rivalsView.setError(null);
        }

        if (isTimeEmpty) {
            timeView.setError("Please enter match time");
        } else {
            timeView.setError(null);
        }

        if (isTipEmpty) {
            tipView.setError("Please enter tip");
        } else {
            tipView.setError(null);
        }

        if (isOddsEmpty) {
            oddsView.setError("Please enter odds");
        } else {
            oddsView.setError(null);
        }

        return !isRivalsEmpty && !isTimeEmpty && !isTipEmpty && !isOddsEmpty;
    }

    private void addTip(String rivals, String time, String tip, String odds) {
        Tip obj = new Tip()
                .withRivals(rivals)
                .withTime(time)
                .withTip(tip)
                .withOdds(odds)
                .withStatus(1);

        FirebaseFirestore.getInstance()
                .collection("tips")
                .add(obj)
                .addOnSuccessListener(documentReference -> {
                    clearAddTipFormData();
                    Toast.makeText(getContext(), R.string.tip_successfully_added, Toast.LENGTH_LONG).show();
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show());
    }

    private void clearAddTipFormData() {
        rivalsView.getText().clear();
        timeView.getText().clear();
        tipView.getText().clear();
        oddsView.getText().clear();
    }
}
