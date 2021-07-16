package com.expenses.flow;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.expenses.flow.ItemList;

import com.expenses.flow.RecyclerViewList.customListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.skydoves.powerspinner.PowerSpinnerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ArrayList<ItemList> mArrayList = new ArrayList<>();
    private RecyclerView mRecyclerView1;
    private customListAdapter mAdapter;
    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        PowerSpinnerView powerSpinner = view.findViewById(R.id.power_spinner);
        FloatingActionButton addItemFAB = view.findViewById(R.id.add_item_fab);
        ImageView editButton = view.findViewById(R.id.edit_item);

        mRecyclerView1 = view.findViewById(R.id.main_list_recyclerview);
        mAdapter = new customListAdapter(mArrayList,this);

        mRecyclerView1.setAdapter(mAdapter);
        mRecyclerView1.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        mRecyclerView1.setItemAnimator( new DefaultItemAnimator());
        mRecyclerView1.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        if(powerSpinner !=null) {
            powerSpinner.selectItemByIndex(0);
            Log.e("powerspinner", " set");
        }
        else {
            Log.e("powerspinner", "not set");
        }

        addItemFAB.setOnClickListener(v->{
            ShowItemCreationDialog();
        });
        if(editButton!=null){
        editButton.setOnClickListener(v->{
            ShowItemEditDialog();
        });
        }

    }
    public void ShowItemCreationDialog(){
        final AlertDialog.Builder alert = new AlertDialog.Builder(getContext(),R.style.MyAlertTheme);
        View mView = getLayoutInflater().inflate(R.layout.item_entry_dialog,null);
        Button btn_cancel = (Button)mView.findViewById(R.id.cancel_button);
        Button btn_okay = (Button)mView.findViewById(R.id.create_item_button);

        final EditText itemNameEditText = mView.findViewById(R.id.item_name_edittext);
        final EditText itemAmountEditText = mView.findViewById(R.id.item_amount_edittext);

        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
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
                    mArrayList.add(credit);
                    mAdapter.notifyDataSetChanged();
                    alertDialog.dismiss();
//                updateUI();
                }
            }
        });
        alertDialog.show();
    }
    public void ShowItemEditDialog(){
        View mView = getLayoutInflater().inflate(R.layout.item_entry_dialog,null);
        final AlertDialog.Builder alert = new AlertDialog.Builder(getContext(),R.style.MyAlertTheme);
        alert.setView(mView);
        Button btn_cancel = mView.findViewById(R.id.cancel_button);
        Button btn_okay = mView.findViewById(R.id.create_item_button);



        btn_okay.setText("Save");
        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);
        btn_cancel.setOnClickListener(v -> alertDialog.dismiss());
        btn_okay.setOnClickListener(v -> {
            Log.d("Dialog", "OK pressed");

            alertDialog.dismiss();
        });
        alertDialog.show();
    }

}