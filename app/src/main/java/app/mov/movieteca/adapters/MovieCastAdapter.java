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

import java.util.List;

import app.mov.movieteca.R;
import app.mov.movieteca.fragments.FullDetailMovie;
import app.mov.movieteca.models.cast.MovieCastsForPerson;
import app.mov.movieteca.utils.Constants;
import app.mov.movieteca.utils.Helper;

/**
 * Created by Catalin on 12/23/2017.
 */

public class MovieCastAdapter extends RecyclerView.Adapter<MovieCastAdapter.CastViewHolder> {

    private Context context;
    private List<MovieCastsForPerson> list;

    public MovieCastAdapter(Context context, List<MovieCastsForPerson> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public CastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cast_plays_item, parent, false);
        return new CastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CastViewHolder holder, int position) {
        Glide.with(context).load(Constants.IMAGE_LOADING_BASE_URL_780 + list.get(position).getBackdrop_path())
                .asBitmap().centerCrop().into(holder.image);
        holder.title.setText(list.get(position).getOriginal_title());
        holder.role.setText(list.get(position).getCharacter());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CastViewHolder extends RecyclerView.ViewHolder {

        public CardView card;
        public ImageView image;
        public TextView title;
        public TextView role;

        public CastViewHolder(View itemView) {
            super(itemView);
            card = (CardView)itemView.findViewById(R.id.cast_plays_card);
            image = (ImageView)itemView.findViewById(R.id.cast_plays_image);
            title = (TextView)itemView.findViewById(R.id.cast_plays_title);
            role = (TextView)itemView.findViewById(R.id.cast_plays_char);
            title.setSelected(true);
            role.setSelected(true);
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences sharedPreferences = context.getSharedPreferences("movies", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt(Constants.movie_id, list.get(getAdapterPosition()).getId());
                    editor.commit();
                    Helper.changeFragment(context, new FullDetailMovie());
                }
            });
        }
    }
}
