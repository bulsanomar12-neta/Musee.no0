package com.example.musee;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.musee.classes.AllPiecesAdapter;
import com.example.musee.classes.FirebaseServices;
import com.example.musee.classes.PieceClass;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class SearchPieceFragment extends Fragment {

    private Spinner categorySpinner, sizeSpinner;
    private EditText searchBar, minPriceEditText, maxPriceEditText;
    private Button searchButton, clearButton;

    private RecyclerView recyclerView;
    private AllPiecesAdapter myAdapter;
    private ArrayList<PieceClass> list, filteredList;

    private String[] categories = {"Select Category", "Oil painting", "acrylic painting", "watercolor painting",
            "pencil drawing", "digital drawing", "other.."};
    private String[] sizes = {"Select Size", "A5 5.8×8.3 in (14.8×21 cm)", "A4 8.3×11.7 in (21×29.7 cm)",
            "A3 11.7×16.5 in (29.7×42 cm)", "A2 16.5×23.4 in (42×59.4 cm)", "A1 23.4×33.1 in (59.4×84.1 cm)",
            "Letter 8.5×11 in (21.6×27.9 cm)", "Legal 8.5×14 in (21.6×35.6 cm)", "8×10 in (20×25 cm)", "9×12 in (23×30 cm)",
            "11×14 in (28×35 cm)", "12×16 in (30×40 cm)", "14×18 in (35×45 cm)", "16×20 in (40×50 cm)", "18×24 in (45×60 cm)",
            "20×24 in (50×60 cm)", "24×30 in (60×75 cm)", "24×36 in (60×90 cm)", "30×40 in (75×100 cm)", "36×48 in (90×120 cm)"};

    public SearchPieceFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_piece, container, false);

        searchBar = view.findViewById(R.id.searchBar);
        categorySpinner = view.findViewById(R.id.categorySpinner);
        sizeSpinner = view.findViewById(R.id.sizeSpinner);
        minPriceEditText = view.findViewById(R.id.minPriceEditText);
        maxPriceEditText = view.findViewById(R.id.maxPriceEditText);
        searchButton = view.findViewById(R.id.btnSearch);
        clearButton = view.findViewById(R.id.btnClear);
        recyclerView = view.findViewById(R.id.rvPieces);

        categorySpinner.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, categories));
        sizeSpinner.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, sizes));

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        list = new ArrayList<>();
        filteredList = new ArrayList<>();
        myAdapter = new AllPiecesAdapter(getActivity(), filteredList);
        recyclerView.setAdapter(myAdapter);

        getPieceList();

        searchButton.setOnClickListener(v -> performSearch());
        clearButton.setOnClickListener(v -> clearSelections());

        return view;
    }

    private void getPieceList() {
        FirebaseServices.getInstance().getFire().collection("pieces").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    list.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                        PieceClass piece = doc.toObject(PieceClass.class);
                        list.add(piece);
                    }
                    filteredList.clear();
                    filteredList.addAll(list);
                    myAdapter.notifyDataSetChanged();
                });
    }

    private void performSearch() {
        String searchText = searchBar.getText().toString().trim().toLowerCase();
        String selectedCategory = categorySpinner.getSelectedItem().toString();
        String selectedSize = sizeSpinner.getSelectedItem().toString();

        String minPriceText = minPriceEditText.getText().toString().trim();
        String maxPriceText = maxPriceEditText.getText().toString().trim();

        boolean categoryFlag = !selectedCategory.equals("Select Category");
        boolean sizeFlag = !selectedSize.equals("Select Size");
        boolean searchFlag = !searchText.isEmpty();
        boolean minPriceFlag = !minPriceText.isEmpty();
        boolean maxPriceFlag = !maxPriceText.isEmpty();

        double minPrice = 0, maxPrice = Double.MAX_VALUE;
        try { if(minPriceFlag) minPrice = Double.parseDouble(minPriceText); } catch(Exception e){ minPrice = 0; }
        try { if(maxPriceFlag) maxPrice = Double.parseDouble(maxPriceText); } catch(Exception e){ maxPrice = Double.MAX_VALUE; }

        filteredList.clear();

        for (PieceClass piece : list) {
            boolean matchCategory = !categoryFlag || piece.getCategory().equalsIgnoreCase(selectedCategory);
            boolean matchSize = !sizeFlag || piece.getSize().equalsIgnoreCase(selectedSize);

            boolean matchPrice = true;
            try {
                double piecePrice = Double.parseDouble(piece.getPrice());
                matchPrice = piecePrice >= minPrice && piecePrice <= maxPrice;
            } catch (Exception e) { matchPrice = false; }

            boolean matchSearchText = !searchFlag || piece.getId().toLowerCase().contains(searchText);

            if (matchCategory && matchSize && matchPrice && matchSearchText) {
                filteredList.add(piece);
            }
        }

        myAdapter.notifyDataSetChanged();
    }

    private void clearSelections() {
        searchBar.setText("");
        categorySpinner.setSelection(0);
        sizeSpinner.setSelection(0);
        minPriceEditText.setText("");
        maxPriceEditText.setText("");

        filteredList.clear();
        filteredList.addAll(list);
        myAdapter.notifyDataSetChanged();
    }
}