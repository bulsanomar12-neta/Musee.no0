package com.example.musee;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.musee.classes.FirebaseServices;
import com.example.musee.classes.MyLocationListener;
import com.example.musee.classes.User;
import com.example.musee.classes.UtilsClass;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.Optional;

import pub.devrel.easypermissions.EasyPermissions;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditUserDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditUserDetailsFragment extends Fragment {

    private static final int GALLERY_REQUEST_CODE = 134;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 123;
    private EditText etFirstNameEditUserDetails, etLastNameEditUserDetails, etUserNameEditUserDetails2,
            etPhoneNumEditUserDetails, etAddressEditUserDetails;
    ///private ImageView ivUser;
    private Button btUpdateEditUserDetails;
    private ImageView imgUserEditUserDetails;
    private FirebaseServices fbs;
    private UtilsClass util;

    ///private Messaging msg;
    private LocationManager locationManager;
    private Location currentLocation;
    private Uri selectedImageUri;
    private boolean flagAlreadyFilled = false;




    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditUserDetailsFragment() {
        // Required empty public constructor
    }

    public static EditUserDetailsFragment newInstance(String param1, String param2) {
        EditUserDetailsFragment fragment = new EditUserDetailsFragment();
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
        View view = inflater.inflate(R.layout.fragment_edit_user_details, container, false);

        etFirstNameEditUserDetails = view.findViewById(R.id.etFirstNameEditUserDetails);
        etLastNameEditUserDetails = view.findViewById(R.id.etLastNameEditUserDetails);
        etUserNameEditUserDetails2 = view.findViewById(R.id.etUserNameEditUserDetails2);
        etPhoneNumEditUserDetails = view.findViewById(R.id.etPhoneNumEditUserDetails);
        etAddressEditUserDetails = view.findViewById(R.id.etAddressEditUserDetails);
        btUpdateEditUserDetails = view.findViewById(R.id.btUpdateEditUserDetails);

        imgUserEditUserDetails = view.findViewById(R.id.imgUserEditUserDetailsFragment);
        imgUserEditUserDetails.setOnClickListener(v -> openGallery());

        util = new UtilsClass();
        fbs = FirebaseServices.getInstance();

        // Address long click -> simple location fill
        etAddressEditUserDetails.setOnLongClickListener(v -> {
            try {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) !=
                                PackageManager.PERMISSION_GRANTED) {
                    // ملاحظة: في المستقبل يمكن طلب الإذن هنا
                    return false;
                }
                if (locationManager == null)
                    locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    // ملاحظة: ترتيب lat,long هو الأفضل
                    etAddressEditUserDetails.setText(location.getLatitude() + "," + location.getLongitude());
                } else {
                    Toast.makeText(getActivity(), "Location not available yet", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception ex) {
                Log.e("EditUserDetails", "Address long click error: " + ex.getMessage());
            }
            return true;
        });

        // Fill current user data
        fillUserData();

        // Update button
        btUpdateEditUserDetails.setOnClickListener(v -> updateUserData());

        return view;
    }
/*
    private void fillUserData() {
        if (flagAlreadyFilled) return;

        Optional<User> current = fbs.getUsers().stream()
                .filter(u -> u.geteMail().equals(fbs.getAuth().getCurrentUser().getEmail()))
                .findFirst();

        if (current.isPresent()) {
            User user = current.get();
            etFirstNameEditUserDetails.setText(user.getFirstName());
            etLastNameEditUserDetails.setText(user.getLastName());
            etUserNameEditUserDetails2.setText(user.getUserName());
            etAddressEditUserDetails.setText(user.getAddress());
            etPhoneNumEditUserDetails.setText(user.getPhoneNum());

            if (user.getPhoto() != null && !user.getPhoto().isEmpty()) {
                Picasso.get().load(user.getPhoto()).into(imgUserEditUserDetails);
            }
        }
        flagAlreadyFilled = true;
    }
 */
