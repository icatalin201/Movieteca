package app.mov.movieteca.models.movies;

/**
 * Created by Catalin on 12/7/2017.
 */

public class Genres {

    private Integer id;
    private String name;

    public Genres(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
