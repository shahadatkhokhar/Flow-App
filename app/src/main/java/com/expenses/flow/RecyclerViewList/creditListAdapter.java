package com.expenses.flow.RecyclerViewList;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.expenses.flow.FirebaseHelper;
import com.expenses.flow.GlobalContent;
import com.expenses.flow.HomeFragment;
import com.expenses.flow.ItemList;
import com.expenses.flow.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class creditListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context mcontext;
    Fragment activityFragment;
    View mView;
    // declaring some fields.
    private final ArrayList<ItemList> creditList;

    // A constructor.
    public creditListAdapter(ArrayList<ItemList> creditList, Fragment activityFragment) {
        this.creditList = creditList;
        this.activityFragment = activityFragment;

    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        Log.v("CreateViewHolder", "in onCreateViewHolder");
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        mView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_entry_dialog, parent, false);
        mcontext = itemView.getContext();

        return new CreditViewHolder(itemView);

    }

    @SuppressLint("SetTextI18n")
    @Override


    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, final int position) {
        Log.d("BindViewHolder", "in onBindViewHolder");
        ((CreditViewHolder) holder).populate(creditList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return creditList.size();
    }

    public class CreditViewHolder extends RecyclerView.ViewHolder {
        TextView name, amount;
        ImageView deleteButton, editButton, itemIndicator;

        public CreditViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            Log.d("Viewholder", "In creditviewholder");
            name = itemView.findViewById(R.id.item_name);
            amount = itemView.findViewById(R.id.item_amount);
            deleteButton = itemView.findViewById(R.id.delete_button);
            itemIndicator = itemView.findViewById(R.id.item_indicator);
            editButton = itemView.findViewById(R.id.edit_item);
        }

        public void populate(ItemList item, int position) {
            name.setText(item.getItemName());
            amount.setText("₹ " + item.getItemAmount());
//            GlobalContent.setTotalCredit(GlobalContent.getTotalCreditAmount()+item.getItemAmount());
//            GlobalContent.addCredit(item.getItemAmount());
            itemIndicator.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
            deleteButton.setOnClickListener(view -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(activityFragment.getActivity());

                builder.setMessage("Do you want to delete " + item.getItemName() + " ?");
                builder.setPositiveButton(R.string.yes, (dialog, id) -> {
                    // User clicked OK button
                    // Get the clicked item label
                    String itemLabel = creditList.get(position).getItemName();
                    // Remove the item on remove/button click
                    creditList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, creditList.size());

                    GlobalContent.setTotalCredit((GlobalContent.getTotalCreditAmount()-item.getItemAmount()));

                    // Show the removed item label`enter code here`
                    Toast.makeText(mcontext, "Deleted " + itemLabel, Toast.LENGTH_SHORT).show();
                    HomeFragment.updatecredit();
                    FirebaseHelper.updateCreditListInFirebase(creditList);

                });
                builder.setNegativeButton(R.string.no, (dialog, id) -> {
                    // User cancelled the dialog
                    dialog.dismiss();
                });
                AlertDialog dialog = builder.create();

                dialog.show();
            });

            editButton.setOnClickListener(view -> {
                AlertDialog.Builder alert = new AlertDialog.Builder(mcontext, R.style.MyAlertTheme);
                String itemLabel = creditList.get(position).getItemName();
                Integer oldItemAmount = creditList.get(position).getItemAmount();

                Button btn_cancel = (Button) mView.findViewById(R.id.cancel_button);
                Button btn_okay = (Button) mView.findViewById(R.id.create_item_button);
                btn_okay.setText(R.string.update);

                RadioButton creditRadio = mView.findViewById(R.id.credit_radio_button);
                creditRadio.setChecked(true);

                final EditText itemNameEditText = mView.findViewById(R.id.item_name_edittext);
                final EditText itemAmountEditText = mView.findViewById(R.id.item_amount_edittext);

                itemNameEditText.setText(itemLabel);
                itemAmountEditText.setText(oldItemAmount.toString());

                if (mView.getParent() != null)
                    ((ViewGroup) mView.getParent()).removeView(mView);
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
                        if (itemName != null && itemName.length() != 0) {
                            Log.d("itemName", itemName);
                            itemNameError = false;
                        } else {
                            Log.e("itemName", "Null");
                            itemNameEditText.setError("Enter Item Name");
                            itemNameError = true;
                        }
                        String itemAmountString = itemAmountEditText.getText().toString().trim();
                        int itemAmount;
                        if (!itemAmountString.equalsIgnoreCase("")) {
                            itemAmount = Integer.parseInt(itemAmountString);
                            itemAmountError = false;
                        } else {
                            itemAmount = 0;
                            Log.e("Item amount", "null");
                            itemAmountEditText.setError("Enter Item Amount");

                            itemAmountError = true;


                        }
                        if (!itemAmountError && !itemNameError) {
                            ItemList credit = null;
                            credit = new ItemList(itemName, itemAmount);
                            creditList.set(position, credit);
                            GlobalContent.setTotalCredit((GlobalContent.getTotalCreditAmount()-oldItemAmount+itemAmount));
                            HomeFragment.updatecredit();
                            notifyItemChanged(position);
                            notifyDataSetChanged();
                            FirebaseHelper.updateCreditListInFirebase(creditList);
                            alertDialog.dismiss();
                        }
                    }
                });

                alertDialog.show();


            });
        }

    }


}
