package com.example.quizrevision;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Objects;

public class DogRecycleViewAdapter extends RecyclerView.Adapter<DogRecycleViewAdapter.MyViewHolder> {

    private final GalleryViewModel viewModel;

    public DogRecycleViewAdapter(GalleryViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        Button buttonDelete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);
            buttonDelete = itemView.findViewById(R.id.buttondelete);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        GalleryItem item = Objects.requireNonNull(viewModel.getUiState().getValue()).getImages().get(position);
        holder.textView.setText(item.name);
        holder.imageView.setImageURI(Uri.parse(item.uri));
        holder.buttonDelete.setOnClickListener(v -> {
            int currentPosition = holder.getAbsoluteAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION) {
                viewModel.removeItem(currentPosition);
                notifyItemRemoved(currentPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Objects.requireNonNull(viewModel.getUiState().getValue()).getImages().size();
    }
}
