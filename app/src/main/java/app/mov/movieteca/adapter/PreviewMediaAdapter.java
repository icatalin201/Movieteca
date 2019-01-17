package app.mov.movieteca.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
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
import java.util.List;

import app.mov.movieteca.R;
import app.mov.movieteca.activity.FullMediaActivity;
import app.mov.movieteca.model.PreviewMedia;
import app.mov.movieteca.model.PreviewMovie;
import app.mov.movieteca.model.PreviewTVShow;
import app.mov.movieteca.utils.Util;

public class PreviewMediaAdapter extends RecyclerView.Adapter<PreviewMediaAdapter.MediaViewHolder> {

    private Context context;
    private List<PreviewMedia> previewMediaList;
    private boolean fulllist;
    private int lastPosition = -1;

    public PreviewMediaAdapter(Context context, List<PreviewMedia> previewMediaList, boolean fulllist) {
        this.context = context;
        this.previewMediaList = previewMediaList;
        this.fulllist = fulllist;
    }

    public void add(List<? extends PreviewMedia> previewMediaList) {
        Collections.shuffle(previewMediaList);
        this.previewMediaList.addAll(previewMediaList);
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

        PreviewMedia previewMedia = previewMediaList.get(i);
        String path;
        if (previewMedia.getType().equals(Util.Constants.MOVIE))  {
            path = Util.Constants.IMAGE_LOADING_BASE_URL_780
                    .concat(((PreviewMovie) previewMedia).getPosterPath());
        } else {
            path = Util.Constants.IMAGE_LOADING_BASE_URL_780
                    .concat(((PreviewTVShow) previewMedia).getPosterPath());
        }

        Glide.with(context)
                .load(path)
                .apply(RequestOptions.centerCropTransform())
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(mediaViewHolder.image);
    }

    @Override
    public int getItemCount() {
        return previewMediaList.size();
    }

    class MediaViewHolder extends RecyclerView.ViewHolder {

        private CardView item;
        private ImageView image;

        MediaViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.item);
            image = itemView.findViewById(R.id.image);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PreviewMedia previewMedia = previewMediaList.get(getAdapterPosition());
                    Intent intent = new Intent(context, FullMediaActivity.class);
                    intent.putExtra("type", previewMedia.getType());
                    if (previewMedia.getType().equals(Util.Constants.MOVIE)) {
                        intent.putExtra("id", ((PreviewMovie) previewMedia).getId());
                    } else {
                        intent.putExtra("id", ((PreviewTVShow) previewMedia).getId());
                    }
                    context.startActivity(intent);
                }
            });
        }
    }
}
