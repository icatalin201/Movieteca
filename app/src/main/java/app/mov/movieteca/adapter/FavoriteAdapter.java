package app.mov.movieteca.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.SortedList;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import app.mov.movieteca.R;
import app.mov.movieteca.activity.CastActivity;
import app.mov.movieteca.activity.FullMediaActivity;
import app.mov.movieteca.model.FavoritePreviewMedia;
import app.mov.movieteca.utils.Util;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    private final SortedList<FavoritePreviewMedia> mSortedList = new SortedList<>(FavoritePreviewMedia.class,
            new SortedList.Callback<FavoritePreviewMedia>() {
                @Override
                public int compare(FavoritePreviewMedia a, FavoritePreviewMedia b) {
                    return mComparator.compare(a, b);
                }

                @Override
                public void onInserted(int position, int count) {
                    notifyItemRangeInserted(position, count);
                }

                @Override
                public void onRemoved(int position, int count) {
                    notifyItemRangeRemoved(position, count);
                }

                @Override
                public void onMoved(int fromPosition, int toPosition) {
                    notifyItemMoved(fromPosition, toPosition);
                }

                @Override
                public void onChanged(int position, int count) {
                    notifyItemRangeChanged(position, count);
                }

                @Override
                public boolean areContentsTheSame(FavoritePreviewMedia oldItem, FavoritePreviewMedia newItem) {
                    return oldItem.equals(newItem);
                }

                @Override
                public boolean areItemsTheSame(FavoritePreviewMedia item1, FavoritePreviewMedia item2) {
                    return item1 == item2;
                }
            });

    private Comparator<FavoritePreviewMedia> mComparator;

    private Context context;
    private int lastPosition = -1;

    public FavoriteAdapter(Context context, Comparator<FavoritePreviewMedia> comparator) {
        this.context = context;
        this.mComparator = comparator;
    }

    public void add(List<FavoritePreviewMedia> favoritePreviewMediaList) {
        Collections.shuffle(favoritePreviewMediaList);
        this.mSortedList.addAll(favoritePreviewMediaList);
        notifyDataSetChanged();
    }

    public void replaceAll(List<FavoritePreviewMedia> models) {
        mSortedList.beginBatchedUpdates();
        for (int i = mSortedList.size() - 1; i >= 0; i--) {
            FavoritePreviewMedia model = mSortedList.get(i);
            if (!models.contains(model)) {
                mSortedList.remove(model);
            }
        }
        mSortedList.addAll(models);
        mSortedList.endBatchedUpdates();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull FavoriteViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.full_preview_media_item_card, viewGroup, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder favoriteViewHolder, int i) {
        Animation animation = AnimationUtils.loadAnimation(context,
                (i > lastPosition) ? R.anim.up_from_bottom
                        : R.anim.down_from_top);
        favoriteViewHolder.itemView.startAnimation(animation);
        lastPosition = favoriteViewHolder.getAdapterPosition();
        FavoritePreviewMedia favoritePreviewMedia = mSortedList.get(i);
        if (favoritePreviewMedia.getPoster() != null) {
            Glide.with(context)
                    .load(Util.Constants.IMAGE_LOADING_BASE_URL_780.concat(favoritePreviewMedia.getPoster()))
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
        return mSortedList.size();
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
                    FavoritePreviewMedia favoritePreviewMedia = mSortedList.get(getAdapterPosition());
                    if (favoritePreviewMedia.getResType().equals(Util.Constants.MOVIE) ||
                            favoritePreviewMedia.getResType().equals(Util.Constants.TV_SHOW)) {
                        intent = new Intent(context, FullMediaActivity.class);
                        intent.putExtra("id", favoritePreviewMedia.getResId());
                        intent.putExtra("type", favoritePreviewMedia.getResType());
                    } else if (favoritePreviewMedia.getResType().equals(Util.Constants.ACTOR)) {
                        intent = new Intent(context, CastActivity.class);
                        intent.putExtra(Util.Constants.CAST_ID, favoritePreviewMedia.getResId());
                    }
                    if (intent != null) {
                        ((AppCompatActivity) context).startActivityForResult(intent, 1);
                    }
                }
            });
        }
    }
}
