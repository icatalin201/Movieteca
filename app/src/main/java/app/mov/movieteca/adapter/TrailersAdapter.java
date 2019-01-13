package app.mov.movieteca.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import app.mov.movieteca.R;
import app.mov.movieteca.model.VideoInfo;
import app.mov.movieteca.utils.Util;

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailersViewHolder> {

    private Context context;
    private List<VideoInfo> videosList;

    public TrailersAdapter(Context context, List<VideoInfo> videosList) {
        this.context = context;
        this.videosList = videosList;
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
                    .load(Util.Constants.YOUTUBE_THUMBNAIL_BASE_URL
                            .concat(videoInfo.getKey())
                            .concat(Util.Constants.YOUTUBE_THUMBNAIL_IMAGE_QUALITY))
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

        private ImageView trailerImage;
        private LinearLayout trailerCard;
        private TextView trailerInfo;


        TrailersViewHolder(@NonNull View itemView) {
            super(itemView);
            trailerCard = itemView.findViewById(R.id.trailer_card);
            trailerImage = itemView.findViewById(R.id.trailer_image);
            trailerInfo = itemView.findViewById(R.id.trailer_info);

            trailerCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(Util.Constants.YOUTUBE_WATCH_BASE_URL +
                                    videosList.get(getAdapterPosition()).getKey()));
                    context.startActivity(intent);
                }
            });
        }
    }
}
