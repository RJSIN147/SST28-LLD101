import java.time.LocalDateTime;

public class ShowBuilder {
    private int showId;
    private Movie movie;
    private Screen screen;
    private LocalDateTime showTime;

    public ShowBuilder setShowId(int showId) {
        this.showId = showId;
        return this;
    }

    public ShowBuilder setMovie(Movie movie) {
        this.movie = movie;
        return this;
    }

    public ShowBuilder setScreen(Screen screen) {
        this.screen = screen;
        return this;
    }

    public ShowBuilder setShowTime(LocalDateTime showTime) {
        this.showTime = showTime;
        return this;
    }

    public Show build() {
        if (movie == null) {
            throw new IllegalStateException("Movie is required.");
        }
        if (screen == null) {
            throw new IllegalStateException("Screen is required.");
        }
        if (showTime == null) {
            throw new IllegalStateException("Show time is required.");
        }
        return new Show(showId, movie, screen, showTime);
    }
}
