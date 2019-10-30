package com.rodjenihm.godfatherstips;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rodjenihm.godfatherstips.fragment.UserFragment;
import com.rodjenihm.godfatherstips.model.AppUser;

import java.text.SimpleDateFormat;
import java.util.List;

public class UserRecyclerViewAdapter extends RecyclerView.Adapter<UserRecyclerViewAdapter.ViewHolder> {

    private final List<AppUser> mValues;
    private final UserFragment.OnListFragmentInteractionListener mListener;

    public UserRecyclerViewAdapter(List<AppUser> items, UserFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        String Uid = mValues.get(position).getUserId();
        String email = "Email: " + mValues.get(position).getEmail();
        String isEmailVerified = "Email verified: " + (mValues.get(position).isEmailVerified() ? "Yes" : "No");
        String createdAt = "Registration date: " + new SimpleDateFormat("dd.MM.yyyy. - HH:mm").format(mValues.get(position).getCreatedAt());

        String roleName;
        if (mValues.get(position).getRoles().contains("ADMIN")) {
            roleName = "ADMIN";
        } else if(mValues.get(position).getRoles().contains("VIP")) {
            roleName = "VIP";
            holder.mSwitchView.setVisibility(View.VISIBLE);
            holder.mSwitchView.setChecked(true);
        } else {
            roleName = "MEMBER";
            holder.mSwitchView.setVisibility(View.VISIBLE);
            holder.mSwitchView.setChecked(false);
        }

        String role = "Role: " + roleName;

        holder.mItem = mValues.get(position);
        holder.mEmailView.setText(email);
        holder.mEmailVerifiedView.setText(isEmailVerified);
        holder.mCreatedAtView.setText(createdAt);
        holder.mRoleView.setText(role);
        holder.mSwitchView.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                FirebaseFirestore.getInstance()
                        .collection("users")
                        .document(Uid)
                        .update("roles", FieldValue.arrayUnion("VIP"))
                        .addOnSuccessListener(aVoid -> {
                            holder.mRoleView.setText("Role: VIP");
                            FirebaseFirestore.getInstance()
                                    .collection("roles")
                                    .document("VIP")
                                    .update("users", FieldValue.arrayUnion(Uid));
                        });
            } else {
                FirebaseFirestore.getInstance()
                        .collection("users")
                        .document(Uid)
                        .update("roles", FieldValue.arrayRemove("VIP"))
                        .addOnSuccessListener(aVoid -> {
                            holder.mRoleView.setText("Role: MEMBER");
                            FirebaseFirestore.getInstance()
                                    .collection("roles")
                                    .document("VIP")
                                    .update("users", FieldValue.arrayRemove(Uid));
                        });
            }
        });

        holder.mView.setOnClickListener(v -> {
            if (null != mListener) {
                mListener.onListFragmentInteraction(holder.mItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mEmailView;
        public final TextView mEmailVerifiedView;
        public final TextView mCreatedAtView;
        public final TextView mRoleView;
        public final Switch mSwitchView;
        public AppUser mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mEmailView = view.findViewById(R.id.email);
            mEmailVerifiedView = view.findViewById(R.id.email_verified);
            mCreatedAtView = view.findViewById(R.id.created_at);
            mRoleView = view.findViewById(R.id.role);
            mSwitchView = view.findViewById(R.id.switch_vip);
        }

        @Override
        public String toString() {
            return "ViewHolder{" +
                    "mView=" + mView +
                    ", mEmailView=" + mEmailView +
                    ", mEmailVerifiedView=" + mEmailVerifiedView +
                    ", mCreatedAtView=" + mCreatedAtView +
                    ", mRoleView=" + mRoleView +
                    ", mItem=" + mItem +
                    '}';
        }
    }
}