package com.example.rxjava;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private static final String TAG = "RecyclerAdapter";

    private List<Post> posts = new ArrayList<>();
    private OnPostClickListener onPostClickListener;

    public RecyclerAdapter(OnPostClickListener onPostClickListener) {
        this.onPostClickListener = onPostClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_post_list_item, null, false);
        return new MyViewHolder(view, onPostClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.bind(posts.get(position));
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void updatePost(Post post) {
        posts.set(posts.indexOf(post), post);
        notifyItemChanged(posts.indexOf(post));
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
        notifyDataSetChanged();
    }


    public interface OnPostClickListener {
        void onPostClick(int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        //       OnPostClickListener onPostClickListener;
        TextView title;

        public MyViewHolder(@NonNull View itemView, final OnPostClickListener onPostClickListener) {
            super(itemView);
            title = itemView.findViewById(R.id.title);

            //           this.onPostClickListener = onPostClickListener;
            itemView.setOnClickListener(v ->
                    onPostClickListener.onPostClick(getAdapterPosition()));
        }

        public void bind(Post post) {
            title.setText(post.getTitle());

        }
    }
}
