package com.example.musee;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musee.classes.AllPiecesAdapter;
import com.example.musee.classes.PieceClass;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UserHomePgFragment extends Fragment {

    private Button btGoToAllUserHomePgFragment;
    private MaterialButton btGoToAddUserHomePgFragmint;
    private View btEditDetailsUserHomePgFragmint;
    private ImageView imgMenuOptions, imgUserHome;
    private FirebaseAuth mAuth;

    // RecyclerViews
    private RecyclerView rvUserPieces;
    private RecyclerView rvCartPieces;

    // Adapters & Lists
    private AllPiecesAdapter userPiecesAdapter;
    private ArrayList<PieceClass> userPiecesList; // لوحات المستخدم

    private AllPiecesAdapter cartPiecesAdapter;
    private ArrayList<PieceClass> cartPiecesList; // لوحات العربة
    // إضافة أعلى مع العناصر الأخرى
    private TextView tvTotalCartPrice;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_homepg, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    private void init(View view) {
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity == null || view == null) return;

        mAuth = FirebaseAuth.getInstance();

        // العناصر الرئيسية
        imgMenuOptions = view.findViewById(R.id.imgMenuOptions);
        imgUserHome = view.findViewById(R.id.imgUserHome);
        btGoToAllUserHomePgFragment = view.findViewById(R.id.btGoToAllUserHomePgFragment);
        btGoToAddUserHomePgFragmint = view.findViewById(R.id.btGoToAddUserHomePgFragmint);
        btEditDetailsUserHomePgFragmint = view.findViewById(R.id.btEditDetailsUserHomePgFragmint);

        /// RecyclerView للوحة المستخدم
        rvUserPieces = view.findViewById(R.id.rvUserPiecesHomePgFragment);
        rvUserPieces.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        userPiecesList = new ArrayList<>();
        userPiecesAdapter = new AllPiecesAdapter(getContext(), userPiecesList);
        rvUserPieces.setAdapter(userPiecesAdapter);

        /// RecyclerView لعربة التسوق
        rvCartPieces = view.findViewById(R.id.rvCartUserHomePgFragment);
        rvCartPieces.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        cartPiecesList = new ArrayList<>();
        cartPiecesAdapter = new AllPiecesAdapter(getContext(), cartPiecesList);
        rvCartPieces.setAdapter(cartPiecesAdapter);

        tvTotalCartPrice = view.findViewById(R.id.tvTotalCartPrice);
        updateCartTotal(); // عرض المجموع الأولي (صفر قبل التحميل)

        // تحميل البيانات من Firestore
        if (mAuth.getCurrentUser() != null) {
            String uid = mAuth.getCurrentUser().getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("users")
                    .document(uid)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            // لوحات المستخدم
                            List<String> userPiecesIds = (List<String>) documentSnapshot.get("userPieces");
                            if (userPiecesIds != null && !userPiecesIds.isEmpty()) {
                                fetchPiecesByIds(userPiecesIds, userPiecesList, userPiecesAdapter, false);
                            }

                            // لوحات العربة
                            List<String> cartIds = (List<String>) documentSnapshot.get("userPiecesCart");
                            if (cartIds != null && !cartIds.isEmpty()) {
                                fetchPiecesByIds(cartIds, cartPiecesList, cartPiecesAdapter, true);
                            }
                        }
                    });
        }

        // تحميل صورة المستخدم
        loadUserImage();

        // أزرار التفاعل
        imgMenuOptions.setOnClickListener(v -> showMyMenu(mainActivity));
        btGoToAllUserHomePgFragment.setOnClickListener(v -> mainActivity.gotoAllPiecesFragment());
        btGoToAddUserHomePgFragmint.setOnClickListener(v -> mainActivity.gotoAddPieceFragment());
        btEditDetailsUserHomePgFragmint.setOnClickListener(v -> mainActivity.gotoEditUserDetailsFragment());
    }

    private void loadUserImage() {
        if (mAuth.getCurrentUser() != null) {
            String uid = mAuth.getCurrentUser().getUid();
            FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(uid)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String imageUrl = documentSnapshot.getString("photo");
                            if (imageUrl != null && !imageUrl.isEmpty()) {
                                Picasso.get()
                                        .load(imageUrl)
                                        .placeholder(android.R.drawable.ic_menu_gallery)
                                        .fit()
                                        .centerCrop()
                                        .into(imgUserHome);
                            }
                        }
                    });
        }
    }

    private void showMyMenu(MainActivity mainActivity) {
        PopupMenu popup = new PopupMenu(getContext(), imgMenuOptions);
        popup.getMenu().add("Sign Out");
        popup.getMenu().add("Delete Account");

        popup.setOnMenuItemClickListener(item -> {
            if (item.getTitle().equals("Sign Out")) {
                mAuth.signOut();
                mainActivity.gotoLogInFragment();
            } else if (item.getTitle().equals("Delete Account")) {
                showDeleteDialog();
            }
            return true;
        });
        popup.show();
    }

    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("حذف الحساب");
        builder.setMessage("هل أنت متأكد؟ لا يمكن التراجع عن هذا الفعل.");
        builder.setPositiveButton("نعم، احذف", (dialog, which) -> deleteAccount());
        builder.setNegativeButton("إلغاء", null);
        builder.show();
    }

    private void deleteAccount() {
        if (mAuth.getCurrentUser() == null) return;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String uid = mAuth.getCurrentUser().getUid();

        db.collection("users").document(uid).delete()
                .addOnSuccessListener(aVoid -> {
                    mAuth.getCurrentUser().delete()
                            .addOnSuccessListener(aVoid1 -> {
                                Toast.makeText(getContext(), "Account deleted", Toast.LENGTH_SHORT).show();
                                MainActivity mainActivity = (MainActivity) getActivity();
                                if (mainActivity != null) mainActivity.gotoLogInFragment();
                            })
                            .addOnFailureListener(e -> Toast.makeText(getContext(), "Auth deletion failed", Toast.LENGTH_SHORT).show());
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Firestore deletion failed", Toast.LENGTH_SHORT).show());
    }

    // جلب كل PieceClass حسب الـ ID وتحديث Adapter
    private void fetchPiecesByIds(List<String> ids, ArrayList<PieceClass> list, AllPiecesAdapter adapter, boolean isCart) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        for (String pieceId : ids) {
            db.collection("pieces").document(pieceId)
                    .get()
                    .addOnSuccessListener(doc -> {
                        if (doc.exists()) {
                            PieceClass piece = doc.toObject(PieceClass.class);
                            if (piece != null) {
                                list.add(piece);
                                adapter.notifyItemInserted(list.size() - 1);

                                // لو هذه العربة، حدث المجموع
                                if (isCart) updateCartTotal();
                            }
                        }
                    });
        }
    }

    private void updateCartTotal() {
        double total = 0.0;
        for (PieceClass piece : cartPiecesList) {
            total += Double.parseDouble(piece.getPrice());
        }
        tvTotalCartPrice.setText("total: " + String.format(Locale.US, "%.2f", total) + " ₪");
    }

}