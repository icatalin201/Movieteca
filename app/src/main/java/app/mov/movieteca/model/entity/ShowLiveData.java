package app.mov.movieteca.model.entity;

import java.util.List;

import app.mov.movieteca.model.response.PreviewTVShow;

public class ShowLiveData {

    private List<PreviewTVShow> popular;
    private List<PreviewTVShow> nowPlaying;
    private List<PreviewTVShow> upcoming;
    private List<PreviewTVShow> topRated;
    private boolean hasError;
    private Throwable throwable;

    public List<PreviewTVShow> getPopular() {
        return popular;
    }

    public void setPopular(List<PreviewTVShow> popular) {
        this.popular = popular;
    }

    public List<PreviewTVShow> getNowPlaying() {
        return nowPlaying;
    }

    public void setNowPlaying(List<PreviewTVShow> nowPlaying) {
        this.nowPlaying = nowPlaying;
    }

    public List<PreviewTVShow> getUpcoming() {
        return upcoming;
    }

    public void setUpcoming(List<PreviewTVShow> upcoming) {
        this.upcoming = upcoming;
    }

    public List<PreviewTVShow> getTopRated() {
        return topRated;
    }

    public void setTopRated(List<PreviewTVShow> topRated) {
        this.topRated = topRated;
    }

    public boolean isHasError() {
        return hasError;
    }

    public void setHasError(boolean hasError) {
        this.hasError = hasError;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }
}
