package app.mov.movieteca.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import app.mov.movieteca.R;
import app.mov.movieteca.model.response.MovieCast;
import app.mov.movieteca.util.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.CastViewHolder> {

    private Context context;
    private List<MovieCast> castList = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onClick(MovieCast movieCast);
    }

    public CastAdapter(Context context, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }

    public void add(List<MovieCast> movieCasts) {
        this.castList.addAll(movieCasts);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CastViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.cast_item, viewGroup, false);
        return new CastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CastViewHolder castViewHolder, int i) {
        MovieCast movieCast = castList.get(i);
        if (movieCast.getProfilePath() != null) {
            Glide.with(context)
                    .load(Constants.IMAGE_LOADING_BASE_URL_342.concat(movieCast.getProfilePath()))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .apply(RequestOptions.circleCropTransform())
                    .into(castViewHolder.castImage);
        } else {
            Glide.with(context)
                    .load(R.drawable.ic_baseline_person_24px)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(castViewHolder.castImage);
        }
        if (movieCast.getName() != null){
            castViewHolder.castName.setText(movieCast.getName());
        }
        if (movieCast.getCharacter() != null){
            castViewHolder.castMovieName.setText(movieCast.getCharacter());
        }
    }

    @Override
    public int getItemCount() {
        return castList.size();
    }

    class CastViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cast_name)
        TextView castName;
        @BindView(R.id.cast_movie_name)
        TextView castMovieName;
        @BindView(R.id.image_view_cast)
        ImageView castImage;
        @BindView(R.id.cast_card)
        CardView castCard;

        CastViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.cast_card)
        void onClick() {
            onItemClickListener.onClick(castList.get(getAdapterPosition()));
        }
    }
}
