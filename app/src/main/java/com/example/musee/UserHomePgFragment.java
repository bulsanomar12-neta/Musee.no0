package com.example.musee;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import android.app.AlertDialog;
import android.widget.Toast;

public class UserHomePgFragment extends Fragment {
    private Button btGoToAllUserHomePgFragment, btGoToAddUserHomePgFragmint, btSignOutUserHomePgFragmint,btEditDetailsUserHomePgFragmint, btDeleteAccountUserHomePgFragment;
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
        return inflater.inflate(R.layout.fragment_user_homepg, container, false);
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
        btGoToAddUserHomePgFragmint = view.findViewById(R.id.btGoToAddUserHomePgFragmint);
        btGoToAddUserHomePgFragmint.setOnClickListener(v -> {
            mainActivity.gotoAddPieceFragment(); // Use MainActivity's method
        });

        // Go to ALL Pieces Fragment
        btGoToAllUserHomePgFragment = view.findViewById(R.id.btGoToAllUserHomePgFragment);
        btGoToAllUserHomePgFragment.setOnClickListener(v -> {
            mainActivity.gotoAllPiecesFragment(); // Use MainActivity's method
        });

        // Sign Out
        btSignOutUserHomePgFragmint = view.findViewById(R.id.btSignOutUserHomePgFragmint);
        btSignOutUserHomePgFragmint.setOnClickListener(v -> {
            // 1. Sign out from Firebase
            mAuth.signOut();
            // 2. Navigate back to the login fragment using MainActivity's method
            mainActivity.gotoLogInFragment();
        });

        btEditDetailsUserHomePgFragmint = view.findViewById(R.id.btEditDetailsUserHomePgFragmint);
        btEditDetailsUserHomePgFragmint.setOnClickListener(v -> {
            mainActivity.gotoEditUserDetailsFragment();
        });

        btDeleteAccountUserHomePgFragment = view.findViewById(R.id.btDeleteAccountUserHomePgFragment);

        btDeleteAccountUserHomePgFragment.setOnClickListener(v -> {

            new AlertDialog.Builder(getContext())
                    .setTitle("Delete Account")
                    .setMessage("Are you sure you want to delete your account?")
                    .setPositiveButton("Yes", (dialog, which) -> deleteAccount())
                    .setNegativeButton("No", null)
                    .show();

        });
    }


    private void deleteAccount()
    {
            if (mAuth.getCurrentUser() == null) {
                Toast.makeText(getContext(),
                        "No user logged in",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            String uid = mAuth.getCurrentUser().getUid();

            // حذف بيانات المستخدم من Firestore
            db.collection("users")
                    .document(uid)
                    .delete()
                    .addOnSuccessListener(aVoid -> {

                        // حذف الحساب من Firebase Auth
                        mAuth.getCurrentUser()
                                .delete()
                                .addOnSuccessListener(aVoid1 -> {

                                    Toast.makeText(getContext(),
                                            "Account deleted",
                                            Toast.LENGTH_SHORT).show();

                                    MainActivity mainActivity = (MainActivity) getActivity();

                                    if(mainActivity != null){
                                        mainActivity.gotoLogInFragment();
                                    }

                                })
                                .addOnFailureListener(e -> {   // ← هنا
                                    Toast.makeText(getContext(),
                                            "Failed to delete auth account",
                                            Toast.LENGTH_SHORT).show();
                                });

                    })
                    .addOnFailureListener(e -> {   // ← وهنا
                        Toast.makeText(getContext(),
                                "Failed to delete user data",
                                Toast.LENGTH_SHORT).show();
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
