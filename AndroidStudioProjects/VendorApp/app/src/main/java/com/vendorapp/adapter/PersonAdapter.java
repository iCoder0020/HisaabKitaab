package com.vendorapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.vendorapp.AppPreferences;
import com.vendorapp.MainActivity;
import com.vendorapp.R;
import com.vendorapp.model.Payment;

public class PersonAdapter extends
        ListAdapter<Payment, PersonAdapter.PersonViewHolder> {

    public interface OnItemClickListener { // 1
        void onItemClicked(Payment payment);
    }

    private OnItemClickListener clickListener;

    public PersonAdapter(OnItemClickListener clickListener) {
        super(DIFF_CALLBACK);
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_payment, parent, false);
        return new PersonViewHolder(view, parent.getContext(), clickListener);
    }

    @Override
    public void onBindViewHolder(PersonViewHolder holder, int position) {
        holder.bindTo(getItem(position));
    }

    static class PersonViewHolder extends RecyclerView.ViewHolder {

        private TextView textDescription;
        //private TextView textTotal;
        //private TextView textDetail;
        private TextView textAmount;
        private AppPreferences preferences;
        private Payment payment;


        PersonViewHolder(@NonNull View itemView, Context mainContext, OnItemClickListener clickListener) {
            super(itemView);
            itemView.setOnClickListener(v -> clickListener.onItemClicked(payment));
            textDescription = itemView.findViewById(R.id.textFriendDescription);
            //textTotal = itemView.findViewById(R.id.textPaymentTotal);
            //textDetail = itemView.findViewById(R.id.textPaymentDetail);
            textAmount = itemView.findViewById(R.id.textFriendAmount);
            preferences = new AppPreferences(mainContext);

        }

        void bindTo(Payment payment) {
            this.payment = payment;
            Log.w("PersonAdapter", Integer.toString(preferences.getId()));
            textDescription.setText(payment.getDescription());
            //Log.w("PersonAdapter", Integer.toString(preferences.getId()));
            //textTotal.setText( "Total Amount: "+Double.toString( payment.getTotalAmount() ));
            Log.w("PersonAdapter", Integer.toString(preferences.getId()));
            String str = "";
            if(payment.getBorrowerID() == preferences.getId()){
                str = "You Lent : ";
            }
            else{
                str = "You Borrowed : ";
            }
            Log.w("PersonAdapter", Integer.toString(preferences.getId()));
            textAmount.setText(str+Double.toString(payment.getLendedAmount()));
            Log.w("PersonAdapter", Integer.toString(preferences.getId()));
        }
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
                    return oldData.equals(newData);
                }
            };
}