package com.expenses.flow;

import static com.expenses.flow.GlobalContent.getSavings;
import static com.expenses.flow.GlobalContent.getTotalCreditAmount;
import static com.expenses.flow.GlobalContent.getTotalDebitAmount;
import static com.expenses.flow.GlobalContent.setAll;
import static com.expenses.flow.GlobalContent.setCredit;
import static com.expenses.flow.GlobalContent.setDebit;
import static com.expenses.flow.GlobalContent.setTotalCredit;
import static com.expenses.flow.GlobalContent.setTotalDebit;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.expenses.flow.RecyclerViewList.creditListAdapter;
import com.expenses.flow.RecyclerViewList.debitListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

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
    private static final ArrayList<ItemList> allList = new ArrayList<>();
    static View globalView;
    static View itemEditDialogView;
    private static ArrayList<ItemList> debitList = new ArrayList<>();
    private static ArrayList<ItemList> creditList = new ArrayList<>();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    //recyclerview for debit
    private RecyclerView debitRecyclerView;
    private debitListAdapter debitAdapter;
    //recyclerview for credit
    private RecyclerView creditRecyclerView;
    private creditListAdapter creditAdapter;
    //recyclerview for all list
    private RecyclerView allRecyclerView;
    private ConcatAdapter allAdapter;
    private TextView debitAmount;
    private TextView creditAmount;
    private TextView savingsAmount;
    private ImageView profileImage;
    PowerSpinnerView powerSpinner;
    static int flag=0;


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
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        powerSpinner = view.findViewById(R.id.power_spinner);
        FloatingActionButton addItemFAB = view.findViewById(R.id.add_item_fab);
        ImageView editButton = view.findViewById(R.id.edit_item);
        globalView = view;

//        GlobalContent.setUserEmail("Khokharshahadat@gmail.com");
//        GlobalContent.setUserName("Shahadat Khokhar");

        profileImage = view.findViewById(R.id.home_profile_image);
        profileImage.setImageBitmap(GlobalContent.getProfileImage());

        debitRecyclerView = view.findViewById(R.id.debit_list_recyclerview);
        debitAdapter = new debitListAdapter(debitList, this);

        creditRecyclerView = view.findViewById(R.id.credit_list_recyclerview);
        creditAdapter = new creditListAdapter(creditList, this);

        allRecyclerView = view.findViewById(R.id.all_list_recyclerview);
        allAdapter = new ConcatAdapter(debitAdapter, creditAdapter);

        debitRecyclerView.setAdapter(debitAdapter);
        debitRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        debitRecyclerView.setItemAnimator(new DefaultItemAnimator());
        debitRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        creditRecyclerView.setAdapter(creditAdapter);
        creditRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        creditRecyclerView.setItemAnimator(new DefaultItemAnimator());
        creditRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        allRecyclerView.setAdapter(allAdapter);
        allRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        allRecyclerView.setItemAnimator(new DefaultItemAnimator());
        allRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        debitAmount = view.findViewById(R.id.debit_amount);
        debitAmount.setText("₹" + getTotalDebitAmount());

        creditAmount = view.findViewById(R.id.credit_amount);
        creditAmount.setText("₹" + getTotalCreditAmount());

        savingsAmount = view.findViewById(R.id.savings_amount);
        savingsAmount.setText("₹" + getSavings());

        profileImage.setOnClickListener(v->{
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new ProfileScreen(),"Profile")
                    .addToBackStack("Profile")
                    .commit();
        });

