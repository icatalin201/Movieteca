package app.mov.movieteca.models.cast;

/**
 * Created by Catalin on 12/23/2017.
 */

public class ShowCastsForPerson {

    private Integer id;
    private String backdrop_path;
    private String credit_id;
    private String original_name;
    private String character;

    public ShowCastsForPerson(Integer id, String backdrop_path, String credit_id, String original_name, String character) {
        this.id = id;
        this.backdrop_path = backdrop_path;
        this.credit_id = credit_id;
        this.original_name = original_name;
        this.character = character;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public String getCredit_id() {
        return credit_id;
    }

    public void setCredit_id(String credit_id) {
        this.credit_id = credit_id;
    }

    public String getOriginal_name() {
        return original_name;
    }

    public void setOriginal_name(String original_name) {
        this.original_name = original_name;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }
}
