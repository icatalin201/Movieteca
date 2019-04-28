package app.mov.movieteca.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import app.mov.movieteca.R;
import app.mov.movieteca.model.response.Genre;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.GenreViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Genre genre);
    }

    private List<Genre> genreList;
    private OnItemClickListener onItemClickListener;

    public GenreAdapter(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        this.genreList = new ArrayList<>();
    }

    public void add(List<Genre> genres) {
        this.genreList.addAll(genres);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GenreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.genre_view, parent, false);
        return new GenreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreViewHolder holder, int position) {
        Genre genre = genreList.get(position);
        holder.render(genre);
    }

    @Override
    public int getItemCount() {
        return genreList == null ? 0 : genreList.size();
    }

    public class GenreViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.card)
        CardView cardView;
        @BindView(R.id.genre)
        TextView textView;

        public GenreViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void render(Genre genre) {
            textView.setText(genre.getName());
        }

        @OnClick(R.id.card)
        void onClick() {
            onItemClickListener.onItemClick(genreList.get(getAdapterPosition()));
        }
    }
}
