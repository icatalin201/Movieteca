package app.mov.movieteca.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import app.mov.movieteca.R;
import app.mov.movieteca.fragments.FullDetailCast;
import app.mov.movieteca.fragments.FullDetailMovie;
import app.mov.movieteca.fragments.FullDetailShow;
import app.mov.movieteca.models.search.SearchResults;
import app.mov.movieteca.utils.Constants;
import app.mov.movieteca.utils.Helper;

/**
 * Created by Catalin on 12/24/2017.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private Context context;
    private List<SearchResults> resultsList;

    public SearchAdapter(Context context, List<SearchResults> results){
        this.context = context;
        this.resultsList = results;
    }

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_item, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position) {
        Glide.with(context).load(Constants.IMAGE_LOADING_BASE_URL_342.concat(resultsList.get(position).getPoster()))
                .asBitmap().centerCrop().into(holder.image);
        holder.title.setText(resultsList.get(position).getName());
        switch (resultsList.get(position).getMediaType()){
            case "movie":
                holder.type.setText("Type: Movie");
                break;
            case "tv":
                holder.type.setText("Type: TV Show");
                break;
            case "person":
                holder.type.setText("Type: Person");
                break;
        }
    }

    @Override
    public int getItemCount() {
        return resultsList.size();
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder{

        public CardView card;
        public ImageView image;
        public TextView title;
        public TextView type;

        public SearchViewHolder(View itemView) {
            super(itemView);
            card = (CardView)itemView.findViewById(R.id.card_search);
            image = (ImageView)itemView.findViewById(R.id.image);
            title = (TextView)itemView.findViewById(R.id.title);
            type = (TextView)itemView.findViewById(R.id.type);
            title.setSelected(true);
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences sharedPreferences = null;
                    SharedPreferences.Editor editor = null;
                    switch (resultsList.get(getAdapterPosition()).getMediaType()){
                        case "movie":
                            sharedPreferences = ((AppCompatActivity)context).getSharedPreferences("movies", Context.MODE_PRIVATE);
                            editor = sharedPreferences.edit();
                            editor.putInt(Constants.movie_id, resultsList.get(getAdapterPosition()).getId());
                            editor.commit();
                            Helper.changeFragment(context, new FullDetailMovie());
                            break;
                        case "tv":
                            sharedPreferences = ((AppCompatActivity)context).getSharedPreferences("tv_shows", Context.MODE_PRIVATE);
                            editor = sharedPreferences.edit();
                            editor.putInt(Constants.tv_show_id, resultsList.get(getAdapterPosition()).getId());
                            editor.commit();
                            Helper.changeFragment(context, new FullDetailShow());
                            break;
                        case "person":
                            sharedPreferences = ((AppCompatActivity)context).getSharedPreferences("cast", Context.MODE_PRIVATE);
                            editor = sharedPreferences.edit();
                            editor.putInt(Constants.cast_id, resultsList.get(getAdapterPosition()).getId());
                            editor.commit();
                            Helper.changeFragment(context, new FullDetailCast());
                            break;
                    }
                }
            });
        }
    }
}
