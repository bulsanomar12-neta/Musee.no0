package com.example.musee;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musee.classes.FirebaseServices;
import com.example.musee.classes.PieceClass;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PieceDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PieceDetailsFragment extends Fragment {
    private static final int PERMISSION_SEND_SMS = 1;
    private static final int REQUEST_CALL_PERMISSION = 2;
    private FirebaseServices fbs;
    private TextView tvArtNamePieceDetails, tvSizePieceDetails, tvInformationPieceDetails, tvArtistNamePieceDetails,
            tvHoursPieceDetails, tvCategoryPieceDetails, tvPricePieceDetails;
    private ImageView imgPieceDetails;
    private PieceClass myPiece;
    private Button btAddtocartlPieceDetailsFragment, btEmailPieceDetailsFragment;
    private ImageButton btnBackFromDetailsToAll;
    private boolean isEnlarged = false;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PieceDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PieceDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PieceDetailsFragment newInstance(String param1, String param2) {
        PieceDetailsFragment fragment = new PieceDetailsFragment();
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
        return inflater.inflate(R.layout.fragment_piece_details, container, false);
    }


    @Override
    public void onStart() {
        super.onStart();
        init();
        ImageView ivPiecePhoto = getView().findViewById(R.id.imgPieceDetails);

        ivPiecePhoto.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {
                ViewGroup.LayoutParams layoutParams = ivPiecePhoto.getLayoutParams();
                if (isEnlarged) {
                    layoutParams.height = 500;
                } else {
                    layoutParams.height = 2200;
                }
                ivPiecePhoto.setLayoutParams(layoutParams);

                // נשנה את המצב הנוכחי של התמונה
                isEnlarged = !isEnlarged;

            }
        });
    }

    public void init() {
        fbs = FirebaseServices.getInstance();
        tvArtNamePieceDetails = getView().findViewById(R.id.tvArtNamePieceDetails);
        tvSizePieceDetails = getView().findViewById(R.id.tvSizePieceDetails);
        tvInformationPieceDetails = getView().findViewById(R.id.tvInformationPieceDetails);
        tvArtistNamePieceDetails = getView().findViewById(R.id.tvArtistNamePieceDetails);
        tvHoursPieceDetails = getView().findViewById(R.id.tvHoursPieceDetails);
        tvCategoryPieceDetails = getView().findViewById(R.id.tvCategoryPieceDetails);
        tvPricePieceDetails = getView().findViewById(R.id.tvPricePieceDetails);
        imgPieceDetails = getView().findViewById(R.id.imgPieceDetails);

        btAddtocartlPieceDetailsFragment = getView().findViewById(R.id.btAddtocartlPieceDetailsFragment); // زر الـ SMS سابقاً
        btEmailPieceDetailsFragment = getView().findViewById(R.id.btEmailPieceDetailsFragment);
        btnBackFromDetailsToAll = getView().findViewById(R.id.btnBackFromDetailsToAll);

        Bundle args = getArguments();
        if (args != null) {
            myPiece = args.getParcelable("pieces");
            if (myPiece != null) {
                tvArtNamePieceDetails.setText(myPiece.getArtistName());
                tvSizePieceDetails.setText(myPiece.getSize());
                tvInformationPieceDetails.setText(myPiece.getInformation());
                tvArtistNamePieceDetails.setText(myPiece.getArtistName());
                tvHoursPieceDetails.setText(myPiece.getHours());
                tvCategoryPieceDetails.setText(myPiece.getCategory());
                tvPricePieceDetails.setText(myPiece.getPrice() + " $");
                if (myPiece.getPhoto() == null || myPiece.getPhoto().isEmpty()) {
                    //Picasso.get().load(R.drawable.ic_fav).into(imgPieceDetails);
                } else {
                    Picasso.get().load(myPiece.getPhoto()).into(imgPieceDetails);
                }
                //  برمجة زر الإيميل
                btEmailPieceDetailsFragment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String artName = myPiece.getArtistName(); // استخراج الاسم من الكائن مباشرة
                        Intent intent = new Intent(Intent.ACTION_SENDTO);
                        intent.setData(Uri.parse("mailto:"));
                        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"gallery@example.com"});
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Inquiry about: " + artName);
                        intent.putExtra(Intent.EXTRA_TEXT, "Hello, I am interested in your artwork: " + artName);

                        try {
                            startActivity(Intent.createChooser(intent, "Send Email..."));
                        } catch (Exception e) {
                            Toast.makeText(getActivity(), "No Email app found", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                //  برمجة زر الشراء
                btAddtocartlPieceDetailsFragment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // حالياً سنعرض رسالة فقط
                        Toast.makeText(getActivity(), myPiece.getArtistName() + " added to cart!", Toast.LENGTH_SHORT).show();
                    }
                });
                btnBackFromDetailsToAll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (getActivity() != null) {
                            // الحصول على MainActivity لاستخدام navigation
                            MainActivity mainActivity = (MainActivity) getActivity();
                            mainActivity.gotoAllPiecesFragment();
                        if (mainActivity == null) {
                            return;
                        }}
                    }
                });
            }
        }
    }

    private void checkAndSendSMS() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.SEND_SMS}, PERMISSION_SEND_SMS);
        } else {
            sendSMS();
        }
    }

    //رقم الهاتف ساتعلم كيف يأحذ من المستخدم وليس من اللوحه نفسها
    private void sendSMS() {
        /*
        String phoneNumber = myPiece.getPhone();
        String message = "I am Interested in your  "+myPiece.getNameCar()+"  car: " + myPiece.getCar_num();

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(getActivity(), "SMS sent.", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "SMS sending failed.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

         */
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_SEND_SMS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendSMS();
            } else {
                Toast.makeText(requireContext(), "Permission denied. Cannot send SMS.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}