package app.mov.movieteca.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import app.mov.movieteca.R;
import app.mov.movieteca.database.Handler;
import app.mov.movieteca.fragments.FullDetailMovie;
import app.mov.movieteca.models.movies.MovieShort;
import app.mov.movieteca.utils.Constants;
import app.mov.movieteca.utils.Helper;

/**
 * Created by Catalin on 12/22/2017.
 */

public class FullListMovieAdapter extends RecyclerView.Adapter<FullListMovieAdapter.FullListMovieViewHolder> {

    public Context context;
    public List<MovieShort> movieList;

    public FullListMovieAdapter(Context context, List<MovieShort> movieList){
        this.context = context;
        this.movieList = movieList;
    }

    @Override
    public FullListMovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.full_list_item_card, parent, false);
        return new FullListMovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FullListMovieViewHolder holder, int position) {
        Glide.with(context.getApplicationContext()).load(Constants.IMAGE_LOADING_BASE_URL_342.concat(movieList.get(position).getBackdrop_path()))
                .asBitmap()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageView);
        holder.textView.setText(movieList.get(position).getOriginal_title());
        if (Handler.isFav(context, "movie", movieList.get(position).getId())){
            holder.fav.setImageResource(R.drawable.ic_favorite_black_18dp);
            holder.fav.setTag("favorit");
        }
        else {
            holder.fav.setImageResource(R.drawable.ic_favorite_border_black_18dp);
            holder.fav.setTag("nefavorit");
        }
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class FullListMovieViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;
        public TextView textView;
        public CardView cardView;
        public ImageButton fav;

        public FullListMovieViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.image);
            textView = (TextView)itemView.findViewById(R.id.title);
            cardView = (CardView)itemView.findViewById(R.id.card_list);
            fav = (ImageButton)itemView.findViewById(R.id.image_button_fav);
            textView.setSelected(true);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences sharedPreferences = context.getSharedPreferences("movies", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt(Constants.movie_id, movieList.get(getAdapterPosition()).getId());
                    editor.commit();
                    Helper.changeFragment(context, new FullDetailMovie());
                }
            });
            fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    if (fav.getTag().equals("favorit")){
                        Handler.removeFromFavorites(context, "movie", movieList.get(getAdapterPosition()).getId());
                        Helper.notifyUser("remove", "fav", movieList.get(getAdapterPosition()).getOriginal_title(), context);
                        fav.setTag("nefavorit");
                        fav.setImageResource(R.drawable.ic_favorite_border_black_18dp);
                    }
                    else if (fav.getTag().equals("nefavorit")){
                        Handler.addToFavorites(context, "movie", movieList.get(getAdapterPosition()).getId(),
                                movieList.get(getAdapterPosition()).getOriginal_title(), movieList.get(getAdapterPosition()).getBackdrop_path());
                        Helper.notifyUser("add", "fav", movieList.get(getAdapterPosition()).getOriginal_title(), context);
                        fav.setTag("favorit");
                        fav.setImageResource(R.drawable.ic_favorite_black_18dp);
                    }
                }
            });
        }
    }
}
