import java.util.ArrayList;
import java.util.List;

public class Theatre {
    private final int theatreId;
    private final String name;
    private final List<Screen> screens;
    private final List<Show> shows;

    public Theatre(int theatreId, String name) {
        this.theatreId = theatreId;
        this.name = name;
        this.screens = new ArrayList<>();
        this.shows = new ArrayList<>();
    }

    public void addScreen(Screen screen) {
        screens.add(screen);
    }

    public void addShow(Show show) {
        shows.add(show);
    }

    public int getTheatreId() {
        return theatreId;
    }

    public String getName() {
        return name;
    }

    public List<Screen> getScreens() {
        return screens;
    }

    public List<Show> getShows() {
        return shows;
    }

    @Override
    public String toString() {
        return "Theatre{'" + name + "', screens=" + screens.size() + ", shows=" + shows.size() + "}";
    }
}
