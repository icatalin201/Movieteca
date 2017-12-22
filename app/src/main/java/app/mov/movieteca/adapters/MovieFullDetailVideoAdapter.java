package app.mov.movieteca.adapters;

import android.content.Context;
import android.content.Intent;
import android.drm.DrmStore;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ConcurrentModificationException;
import java.util.List;

import app.mov.movieteca.R;
import app.mov.movieteca.models.movies.Movie;
import app.mov.movieteca.models.movies.VideoInfo;
import app.mov.movieteca.models.movies.Videos;
import app.mov.movieteca.utils.Constants;

/**
 * Created by Catalin on 12/21/2017.
 */

public class MovieFullDetailVideoAdapter extends RecyclerView.Adapter<MovieFullDetailVideoAdapter.MovieFullDetailViewHolder> {

    private Context context;
    private List<VideoInfo> videosList;

    public MovieFullDetailVideoAdapter(Context context, List<VideoInfo> videosList){
        this.context = context;
        this.videosList = videosList;
    }

    @Override
    public MovieFullDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_item_trailer, parent, false);
        return new MovieFullDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieFullDetailViewHolder holder, int position) {
        new GetImage(holder.trailerImage).execute(Constants.YOUTUBE_THUMBNAIL_BASE_URL + videosList.get(position).getKey() + Constants.YOUTUBE_THUMBNAIL_IMAGE_QUALITY);
        if (videosList.get(position).getName() != null) {
            holder.trailerInfo.setText(videosList.get(position).getName());
        }
        else{
            holder.trailerInfo.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return videosList.size();
    }

    public class MovieFullDetailViewHolder extends RecyclerView.ViewHolder {

        public ImageView trailerImage;
        public LinearLayout trailerCard;
        public TextView trailerInfo;

        public MovieFullDetailViewHolder(View itemView) {
            super(itemView);
            trailerCard = (LinearLayout) itemView.findViewById(R.id.trailer_card);
            trailerImage = (ImageView)itemView.findViewById(R.id.trailer_image);
            trailerInfo = (TextView)itemView.findViewById(R.id.trailer_info);

            trailerCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.YOUTUBE_WATCH_BASE_URL + videosList.get(getAdapterPosition()).getKey()));
                    context.startActivity(intent);
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
