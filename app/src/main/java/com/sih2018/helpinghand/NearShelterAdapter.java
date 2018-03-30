package com.sih2018.helpinghand;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by prathmeshmhapsekar on 20/03/18.
 */

public class NearShelterAdapter extends RecyclerView.Adapter<NearShelterAdapter.NearShelterAdapterViewHolder> {

    private String[] mShelterData;


    private final NearShelterAdapter.NearShelterAdapterOnClickHandler mClickHandler;

    public interface NearShelterAdapterOnClickHandler {
        void onClick(String shelterdata);
    }

    public NearShelterAdapter(NearShelterAdapter.NearShelterAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public class NearShelterAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mShelterTextView;

        public NearShelterAdapterViewHolder(View view) {
            super(view);
            mShelterTextView = (TextView) view.findViewById(R.id.tv_shelter_data);
            view.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            String shelterdata = mShelterData[adapterPosition];
            mClickHandler.onClick(shelterdata);
        }
    }


    @Override
    public NearShelterAdapter.NearShelterAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.nearshelter_list;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new NearShelterAdapter.NearShelterAdapterViewHolder(view);
    }


    @Override
    public void onBindViewHolder(NearShelterAdapter.NearShelterAdapterViewHolder nearShelterAdapterViewHolder, int position) {
        String shelterdata = mShelterData[position];
        nearShelterAdapterViewHolder.mShelterTextView.setText(shelterdata);
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
