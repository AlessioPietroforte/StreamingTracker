package it.example.applicazioneufficiale.ui.charts;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import it.example.applicazioneufficiale.R;
import it.example.applicazioneufficiale.ui.MovieDetails;

public class ItemRecyclerAdapter extends RecyclerView.Adapter<ItemRecyclerAdapter.ItemViewHolder> {

    Context context;
    List<CategoryItem> categoryItemList;

    public ItemRecyclerAdapter(Context context, List<CategoryItem> categoryItemList) {
        this.context = context;
        this.categoryItemList = categoryItemList;
    }

    @NonNull
    @NotNull
    @Override
    public ItemRecyclerAdapter.ItemViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.category_recycler_row_items,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ItemRecyclerAdapter.ItemViewHolder holder, int position) {
        //qui ci sta il fetching delle immagini dal server quindi si usa glide
        Glide.with(context).load(categoryItemList.get(position).getImageUrl()).into(holder.itemImage);

        holder.itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context, MovieDetails.class);
                i.putExtra("movieId", categoryItemList.get(position).getId());
                i.putExtra("movieName", categoryItemList.get(position).getMovieName());
                i.putExtra("movieImageUrl", categoryItemList.get(position).getImageUrl());
                i.putExtra("movieFile", categoryItemList.get(position).getFileUrl());
                context.startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return categoryItemList.size();
    }

    public static final class ItemViewHolder extends RecyclerView.ViewHolder{

        ImageView itemImage;


        public ItemViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            itemImage = itemView.findViewById(R.id.item_image);
        }
    }

}
