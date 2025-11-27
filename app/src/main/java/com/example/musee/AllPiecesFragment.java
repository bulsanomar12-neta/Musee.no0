package com.example.musee;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.musee.classes.AllPiecesAdapter;
import com.example.musee.classes.FirebaseServices;
import com.example.musee.classes.PieceClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllPiecesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllPiecesFragment extends Fragment {
    private RecyclerView rvAllPiecesFragment;
    private FirebaseServices fbs;
    private AllPiecesAdapter myAdapter;
    private ArrayList<PieceClass> pieces, filteredList;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AllPiecesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllPiecesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllPiecesFragment newInstance(String param1, String param2) {
        AllPiecesFragment fragment = new AllPiecesFragment();
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
        return inflater.inflate(R.layout.fragment_all_pieces, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    private void init() {
        rvAllPiecesFragment = getView().findViewById(R.id.rvAllPiecesFragment);
        //ivProfile = getView().findViewById(R.id.ivProfileCarListMapFragment);
        fbs = FirebaseServices.getInstance();
        fbs.setUserChangeFlag(false);
        /*if (fbs.getAuth().getCurrentUser() == null)
            fbs.setCurrentUser(fbs.getCurrentObjectUser()); */
        pieces = new ArrayList<>();

        rvAllPiecesFragment.setHasFixedSize(true);
        rvAllPiecesFragment.setLayoutManager(new LinearLayoutManager(getActivity()));

        pieces = getPieces();
        myAdapter = new AllPiecesAdapter(getActivity(), pieces);
        filteredList = new ArrayList<>();

        myAdapter.setOnItemClickListener(new AllPiecesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Handle item click here
                String selectedItem = pieces.get(position).getArtistName();
                Toast.makeText(getActivity(), "Clicked: " + selectedItem, Toast.LENGTH_SHORT).show();

                Bundle args = new Bundle();
                ///args.putParcelable("piece", (Parcelable) pieces.get(position)); // or use Parcelable for better performance
                //CarDetailsFragment cd = new CarDetailsFragment();
                //cd.setArguments(args);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                //ft.replace(R.id.frameLayout,cd);
                ft.commit();
            }
        });
    }

    /// /////////////////////
    ///private void applyFilter(String query) {}

    public ArrayList<PieceClass> getPieces()
    {
        ArrayList<PieceClass> pieces = new ArrayList<>();

        try {
            pieces.clear();
            fbs.getFire().collection("pieces")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    pieces.add(document.toObject(PieceClass.class));
                                }

                                AllPiecesAdapter adapter = new AllPiecesAdapter(getActivity(), pieces);
                                rvAllPiecesFragment.setAdapter(adapter);
                                //addUserToCompany(companies, user);
                            } else {
                                //Log.e("AllRestActivity: readData()", "Error getting documents.", task.getException());
                            }
                        }
                    });
        }
        catch (Exception e)
        {
            Log.e("getCompaniesMap(): ", e.getMessage());
        }

        return pieces;
    }
}