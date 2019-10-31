package com.rodjenihm.godfatherstips.fragment;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.rodjenihm.godfatherstips.R;
import com.rodjenihm.godfatherstips.TipRecyclerViewAdapter;
import com.rodjenihm.godfatherstips.model.Tip;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TipFragment extends Fragment {
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private int status;
    private boolean admin = false;

    public TipFragment withStatus(int status) {
        this.status = status;
        return this;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TipFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static TipFragment newInstance(int columnCount) {
        TipFragment fragment = new TipFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tip_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            final List<Tip> tips = new ArrayList<>();
            TipRecyclerViewAdapter adapter = new TipRecyclerViewAdapter(tips, mListener).withAdminPrivilege(admin);
            recyclerView.setAdapter(adapter);

            Query q;
            if (status == 1) {
                q = FirebaseFirestore.getInstance().collection("tips")
                        .whereEqualTo("status", 1);
            } else {
                q = FirebaseFirestore.getInstance().collection("tips")
                        .whereGreaterThan("status", 1);
            }

            q.get().addOnSuccessListener(queryDocumentSnapshots -> {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Tip obj = documentSnapshot.toObject(Tip.class).withId(documentSnapshot.getId());
                            tips.add(obj);
                            adapter.notifyDataSetChanged();
                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show());
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    public TipFragment withAdmin(boolean admin) {
        this.admin = admin;
        return this;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Tip item);
    }
}
