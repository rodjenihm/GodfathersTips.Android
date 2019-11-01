package com.rodjenihm.godfatherstips.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.rodjenihm.godfatherstips.R;
import com.rodjenihm.godfatherstips.model.Message;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {
    private FirebaseListAdapter<Message> adapter;

    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        FloatingActionButton fab =
                view.findViewById(R.id.fab);

        fab.setOnClickListener(view1 -> {
            EditText input = view.findViewById(R.id.input);

            Message obj = new Message()
                    .withText(input.getText().toString())
                    .withTime(new Date())
                    .withSenderEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail());

            FirebaseDatabase.getInstance()
                    .getReference()
                    .child("messages")
                    .push()
                    .setValue(obj)
                    .addOnFailureListener(e -> Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show());

            input.clearFocus();
            input.setText("");
        });

        ListView listOfMessages = view.findViewById(R.id.list_of_messages);

        adapter = new FirebaseListAdapter<Message>(getActivity(), Message.class,
                R.layout.message, FirebaseDatabase.getInstance().getReference().child("messages")) {
            @Override
            protected void populateView(View v, Message model, int position) {
                // Get references to the views of message.xml
                TextView messageText = v.findViewById(R.id.message_text);
                TextView messageUser = v.findViewById(R.id.message_user);
                TextView messageTime = v.findViewById(R.id.message_time);

                // Set their text
                messageText.setText(model.getText());
                messageUser.setText(model.getSenderEmail());

                // Format the date before showing it
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                        model.getTime()));

                if (model.getSenderEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                    v.setBackground(getResources().getDrawable(R.drawable.custom_message_me));
                } else {
                    v.setBackground(getResources().getDrawable(R.drawable.custom_message));
                }
            }
        };

        listOfMessages.setAdapter(adapter);

        return view;
    }

}