private void fillUserData() {

    if (flagAlreadyFilled) return;

    //  الحصول على UID بدلاً من البحث في القائمة
    String uid = fbs.getAuth().getCurrentUser().getUid();

    //  جلب المستخدم مباشرة من Firestore
    FirebaseFirestore.getInstance()
            .collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener(documentSnapshot -> {

                if (documentSnapshot.exists()) {

                    //  تحويل الوثيقة إلى كائن User
                    User user = documentSnapshot.toObject(User.class);

                    if (user != null) {

                        etFirstNameEditUserDetails.setText(user.getFirstName());
                        etLastNameEditUserDetails.setText(user.getLastName());
                        etUserNameEditUserDetails2.setText(user.getUserName());
                        etAddressEditUserDetails.setText(user.getAddress());
                        etPhoneNumEditUserDetails.setText(user.getPhoneNum());

                        if (user.getPhoto() != null && !user.getPhoto().isEmpty()) {
                            Picasso.get().load(user.getPhoto()).into(imgUserEditUserDetails);
                        }
                    }
                }

            })
            .addOnFailureListener(e ->
                    Log.e("EditUserDetails", "Error loading user: " + e.getMessage())
            );

    flagAlreadyFilled = true;
}

    private void updateUserData() {
        String firstName = etFirstNameEditUserDetails.getText().toString().trim();
        String lastName = etLastNameEditUserDetails.getText().toString().trim();
        String userName = etUserNameEditUserDetails2.getText().toString().trim();
        String phoneNum = etPhoneNumEditUserDetails.getText().toString().trim();
        String address = etAddressEditUserDetails.getText().toString().trim();

        if (firstName.isEmpty() || lastName.isEmpty() || userName.isEmpty() || phoneNum.isEmpty() || address.isEmpty()) {
            Toast.makeText(getActivity(), "Some fields are empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!checkAddressFormat(address)) {
            util.showMessageDialog(getActivity(), "Incorrect address format. Long click, app will enter location.");
            return;
        }

        // الحصول على UID الخاص بالمستخدم الحالي
        String uid = fbs.getAuth().getCurrentUser().getUid();
        DocumentReference userRef = FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid);

        // جلب المستند الحالي للمستخدم
        userRef.get().addOnSuccessListener(document -> {
            if (document.exists()) {
                User user = document.toObject(User.class);

                // تعديل الحقول
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setUserName(userName);
                user.setPhoneNum(phoneNum);
                user.setAddress(address);

                // رفع الصورة إذا اختار المستخدم واحدة
                if (selectedImageUri != null) {
                    StorageReference ref = FirebaseStorage.getInstance()
                            .getReference("profile_images/" + uid + ".jpg");

                    ref.putFile(selectedImageUri)
                            .continueWithTask(task -> ref.getDownloadUrl())
                            .addOnSuccessListener(uri -> {
                                user.setPhoto(uri.toString());
                                userRef.set(user) // تحديث المستند بالكامل بعد رفع الصورة
                                        .addOnSuccessListener(aVoid ->
                                                Toast.makeText(getActivity(), "Data updated successfully!", Toast.LENGTH_SHORT).show())
                                        .addOnFailureListener(e ->
                                                Toast.makeText(getActivity(), "Failed to update data", Toast.LENGTH_SHORT).show());
                            })
                            .addOnFailureListener(e ->
                                    Toast.makeText(getActivity(), "Failed to upload image", Toast.LENGTH_SHORT).show());
                } else {
                    // تحديث البيانات بدون صورة
                    userRef.set(user)
                            .addOnSuccessListener(aVoid ->
                                    Toast.makeText(getActivity(), "Data updated successfully!", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e ->
                                    Toast.makeText(getActivity(), "Failed to update data", Toast.LENGTH_SHORT).show());
                }

            } else {
                Toast.makeText(getActivity(), "Current user not found!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e ->
                Toast.makeText(getActivity(), "Error fetching user data", Toast.LENGTH_SHORT).show());
    }

    private void requestLocationPermission() {
        if (EasyPermissions.hasPermissions(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            startLocationUpdates();
        } else {
            EasyPermissions.requestPermissions(
                    this,
                    "Location permission is required for this app",
                    LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION
            );
        }
    }

    private void startLocationUpdates() {
        locationManager = (LocationManager) ((MainActivity)getActivity()).getSystemService(Context.LOCATION_SERVICE);

        if (locationManager != null) {
            MyLocationListener locationListener = new MyLocationListener(getActivity());
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }

    private boolean checkAddressFormat(String address) {
            try {
                String[] arr = address.split(",");
                // كان: Arrays.stream(arr).count() != 2
                if (arr.length != 2) return false;
                double lat = Double.parseDouble(arr[0]);
                double lng = Double.parseDouble(arr[1]);
                return  true;
            } catch (NumberFormatException e) {
                Log.e("SignupFragment: checkAddressFormat: ", "String is not parseable to double.");
                return false;
            }
        }

    public void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            imgUserEditUserDetails.setImageURI(selectedImageUri);
        }
    }
}
