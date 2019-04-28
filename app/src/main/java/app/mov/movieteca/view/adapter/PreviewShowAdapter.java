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
import app.mov.movieteca.model.response.PreviewTVShow;
import app.mov.movieteca.util.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PreviewShowAdapter extends RecyclerView.Adapter<PreviewShowAdapter.PreviewShowViewHolder> {

    private Context context;
    private List<PreviewTVShow> previewTVShowList = new ArrayList<>();
    private boolean hasFullList;
    private int lastPosition = -1;
    private OnItemClickedListener onItemClickedListener;

    public interface OnItemClickedListener {
        void onClick(PreviewTVShow previewTVShow);
    }

    public PreviewShowAdapter(OnItemClickedListener onItemClickedListener,
                               Context context, boolean hasFullList) {
        this.context = context;
        this.hasFullList = hasFullList;
        this.onItemClickedListener = onItemClickedListener;
    }

    public void add(List<PreviewTVShow> previewTVShowList) {
        if (!hasFullList) this.previewTVShowList.clear();
        Collections.shuffle(previewTVShowList);
        this.previewTVShowList.addAll(previewTVShowList);
        notifyDataSetChanged();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull PreviewShowViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (hasFullList) holder.itemView.clearAnimation();
    }

    @NonNull
    @Override
    public PreviewShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int resource = R.layout.preview_media_item_card;
        if (hasFullList) resource = R.layout.media_item_card;
        View view = LayoutInflater.from(context).inflate(resource, parent, false);
        return new PreviewShowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PreviewShowViewHolder holder, int position) {

        if (hasFullList && position > lastPosition) {
//            Animation animation = AnimationUtils.loadAnimation(context,
//                    (i > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.up_from_bottom);
            holder.itemView.startAnimation(animation);
            lastPosition = holder.getAdapterPosition();
        }
        PreviewTVShow previewTVShow = previewTVShowList.get(position);
        Glide.with(context)
                .load(Constants.IMAGE_LOADING_BASE_URL_780.concat(previewTVShow.getPosterPath()))
                .apply(RequestOptions.centerCropTransform())
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return previewTVShowList.size();
    }

    public class PreviewShowViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item)
        CardView item;
        @BindView(R.id.image)
        ImageView image;

        public PreviewShowViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.item)
        void onClick() {
            onItemClickedListener.onClick(previewTVShowList.get(getAdapterPosition()));
        }
    }
}
