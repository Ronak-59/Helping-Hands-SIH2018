package com.sih2018.helpinghand;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by prathmeshmhapsekar on 27/03/18.
 */

public class TransAdapter extends RecyclerView.Adapter<TransAdapter.TransAdapterViewHolder> {

    private String[][] mTransData;


    private final TransAdapter.TransAdapterOnClickHandler mClickHandler;

    public interface TransAdapterOnClickHandler {
        void onClick(String transdata);
    }

    public TransAdapter(TransAdapter.TransAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public class TransAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mTransTextView,mTransSTextView,mTransDTextView;

        public TransAdapterViewHolder(View view) {
            super(view);
            mTransTextView = (TextView) view.findViewById(R.id.tv_trans_data);
            mTransSTextView = (TextView) view.findViewById(R.id.trans_source);
            mTransDTextView = (TextView) view.findViewById(R.id.trans_dest);
            view.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            String transid = mTransData[adapterPosition][0];
            mClickHandler.onClick(transid);
        }
    }


    @Override
    public TransAdapter.TransAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.trans_list;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new TransAdapter.TransAdapterViewHolder(view);
    }


    @Override
    public void onBindViewHolder(TransAdapter.TransAdapterViewHolder TransAdapterViewHolder, int position) {
        String transname = mTransData[position][1];
        String transsname = mTransData[position][2];
        String transdname = mTransData[position][3];
        //Log.e("Debug",transname+" "+transsname+" "+transdname);
        TransAdapterViewHolder.mTransTextView.setText(transname);
        TransAdapterViewHolder.mTransSTextView.setText(transsname);
        TransAdapterViewHolder.mTransDTextView.setText(transdname);
    }


    @Override
    public int getItemCount() {
        if (null == mTransData) return 0;
        return mTransData.length;
    }


    public void setTransData(String[][] TransData) {
        mTransData = TransData;
        notifyDataSetChanged();
    }
}
