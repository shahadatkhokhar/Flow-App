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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
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
    FirebaseFirestore db;
    FirebaseStorage storage;
    LinearLayout imageUploadingIndicator;
    Button logoutButton;
    EditText nameEdit;

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
        storage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_screen, container, false);
    }



    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final int PICK_IMAGE = 1;

        nameView = requireView().findViewById(R.id.profile_name_display);
        nameView.setText(GlobalContent.getUserName());
        editProfileButton = getView().findViewById(R.id.profile_screen_edit_button);
        saveProfileButton = getView().findViewById(R.id.profile_save_button);
        profilePictureUpdateButton = getView().findViewById(R.id.profile_picture_update_button);
        saveProfileButton.setVisibility(View.GONE);
        profilePicture = getView().findViewById(R.id.more_screen_image);
        imageUploadingIndicator = getView().findViewById(R.id.image_uploading_indicator);
        logoutButton = getView().findViewById(R.id.logout_button);
        nameEdit = getView().findViewById(R.id.profile_name_edit);
        if(GlobalContent.getProfileImage()!=null){
            profilePicture.setImageBitmap(GlobalContent.getProfileImage());
        }
        nameEdit.setText(GlobalContent.getUserName());

        profilePictureUpdateButton.setVisibility(View.GONE);

        profilePictureUpdateButton.setOnClickListener(image->{
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);

            mGetContent.launch("image/*");
        });


        editProfileButton.setOnClickListener(v->{
            saveProfileButton.setVisibility(View.VISIBLE);
            editProfileButton.setVisibility(View.GONE);
            nameView.setVisibility(View.GONE);
            nameEdit.setVisibility(View.VISIBLE);
            profilePictureUpdateButton.setVisibility(View.VISIBLE);

        });

        saveProfileButton.setOnClickListener(v->{
            String newName = nameEdit.getText().toString();


            if(newName.trim().equalsIgnoreCase("")){
                GlobalContent.setUserName(GlobalContent.getUserName());

            }
            else{
                GlobalContent.setUserName(newName);
                Log.d("newname",newName+"");
                nameView.setText(newName);
                updateNameInFirebase();
            }
            saveProfileButton.setVisibility(View.GONE);
            editProfileButton.setVisibility(View.VISIBLE);
            profilePictureUpdateButton.setVisibility(View.GONE);
            nameView.setVisibility(View.VISIBLE);
            nameEdit.setVisibility(View.GONE);

        });

        logoutButton.setOnClickListener(v->{
            FirebaseAuth.getInstance().signOut();
            getActivity().startActivity(new Intent(getContext(),LoginScreen.class));
            getActivity().finish();
        });

    }

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            uri -> {
                // Handle the returned Uri
                Log.d("Activity result",uri+"");
                if(uri!=null){
                    saveProfileButton.setVisibility(View.GONE);
                    imageUploadingIndicator.setVisibility(View.VISIBLE);
                    profilePicture.setImageURI(Uri.parse(String.valueOf(uri)));
                    Bitmap image = null;
                    try {
                        image = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    GlobalContent.setProfileImage(image);

                    StorageReference storageRef = storage.getReference();

                    StorageReference profileImg = storageRef.child("ProfileImages/" + GlobalContent.getUserEmail());

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data = baos.toByteArray();

                    UploadTask uploadTask = profileImg.putBytes(data);

                    Task<Uri> urlTask = uploadTask.continueWithTask(task -> {
                        if (!task.isSuccessful()) throw task.getException();

                        // Continue with the task to get the download URL
                        return profileImg.getDownloadUrl();
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                Log.d("Downloaduri", downloadUri.toString());
                                GlobalContent.setProfileImageUrl(downloadUri.toString());

                                db.collection("Users").document(GlobalContent.getUserEmail())
                                        .update("ProfileImage", downloadUri.toString())
                                        .addOnSuccessListener(documentReference -> {
                                            Log.d("Success", "DocumentSnapshot Updated Profile Pic ");
                                            Toast.makeText(getContext(),"Profile Picture Updated" ,Toast.LENGTH_SHORT).show();

                                            saveProfileButton.setVisibility(View.VISIBLE);
                                            imageUploadingIndicator.setVisibility(View.GONE);
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.w("Failure", "Error updating Profile Pic", e);
                                            saveProfileButton.setVisibility(View.VISIBLE);
                                            imageUploadingIndicator.setVisibility(View.GONE);
                                        });
                            }

                        }
                    });
                }

            });

    private void updateNameInFirebase(){
        db.collection("Users").document(GlobalContent.getUserEmail())
                .update("Name",GlobalContent.getUserName())
                .addOnSuccessListener(documentReference -> Log.d("Success", "DocumentSnapshot Updated Name "))
                .addOnFailureListener(e -> Log.w("Failure", "Error updating Name", e));
    }
}