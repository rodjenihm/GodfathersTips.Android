package com.rodjenihm.godfatherstips;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rodjenihm.godfatherstips.fragment.TipFragment;
import com.rodjenihm.godfatherstips.model.Tip;

import java.util.List;

public class TipRecyclerViewAdapter extends RecyclerView.Adapter<TipRecyclerViewAdapter.ViewHolder> {
    private final List<Tip> mValues;
    private final TipFragment.OnListFragmentInteractionListener mListener;

    private boolean admin = false;

    public TipRecyclerViewAdapter(List<Tip> items, TipFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_tip, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        int status = mValues.get(position).getStatus();

        switch (status) {
            case 1:
                holder.mView.setBackground(
                        holder.mView.getContext().getResources().getDrawable(R.drawable.custom_tip_pending));
                if (admin) {
                    holder.mArchiveView.setVisibility(View.VISIBLE);
                }
                break;
            case 2:
                holder.mView.setBackground(
                        holder.mView.getContext().getResources().getDrawable(R.drawable.custom_tip_won));
                break;
            case 3:
                holder.mView.setBackground(
                        holder.mView.getContext().getResources().getDrawable(R.drawable.custom_tip_lost));
                break;
        }

        holder.mItem = mValues.get(position);
        holder.mRivalsView.setText(mValues.get(position).getRivals());
        holder.mTimeView.setText(mValues.get(position).getTime());
        holder.mTextView.setText(mValues.get(position).getTip());
        holder.mOddsView.setText(String.valueOf(mValues.get(position).getOdds()));
        holder.mArchiveView.setOnClickListener(v -> {
            String[] options = {"won", "lost"};

            AlertDialog.Builder builder = new AlertDialog.Builder(holder.mView.getContext());
            builder.setTitle("Pick tip status");
            builder.setItems(options, (dialog, which) -> {
                String tipId = holder.mItem.getTipId();
                Log.i("a", String.valueOf(which));
                FirebaseFirestore.getInstance()
                        .collection("tips")
                        .document(tipId)
                        .update("status", which + 2)
                        .addOnSuccessListener(aVoid -> {
                            mValues.remove(position);
                            notifyDataSetChanged();
                        })
                        .addOnFailureListener(e -> Toast.makeText(holder.mView.getContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show());
            });
            builder.show();
        });

        holder.mView.setOnClickListener(v -> {
            if (null != mListener) {
                mListener.onListFragmentInteraction(holder.mItem);
            }
        });
    }

    public TipRecyclerViewAdapter withAdminPrivilege(boolean admin) {
        this.admin = admin;
        return this;
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mRivalsView;
        public final TextView mTimeView;
        public final TextView mTextView;
        public final TextView mOddsView;
        public final Button mArchiveView;
        public Tip mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mRivalsView = view.findViewById(R.id.rivals);
            mTimeView = view.findViewById(R.id.time);
            mTextView = view.findViewById(R.id.text);
            mOddsView = view.findViewById(R.id.odds);
            mArchiveView = view.findViewById(R.id.archive);
        }

        @Override
        public String toString() {
            return "ViewHolder{" +
                    "mView=" + mView +
                    ", mRivalsView=" + mRivalsView +
                    ", mTimeView=" + mTimeView +
                    ", mTextView=" + mTextView +
                    ", mOddsView=" + mOddsView +
                    ", mArchiveView=" + mArchiveView +
                    ", mItem=" + mItem +
                    '}';
        }
    }
}