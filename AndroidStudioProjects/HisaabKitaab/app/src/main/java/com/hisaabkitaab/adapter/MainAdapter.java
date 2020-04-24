package com.hisaabkitaab.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.hisaabkitaab.R;
import com.hisaabkitaab.model.Friend;

public class MainAdapter extends
        ListAdapter<Friend, MainAdapter.MainViewHolder> {

    public MainAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Friend> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Friend>() {
                @Override
                public boolean areItemsTheSame(@NonNull Friend oldData,
                                               @NonNull Friend newData) {
                    return oldData.getId() == (newData.getId());
                }

                @Override
                public boolean areContentsTheSame(@NonNull Friend oldData,
                                                  @NonNull Friend newData) {
                    return false;
                }
            };

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_person, parent, false);
        return new MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        holder.bindTo(getItem(position));
    }

    static class MainViewHolder extends RecyclerView.ViewHolder {

        private TextView textName;
        private TextView textAmount;

        MainViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textMainName);
            textAmount = itemView.findViewById(R.id.textMainAmount);
        }

        void bindTo(Friend friend) {
            textName.setText(friend.getUsername());
            double amount = friend.getAmount();
            String stramt = "";
            if(amount<0){
                amount = amount*-1;
                stramt = "You Lent: "+Double.toString(amount);
            }
            else{
                stramt = "You Borrowed: "+Double.toString(amount);
            }
            textAmount.setText(stramt);
        }
    }
}