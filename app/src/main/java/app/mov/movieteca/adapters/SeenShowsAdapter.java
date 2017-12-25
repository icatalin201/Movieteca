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
import app.mov.movieteca.fragments.FullDetailShow;
import app.mov.movieteca.models.tvshows.TVShowShort;
import app.mov.movieteca.utils.Constants;
import app.mov.movieteca.utils.Helper;

/**
 * Created by Catalin on 12/24/2017.
 */

public class SeenShowsAdapter extends RecyclerView.Adapter<SeenShowsAdapter.SeenShowsViewHolder> {

    private Context context;
    public List<TVShowShort> showShortList;

    public SeenShowsAdapter(Context context, List<TVShowShort> showShortList){
        this.context = context;
        this.showShortList = showShortList;
    }

    @Override
    public SeenShowsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.seen_item_card, parent, false);
        return new SeenShowsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SeenShowsViewHolder holder, int position) {
        Glide.with(context.getApplicationContext()).load(Constants.IMAGE_LOADING_BASE_URL_342.concat(showShortList.get(position).getBackdrop_path()))
                .asBitmap()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageView);
        holder.textView.setText(showShortList.get(position).getOriginal_name());
    }

    @Override
    public int getItemCount() {
        return showShortList.size();
    }

    public class SeenShowsViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView textView;
        public CardView cardView;

        public SeenShowsViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.image);
            textView = (TextView)itemView.findViewById(R.id.title);
            cardView = (CardView)itemView.findViewById(R.id.card_list);
            textView.setSelected(true);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences sharedPreferences = context.getSharedPreferences("tv_shows", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt(Constants.tv_show_id, showShortList.get(getAdapterPosition()).getId());
                    editor.commit();
                    Helper.changeFragment(context, new FullDetailShow());
                }
            });
        }
    }
}
