package com.expenses.flow;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.skydoves.powerspinner.PowerSpinnerView;

import org.jetbrains.annotations.NotNull;

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
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_home, null);
        PowerSpinnerView powerSpinner;

        powerSpinner = root.findViewById(R.id.power_spinner);
        powerSpinner.selectItemByIndex(0);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        PowerSpinnerView powerSpinner = view.findViewById(R.id.power_spinner);
        FloatingActionButton addItemFAB = view.findViewById(R.id.add_item_fab);
        ImageView editButton = view.findViewById(R.id.edit_item);
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
        editButton.setOnClickListener(v->{
            ShowItemEditDialog();
        });

    }
    public void ShowItemCreationDialog(){
        final AlertDialog.Builder alert = new AlertDialog.Builder(getContext(),R.style.MyAlertTheme);
        View mView = getLayoutInflater().inflate(R.layout.item_entry_dialog,null);
        Button btn_cancel = (Button)mView.findViewById(R.id.cancel_button);
        Button btn_okay = (Button)mView.findViewById(R.id.create_item_button);
        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        btn_okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Dialog", "OK pressed");
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }
    public void ShowItemEditDialog(){
        final AlertDialog.Builder alert = new AlertDialog.Builder(getContext(),R.style.MyAlertTheme);
        View mView = getLayoutInflater().inflate(R.layout.item_entry_dialog,null);
        Button btn_cancel = (Button)mView.findViewById(R.id.cancel_button);
        Button btn_okay = (Button)mView.findViewById(R.id.create_item_button);
        btn_okay.setText("Save");
        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        btn_okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Dialog", "OK pressed");
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

}