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
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import app.mov.movieteca.R;
import app.mov.movieteca.model.FavoritePreviewMedia;
import app.mov.movieteca.util.Constants;
import app.mov.movieteca.view.cast.CastActivity;
import app.mov.movieteca.view.movie.MovieActivity;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    private List<FavoritePreviewMedia> favoritePreviewMediaList = new ArrayList<>();
    private Context context;
    private int lastPosition = -1;

    public FavoriteAdapter(Context context) {
        this.context = context;
    }

    public void add(List<FavoritePreviewMedia> favoritePreviewMediaList) {
        Collections.shuffle(favoritePreviewMediaList);
        this.favoritePreviewMediaList= favoritePreviewMediaList;
        notifyDataSetChanged();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull FavoriteViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.media_item_card, viewGroup, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder favoriteViewHolder, int i) {
        if (i > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.up_from_bottom);
            favoriteViewHolder.itemView.startAnimation(animation);
            lastPosition = favoriteViewHolder.getAdapterPosition();
        }

        FavoritePreviewMedia favoritePreviewMedia = favoritePreviewMediaList.get(i);
        if (favoritePreviewMedia.getPoster() != null) {
            Glide.with(context)
                    .load(Constants.IMAGE_LOADING_BASE_URL_780.concat(favoritePreviewMedia.getPoster()))
                    .apply(RequestOptions.centerCropTransform())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(favoriteViewHolder.image);
        } else {
            Glide.with(context)
                    .load(R.drawable.ic_baseline_movie_creation_24px)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(favoriteViewHolder.image);
        }
    }

    @Override
    public int getItemCount() {
        return favoritePreviewMediaList.size();
    }

    class FavoriteViewHolder extends RecyclerView.ViewHolder {

        private CardView item;
        private ImageView image;

        FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.item);
            image = itemView.findViewById(R.id.image);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = null;
                    FavoritePreviewMedia favoritePreviewMedia = favoritePreviewMediaList.get(getAdapterPosition());
                    if (favoritePreviewMedia.getResType().equals(Constants.MOVIE) ||
                            favoritePreviewMedia.getResType().equals(Constants.TV_SHOW)) {
                        intent = new Intent(context, MovieActivity.class);
                        intent.putExtra("id", favoritePreviewMedia.getResId());
                        intent.putExtra("type", favoritePreviewMedia.getResType());
                    } else if (favoritePreviewMedia.getResType().equals(Constants.ACTOR)) {
                        intent = new Intent(context, CastActivity.class);
                        intent.putExtra("id", favoritePreviewMedia.getResId());
                    }
                    if (intent != null) {
                        ((AppCompatActivity) context).startActivityForResult(intent, 1);
                    }
                }
            });
        }
    }
}
