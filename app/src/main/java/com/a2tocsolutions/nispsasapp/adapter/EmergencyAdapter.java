package com.a2tocsolutions.nispsasapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.a2tocsolutions.nispsasapp.Fragments.AlertFrag;
import com.a2tocsolutions.nispsasapp.R;
import com.a2tocsolutions.nispsasapp.model.Emergency;
import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class EmergencyAdapter extends RecyclerView.Adapter<EmergencyAdapter.MyViewHolder> {

    private AlertFrag mContext;
    private List<Emergency> emergencyList;
    final private ItemClickListener mItemClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title;
        public TextView subtitle;
        public CircleImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            subtitle = (TextView) view.findViewById(R.id.subtitle);
            thumbnail = (CircleImageView) view.findViewById(R.id.thumbnail);

            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
          int elementId = emergencyList.get(getAdapterPosition()).getId();
          mItemClickListener.onItemClickListener(elementId);
        }
    }


    public EmergencyAdapter(AlertFrag mContext, List<Emergency> emergencies, ItemClickListener listener) {
        this.mContext = mContext;
        this.emergencyList = emergencies;
        mItemClickListener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.emergency_items, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        Emergency emergency = emergencyList.get(position);
        holder.title.setText(emergency.getName());
        holder.subtitle.setText(emergency.getSub());

        // loading album cover using Glide library
        Glide.with(mContext).load(emergency.getThumbnail()).into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return emergencyList.size();
    }

    public interface ItemClickListener {
        void onItemClickListener(int itemId);
    }
}
