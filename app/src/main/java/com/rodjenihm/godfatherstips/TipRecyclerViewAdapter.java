package com.rodjenihm.godfatherstips;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.rodjenihm.godfatherstips.model.Tip;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TipRecyclerViewAdapter extends RecyclerView.Adapter<TipRecyclerViewAdapter.ViewHolder> {

    private final List<Tip> mValues;
    private final OnListFragmentInteractionListener mListener;

    public TipRecyclerViewAdapter(List<Tip> items, OnListFragmentInteractionListener listener) {
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
        String status = mValues.get(position).getStatus();

        switch (status) {
            case "pending":
                holder.mView.setBackground(
                        holder.mView.getContext().getResources().getDrawable(R.drawable.custom_tip_pending));
                break;
            case "won":
                holder.mView.setBackground(
                        holder.mView.getContext().getResources().getDrawable(R.drawable.custom_tip_won));
                break;
            case "lost":
                holder.mView.setBackground(
                        holder.mView.getContext().getResources().getDrawable(R.drawable.custom_tip_lost));
                break;
        }

        holder.mItem = mValues.get(position);
        holder.mRivalsView.setText(mValues.get(position).getRivals());
        holder.mTimeView.setText(mValues.get(position).getTime());
        holder.mTextView.setText(mValues.get(position).getTip());
        holder.mOddsView.setText(String.valueOf(mValues.get(position).getOdds()));

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
        public final TextView mRivalsView;
        public final TextView mTimeView;
        public final TextView mTextView;
        public final TextView mOddsView;
        public Tip mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mRivalsView = view.findViewById(R.id.rivals);
            mTimeView = view.findViewById(R.id.time);
            mTextView = view.findViewById(R.id.text);
            mOddsView = view.findViewById(R.id.odds);
        }

        @Override
        public String toString() {
            return "ViewHolder{" +
                    "mView=" + mView +
                    ", mRivalsView=" + mRivalsView +
                    ", mTimeView=" + mTimeView +
                    ", mTextView=" + mTextView +
                    ", mOddsView=" + mOddsView +
                    ", mItem=" + mItem +
                    '}';
        }
    }
}