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
import app.mov.movieteca.model.response.Person;
import app.mov.movieteca.util.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.PeopleViewHolder> {

    public interface OnItemClickListener {
        void onClick(Person person);
    }

    private List<Person> personList = new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    private Context context;

    public PeopleAdapter(OnItemClickListener onItemClickListener, Context context) {
        this.onItemClickListener = onItemClickListener;
        this.context = context;
    }

    public void add(List<Person> personList) {
        this.personList.addAll(personList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PeopleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.person_item, parent, false);
        return new PeopleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PeopleViewHolder holder, int position) {
        Person person = personList.get(position);
        holder.name.setText(person.getName());
        if (person.getProfilePath() != null) {
            Glide.with(context)
                    .load(Constants.IMAGE_LOADING_BASE_URL_342.concat(person.getProfilePath()))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.image);
        } else {
            Glide.with(context)
                    .load(R.drawable.ic_baseline_person_24px)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(holder.image);
        }
    }

    @Override
    public int getItemCount() {
        return personList.size();
    }

    public class PeopleViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.name)
        TextView name;

        public PeopleViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.item)
        void onClick() {
            onItemClickListener.onClick(personList.get(getAdapterPosition()));
        }
    }
}
