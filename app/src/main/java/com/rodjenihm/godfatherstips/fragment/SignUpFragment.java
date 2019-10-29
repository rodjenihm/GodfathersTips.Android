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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rodjenihm.godfatherstips.R;
import com.rodjenihm.godfatherstips.Utilities;
import com.rodjenihm.godfatherstips.model.AppUser;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment {
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();


    private EditText emailView;
    private EditText passwordView;
    private EditText passwordConfirmView;

    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        emailView = view.findViewById(R.id.email);

        passwordView = view.findViewById(R.id.password);

        passwordConfirmView = view.findViewById(R.id.password_confirm);

        Button signUpButton = view.findViewById(R.id.button_sign_up);
        signUpButton.setOnClickListener(v -> {
            if (validateRegisterFormData()) {
                createUserWithEmailAndPassword(emailView.getText().toString(), passwordView.getText().toString());
            }
        });

        return view;
    }

    private boolean validateRegisterFormData() {
        String email = emailView.getText().toString();
        String password = passwordView.getText().toString();
        String passwordConfirm = passwordConfirmView.getText().toString();

        boolean isEmailEmpty = email.trim().length() <= 0;
        boolean isPasswordEmpty = password.trim().length() <= 0;
        boolean isPasswordConfirmEmpty = passwordConfirm.trim().length() <= 0;

        boolean isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        boolean isPasswordValid = password.length() >= 6;
        boolean isPasswordConfirmValid = passwordConfirm.equals(password);

        if (isEmailEmpty) {
            emailView.setError(getResources().getString(R.string.email_required));
        } else if (!isEmailValid) {
            emailView.setError(getResources().getString(R.string.email_invalid));
        } else {
            emailView.setError(null);
        }

        if (isPasswordEmpty) {
            passwordView.setError(getResources().getString(R.string.password_required));
        } else if (!isPasswordValid) {
            passwordView.setError(getResources().getString(R.string.password_invalid));
        } else {
            passwordView.setError(null);

            if (isPasswordConfirmEmpty) {
                passwordConfirmView.setError(getResources().getString(R.string.password_confirm_required));
            } else if (!isPasswordConfirmValid) {
                passwordConfirmView.setError(getResources().getString(R.string.password_confirm_invalid));
            } else {
                passwordConfirmView.setError(null);
            }
        }

        return !isEmailEmpty && isEmailValid
                && !isPasswordEmpty && isPasswordValid
                && !isPasswordConfirmEmpty && isPasswordConfirmValid;
    }

    private void createUserWithEmailAndPassword(String email, String password) {
        final ProgressDialog dlg = new ProgressDialog(getContext());
        dlg.setTitle(R.string.please_wait);
        dlg.setMessage(getResources().getString(R.string.creating_account));
        dlg.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    String Uid = mAuth.getCurrentUser().getUid();

                    // Create helper user because default FirebaseUser su*ks and cannot be customized
                    AppUser user = new AppUser()
                            .withUserId(Uid)
                            .withEmail(email)
                            .withEmailVerified(true)
                            .withCreatedAt(Timestamp.now())
                            .withRole("MEMBER");

                    firestore.collection("users")
                            .document(Uid)
                            .set(user)
                            .addOnSuccessListener(aVoid -> {
                                // Add user to MEMBER role by default
                                firestore.collection("roles")
                                        .document("MEMBER")
                                        .update("users", FieldValue.arrayUnion(Uid))
                                        .addOnSuccessListener(aVoid1 -> {
                                            dlg.dismiss();
                                            Utilities.showAlertDialog(
                                                    getContext(),
                                                    getResources().getString(R.string.creating_account_success),
                                                    getResources().getString(R.string.email_confirm_require),
                                                    () -> {
                                                        clearRegisterFormData();
                                                        sendVerificationEmail(mAuth.getCurrentUser());
                                                    });
                                        })
                                        .addOnFailureListener(e -> {
                                            dlg.dismiss();
                                            Utilities.showAlertDialog(
                                                    getContext(),
                                                    getResources().getString(R.string.creating_role_failure),
                                                    e.getLocalizedMessage(),
                                                    () -> deleteUser(mAuth.getCurrentUser()));
                                        });
                            })
                            .addOnFailureListener(e -> {
                                dlg.dismiss();
                                Utilities.showAlertDialog(
                                        getContext(),
                                        getResources().getString(R.string.creating_user_failure),
                                        e.getLocalizedMessage(),
                                        () -> deleteUser(mAuth.getCurrentUser()));
                            });
                })
                .addOnFailureListener(e -> {
                    dlg.dismiss();
                    Utilities.showAlertDialog(
                            getContext(),
                            getResources().getString(R.string.creating_account_failure),
                            e.getLocalizedMessage(),
                            mAuth::signOut);
                });
    }

    private void deleteUser(FirebaseUser user) {
        user.delete()
                .addOnCompleteListener(task -> mAuth.signOut());
    }

    private void sendVerificationEmail(FirebaseUser user) {
        user.sendEmailVerification()
                .addOnCompleteListener(task -> mAuth.signOut());
    }

    private void clearRegisterFormData() {
        emailView.getText().clear();
        passwordView.getText().clear();
        passwordConfirmView.getText().clear();
    }
}
