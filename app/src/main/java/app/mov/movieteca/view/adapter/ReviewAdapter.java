package app.mov.movieteca.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import app.mov.movieteca.R;
import app.mov.movieteca.model.response.Review;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder> {

    public interface OnItemClickListener {
        void onClick(Review review);
    }

    private List<Review> reviewList = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public ReviewAdapter(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void add(List<Review> reviewList) {
        this.reviewList.addAll(reviewList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_item, parent, false);
        return new ReviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewHolder holder, int position) {
        Review review = reviewList.get(position);
        holder.content.setText(review.getContent());
        holder.title.setText(review.getAuthor());
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public class ReviewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.content)
        TextView content;

        public ReviewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.more)
        void onClick() {
            onItemClickListener.onClick(reviewList.get(getAdapterPosition()));
        }
    }
}
