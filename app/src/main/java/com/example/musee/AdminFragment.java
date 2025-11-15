package com.example.musee;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class AdminFragment extends Fragment {
    private Button btGoToAllAdminFragment, btGoToAddAdminFragmint;


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
    public void onStart()
    {
        super.onStart();
        init();
    }

    private void init() {
        // go to Add pieces Fragment
        btGoToAddAdminFragmint = getView().findViewById(R.id.btGoToAddAdminFragmint);
        btGoToAddAdminFragmint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoAddPieceFragment();
            }
        });
        // go to ALL pieces Fragment
        btGoToAllAdminFragment = getView().findViewById(R.id.btGoToAllAdminFragment);
        btGoToAllAdminFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    gotoAllPiecesFragment();
            }
        });
    }


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
}