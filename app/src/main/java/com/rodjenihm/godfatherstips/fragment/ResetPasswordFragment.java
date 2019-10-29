package com.rodjenihm.godfatherstips.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.rodjenihm.godfatherstips.R;
import com.rodjenihm.godfatherstips.Utilities;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResetPasswordFragment extends Fragment {
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private EditText emailView;

    public ResetPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);

        emailView = view.findViewById(R.id.email);

        Button resetButton = view.findViewById(R.id.button_reset);
        resetButton.setOnClickListener(v -> {
            if (validatePasswordResetFormData()) {
                sendPasswordResetEmail(emailView.getText().toString());
            }
        });

        return view;
    }

    private boolean validatePasswordResetFormData() {
        String email = emailView.getText().toString();

        boolean isEmailEmpty = email.trim().length() <= 0;
        boolean isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();

        if (isEmailEmpty) {
            emailView.setError(getResources().getString(R.string.email_required));
        } else if (!isEmailValid) {
            emailView.setError(getResources().getString(R.string.email_invalid));
        } else {
            emailView.setError(null);
        }

        return !isEmailEmpty && isEmailValid;
    }

    private void sendPasswordResetEmail(String email) {
        final ProgressDialog dlg = new ProgressDialog(getContext());
        dlg.setTitle(R.string.please_wait);
        dlg.setMessage(getResources().getString(R.string.reset_link_sending));
        dlg.show();

        mAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener(aVoid -> {
                    dlg.dismiss();
                    Utilities.showAlertDialog(
                            getContext(),
                            getResources().getString(R.string.reset_link_sent),
                            getResources().getString(R.string.reset_link_sent_message),
                            () -> emailView.getText().clear());
                })
                .addOnFailureListener(e -> {
                    dlg.dismiss();
                    Utilities.showAlertDialog(
                            getContext(),
                            getResources().getString(R.string.reset_link_not_sent),
                            e.getLocalizedMessage(),
                            null);
                });
    }
}
