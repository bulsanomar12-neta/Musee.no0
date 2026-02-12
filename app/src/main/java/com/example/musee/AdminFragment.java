package com.example.musee;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.google.firebase.auth.FirebaseAuth;

public class AdminFragment extends Fragment {
    private Button btGoToAllAdminFragment, btGoToAddAdminFragmint, btSignOutAdminFragmint,btEditDetailsAdminFragmint;
    // We will initialize mAuth inside onStart
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        init(getView());
    }

    private void init(View view) {
        // Get the MainActivity once to call its public navigation methods
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity == null || view == null) { // Also check if the view is null
            return; // Exit if the activity or view is not available
        }
        // Initialize FirebaseAuth here.
        mAuth = FirebaseAuth.getInstance();

        // Go to Add Piece Fragment
        btGoToAddAdminFragmint = view.findViewById(R.id.btGoToAddAdminFragmint);
        btGoToAddAdminFragmint.setOnClickListener(v -> {
            mainActivity.gotoAddPieceFragment(); // Use MainActivity's method
        });

        // Go to ALL Pieces Fragment
        btGoToAllAdminFragment = view.findViewById(R.id.btGoToAllAdminFragment);
        btGoToAllAdminFragment.setOnClickListener(v -> {
            mainActivity.gotoAllPiecesFragment(); // Use MainActivity's method
        });

        // Sign Out
        btSignOutAdminFragmint = view.findViewById(R.id.btSignOutAdminFragmint);
        btSignOutAdminFragmint.setOnClickListener(v -> {
            // 1. Sign out from Firebase
            mAuth.signOut();
            // 2. Navigate back to the login fragment using MainActivity's method
            mainActivity.gotoLogInFragment();
        });

        btEditDetailsAdminFragmint = view.findViewById(R.id.btEditDetailsAdminFragmint);
        btEditDetailsAdminFragmint.setOnClickListener(v -> {
            mainActivity.gotoEditUserDetailsFragment();
        });
    }


    /*
    private void gotoAllPiecesFragment() {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();//.getActivity()=> لاننا ب fragment  وليس ب activity.
        ft.replace(R.id.frameLayOutMain, new AllPiecesFragment());// ادخال من والى
        ft.commit();
    }
    private void gotoAddPieceFragment() {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();//.getActivity()=> لاننا ب fragment  وليس ب activity.
        ft.replace(R.id.frameLayOutMain, new AddPieceFragment());// ادخال من والى
        ft.commit();
    }
    */
}
