package app.mov.movieteca.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
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
import app.mov.movieteca.fragments.FullDetailShow;
import app.mov.movieteca.models.tvshows.TVShowShort;
import app.mov.movieteca.utils.Constants;
import app.mov.movieteca.utils.Helper;

/**
 * Created by Catalin on 12/24/2017.
 */

public class TVShortAdapter extends RecyclerView.Adapter<TVShortAdapter.TVShortViewHolder> {

    private Context context;
    private List<TVShowShort> tvShowShortList;

    public TVShortAdapter(Context context, List<TVShowShort> tvShowShortList){
        this.context = context;
        this.tvShowShortList = tvShowShortList;
    }

    @Override
    public TVShortViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.show_item_card, parent, false);
        return new TVShortViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TVShortViewHolder holder, int position) {
        Glide.with(context.getApplicationContext()).load(Constants.IMAGE_LOADING_BASE_URL_342.concat(tvShowShortList.get(position).getBackdrop_path()))
                .asBitmap()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.movieImageView);
        if (tvShowShortList.get(position).getOriginal_name() != null)
            holder.movieTitleTextView.setText(tvShowShortList.get(position).getOriginal_name());
        else {
            holder.movieTitleTextView.setText("");
        }
        if (tvShowShortList.get(position).getVote_average() != null && tvShowShortList.get(position).getVote_average() > 0) {
            holder.movieRatingTextView.setVisibility(View.VISIBLE);
            holder.movieRatingTextView.setText(String.format("%.1f", tvShowShortList.get(position).getVote_average()) + Constants.RATING_SYMBOL);
        } else {
            holder.movieRatingTextView.setVisibility(View.GONE);
        }
        if (Handler.isFav(context, "tv_show", tvShowShortList.get(position).getId())){
            holder.favButton.setImageResource(R.drawable.ic_favorite_black_18dp);
            holder.favButton.setTag("favorit");
        }
        else {
            holder.favButton.setImageResource(R.drawable.ic_favorite_border_black_18dp);
            holder.favButton.setTag("nefavorit");
        }
        if (Handler.isSeen(context, "movie", tvShowShortList.get(position).getId())){
            holder.seen.setVisibility(View.VISIBLE);
        }
        else {
            holder.seen.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return tvShowShortList.size();
    }

    public class TVShortViewHolder extends RecyclerView.ViewHolder {

        public CardView movieCard;
        public TextView movieTitleTextView;
        public TextView movieRatingTextView;
        public ImageView movieImageView;
        public ImageButton favButton;
        public ImageButton seen;

        public TVShortViewHolder(View itemView) {
            super(itemView);
            movieCard = (CardView) itemView.findViewById(R.id.card_view_show_card);
            movieImageView = (ImageView) itemView.findViewById(R.id.image_view);
            movieTitleTextView = (TextView) itemView.findViewById(R.id.text_view_title_show_card);
            movieRatingTextView = (TextView) itemView.findViewById(R.id.text_view_rating_show_card);
            favButton = (ImageButton) itemView.findViewById(R.id.image_button_fav);
            seen = (ImageButton)itemView.findViewById(R.id.image_button_seen);
            movieTitleTextView.setSelected(true);
            favButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    if (favButton.getTag().equals("favorit")){
                        Handler.removeFromFavorites(context, "tv_show", tvShowShortList.get(getAdapterPosition()).getId());
                        Helper.notifyUser("remove", "fav", tvShowShortList.get(getAdapterPosition()).getOriginal_name(), context);
                        favButton.setTag("nefavorit");
                        favButton.setImageResource(R.drawable.ic_favorite_border_black_18dp);
                    }
                    else if (favButton.getTag().equals("nefavorit")){
                        Handler.addToFavorites(context, "tv_show", tvShowShortList.get(getAdapterPosition()).getId(),
                                tvShowShortList.get(getAdapterPosition()).getOriginal_name(), tvShowShortList.get(getAdapterPosition()).getBackdrop_path());
                        Helper.notifyUser("add", "fav", tvShowShortList.get(getAdapterPosition()).getOriginal_name(), context);
                        favButton.setTag("favorit");
                        favButton.setImageResource(R.drawable.ic_favorite_black_18dp);
                    }
                }
            });

            movieCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences sharedPreferences = context.getSharedPreferences("tv_shows", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt(Constants.tv_show_id, tvShowShortList.get(getAdapterPosition()).getId());
                    editor.commit();
                    Helper.changeFragment(context, new FullDetailShow());
                }
            });
        }
    }
}
