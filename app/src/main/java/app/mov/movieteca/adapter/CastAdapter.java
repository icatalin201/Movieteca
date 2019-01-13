package app.mov.movieteca.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import app.mov.movieteca.R;
import app.mov.movieteca.activity.CastActivity;
import app.mov.movieteca.model.MovieCast;
import app.mov.movieteca.utils.Util;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.CastViewHolder> {

    private Context context;
    private List<MovieCast> castList;

    public CastAdapter(Context context, List<MovieCast> castList) {
        this.context = context;
        this.castList = castList;
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
        if (movieCast.getProfile_path() != null) {
            Glide.with(context)
                    .load(Util.Constants.IMAGE_LOADING_BASE_URL_342.concat(movieCast.getProfile_path()))
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

        private TextView castName;
        private TextView castMovieName;
        private ImageView castImage;
        private CardView castCard;

        CastViewHolder(@NonNull View itemView) {
            super(itemView);
            castName = itemView.findViewById(R.id.cast_name);
            castMovieName = itemView.findViewById(R.id.cast_movie_name);
            castImage = itemView.findViewById(R.id.image_view_cast);
            castCard = itemView.findViewById(R.id.cast_card);
            castName.setSelected(true);
            castMovieName.setSelected(true);
            castCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, CastActivity.class);
                    intent.putExtra(Util.Constants.CAST_ID, castList.get(getAdapterPosition()).getId());
                    context.startActivity(intent);
                }
            });
        }
    }
}