//        GlobalDBContents.readWriteTest(GlobalContent.getUserEmail());
        if(flag==0){
            if(GlobalContent.getDebitList()!=null)
            {
                debitList.addAll(GlobalContent.getDebitList());
                debitAdapter.notifyDataSetChanged();
            }
            if(GlobalContent.getCreditList()!=null)
            {
                creditList.addAll(GlobalContent.getCreditList());
                creditAdapter.notifyDataSetChanged();
            }
        }
        debitAdapter.notifyDataSetChanged();
        creditAdapter.notifyDataSetChanged();


        if (powerSpinner != null) {
            powerSpinner.selectItemByIndex(0);
            Log.e("powerspinner", " set");
        } else {
            Log.e("powerspinner", "not set");
        }
        assert powerSpinner != null;
        powerSpinner.setOnSpinnerItemSelectedListener((OnSpinnerItemSelectedListener<String>) (oldIndex, oldItem, newIndex, newItem) -> {
                    Log.d("PowerSPinner", "Selected " + newItem + " " + newIndex);
                    if (newItem.equalsIgnoreCase("Debit")) {
                        setDebit();
                        debitRecyclerView.setVisibility(View.VISIBLE);
                        creditRecyclerView.setVisibility(View.GONE);
                        allRecyclerView.setVisibility(View.GONE);

                    } else if (newItem.equalsIgnoreCase("Credit")) {
                        setCredit();
                        debitRecyclerView.setVisibility(View.GONE);
                        creditRecyclerView.setVisibility(View.VISIBLE);
                        allRecyclerView.setVisibility(View.GONE);
                    } else if (newItem.equalsIgnoreCase("All")) {
                        setAll();
                        debitRecyclerView.setVisibility(View.GONE);
                        creditRecyclerView.setVisibility(View.GONE);
                        allRecyclerView.setVisibility(View.VISIBLE);
                    }
                }
        );

        addItemFAB.setOnClickListener(v -> {
            ShowItemCreationDialog(view);
        });

    }



    public void ShowItemCreationDialog(View view) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(getContext(), R.style.MyAlertTheme);
        View mView = getLayoutInflater().inflate(R.layout.item_entry_dialog, null);
        itemEditDialogView = mView;
        Button btn_cancel = (Button) mView.findViewById(R.id.cancel_button);
        Button btn_okay = (Button) mView.findViewById(R.id.create_item_button);
        RadioGroup debitCreditRadioGroup = mView.findViewById(R.id.debit_credit_radiogroup);
        RadioButton creditRadio = mView.findViewById(R.id.credit_radio_button);
        creditRadio.setChecked(true);
        powerSpinner = view.findViewById(R.id.power_spinner);


        final EditText itemNameEditText = mView.findViewById(R.id.item_name_edittext);
        final EditText itemAmountEditText = mView.findViewById(R.id.item_amount_edittext);

        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(true);
        btn_cancel.setOnClickListener(v -> alertDialog.dismiss());
        btn_okay.setOnClickListener(new View.OnClickListener() {
            boolean itemNameError = false;
            boolean itemAmountError = false;

            @SuppressLint({"SetTextI18n", "NotifyDataSetChanged", "NonConstantResourceId"})
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
                    switch (debitCreditRadioGroup.getCheckedRadioButtonId()) {
                        case R.id.credit_radio_button:
                            ItemList credit = null;
                            credit = new ItemList(itemName, itemAmount);
                            creditList.add(credit);
                            powerSpinner.selectItemByIndex(1);
                            allList.add(credit);
                            creditAdapter.notifyDataSetChanged();
                            allAdapter.notifyDataSetChanged();

                            setTotalCredit((getTotalCreditAmount() + itemAmount));

                            creditAmount = view.findViewById(R.id.credit_amount);
                            creditAmount.setText("₹" + (getTotalCreditAmount()));

                            savingsAmount = view.findViewById(R.id.savings_amount);
                            savingsAmount.setText("₹" + (getSavings()));
                            alertDialog.dismiss();
                            FirebaseHelper.updateCreditListInFirebase(creditList);
                            break;

                        case R.id.debit_radio_button:
                            ItemList debit = null;
                            debit = new ItemList(itemName, itemAmount);
                            debitList.add(debit);
                            powerSpinner.selectItemByIndex(0);
                            allList.add(debit);
                            debitAdapter.notifyDataSetChanged();
                            allAdapter.notifyDataSetChanged();

                            setTotalDebit((getTotalDebitAmount() + itemAmount));

                            debitAmount = view.findViewById(R.id.debit_amount);
                            debitAmount.setText("₹" + (getTotalDebitAmount()));

                            savingsAmount = view.findViewById(R.id.savings_amount);
                            savingsAmount.setText("₹" + (getSavings()));

                            alertDialog.dismiss();
                            FirebaseHelper.updateDebitListInFirebase(debitList);

                            break;
                    }

                }
            }
        });
        alertDialog.show();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("paused","fragment paused");
        flag=1;
        powerSpinner.dismiss();
    }

    @Override
    public void onResume() {
        super.onResume();

        debitRecyclerView = getView().findViewById(R.id.debit_list_recyclerview);

        creditRecyclerView = getView().findViewById(R.id.credit_list_recyclerview);

        PowerSpinnerView powerSpinner = getView().findViewById(R.id.power_spinner);

        debitRecyclerView.setVisibility(View.VISIBLE);
        powerSpinner.selectItemByIndex(0);

        creditRecyclerView.setVisibility(View.GONE);
        allRecyclerView.setVisibility(View.GONE);
    }

    @SuppressLint("SetTextI18n")
    public static void updateDebit() {

        TextView debitAmount;
        TextView savingsAmount;
        debitAmount = globalView.findViewById(R.id.debit_amount);
        debitAmount.setText("₹" + (getTotalDebitAmount()));

        savingsAmount = globalView.findViewById(R.id.savings_amount);
        savingsAmount.setText("₹" + (getSavings()));
    }

    @SuppressLint("SetTextI18n")
    public static void updatecredit() {

        TextView creditAmount;
        TextView savingsAmount;
        creditAmount = globalView.findViewById(R.id.credit_amount);
        creditAmount.setText("₹" + (getTotalCreditAmount()));

        savingsAmount = globalView.findViewById(R.id.savings_amount);
        savingsAmount.setText("₹" + (getSavings()));
    }

}