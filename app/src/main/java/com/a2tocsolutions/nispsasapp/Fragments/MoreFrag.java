package com.a2tocsolutions.nispsasapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.a2tocsolutions.nispsasapp.R;
import com.a2tocsolutions.nispsasapp.activities.UploadMediaActivity;
import com.a2tocsolutions.nispsasapp.adapter.MoreDataAdapter;
import com.a2tocsolutions.nispsasapp.model.MoreData;

import java.util.ArrayList;
import java.util.List;

public class MoreFrag extends Fragment implements MoreDataAdapter.ItemClickListener{
    View v;
    private RecyclerView myrecyclerview;
    private static final String TAG = "MoreFrag";
    private MoreDataAdapter rAdapter;
    private AppCompatImageView camero;
    private TextView tv;
    private List<MoreData> moreDataList;
    public MoreFrag(){
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v= inflater.inflate(R.layout.morefrag, container, false);
        myrecyclerview = (RecyclerView) v.findViewById(R.id.more_recyclerview);
        rAdapter = new MoreDataAdapter(getContext(), moreDataList);

        myrecyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        myrecyclerview.setItemAnimator(new DefaultItemAnimator());
        myrecyclerview.setAdapter(rAdapter);
        moreData();

        tv=(TextView) v.findViewById(R.id.flytext);
        tv.setSelected(true);

        camero = (AppCompatImageView) v.findViewById(R.id.camera);
        camero.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), UploadMediaActivity.class);
            startActivity(intent);
        });
        return v;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        moreDataList = new ArrayList<>();


    }

    private void moreData() {
        int[] covers = new int[]{
                R.drawable.algon,
                R.drawable.complaints,
                R.drawable.commerce,
                R.drawable.bank
        };

        MoreData a = new MoreData(1, "ALGON", "", covers[0], "");
        moreDataList.add(a);

        a = new MoreData(2, "Complaints", "", covers[1], "");
        moreDataList.add(a);

        a = new MoreData(3, "Commerce", "", covers[2], "");
        moreDataList.add(a);

        a = new MoreData(4, "Other Institutions", "", covers[3], "");
        moreDataList.add(a);

        rAdapter.notifyDataSetChanged();
    }





}
