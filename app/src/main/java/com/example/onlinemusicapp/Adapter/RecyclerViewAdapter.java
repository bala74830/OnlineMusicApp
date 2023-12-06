package com.example.onlinemusicapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onlinemusicapp.Model.Upload;
import com.example.onlinemusicapp.R;
import com.example.onlinemusicapp.SongsActivity;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>{


    private Context context;
    private List<Upload> uploadList;

    public RecyclerViewAdapter(Context context, List<Upload> uploadList) {
        this.context = context;
        this.uploadList = uploadList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup viewGroup, int i) {

        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view=inflater.inflate(R.layout.card_view_item,viewGroup,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull MyViewHolder myViewHolder, int i) {


        final Upload upload=uploadList.get(i);
        myViewHolder.tv_book_title.setText(upload.getName());

        Glide.with(context).load(upload.getUrl()).into(myViewHolder.imd_book_thumbnail);

        myViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(context, SongsActivity.class);
                intent.putExtra("songsCategory",upload.getsongscategory());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return uploadList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv_book_title;
        ImageView imd_book_thumbnail;
        CardView cardView;

        public MyViewHolder(@androidx.annotation.NonNull View itemView) {
            super(itemView);

            tv_book_title=itemView.findViewById(R.id.book_title_id);
            imd_book_thumbnail=itemView.findViewById(R.id.book_img_id);
            cardView=itemView.findViewById(R.id.card_view_id);


        }
    }

}
