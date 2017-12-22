package app.mov.movieteca.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.List;

import app.mov.movieteca.R;
import app.mov.movieteca.fragments.FullDetailMovie;
import app.mov.movieteca.models.movies.MovieShort;
import app.mov.movieteca.utils.Constants;

/**
 * Created by Catalin on 12/22/2017.
 */

public class MovieSimilarAdapter extends RecyclerView.Adapter<MovieSimilarAdapter.MovieSimilarViewHolder> {

    public Context context;
    public List<MovieShort> movieShortList;

    public MovieSimilarAdapter(Context context, List<MovieShort> movieShortList){
        this.context = context;
        this.movieShortList = movieShortList;
    }

    @Override
    public MovieSimilarAdapter.MovieSimilarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_item_similar, parent, false);
        return new MovieSimilarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieSimilarAdapter.MovieSimilarViewHolder holder, int position) {
        new GetImage(holder.imageView).execute(Constants.IMAGE_LOADING_BASE_URL_780 + movieShortList.get(position).getBackdrop_path());
        if (movieShortList.get(position).getOriginal_title() != null){
            holder.similarText.setText(movieShortList.get(position).getOriginal_title());
        }
    }

    @Override
    public int getItemCount() {
        return movieShortList.size();
    }

    public class MovieSimilarViewHolder extends RecyclerView.ViewHolder {

        public CardView similarCard;
        public ImageView imageView;
        public TextView similarText;

        public MovieSimilarViewHolder(View itemView) {
            super(itemView);
            similarCard = (CardView)itemView.findViewById(R.id.similar_card);
            imageView = (ImageView)itemView.findViewById(R.id.image_view);
            similarText = (TextView)itemView.findViewById(R.id.text_view_title_show_card);

            similarCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences sharedPreferences = context.getSharedPreferences("movies", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt(Constants.movie_id, movieShortList.get(getAdapterPosition()).getId());
                    editor.commit();
                    FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.FragmentContainer, new FullDetailMovie()).commit();
                }
            });
        }
    }

    private class GetImage extends AsyncTask<String, Void, Bitmap> {

        ImageView bmImage;

        public GetImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
