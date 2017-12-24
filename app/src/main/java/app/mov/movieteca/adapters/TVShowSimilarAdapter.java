package app.mov.movieteca.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import app.mov.movieteca.R;
import app.mov.movieteca.fragments.FullDetailMovie;
import app.mov.movieteca.fragments.FullDetailShow;
import app.mov.movieteca.models.movies.MovieShort;
import app.mov.movieteca.models.tvshows.TVShow;
import app.mov.movieteca.models.tvshows.TVShowShort;
import app.mov.movieteca.utils.Constants;
import app.mov.movieteca.utils.Helper;

/**
 * Created by Catalin on 12/24/2017.
 */

public class TVShowSimilarAdapter extends RecyclerView.Adapter<TVShowSimilarAdapter.TVShowViewHolder> {

    public Context context;
    public List<TVShowShort> movieShortList;

    public TVShowSimilarAdapter(Context context, List<TVShowShort> movieShortList){
        this.context = context;
        this.movieShortList = movieShortList;
    }

    @Override
    public TVShowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_item_similar, parent, false);
        return new TVShowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TVShowViewHolder holder, int position) {
        Glide.with(context.getApplicationContext()).load(Constants.IMAGE_LOADING_BASE_URL_342.concat(movieShortList.get(position).getBackdrop_path()))
                .asBitmap()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageView);
        if (movieShortList.get(position).getOriginal_name() != null){
            holder.similarText.setText(movieShortList.get(position).getOriginal_name());
        }
    }

    @Override
    public int getItemCount() {
        return movieShortList.size();
    }

    public class TVShowViewHolder extends RecyclerView.ViewHolder {

        public CardView similarCard;
        public ImageView imageView;
        public TextView similarText;

        public TVShowViewHolder(View itemView) {
            super(itemView);
            similarCard = (CardView)itemView.findViewById(R.id.similar_card);
            imageView = (ImageView)itemView.findViewById(R.id.image_view);
            similarText = (TextView)itemView.findViewById(R.id.text_view_title_show_card);

            similarCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences sharedPreferences = context.getSharedPreferences("tv_shows", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt(Constants.tv_show_id, movieShortList.get(getAdapterPosition()).getId());
                    editor.commit();
                    Helper.changeFragment(context, new FullDetailShow());
                }
            });
        }
    }

}
