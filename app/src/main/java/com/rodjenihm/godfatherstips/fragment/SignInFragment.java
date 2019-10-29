package com.rodjenihm.godfatherstips.fragment;


import android.app.Activity;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.rodjenihm.godfatherstips.R;
import com.rodjenihm.godfatherstips.Utilities;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment {
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private EditText emailView;
    private EditText passwordView;

    public SignInFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        emailView = view.findViewById(R.id.email);

        passwordView = view.findViewById(R.id.password);

        Button signInButton = view.findViewById(R.id.button_sign_in);
        signInButton.setOnClickListener(v -> {
            if (validateLoginFormData()) {
                signInUserWithEmailAndPassword(emailView.getText().toString(), passwordView.getText().toString());
            }
        });

        return view;
    }

    private boolean validateLoginFormData() {
        String email = emailView.getText().toString();
        String password = passwordView.getText().toString();

        boolean isEmailEmpty = email.trim().length() <= 0;
        boolean isPasswordEmpty = password.trim().length() <= 0;
        boolean isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();

        if (isEmailEmpty) {
            emailView.setError(getResources().getString(R.string.email_required));
        } else if (!isEmailValid) {
            emailView.setError(getResources().getString(R.string.email_invalid));
        } else {
            emailView.setError(null);
        }

        if (isPasswordEmpty) {
            passwordView.setError(getResources().getString(R.string.password_required));
        } else {
            passwordView.setError(null);
        }

        return !isEmailEmpty && isEmailValid && !isPasswordEmpty;
    }

    private void signInUserWithEmailAndPassword(String email, String password) {
        final ProgressDialog dlg = new ProgressDialog(getContext());
        dlg.setTitle(R.string.please_wait);
        dlg.setMessage(getResources().getString(R.string.signing_in));
        dlg.show();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    if (mAuth.getCurrentUser().isEmailVerified()) {
                        dlg.dismiss();
                        Activity currentActivity = getActivity();
                        currentActivity.recreate();
                    } else {
                        dlg.dismiss();
                        Utilities.showAlertDialog(
                                getContext(),
                                getResources().getString(R.string.signing_in_failure),
                                getResources().getString(R.string.email_confirm_require),
                                mAuth::signOut);
                    }
                })
                .addOnFailureListener(e -> {
                    dlg.dismiss();
                    Utilities.showAlertDialog(
                            getContext(),
                            getResources().getString(R.string.signing_in_failure),
                            e.getLocalizedMessage(),
                            mAuth::signOut);
                });
    }
}
