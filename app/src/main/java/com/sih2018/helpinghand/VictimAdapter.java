package com.sih2018.helpinghand;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by prathmeshmhapsekar on 19/03/18.
 */

public class VictimAdapter extends RecyclerView.Adapter<VictimAdapter.VictimAdapterViewHolder> {

    private String[] mShelterData;


    private final VictimAdapterOnClickHandler mClickHandler;

    public interface VictimAdapterOnClickHandler {
        void onClick(String victimdata);
    }

    public VictimAdapter(VictimAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public class VictimAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mShelterTextView;

        public VictimAdapterViewHolder(View view) {
            super(view);
            mShelterTextView = (TextView) view.findViewById(R.id.tv_shelter_data);
            view.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            String victimdata = mShelterData[adapterPosition];
            mClickHandler.onClick(victimdata);
        }
    }


    @Override
    public VictimAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.user_list;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new VictimAdapterViewHolder(view);
    }


    @Override
    public void onBindViewHolder(VictimAdapterViewHolder VictimAdapterViewHolder, int position) {
        String victimdata = mShelterData[position];
        VictimAdapterViewHolder.mShelterTextView.setText(victimdata);
    }


    @Override
    public int getItemCount() {
        if (null == mShelterData) return 0;
        return mShelterData.length;
    }


    public void setShelterData(String[] shelterData) {
        mShelterData = shelterData;
        notifyDataSetChanged();
    }
}