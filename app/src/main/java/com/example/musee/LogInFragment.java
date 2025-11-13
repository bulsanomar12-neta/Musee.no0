package com.example.musee;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musee.classes.FirebaseServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LogInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LogInFragment extends Fragment {
    private EditText etUserName, etPassword;
    private TextView  tvSignUpLink;
    private TextView tvForgotPasswordLogIn;
    private Button btLogIn;
    private FirebaseServices fbs;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LogInFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LogInFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LogInFragment newInstance(String param1, String param2) {
        LogInFragment fragment = new LogInFragment();
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
        return inflater.inflate(R.layout.fragment_log_in, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        // connecting companions
        // R is class that have all the valls
        fbs = FirebaseServices.getInstance();
        etUserName = getView().findViewById(R.id.etUserNameLogIn);
        etPassword = getView().findViewById(R.id.etPasswordLogIn);
        tvSignUpLink = getView().findViewById(R.id.tvSignUpLinkLogIn);
        tvSignUpLink.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              gotoSignUpFragment();//<-------------------------------------------------
          }
          });
        tvForgotPasswordLogIn = getView().findViewById(R.id.tvForgotPasswordLogIn);
        tvForgotPasswordLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoForgotPasswordFragment();//<---------------------------------------
            }
        });
        btLogIn = getActivity().findViewById(R.id.btLogInLogIn);
        btLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Data chick
                // trim() cut the space
                String username = etUserName.getText().toString();
                String password = etPassword.getText().toString();
                if (username.trim().isEmpty() || password.trim().isEmpty()){
                    Toast.makeText(getActivity(), "some fields are empty", Toast.LENGTH_LONG).show();
                    return;
                }
                // LodIN
                fbs.getAuth().signInWithEmailAndPassword(username , password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
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
    private void gotoSignUpFragment() {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();//.getActivity()=> لاننا ب fragment  وليس ب activity.
        ft.replace(R.id.frameLayOutMain, new SignUpFragment());// ادخال من والى
        ft.commit();
    }

    private void gotoForgotPasswordFragment() {
        FragmentTransaction ft1 = getActivity().getSupportFragmentManager().beginTransaction();//.getActivity()=> لاننا ب fragment  وليس ب activity.
        ft1.replace(R.id.frameLayOutMain, new ForgotPasswordFragment());// ادخال من والى
        ft1.commit();
    }
}