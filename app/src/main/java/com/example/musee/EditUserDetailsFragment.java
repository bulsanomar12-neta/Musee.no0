package com.example.musee;

import android.Manifest;
import android.content.Context;
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
    private FirebaseServices fbs;
    ///private Messaging msg;
    private LocationManager locationManager;
    private boolean flagAlreadtFilled = false;
    private UtilsClass util;




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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditUserDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        return inflater.inflate(R.layout.fragment_edit_user_details, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        fbs=FirebaseServices.getInstance();
        etFirstNameEditUserDetails=getView().findViewById(R.id.etFirstNameEditUserDetails);
        etLastNameEditUserDetails=getView().findViewById(R.id.etLastNameEditUserDetails);
        etUserNameEditUserDetails2=getView().findViewById(R.id.etUserNameEditUserDetails2);
        //etEmailEditUserDetails=getView().findViewById(R.id.etEmailEditUserDetails);
        etPhoneNumEditUserDetails=getView().findViewById(R.id.etPhoneNumEditUserDetails);
        etAddressEditUserDetails=getView().findViewById(R.id.etAddressEditUserDetails);
        etAddressEditUserDetails.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                try {
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return false;
                    }
                    Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    double longitude = location.getLongitude();
                    double latitude = location.getLatitude();
                    //location.
                    etAddressEditUserDetails.setText(String.valueOf(longitude) + "," + String.valueOf(latitude));
                    //gotoMapAddressFragment();
                }
                catch (Exception ex)
                {
                    Log.e("Err", ex.getMessage());
                }
                return false;
            }
        });
        /*
        if(imageStr == null){
            Glide.with(getContext()).load(com.google.android.gms.base.R.drawable.common_google_signin_btn_text_dark_focused).into(ivUser);}
         */
        btUpdateEditUserDetails=getView().findViewById(R.id.btUpdateEditUserDetails);
        btUpdateEditUserDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = etFirstNameEditUserDetails.getText().toString().trim();
                String lastName = etLastNameEditUserDetails.getText().toString().trim();
                String userName = etUserNameEditUserDetails2.getText().toString().trim();
                //String email = etEmailEditUserDetails.getText().toString().trim();
                String phoneNum = etPhoneNumEditUserDetails.getText().toString().trim();
                String address = etAddressEditUserDetails.getText().toString().trim();
                if(firstName.trim().isEmpty() || lastName.trim().isEmpty() || userName.trim().isEmpty() ||
                        phoneNum.trim().isEmpty() || address.trim().isEmpty()){
                    Toast.makeText(getActivity(), "some fields are empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!checkAddressFormat(address)) {
                    util.showMessageDialog(getActivity(), "Incorrect address format. Delete any " +
                            "remaining characters. Long click, and app will enter the location for you.");
                    return;
                }
                User u2;
                /// /////////////////////////
                Optional<User> current = fbs.getUsers().stream().filter(b -> b.getUserName().
                        equals(fbs.getAuth().getCurrentUser().getEmail())).findFirst();
                if(current != null)
                {
                    if (!current.get().getFirstName().equals(firstName) ||
                        !current.get().getLastName().equals(lastName) ||
                        !current.get().getUserName().equals(userName) ||
                        !current.get().getPhoneNum().equals(phoneNum) ||
                        !current.get().getAddress().equals(address))
                    {
                        User user;
                        /*
                        if (fbs.getSelectedImageURL() != null)
                            user = new User(firstname, lastname, fbs.getAuth().getCurrentUser().getEmail(),
                                    current.get().getType(), address, address2, phone, fbs.getSelectedImageURL().toString());
                        else
                            user = new User(firstname, lastname, fbs.getAuth().getCurrentUser().getEmail(),
                                    current.get().getType(), address, address2, phone, "");
                         */
                        user = new User(firstName, lastName, userName, phoneNum, address);
                        fbs.updateUser(user);
                        util.showMessageDialog(getActivity(), "Data updated succesfully!");
                    }
                    else {
                        util.showMessageDialog(getActivity(), "No changes were made!");
                    }
                }
            }
        });
        /*
        ivUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
         */
        fillUserData();
        flagAlreadtFilled = true;
        requestLocationPermission();
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

    private void fillUserData() {
        if (flagAlreadtFilled)
            return;
        Optional<User> current = fbs.getUsers().stream()
                .filter(b -> b.getUserName().equals(fbs.getAuth().getCurrentUser().getEmail())).findFirst();
        if (current != null)
        {
            etFirstNameEditUserDetails.setText(current.get().getFirstName());
            etLastNameEditUserDetails.setText(current.get().getLastName());
            etUserNameEditUserDetails2.setText(current.get().getUserName());
            etAddressEditUserDetails.setText(current.get().getAddress());
            etPhoneNumEditUserDetails.setText(current.get().getPhoneNum());

            /*
            if (current.get().getPhoto() != null && !current.get().getPhoto().isEmpty()) {
                Picasso.get().load(current.get().getPhoto()).into(ivUser);
                fbs.setSelectedImageURL(Uri.parse(current.get().getPhoto()));

            }

             */
        }
    }
        private boolean checkAddressFormat(String address) {
            try {
                String[] arr = address.split(",");
                if (Arrays.stream(arr).count() != 2)
                    return false;
                double lat = Double.parseDouble(arr[0]);
                double lng = Double.parseDouble(arr[1]);
                return  true;
            } catch (NumberFormatException e) {
                Log.e("SignupFragment: checkAddressFormat: ", "String is not parseable to double.");
                return false;
            }
        }
}