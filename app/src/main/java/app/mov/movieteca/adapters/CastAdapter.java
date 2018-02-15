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
import app.mov.movieteca.fragments.FullDetailCast;
import app.mov.movieteca.models.cast.Person;
import app.mov.movieteca.utils.Constants;
import app.mov.movieteca.utils.Helper;

/**
 * Created by Catalin on 12/24/2017.
 */

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.CastViewHolder> {

    public Context context;
    public List<Person> personList;

    public CastAdapter(Context context, List<Person> personList){
        this.context = context;
        this.personList = personList;
    }

    @Override
    public CastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.full_list_item_card, parent, false);
        return new CastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CastViewHolder holder, int position) {
        Glide.with(context.getApplicationContext()).load(Constants.IMAGE_LOADING_BASE_URL_342.concat(personList.get(position).getProfile_path()))
                .asBitmap()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageView);
        holder.textView.setText(personList.get(position).getName());
        if (Handler.isFav(context, "cast", personList.get(position).getId())){
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
        return personList.size();
    }

    public class CastViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView textView;
        public CardView cardView;
        public ImageButton fav;

        public CastViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.image);
            textView = (TextView)itemView.findViewById(R.id.title);
            cardView = (CardView)itemView.findViewById(R.id.card_list);
            fav = (ImageButton)itemView.findViewById(R.id.image_button_fav);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences sharedPreferences = context.getSharedPreferences("cast", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt(Constants.cast_id, personList.get(getAdapterPosition()).getId());
                    editor.commit();
                    Helper.changeFragment(context, new FullDetailCast());
                }
            });
            fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    if (fav.getTag().equals("favorit")){
                        Handler.removeFromFavorites(context, "cast", personList.get(getAdapterPosition()).getId());
                        Helper.notifyUser("remove", "fav", personList.get(getAdapterPosition()).getName(), context);
                        fav.setTag("nefavorit");
                        fav.setImageResource(R.drawable.ic_favorite_border_black_18dp);
                    }
                    else if (fav.getTag().equals("nefavorit")){
                        Handler.addToFavorites(context, "cast", personList.get(getAdapterPosition()).getId(),
                                personList.get(getAdapterPosition()).getName(), personList.get(getAdapterPosition()).getProfile_path());
                        Helper.notifyUser("add", "fav", personList.get(getAdapterPosition()).getName(), context);
                        fav.setTag("favorit");
                        fav.setImageResource(R.drawable.ic_favorite_black_18dp);
                    }
                }
            });
        }
    }
}
