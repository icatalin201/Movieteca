package app.mov.movieteca.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import app.mov.movieteca.R;
import app.mov.movieteca.model.SearchResults;
import app.mov.movieteca.util.Constants;
import app.mov.movieteca.view.cast.CastActivity;
import app.mov.movieteca.view.movie.MovieActivity;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private Context context;
    private int lastPosition = -1;
    private List<SearchResults> searchResultsList;

    public SearchAdapter(Context context, List<SearchResults> searchResultsList) {
        this.context = context;
        this.searchResultsList = searchResultsList;
    }

    public void clear() {
        this.searchResultsList.clear();
        notifyDataSetChanged();
    }

    public void add(List<SearchResults> searchResultsList) {
        this.searchResultsList.addAll(searchResultsList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.media_item_card,
                viewGroup, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder searchViewHolder, int i) {
        Animation animation = AnimationUtils.loadAnimation(context,
                (i > lastPosition) ? R.anim.up_from_bottom
                        : R.anim.down_from_top);
        searchViewHolder.itemView.startAnimation(animation);
        lastPosition = searchViewHolder.getAdapterPosition();

        SearchResults searchResults = searchResultsList.get(i);
        if (searchResults.getPoster() != null) {
            Glide.with(context)
                    .load(Constants.IMAGE_LOADING_BASE_URL_780.concat(searchResults.getPoster()))
                    .apply(RequestOptions.centerCropTransform())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(searchViewHolder.image);
        } else {
            Glide.with(context)
                    .load(R.drawable.ic_baseline_movie_creation_24px)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(searchViewHolder.image);
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull SearchViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    @Override
    public int getItemCount() {
        return searchResultsList.size();
    }

    class SearchViewHolder extends RecyclerView.ViewHolder {

        private CardView item;
        private ImageView image;

        SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.item);
            image = itemView.findViewById(R.id.image);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = null;
                    SearchResults searchResults = searchResultsList.get(getAdapterPosition());
                    if (searchResults.getMediaType().equals(Constants.MOVIE) ||
                            searchResults.getMediaType().equals(Constants.TV_SHOW)) {
                        intent = new Intent(context, MovieActivity.class);
                        intent.putExtra("id", searchResults.getId());
                        intent.putExtra("type", searchResults.getMediaType());
                    } else if (searchResults.getMediaType().equals(Constants.ACTOR)) {
                        intent = new Intent(context, CastActivity.class);
                        intent.putExtra("id", searchResults.getId());
                    }
                    if (intent != null) {
                        context.startActivity(intent);
                    }
                }
            });
        }
    }
}
