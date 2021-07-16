package com.expenses.flow.RecyclerViewList;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.expenses.flow.ItemList;
import com.expenses.flow.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class customListAdapter extends RecyclerView.Adapter<customListAdapter.CreditViewHolder> {

    // declaring some fields.
    private ArrayList<ItemList> arrayList;
    Context mcontext;
    Fragment activityFragment;
    View mView;

    final int VIEW_TYPE_DEBIT = 0;
    final int VIEW_TYPE_CREDIT = 1;
    final int VIEW_TYPE_ALL = 1;

    // A constructor.
    public customListAdapter(ArrayList<ItemList> arrayList, Fragment activityFragment) {
        this.arrayList = arrayList;
        this.activityFragment = activityFragment;

    }

    public class CreditViewHolder extends RecyclerView.ViewHolder {
        TextView name, amount;
        ImageView deleteButton, editButton;
        public CreditViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            Log.d("Viewholder","In viewholder");
            name = itemView.findViewById(R.id.item_name);
            amount = itemView.findViewById(R.id.item_amount);
            deleteButton = itemView.findViewById(R.id.delete_button);
            editButton = itemView.findViewById(R.id.edit_item);
        }
    }


    @NonNull
    @NotNull
    @Override
    public CreditViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        Log.v("CreateViewHolder", "in onCreateViewHolder");
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item,parent,false);
        mView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_entry_dialog,parent,false);
        mcontext = itemView.getContext();
        return new CreditViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull @NotNull CreditViewHolder holder,final int position) {
        Log.v("BindViewHolder", "in onBindViewHolder");
        ItemList item = arrayList.get(position);
        Log.d("name",""+item.getItemName());
        holder.name.setText(item.getItemName());
        holder.amount.setText("$ "+ item.getItemAmount());
        holder.deleteButton.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(activityFragment.getActivity());

            builder.setMessage("Do you want to delete "+ item.getItemName() +" ?");
            builder.setPositiveButton(R.string.yes, (dialog, id) -> {
                // User clicked OK button
                // Get the clicked item label
                String itemLabel = arrayList.get(position).getItemName();
                // Remove the item on remove/button click
                arrayList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,arrayList.size());
                // Show the removed item label`enter code here`
            Toast.makeText(mcontext,"Deleted " + itemLabel,Toast.LENGTH_SHORT).show();
            });
            builder.setNegativeButton(R.string.no, (dialog, id) -> {
                // User cancelled the dialog
                dialog.dismiss();
            });
            AlertDialog dialog = builder.create();

            dialog.show();
        });

        holder.editButton.setOnClickListener(view->{
            AlertDialog.Builder alert = new AlertDialog.Builder(mcontext,R.style.MyAlertTheme);
            String itemLabel = arrayList.get(position).getItemName();
            Integer itemAmount = arrayList.get(position).getItemAmount();

            Button btn_cancel = (Button)mView.findViewById(R.id.cancel_button);
            Button btn_okay = (Button)mView.findViewById(R.id.create_item_button);
            btn_okay.setText(R.string.update);

            final EditText itemNameEditText = mView.findViewById(R.id.item_name_edittext);
            final EditText itemAmountEditText = mView.findViewById(R.id.item_amount_edittext);

            itemNameEditText.setText(itemLabel);
            itemAmountEditText.setText(itemAmount.toString());

            if(mView.getParent()!=null)
                ((ViewGroup)mView.getParent()).removeView(mView);
            alert.setView(mView);

            AlertDialog alertDialog = alert.create();
            alertDialog.setCanceledOnTouchOutside(true);
            btn_cancel.setOnClickListener(v -> alertDialog.dismiss());
            btn_okay.setOnClickListener(new View.OnClickListener() {
                boolean itemNameError = false;
                boolean itemAmountError = false;

                @Override
                public void onClick(View v) {
                    Log.d("Dialog", "OK pressed");
                    String itemName = itemNameEditText.getText().toString();
                    if(itemName!=null && itemName.length()!=0){
                        Log.d("itemName", itemName);
                        itemNameError = false;
                    }
                    else{
                        Log.e("itemName", "Null");
                        itemNameEditText.setError("Enter Item Name");
                        itemNameError = true;
                    }
                    String itemAmountString = itemAmountEditText.getText().toString().trim();
                    int itemAmount;
                    if(!itemAmountString.equalsIgnoreCase("")){
                        itemAmount = Integer.parseInt(itemAmountString);
                        itemAmountError = false;
                    }
                    else{
                        itemAmount = 0;
                        Log.e("Item amount", "null");
                        itemAmountEditText.setError("Enter Item Amount");

                        itemAmountError = true;


                    }
//                ItemList.addCreditItem(itemName,itemAmount);
                    if(!itemAmountError && !itemNameError) {
                        ItemList credit = null;
                        credit = new ItemList(itemName, itemAmount);
                        arrayList.set(position,credit);
                        notifyItemChanged(position);
                        alertDialog.dismiss();
//                updateUI();
                    }
                }
            });

            alertDialog.show();


        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void showDeleteConfirmationDialog(){

    }

}
