package com.example.musee;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.musee.classes.FirebaseServices;
import com.example.musee.classes.UtilsClass;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpFragment extends Fragment {

    private EditText etGmailSignUp, etPasswordSignUp, etConfirmPasswordSignUp, etPhoneNumSignUp, etFirstNameSignUp, etLastNameSignUp, etUserNameSignUp;
    private Button btSignUp;
    private FirebaseServices fbs;

    private UtilsClass msg;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SignUpFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignUpFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignUpFragment newInstance(String param1, String param2) {
        SignUpFragment fragment = new SignUpFragment();
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
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        // connecting companions
        // R is class that have all the valls
        fbs = FirebaseServices.getInstance();
        etGmailSignUp = getView().findViewById(R.id.etGmailSignUp);
        etPasswordSignUp = getView().findViewById(R.id.etPasswordSignUp);
        etConfirmPasswordSignUp = getView().findViewById(R.id.etConfirmPasswordSignUp);
        etPhoneNumSignUp = getView().findViewById(R.id.tvPhoneNumSignUp);
        etFirstNameSignUp = getView().findViewById(R.id.etFirstNameSignUp);
        etLastNameSignUp = getView().findViewById(R.id.etLastNameSignUp);
        etUserNameSignUp = getView().findViewById(R.id.etUserNameSignUp);
        msg = UtilsClass.getInstance();
        btSignUp = getActivity().findViewById(R.id.btSignUpSignup);
        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Data chick
                // trim() cut the space
                String username = etUserNameSignUp.getText().toString().trim();
                String password = etPasswordSignUp.getText().toString().trim();
                String confirmPassword = etConfirmPasswordSignUp.getText().toString().trim();
                String phoneNum = etPhoneNumSignUp.getText().toString().trim();
                String firstName = etFirstNameSignUp.getText().toString().trim();
                String lastName = etLastNameSignUp.getText().toString().trim();
                String gmail = etGmailSignUp.getText().toString().trim();
                if (username.trim().isEmpty() || password.trim().isEmpty() || confirmPassword.trim().isEmpty() ||
                        phoneNum.trim().isEmpty() || firstName.trim().isEmpty() || lastName.trim().isEmpty() || gmail.trim().isEmpty()){
                    Toast.makeText(getActivity(), "some fields are empty", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!password.equals(confirmPassword)) {
                    msg.showMessageDialog(getActivity(), "Password are not identical!");
                    return;
                }
                // SignUp
                fbs.getAuth().createUserWithEmailAndPassword(gmail , password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "something went wrong shick again.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}