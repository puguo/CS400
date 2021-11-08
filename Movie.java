import java.util.List;

/**
 * This class represents a movie.
 * @author Kevin Kristensen
 */
public class Movie implements MovieInterface {
	private String title; // movie title
	private Integer year; // year movie was released
	private List<String> genre; // genre categories for the movie
	private String director; // director of the movie
	private String description; // brief description of the movie
	private Float avg_vote; // average rating of the movie (0.0-10.0)
	
	/**
	 * Constructs an instance with the provided data.
	 * @param title movie title
	 * @param year year movie was released
	 * @param genre genre categories for the movie
	 * @param director director of the movie
	 * @param description brief description of the movie
	 * @param avg_vote average rating of the movie (0.0-10.0)
	 */
	public Movie(String title, int year, List<String> genre, String director,
			     String description, float avg_vote) {
		this.title = title;
		this.year = year;
		this.genre = genre;
		this.director = director;
		this.description = description;
		this.avg_vote = avg_vote;
	}

	/**
	 * Returns the title of the movie.
	 * @return the title of the movie
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Returns the year the movie was released.
	 * @return the year the movie was released
	 */
	public Integer getYear() {
		return year;
	}

	/**
	 * Returns the genre categories of the movie.
	 * @return the genre categories of the movie
	 */
	public List<String> getGenres() {
		return genre;
	}
	
	/**
	 * Returns the director of the movie.
	 * @return the director of the movie
	 */
	public String getDirector() {
		return director;
	}

	/**
	 * Returns the movie description.
	 * @return the movie description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Returns the average rating of the movie.
	 * @return the average rating of the movie
	 */
	public Float getAvgVote() {
		return avg_vote;
	}

	/**
	 * Compares this movie to another movie object by their average rating.
	 * @return 1 if the other movie has a higher average rating (in which case
	 *         the other movie will be placed before this movie in a sorted
	 *         list), 0 if the movies have the same rating, -1 if this movie
	 *         has a higher rating
	 */
	public int compareTo(MovieInterface otherMovie) {
		float diff = avg_vote - otherMovie.getAvgVote();
		if (diff < 0) {
			return 1;
		} else if (diff > 0) {
			return -1;
		} else {
			return 0;
		}
	}
	
	/**
	 * Prints the fields representing the movie to the console (mostly for 
	 * debugging purposes).
	 */
	public void printMovie() {
		System.out.println("Title: " + title);
		System.out.println("Year: " + year);
		String genreString = "";
		for(String s : genre) {
			genreString += s;
		}
		System.out.println("Genres: " + genreString);
		System.out.println("Director: " + director);
		System.out.println("Description: " + description);
		System.out.println("Avg Vote: " + avg_vote);
	}
	
}
