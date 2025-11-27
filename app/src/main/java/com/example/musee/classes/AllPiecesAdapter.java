package com.example.musee.classes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
//import com.squareup.picasso.Picasso;

import com.example.musee.MainActivity;
import com.example.musee.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AllPiecesAdapter extends RecyclerView.Adapter<AllPiecesAdapter.MyViewHolder> {
    ArrayList<PieceClass> allPieces;
    Context context;
    private AllPiecesAdapter.OnItemClickListener itemClickListener;
    private FirebaseServices fbs;

    public AllPiecesAdapter(Context context, ArrayList<PieceClass> allPieces) {
        this.context = context;
        this.allPieces = allPieces;
        this.fbs = FirebaseServices.getInstance();
        this.itemClickListener = new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                /*
                String selectedItem = filteredList.get(position).getNameCar();
                Toast.makeText(getActivity(), "Clicked: " + selectedItem, Toast.LENGTH_SHORT).show(); */
                Bundle args = new Bundle();
                //args.putParcelable("piece", (Parcelable) allPieces.get(position)); // or use Parcelable for better performance
                // CarDetailsFragment cd = new CarDetailsFragment();
                // cd.setArguments(args);
                FragmentTransaction ft= ((MainActivity)context).getSupportFragmentManager().beginTransaction();
                //ft.replace(R.id.frameLayout,cd);
                ft.commit();
            }
        } ;
    }

    @NonNull
    @Override
    public AllPiecesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return  new AllPiecesAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AllPiecesAdapter.MyViewHolder holder, int position) {
        PieceClass piece = allPieces.get(position);
        //User u = fbs.getCurrentUser();
        holder.ida.setText(piece.getId());
        holder.artista.setText(piece.getArtistName());
        holder.hoursa.setText(piece.getHours());
        holder.categorya.setText(piece.getCategory());
        holder.siza.setText(piece.getSize());
        holder.informationa.setText(piece.getInformation());
        holder.prica.setText(piece.getPrice());
        holder.itemView.setOnClickListener(v -> {
            if (itemClickListener != null)
                itemClickListener.onItemClick(position);
        });

        // في تطبيقي يجب وجود الصوره دائما
        if (piece.getPhoto() == null || piece.getPhoto().isEmpty())
        {
            //Picasso.get().load(R.drawable.ic_fav).into(holder.imga);
        }
        else {
            Picasso.get().load(piece.getPhoto()).into(holder.imga);
        }

    }

    @Override
    public int getItemCount() {return allPieces.size();}

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(AllPiecesAdapter.OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView ida, artista, hoursa, informationa,siza,prica,categorya;
        ImageView imga;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ida = itemView.findViewById(R.id.tvArtNameItem);
            artista = itemView.findViewById(R.id.tvArtistNameItem);
            hoursa = itemView.findViewById(R.id.tvHoursItem);
            categorya = itemView.findViewById(R.id.tvCategoryItem);
            siza = itemView.findViewById(R.id.tvSizeItem);
            informationa = itemView.findViewById(R.id.tvInformationItem);
            prica = itemView.findViewById(R.id.tvPriceItem);
            imga = itemView.findViewById(R.id.imgItem);
        }
    }
}
