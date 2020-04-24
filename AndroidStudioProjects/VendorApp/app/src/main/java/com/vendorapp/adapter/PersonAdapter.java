package com.vendorapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.vendorapp.R;
import com.vendorapp.model.Payment;

public class PersonAdapter extends
        ListAdapter<Payment, PersonAdapter.PersonViewHolder> {

    public PersonAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Payment> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Payment>() {
                @Override
                public boolean areItemsTheSame(@NonNull Payment oldData,
                                               @NonNull Payment newData) {
                    return oldData.getId() == (newData.getId());
                }

                @Override
                public boolean areContentsTheSame(@NonNull Payment oldData,
                                                  @NonNull Payment newData) {
                    return false;
                }
            };

    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_person, parent, false);
        return new PersonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PersonViewHolder holder, int position) {
        holder.bindTo(getItem(position));
    }

    static class PersonViewHolder extends RecyclerView.ViewHolder {

        private TextView textName;
        private TextView textAmount;

        PersonViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textMainName);
            textAmount = itemView.findViewById(R.id.textMainAmount);
        }

        void bindTo(Payment payment) {
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