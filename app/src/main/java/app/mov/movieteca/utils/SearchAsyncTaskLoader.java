package app.mov.movieteca.utils;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import app.mov.movieteca.models.search.Search;
import app.mov.movieteca.models.search.SearchResults;

/**
 * Created by Catalin on 12/24/2017.
 */

public class SearchAsyncTaskLoader extends AsyncTaskLoader<Search> {

    private Context context;
    private String keyword;
    private String page;

    public SearchAsyncTaskLoader(Context context, String keyword, String page){
        super(context);
        this.context = context;
        if (keyword.contains(" ")){
            this.keyword = keyword.replace(" ", "%20");
        }
        else {
            this.keyword = keyword;
        }
        this.page = page;
    }

    @Override
    public Search loadInBackground() {
        try {
            String urlString = "https://api.themoviedb.org/3/" + "search/multi"
                    + "?"
                    + "api_key=" + Constants.api_key
                    + "&"
                    + "query=" + keyword
                    + "&"
                    + "page=" + page;
            URL url = new URL(urlString);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");

            httpURLConnection.connect();

            if (httpURLConnection.getResponseCode() != 200) return null;

            InputStream inputStream = httpURLConnection.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            String jsonString = "";
            while (scanner.hasNext()) {
                jsonString += scanner.nextLine();
            }
            Log.i("json", jsonString);
            Log.i("url", urlString);
            // Parse JSON
            JSONObject searchJsonObject = new JSONObject(jsonString);
            Search searchResponse = new Search();
            searchResponse.setPage(searchJsonObject.getInt("page"));
            searchResponse.setTotal_pages(searchJsonObject.getInt("total_pages"));
            JSONArray resultsJsonArray = searchJsonObject.getJSONArray("results");
            List<SearchResults> searchResults = new ArrayList<>();
            for (int i = 0; i < resultsJsonArray.length(); i++) {
                JSONObject result = (JSONObject) resultsJsonArray.get(i);
                SearchResults searchResult = new SearchResults();
                switch (result.getString("media_type")) {
                    case "movie":
                        searchResult.setId(result.getInt("id"));
                        searchResult.setPoster(result.getString("poster_path"));
                        searchResult.setName(result.getString("title"));
                        searchResult.setMediaType("movie");
                        break;
                    case "tv":
                        searchResult.setId(result.getInt("id"));
                        searchResult.setPoster(result.getString("poster_path"));
                        searchResult.setName(result.getString("name"));
                        searchResult.setMediaType("tv");
                        break;
                    case "person":
                        searchResult.setId(result.getInt("id"));
                        searchResult.setPoster(result.getString("profile_path"));
                        searchResult.setName(result.getString("name"));
                        searchResult.setMediaType("person");
                        break;
                }
                searchResults.add(searchResult);
            }
            searchResponse.setResults(searchResults);

            return searchResponse;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
