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
            String rivals = rivalsView.getText().toString();
            String time = timeView.getText().toString();
            String tip = tipView.getText().toString();
            String odds = oddsView.getText().toString();

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
        });

        return view;
    }

    private void clearAddTipFormData() {
        rivalsView.getText().clear();
        timeView.getText().clear();
        tipView.getText().clear();
        oddsView.getText().clear();
    }
}
