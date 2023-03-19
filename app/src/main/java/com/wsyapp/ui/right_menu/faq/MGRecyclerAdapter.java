package com.wsyapp.ui.right_menu.faq;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

/**
 * Created by mg on 30/07/16.
 */
public class MGRecyclerAdapter extends RecyclerView.Adapter<MGRecyclerAdapter.ViewHolder> {

    OnMGRecyclerAdapterListener mCallback;
    private int resId;
    private int count;

    public MGRecyclerAdapter(int count, int resId) {
        this.count = count;
        this.resId = resId;
    }

    public void setOnMGRecyclerAdapterListener(OnMGRecyclerAdapterListener listener) {

        try {
            mCallback = listener;
        } catch (ClassCastException e) {
            throw new ClassCastException(this.toString() + " must implement OnMGRecyclerAdapterListener");
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(resId, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
//        holder.mTextView.setText(mDataset[position]);
        if (mCallback != null) {
            try {
                mCallback.onMGRecyclerAdapterCreated(this, holder, position);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return count;
    }

    public interface OnMGRecyclerAdapterListener {
        void onMGRecyclerAdapterCreated(MGRecyclerAdapter adapter, ViewHolder v, int position) throws JSONException;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public View view;

        public ViewHolder(View v) {
            super(v);
            view = v;
        }
    }
}
