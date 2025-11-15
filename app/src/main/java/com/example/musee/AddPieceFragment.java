package com.example.musee;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.musee.classes.FirebaseServices;
import com.example.musee.classes.PieceClass;
import com.example.musee.classes.UtilsClass;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddPieceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddPieceFragment extends Fragment {
    private EditText etIdAddPieceFragment, etArtistAddPieceFragment, etHoursAddPieceFragment, etInformationAddPieceFragment,etSizeAddPieceFragment;
    private Spinner spCategoryAddPiece;
    private Button btAddPieceFragment;
    private FirebaseServices fbs;
    private UtilsClass utils; // ✅ تمت الإضافة (لرفع الصورة)

    private ImageView imgVImageAddPieceFragment;
    private Uri selectedImageUri; // ✅ تمت الإضافة (لتخزين الصورة مؤقتًا)

    private final ActivityResultLauncher<Intent> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode()== Activity.RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                    imgVImageAddPieceFragment.setImageURI(selectedImageUri);

                    // ✅ تمت الإضافة من كود الأستاذ: رفع الصورة بعد اختيارها
                    utils.uploadImage(getActivity(), selectedImageUri);
                }
            });


        // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddPieceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddPieceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddPieceFragment newInstance(String param1, String param2) {
        AddPieceFragment fragment = new AddPieceFragment();
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
        return inflater.inflate(R.layout.fragment_add_piece, container, false);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        connectComponents();
    }

    private void connectComponents() {
        etIdAddPieceFragment = getView().findViewById(R.id.etIdAddPieceFragment);
        spCategoryAddPiece = getView().findViewById(R.id.spCategoryAddPiece);
        etArtistAddPieceFragment = getView().findViewById(R.id.etArtistAddPieceFragment);
        etHoursAddPieceFragment = getView().findViewById(R.id.etHoursAddPieceFragment);
        etInformationAddPieceFragment = getView().findViewById(R.id.etInformationAddPieceFragment);
        etSizeAddPieceFragment = getView().findViewById(R.id.etSizeAddPieceFragment);


        imgVImageAddPieceFragment = getView().findViewById(R.id.imgItem);
        imgVImageAddPieceFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });


        utils = UtilsClass.getInstance();// ✅ تمت الإضافة من كود الأستاذ
        fbs = FirebaseServices.getInstance();

        btAddPieceFragment = getView().findViewById(R.id.btAddPieceFragment);
        btAddPieceFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id,artist,hours,information,category,size;
                id = etIdAddPieceFragment.getText().toString();
                category = spCategoryAddPiece.getSelectedItem().toString();
                artist = etArtistAddPieceFragment.getText().toString();
                hours = etHoursAddPieceFragment.getText().toString();
                information = etInformationAddPieceFragment.getText().toString();
                size = etSizeAddPieceFragment.getText().toString();


                // ✅ تمت الإضافة من كود الأستاذ: جلب رابط الصورة من FirebaseServices
                String photo = (fbs.getSelectedImageURL() != null) ? fbs.getSelectedImageURL().toString() : "";

                if(id.trim().isEmpty() || artist.trim().isEmpty() || hours.trim().isEmpty() || information.trim().isEmpty() || category.trim().isEmpty()){
                    Toast.makeText(getActivity(), "Some fields are empty.", Toast.LENGTH_LONG).show();
                    return;
                }

                // ✅ عدلت: تمرير رابط الصورة إلى كائن PieceClass
                // كان ناقص الصوره
                PieceClass piece = new PieceClass(id,category,artist,hours,size,information,"");


                fbs.getFire().collection("pieces").add(piece).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(requireContext(), "congrats.", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Something went wrong.", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageLauncher.launch(intent);
    }

}