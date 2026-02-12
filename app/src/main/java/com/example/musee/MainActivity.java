package com.example.musee;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;

import com.example.musee.classes.FirebaseServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Stack;

public class MainActivity extends AppCompatActivity {
    private Stack<Fragment> fragmentStack = new Stack<>();
    private FirebaseServices fbs;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            return insets;
        });

        if (savedInstanceState == null) {
            // Initialize Firebase Auth
            mAuth = FirebaseAuth.getInstance();

            // Check if user is signed in (non-null).
            FirebaseUser currentUser = mAuth.getCurrentUser();

            // This 'if/else' is the key.
            if (currentUser == null) {
                // If no user is logged in, show the login page.
                gotoLogInFragment();
            } else {
                // If a user IS logged in, show the main content page.
                // This fixes the white screen and the login loop.
                gotoAdminFragment();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frameLayOutMain);
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    void gotoAllPiecesFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayOutMain, new AllPiecesFragment());
        ft.commit();
    }

    public void gotoLogInFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayOutMain, new LogInFragment());// ادخال من والى
        ft.commit();
    }


    public void gotoAddPieceFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayOutMain, new AddPieceFragment());// ادخال من والى
        ft.commit();
    }
    public void gotoAdminFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayOutMain, new AdminFragment());// ادخال من والى
        ft.commit();
    }

    public void pushFragment(Fragment fragment) {
        fragmentStack.push(fragment);
        /*
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit(); */
    }

    public void gotoEditUserDetailsFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayOutMain, new EditUserDetailsFragment());// ادخال من والى
        ft.commit();
    }
}