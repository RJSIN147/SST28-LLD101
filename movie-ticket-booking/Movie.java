public class Movie {
    private final int movieId;
    private final String title;
    private final String genre;
    private final int durationMinutes;

    public Movie(int movieId, String title, String genre, int durationMinutes) {
        this.movieId = movieId;
        this.title = title;
        this.genre = genre;
        this.durationMinutes = durationMinutes;
    }

    public int getMovieId() {
        return movieId;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    @Override
    public String toString() {
        return "Movie{'" + title + "', genre='" + genre + "', " + durationMinutes + " min}";
    }
}
