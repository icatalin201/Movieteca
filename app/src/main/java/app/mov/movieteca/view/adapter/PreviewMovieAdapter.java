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
import app.mov.movieteca.model.response.PreviewMovie;
import app.mov.movieteca.util.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PreviewMovieAdapter extends RecyclerView.Adapter<PreviewMovieAdapter.MediaViewHolder> {

    private Context context;
    private List<PreviewMovie> previewMovieList = new ArrayList<>();
    private boolean hasFullList;
    private int lastPosition = -1;
    private OnItemClickedListener onItemClickedListener;

    public interface OnItemClickedListener {
        void onClick(PreviewMovie previewMovie);
    }

    public PreviewMovieAdapter(OnItemClickedListener onItemClickedListener,
                               Context context, boolean hasFullList) {
        this.context = context;
        this.hasFullList = hasFullList;
        this.onItemClickedListener = onItemClickedListener;
    }
    
    public void add(List<PreviewMovie> previewMovieList) {
        if (!hasFullList) this.previewMovieList.clear();
        Collections.shuffle(previewMovieList);
        this.previewMovieList.addAll(previewMovieList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.previewMovieList.clear();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull MediaViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (hasFullList) holder.itemView.clearAnimation();
    }

    @NonNull
    @Override
    public MediaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        int resource = R.layout.preview_media_item_card;
        if (hasFullList) resource = R.layout.media_item_card;
        View view = LayoutInflater.from(context).inflate(resource, viewGroup, false);
        return new MediaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MediaViewHolder mediaViewHolder, int i) {

        if (hasFullList && i > lastPosition) {
//            Animation animation = AnimationUtils.loadAnimation(context,
//                    (i > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.up_from_bottom);
            mediaViewHolder.itemView.startAnimation(animation);
            lastPosition = mediaViewHolder.getAdapterPosition();
        }
        PreviewMovie previewMovie = previewMovieList.get(i);
        Glide.with(context)
                .load(Constants.IMAGE_LOADING_BASE_URL_780.concat(previewMovie.getPosterPath()))
                .apply(RequestOptions.centerCropTransform())
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(mediaViewHolder.image);
    }

    @Override
    public int getItemCount() {
        return previewMovieList.size();
    }

    class MediaViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item)
        CardView item;
        @BindView(R.id.image)
        ImageView image;

        MediaViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.item)
        void onClick() {
            onItemClickedListener.onClick(previewMovieList.get(getAdapterPosition()));
        }
    }
}
