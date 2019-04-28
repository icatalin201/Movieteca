package app.mov.movieteca.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import app.mov.movieteca.R;
import app.mov.movieteca.model.response.ShowCastsForPerson;
import app.mov.movieteca.util.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PersonShowAdapter extends RecyclerView.Adapter<PersonShowAdapter.PersonViewHolder> {

    public interface OnItemClickListener {
        void onClick(ShowCastsForPerson movieCastsForPerson);
    }

    private List<ShowCastsForPerson> movieCastsForPersonList = new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    private Context context;

    public PersonShowAdapter(OnItemClickListener onItemClickListener, Context context) {
        this.onItemClickListener = onItemClickListener;
        this.context = context;
    }

    public void add(List<ShowCastsForPerson> movieCastsForPeople) {
        this.movieCastsForPersonList.addAll(movieCastsForPeople);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PersonShowAdapter.PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.person_media_item, parent, false);
        return new PersonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonShowAdapter.PersonViewHolder holder, int position) {
        ShowCastsForPerson movieCastsForPerson = movieCastsForPersonList.get(position);

        if (movieCastsForPerson.getBackdropPath() != null) {
            Glide.with(context)
                    .load(Constants.IMAGE_LOADING_BASE_URL_780.concat(movieCastsForPerson.getBackdropPath()))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .apply(RequestOptions.centerCropTransform())
                    .into(holder.image);
        } else {
            Glide.with(context)
                    .load(R.drawable.ic_baseline_movie_creation_24px)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(holder.image);
        }
        holder.title.setText(movieCastsForPerson.getOriginalName());
        holder.role.setText(movieCastsForPerson.getCharacter());
    }

    @Override
    public int getItemCount() {
        return movieCastsForPersonList.size();
    }

    public class PersonViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.role)
        TextView role;

        public PersonViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.item)
        void onClick() {
            onItemClickListener.onClick(movieCastsForPersonList.get(getAdapterPosition()));
        }
    }
}
