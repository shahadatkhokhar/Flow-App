package com.expenses.flow;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileScreen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileScreen extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView nameView;
    private FloatingActionButton editProfileButton;
    private FloatingActionButton saveProfileButton;
    private TextView profilePictureUpdateButton;
    private static ImageView profilePicture;


    public ProfileScreen() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileScreen.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileScreen newInstance(String param1, String param2) {
        ProfileScreen fragment = new ProfileScreen();
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
        return inflater.inflate(R.layout.fragment_profile_screen, container, false);
    }

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            uri -> {
                // Handle the returned Uri
                Log.d("Activity result",uri+"");
                profilePicture.setImageURI(Uri.parse(String.valueOf(uri)));
                Bitmap image = null;
                try {
                    image = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                GlobalContent.setProfileImage(image);

            });

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final int PICK_IMAGE = 1;

        nameView = getView().findViewById(R.id.edit_profile_name);
        nameView.setText(GlobalContent.getUserName());
        editProfileButton = getView().findViewById(R.id.profile_screen_edit_button);
        saveProfileButton = getView().findViewById(R.id.profile_save_button);
        profilePictureUpdateButton = getView().findViewById(R.id.profile_picture_update_button);
        saveProfileButton.setVisibility(View.GONE);
        profilePicture = getView().findViewById(R.id.more_screen_image);
        if(GlobalContent.getProfileImage()!=null){
            profilePicture.setImageBitmap(GlobalContent.getProfileImage());
        }

        nameView.setCursorVisible(false);
        nameView.setFocusable(false);

//        nameView.setEnabled(false);
        nameView.setClickable(false);
        nameView.setFocusableInTouchMode(false);

        final String[] newName = new String[1];

        nameView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                newName[0] = s.toString();

                Log.d("name",newName[0]);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        profilePictureUpdateButton.setVisibility(View.GONE);

        profilePictureUpdateButton.setOnClickListener(image->{
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);

            mGetContent.launch("image/*");
        });


        editProfileButton.setOnClickListener(v->{
            nameView.setBackground(getContext().getResources().getDrawable(R.drawable.round_edittext_profilescreen));
            saveProfileButton.setVisibility(View.VISIBLE);
            editProfileButton.setVisibility(View.GONE);
            nameView.setCursorVisible(true);
            nameView.setFocusable(true);
//            nameView.setEnabled(true);
            nameView.setClickable(true);
            nameView.setFocusableInTouchMode(true);
            profilePictureUpdateButton.setVisibility(View.VISIBLE);

        });

        saveProfileButton.setOnClickListener(v->{
            nameView.setBackgroundColor(Color.parseColor("#ffffff"));

            if(newName[0]==null || newName[0].trim().equalsIgnoreCase("")){
                GlobalContent.setUserName(GlobalContent.getUserName());

            }
            else{
                GlobalContent.setUserName(newName[0]);
                Log.d("newname",newName[0]+"");
            }
            saveProfileButton.setVisibility(View.GONE);
            editProfileButton.setVisibility(View.VISIBLE);
            nameView.setCursorVisible(false);
            nameView.setFocusable(false);
            nameView.setClickable(false);
            nameView.setFocusableInTouchMode(false);
            profilePictureUpdateButton.setVisibility(View.GONE);




        });



    }
}