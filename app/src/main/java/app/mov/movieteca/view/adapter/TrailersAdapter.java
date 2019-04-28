package app.mov.movieteca.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import app.mov.movieteca.R;
import app.mov.movieteca.model.response.VideoInfo;
import app.mov.movieteca.util.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailersViewHolder> {

    private Context context;
    private List<VideoInfo> videosList = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onClick(VideoInfo videoInfo);
    }

    public TrailersAdapter(Context context, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }

    public void add(List<VideoInfo> videosList) {
        this.videosList.addAll(videosList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TrailersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.trailer_item, viewGroup, false);
        return new TrailersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailersViewHolder trailersViewHolder, int i) {
        VideoInfo videoInfo = videosList.get(i);
        if (videoInfo.getKey() != null) {
            Glide.with(context)
                    .load(Constants.YOUTUBE_THUMBNAIL_BASE_URL
                            .concat(videoInfo.getKey())
                            .concat(Constants.YOUTUBE_THUMBNAIL_IMAGE_QUALITY))
                    .apply(RequestOptions.centerCropTransform())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(trailersViewHolder.trailerImage);
        } else {
            Glide.with(context)
                    .load(R.drawable.ic_baseline_movie_creation_24px)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(trailersViewHolder.trailerImage);
        }
        if (videoInfo.getName() != null) {
            trailersViewHolder.trailerInfo.setText(videoInfo.getName());
        }
        else{
            trailersViewHolder.trailerInfo.setText("");
        }

    }

    @Override
    public int getItemCount() {
        return videosList.size();
    }

    class TrailersViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.trailer_image)
        ImageView trailerImage;
        @BindView(R.id.trailer_info)
        TextView trailerInfo;


        TrailersViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.trailer_card)
        void onClick() {
            onItemClickListener.onClick(videosList.get(getAdapterPosition()));
        }
    }
}
