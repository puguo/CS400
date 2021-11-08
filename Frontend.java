import java.util.Scanner;
import java.util.List;
import java.lang.NumberFormatException;

public class Frontend {
    private Backend BD;
    private List<String> selectedGenres;
    private List<String> selectedRatings;
    private List<String> GENRES;
    private final String[] RATINGS;

    public static void main(String[] args) {
        Frontend application = new Frontend(args);
        application.run();
    }

    public Frontend(String[] args) {
        this.BD = new Backend(args);
        this.GENRES = BD.getAllGenres();
        this.RATINGS = new String[] {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    }

    /**
     * Runs the Movie Mapper program
     */
    public void run() {
        // add all ratings to selection; 0.0 - 10.0
        addAllRatings();
        System.out.println("========================================================\n");
        System.out.println("Movie Mapper has successfully loaded!");
        baseMode(1);
    }

    /**
     * Allows user to select between base mode, genre selection mode, and rating selection mode
     * 
     * @param inBaseMode            indicates whether user is in base mode
     * @param inGenreSelectionMode  indicates whether user is in genre selection mode
     * @param inRatingSelectionMode indicates whether user is in rating selection mode
     */
    private void selection(boolean inBaseMode, boolean inGenreSelectionMode,
            boolean inRatingSelectionMode) {
        Scanner scan = null;
        String selection = null;
        boolean skipMessages = false;
        int numberInput = 0;

        if (inBaseMode) {
            do {
                scan = new Scanner(System.in);
                selection = scan.next();

                try {
                    numberInput = Integer.parseInt(selection);
                    baseMode(numberInput);
                    skipMessages = true;
                    break;
                } catch (NumberFormatException ex) {
                    if (!selection.equals("g") && !selection.equals("r") && !selection.equals("x"))
                        System.out.print(
                                "Whoops! You didn't enter in a valid command. Please try again: ");
                }
            } while (!selection.equals("g") && !selection.equals("r") && !selection.equals("x"));

            if (!skipMessages) {
                switch (selection) {
                    case "g":
                        System.out.println(
                                "========================================================\n");
                        System.out.println("You are currently in genre selection mode...");
                        genreSelectionMode(false, true);
                        break;
                    case "r":
                        System.out.println(
                                "========================================================\n");
                        System.out.println("You are currently in rating selection mode...");
                        ratingSelectionMode(false, true);
                        break;
                    case "x":
                        System.out.println(
                                "========================================================\n");
                        System.out.println("You've selected exit. Goodbye!");
                        return;
                }
            }
        } else if (inGenreSelectionMode) {
            do {
                try {
                    scan = new Scanner(System.in);
                    selection = scan.next();
                    numberInput = Integer.parseInt(selection);
                    if (!(numberInput > 0 && numberInput <= GENRES.size()))
                        throw new Exception();
                    break;
                } catch (NumberFormatException ex) {
                    if (!selection.equals("x") && !selection.equals("s"))
                        System.out.print(
                                "Whoops! You didn't enter in a valid command. Please try again: ");
                } catch (Exception ex) {
                    System.out.print(
                            "Whoops! The number that you choose if out of bounds. Please try again: ");
                }
            } while (!selection.equals("x") && !selection.equals("s"));

            if (!selection.equals("x"))
                if (selection.equals("s")) {
                    genreSelectionMode(true, true);
                } else {
                    selectDeselectGenre(Integer.parseInt(selection), false);
                }
            else {
                baseMode(1);
            }
        } else if (inRatingSelectionMode) {
            do {
                try {
                    scan = new Scanner(System.in);
                    selection = scan.next();
                    numberInput = Integer.parseInt(selection);
                    if (!(numberInput >= 0 && numberInput <= 10))
                        throw new Exception();
                    break;
                } catch (NumberFormatException ex) {
                    if (!selection.equals("x") && !selection.equals("s"))
                        System.out.print(
                                "Whoops! You didn't enter in a valid command. Please try again: ");
                } catch (Exception ex) {
                    System.out.print(
                            "Whoops! The number that you choose if out of bounds. Please try again: ");
                }
            } while (!selection.equals("x") && !selection.equals("s"));

            if (!selection.equals("x"))
                if (selection.equals("s")) {
                    ratingSelectionMode(true, true);
                } else {
                    selectDeselectRating(Integer.parseInt(selection), false);
                }
            else {
                baseMode(1);
            }
        }
    }

    /**
     * Users will be able to scroll through the list by typing in numbers as commands of the rank
     * (by rating) of the movies to display.
     * 
     * @param startingIndex Number that corresponds to the first rank position
     */
    private void baseMode(int startingIndex) {
        // Base Mode
        // get the top three movies by average rating
        List<MovieInterface> baseModeMovies = BD.getThreeMovies(startingIndex - 1);
        MovieInterface movie1 = null;
        MovieInterface movie2 = null;
        MovieInterface movie3 = null;

        if (baseModeMovies.size() == 1) {
            movie1 = baseModeMovies.get(0);
        } else if (baseModeMovies.size() == 2) {
            movie1 = baseModeMovies.get(0);
            movie2 = baseModeMovies.get(1);
        } else if (baseModeMovies.size() == 3) {
            movie1 = baseModeMovies.get(0);
            movie2 = baseModeMovies.get(1);
            movie3 = baseModeMovies.get(2);
        }

        String title1 = movie1 == null ? "" : movie1.getTitle();
        String title2 = movie2 == null ? "" : movie2.getTitle();
        String title3 = movie3 == null ? "" : movie3.getTitle();
        String rating1 = movie1 == null ? "" : "" + movie1.getAvgVote();
        String rating2 = movie2 == null ? "" : "" + movie2.getAvgVote();
        String rating3 = movie3 == null ? "" : "" + movie3.getAvgVote();

        System.out.println("========================================================\n");
        System.out.println("You are currently in base mode...");
        System.out.println("Here are the movies ranked " + startingIndex + " to "
                + (startingIndex + 2) + " by average rating: \n");

        System.out.printf("%-15s%-40s\n", "Average Vote", "Movie Title");
        System.out.printf("%-15s%-40s\n", rating1, "(" + (startingIndex) + ") " + title1);
        System.out.printf("%-15s%-40s\n", rating2, "(" + (startingIndex + 1) + ") " + title2);
        System.out.printf("%-15s%-40s\n", rating3, "(" + (startingIndex + 2) + ") " + title3);

        System.out.printf("\n%-15s%-15s\n", "Commands", "Purpose");
        System.out.printf("%-15s%-15s\n", "(number)",
                "to view the movies ranked <number> to <number + 2>");
        System.out.printf("%-15s%-15s\n", "(g)", "to enter the genre selection mode");
        System.out.printf("%-15s%-15s\n", "(r)", "to enter the rating selection mode");
        System.out.printf("%-15s%-15s\n", "(x)", "to exit");
        System.out.print("\nEnter in a command: ");

        selection(true, false, false);
    }

    /**
     * Selects all ratings (0-10) to start off with
     */
    private void addAllRatings() {
        int i = 0;
        while (i <= 10) {
            BD.addAvgRating("" + i);
            i += 1;
        }
    }

    /**
     * Allows users to select or deselect genres
     * 
     * @param genreNumber         Number that corresponds to a genre
     * @param showGenreSelections Shows selected genres if true
     */
    private void selectDeselectGenre(int genreNumber, boolean showGenreSelections) {
        if (elementInOtherList(selectedGenres, GENRES.get(genreNumber - 1))) {
            System.out.print("Deselected: " + GENRES.get(genreNumber - 1) + "...");
            BD.removeGenre(GENRES.get(genreNumber - 1));
        } else {
            BD.addGenre(GENRES.get(genreNumber - 1));
            System.out.print("Selected: " + GENRES.get(genreNumber - 1) + "...");
        }
        genreSelectionMode(true, showGenreSelections);
    }

    /**
     * Allows users to select or deselect ratings
     * 
     * @param rating               Number that corresponds to a genre
     * @param showRatingSelections Shows selected genres if true
     */
    private void selectDeselectRating(int rating, boolean showRatingSelections) {
        if (elementInOtherList(selectedRatings, RATINGS[rating])) {
            System.out.print("Deselected: " + RATINGS[rating] + "...");
            BD.removeAvgRating(RATINGS[rating]);
        } else {
            System.out.print("Selected: " + RATINGS[rating] + "...");
            BD.addAvgRating(RATINGS[rating]);
        }
        ratingSelectionMode(true, showRatingSelections);
    }

    /**
     * Checks to see if an element is within another list
     * 
     * @param list    List to be searched
     * @param element Element of interest
     * @return true if element is in List
     */
    private boolean elementInOtherList(List<String> list, String element) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(element))
                return true;
        }
        return false;
    }

    /**
     * The genre selection mode lets users select and deselect genres based on which movies are
     * retrieved from the database.
     * 
     * @param addedRemovedGenre   Indicates whether a genre has been recently added/removed
     * @param showGenreSelections Shows list of selected genres
     */
    private void genreSelectionMode(boolean addedRemovedGenre, boolean showGenreSelections) {
        selectedGenres = BD.getGenres();

        if (!addedRemovedGenre) {
            System.out.println(
                    "The genre(s) that you select/deselect will be used to filter your movie results.");
            System.out.println("Selected genres are indicated with a \"*\"");
        }

        if (showGenreSelections) {
            System.out.println("Here are all the selected/deselected genres: ");
            System.out.printf("\n%-6s%-14s%-20s\n", "", "Genre", "Selected");
            // generate list of selected/deselected genres
            for (int i = 0; i < GENRES.size(); i++) {
                System.out.printf("%-6s%-14s%-20s\n", "(" + (i + 1) + ") ", GENRES.get(i),
                        elementInOtherList(selectedGenres, GENRES.get(i)) ? "*" : "");
            }
        }

        if (!addedRemovedGenre) {
            System.out.printf("\n%-15s%-15s\n", "Commands", "Purpose");
            System.out.printf("%-15s%-15s\n", "(number)",
                    "to select/deselect the genre associated with <number>");
            System.out.printf("%-15s%-15s\n", "(s)",
                    "to show the list of selected/deselected genres");
            System.out.printf("%-15s%-15s\n", "(x)", "to return to base mode");
        }

        System.out.print("\nEnter in a command: ");
        selection(false, true, false);
    }

    /**
     * The rating selection mode lets users select and deselect ratings based on which movies are
     * retrieved from the database.
     * 
     * @param addedRemovedRating   Indicates whether a rating has been recently added/removed
     * @param showRatingSelections Shows list of selected ratings
     */
    private void ratingSelectionMode(boolean addedRemovedRating, boolean showRatingSelections) {
        selectedRatings = BD.getAvgRatings();

        if (!addedRemovedRating) {
            System.out.println(
                    "The rating(s) that you select/deselect will be used to filter your movie results.");
            System.out.println("Selected ratings are indicated with a \"*\"");
        }

        if (showRatingSelections) {
            System.out.println("Here are all the selected/deselected ratings: ");
            System.out.printf("\n%-14s%-20s\n", "Rating", "Selected");
            // generate list of selected/deselected ratings
            for (int i = 0; i < RATINGS.length; i++) {
                System.out.printf("%-20s%-20s\n", RATINGS[i],
                        elementInOtherList(selectedRatings, RATINGS[i]) ? "*" : "");
            }
        }

        if (!addedRemovedRating) {
            System.out.printf("\n%-15s%-15s\n", "Commands", "Purpose");
            System.out.printf("%-15s%-15s\n", "(number)", "to select/deselect a rating");
            System.out.printf("%-15s%-15s\n", "(s)",
                    "to show the list of selected/deselected ratings");
            System.out.printf("%-15s%-15s\n", "(x)", "to return to base mode");
        }

        System.out.print("\nEnter in a command: ");
        selection(false, false, true);
    }
}
