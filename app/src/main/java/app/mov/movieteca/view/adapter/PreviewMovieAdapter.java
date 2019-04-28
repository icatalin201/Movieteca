package app.mov.movieteca.view.adapter;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import app.mov.movieteca.R;
import app.mov.movieteca.model.PreviewMovie;
import app.mov.movieteca.util.Util;

public class PreviewMovieAdapter extends RecyclerView.Adapter<PreviewMovieAdapter.MediaViewHolder> {

    private Context context;
    private List<PreviewMovie> previewMovieList;
    private boolean fulllist;
    private int lastPosition = -1;
    private OnItemClickedListener onItemClickedListener;

    public interface OnItemClickedListener {
        void onClick(PreviewMovie previewMovie);
    }

    public PreviewMovieAdapter(Context context, boolean full, OnItemClickedListener onItemClickedListener) {
        this.context = context;
        this.previewMovieList = new ArrayList<>();
        this.fulllist = full;
        this.onItemClickedListener = onItemClickedListener;
    }

    public PreviewMovieAdapter(Context context, List<PreviewMovie> previewMovieList, boolean full) {
        this.context = context;
        this.previewMovieList = previewMovieList;
        this.fulllist = full;
    }

    public void add(List<PreviewMovie> previewMovieList) {
        if (!fulllist) this.previewMovieList.clear();
        Collections.shuffle(previewMovieList);
        this.previewMovieList.addAll(previewMovieList);
        notifyDataSetChanged();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull MediaViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (fulllist) holder.itemView.clearAnimation();
    }

    @NonNull
    @Override
    public MediaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        int resource = R.layout.preview_media_item_card;
        if (fulllist) resource = R.layout.full_preview_media_item_card;
        View view = LayoutInflater.from(context)
                .inflate(resource, viewGroup, false);
        return new MediaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MediaViewHolder mediaViewHolder, int i) {

        if (fulllist) {
            Animation animation = AnimationUtils.loadAnimation(context,
                    (i > lastPosition) ? R.anim.up_from_bottom
                            : R.anim.down_from_top);
            mediaViewHolder.itemView.startAnimation(animation);
            lastPosition = mediaViewHolder.getAdapterPosition();
        }
        PreviewMovie previewMovie = previewMovieList.get(i);
        Glide.with(context)
                .load(Util.Constants.IMAGE_LOADING_BASE_URL_780.concat(previewMovie.getPosterPath()))
                .apply(RequestOptions.centerCropTransform())
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(mediaViewHolder.image);
    }

    @Override
    public int getItemCount() {
        return previewMovieList.size();
    }

    class MediaViewHolder extends RecyclerView.ViewHolder {

        private CardView item;
        private ImageView image;

        MediaViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.item);
            image = itemView.findViewById(R.id.image);
            item.setOnClickListener(view ->
                    onItemClickedListener.onClick(previewMovieList.get(getAdapterPosition())));
        }
    }
}
