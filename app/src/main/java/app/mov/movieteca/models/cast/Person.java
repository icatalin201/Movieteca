package app.mov.movieteca.models.cast;

/**
 * Created by Catalin on 12/23/2017.
 */

public class Person {

    private String biography;
    private String birthday;
    private String deathday;
    private Integer gender;
    private Integer id;
    private String imdb_id;
    private String name;
    private String place_of_birth;
    private String profile_path;

    public Person(String biography, String birthday, String deathday, Integer gender, Integer id,
                  String imdb_id, String name, String place_of_birth, String profile_path) {
        this.biography = biography;
        this.birthday = birthday;
        this.deathday = deathday;
        this.gender = gender;
        this.id = id;
        this.imdb_id = imdb_id;
        this.name = name;
        this.place_of_birth = place_of_birth;
        this.profile_path = profile_path;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getDeathday() {
        return deathday;
    }

    public void setDeathday(String deathday) {
        this.deathday = deathday;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImdb_id() {
        return imdb_id;
    }

    public void setImdb_id(String imdb_id) {
        this.imdb_id = imdb_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace_of_birth() {
        return place_of_birth;
    }

    public void setPlace_of_birth(String place_of_birth) {
        this.place_of_birth = place_of_birth;
    }

    public String getProfile_path() {
        return profile_path;
    }

    public void setProfile_path(String profile_path) {
        this.profile_path = profile_path;
    }
}
