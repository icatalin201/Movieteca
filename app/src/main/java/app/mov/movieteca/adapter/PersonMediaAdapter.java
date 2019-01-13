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
import app.mov.movieteca.activity.FullMediaActivity;
import app.mov.movieteca.model.BaseMediaForPerson;
import app.mov.movieteca.model.MovieCastsForPerson;
import app.mov.movieteca.model.ShowCastsForPerson;
import app.mov.movieteca.utils.Util;

public class PersonMediaAdapter extends RecyclerView.Adapter<PersonMediaAdapter.PersonMediaViewHolder> {

    private Context context;
    private List<BaseMediaForPerson> list;

    public PersonMediaAdapter(Context context, List<BaseMediaForPerson> list) {
        this.context = context;
        this.list = list;
    }

    public void add(List<? extends BaseMediaForPerson> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PersonMediaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.person_media_item, viewGroup, false);
        return new PersonMediaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonMediaViewHolder personMediaViewHolder, int i) {
        String title;
        String role;
        String path = null;
        BaseMediaForPerson model = list.get(i);
        if (model.getType().equals(Util.Constants.MOVIE)) {
            title = ((MovieCastsForPerson) model).getOriginal_title();
            role = ((MovieCastsForPerson) model).getCharacter();
            if (((MovieCastsForPerson) model).getBackdrop_path() != null) {
                path = Util.Constants.IMAGE_LOADING_BASE_URL_780.concat(((MovieCastsForPerson) model).getBackdrop_path());
            }
        } else {
            title = ((ShowCastsForPerson) model).getOriginal_name();
            role = ((ShowCastsForPerson) model).getCharacter();
            if (((ShowCastsForPerson) model).getBackdrop_path() != null) {
                path = Util.Constants.IMAGE_LOADING_BASE_URL_780.concat(((ShowCastsForPerson) model).getBackdrop_path());
            }
        }
        if (path != null) {
            Glide.with(context)
                    .load(path)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .apply(RequestOptions.centerCropTransform())
                    .into(personMediaViewHolder.image);
        } else {
            Glide.with(context)
                    .load(R.drawable.ic_baseline_movie_creation_24px)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(personMediaViewHolder.image);
        }
        personMediaViewHolder.title.setText(title);
        personMediaViewHolder.role.setText(role);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class PersonMediaViewHolder extends RecyclerView.ViewHolder {

        private CardView card;
        private ImageView image;
        private TextView title;
        private TextView role;

        PersonMediaViewHolder(@NonNull View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.item);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            role = itemView.findViewById(R.id.role);
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BaseMediaForPerson previewMedia = list.get(getAdapterPosition());
                    Intent intent = new Intent(context, FullMediaActivity.class);
                    intent.putExtra("type", previewMedia.getType());
                    if (previewMedia.getType().equals(Util.Constants.MOVIE)) {
                        intent.putExtra("id", ((MovieCastsForPerson) previewMedia).getId());
                    } else {
                        intent.putExtra("id", ((ShowCastsForPerson) previewMedia).getId());
                    }
                    context.startActivity(intent);
                }
            });
        }
    }
}
