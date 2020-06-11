package com.erik.project4;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

//Helpful adapter documentation
//https://developer.android.com/reference/android/support/v7/widget/RecyclerView.Adapter


//This is handling the recycler view stuff
public class HolderAdapter extends RecyclerView.Adapter<HolderAdapter.HolderHolder> {
    //Create a new list of holders
    private List<Holder> holders = new ArrayList<>();
    //Add a click listener
    private OnItemClickListener listener;

    @NonNull
    @Override
    public HolderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Create and inflate the view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.holder_item, parent, false);

        return new HolderHolder(itemView);
    }

    //Get data from the single holder and put them in the recycler view holder
    //Get set the textviews equal to the values in the holder
    @Override
    public void onBindViewHolder(@NonNull HolderHolder holder, int position) {
        Holder currentHolder = holders.get(position);
        //Set the text we want to put in the textviews
        holder.tvName.setText(currentHolder.getName());
        holder.tvCategory.setText(currentHolder.getCategory());
        holder.tvAmount.setText(String.valueOf(currentHolder.getAmount()));
        holder.tvDate.setText(currentHolder.getDate());
        holder.tvNote.setText(currentHolder.getNote());
    }

    //Just return the size of the holders
    @Override
    public int getItemCount() {
        return holders.size();
    }


    //Gets live data nodes into recycler view
    public void setHolders(List<Holder> holders) {
        this.holders = holders;
        notifyDataSetChanged();
    }

    //Returns holder at specific location
    public Holder getHolderIndex(int position) {
        return holders.get(position);
    }

    //Holds each view in the recycler view holder
    class HolderHolder extends RecyclerView.ViewHolder {
        //All the different attributes
        private TextView tvName;
        private TextView tvAmount;
        private TextView tvCategory;
        private TextView tvDate;
        private TextView tvNote;

        //Set all the textviews equal to what is on the card view
        public HolderHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvAmount = itemView.findViewById(R.id.tv_amount);
            tvCategory = itemView.findViewById(R.id.tv_category);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvNote = itemView.findViewById(R.id.tv_note);

            //This sets up the ability to click the holders
            //Click on the specific item
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Get the position of the item we clicked
                    int pos = getAdapterPosition();
                    if (listener != null && pos != RecyclerView.NO_POSITION) {
                        //Pass the holder of the position we got and call the method
                        listener.onItemClick(holders.get(pos));
                    }
                }
            });
        }
    }


    public interface OnItemClickListener {
        void onItemClick(Holder holder);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


}
