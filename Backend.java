// --== CS400 File Header Information ==--
// Name: Pu Guo
// Email: pguo25@wisc.edu
// Team: JB Blue
// Role: Backend developer
// TA: Xinyi
// Lecturer: Gary Dahl
// Notes to Grader: Found bugs in HashTableMap class done in phase 2, in which I forgot to 
//                  use the absolute value of hash code, fixed in this version

import java.util.List;
import java.util.zip.DataFormatException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;

/**
 * this class store Data got in files into two hash table, and access the information by selecting
 * genres and ratings, modifier for genres, ratings, and the accessor for movies are implemented
 * @author Pu Guo
 */
public class Backend implements BackendInterface{
  private ArrayList<String> Genres;//store selected Genres
  private ArrayList<String> Ratings;//store selected Ratings
  private MapADT<String, List<MovieInterface>> hashGenre;//hash table that use genre as key
  private MapADT<String, List<MovieInterface>> hashRating;//hash table that use rating as key
  private ArrayList<MovieInterface> Movies; //selected movies
  private List<String> AllGenre;
  
  /**
   * Constructor that takes in a FileReader and parse them into two hash tables, as well as 
   * initializing all the list, it also handles exceptions
   * @param inputFileReader the file reader that contains data
   */
  public Backend(Reader inputFileReader) {
    Genres = new ArrayList<String>();
    Ratings = new ArrayList<String>();
    Movies = new ArrayList<MovieInterface>();
    MovieDataReaderInterface reader = new MovieDataReader();
    hashGenre = new HashTableMap<String, List<MovieInterface>>();
    hashRating = new HashTableMap<String, List<MovieInterface>>();
    AllGenre = new ArrayList<String>();
    //initialize all field
    try {
      //retrieve the list of movie objects
      List<MovieInterface> list = reader.readDataSet(inputFileReader);
      for(int i =0;i<list.size();i++) {
        //put into hashGenre table
        //movie with multiple genres will be put in multiple times
        for(int j =0;j<list.get(i).getGenres().size();j++){
          if(!hashGenre.containsKey(list.get(i).getGenres().get(j))){
              List<MovieInterface> mov = new ArrayList<MovieInterface>();
              mov.add(list.get(i));
              hashGenre.put(list.get(i).getGenres().get(j),mov);
              AllGenre.add(list.get(i).getGenres().get(j));
          }
          else{
              List<MovieInterface> mov = hashGenre.get(list.get(i).getGenres().get(j));
              mov.add(list.get(i));
              hashGenre.put(list.get(i).getGenres().get(j),mov);
          }
      }
        //transfer rating from float to string of its first digit
        double a = Math.floor(list.get(i).getAvgVote());
        String rate = (int) a+"";
        for(int j =0;j<=10;j++) {
          List<MovieInterface> mov = new ArrayList<MovieInterface>();
          hashRating.put(j+"", mov);
        }
        List<MovieInterface> mov = hashRating.get(rate);
        mov.add(list.get(i));
        hashRating.put(rate, mov);
      }
      //catch all exceptions
    }catch(FileNotFoundException e1) {
      System.out.println(e1.getMessage());
    }catch(IOException e2) {
      System.out.println(e2.getMessage());
    }catch(DataFormatException e3) {
      System.out.println(e3.getMessage());
    }
  }
  
  
  /**
   * Constructor that takes in a File path and parse the file into two hash tables, as well as 
   * initializing all the list, it also handles exceptions
   * @param inputFileReader the file reader that contains data
   */
  public Backend(String[] args){
    Genres = new ArrayList<String>();
    Ratings = new ArrayList<String>();
    Movies = new ArrayList<MovieInterface>();
    MovieDataReaderInterface reader = new MovieDataReader();
    hashGenre = new HashTableMap<String, List<MovieInterface>>();
    hashRating = new HashTableMap<String, List<MovieInterface>>();
    AllGenre = new ArrayList<String>();
    
    try {
      //all same except we need to create File and FileReader ourselves 
      File f = new File(args[0]);
      FileReader fReader =new FileReader(f);
      List<MovieInterface> list = reader.readDataSet(fReader);
      for(int i =0;i<list.size();i++) {
        for(String genreName:list.get(i).getGenres()) {
          if(!hashGenre.containsKey(genreName)) {
            List<MovieInterface> mov = new ArrayList<MovieInterface>();
            mov.add(list.get(i));
            hashGenre.put(genreName, mov);
            AllGenre.add(genreName);
          }
          else {
            List<MovieInterface> mov = hashGenre.get(genreName);
            mov.add(list.get(i));
            hashGenre.put(genreName, mov);
          }
        }
        double a = Math.floor(list.get(i).getAvgVote());
        String rate = (int) a+"";
        /*
        if(!hashRating.containsKey(rate)) {
          List<MovieInterface> mov = new ArrayList<MovieInterface>();
          mov.add(list.get(i));
          hashRating.put(rate, mov);
        }
        else {*/
        for(int j =0;j<=10;j++) {
          List<MovieInterface> mov = new ArrayList<MovieInterface>();
          hashRating.put(j+"", mov);
        }
        List<MovieInterface> mov = hashRating.get(rate);
        mov.add(list.get(i));
        hashRating.put(rate, mov);
      }
    }catch(FileNotFoundException e1) {
      System.out.println(e1.getMessage());
    }catch(IOException e2) {
      System.out.println(e2.getMessage());
    }catch(DataFormatException e3) {
      System.out.println(e3.getMessage());
    }
  }

  /**
   * Select a Genre
   * @param genre selected genre type
   */
  @Override
  public void addGenre(String genre) {
    Genres.add(genre);
    //add all movie with selected genre first
    for(int i =0;i<hashGenre.get(genre).size();i++) {
      //ensure that each movie only added once
      if(!Movies.contains(hashGenre.get(genre).get(i))) {
        Movies.add(hashGenre.get(genre).get(i));
      }
    }
    //remove not qualified movies
    update();
  }

  /**
   * Select a rating
   * @param rating selected rating value
   */
  @Override
  public void addAvgRating(String rating) {
    Ratings.add(rating);
    for(int i =0;i<hashRating.get(rating).size();i++) {
      if(!Movies.contains(hashRating.get(rating).get(i))) {
        Movies.add(hashRating.get(rating).get(i));
      }
    }
    update();
  }
  
  /**
   * This method check if movies in Movies list still qualifies the selected genres and ratings
   */
  private void update() {
    ArrayList<MovieInterface> newMovies = new ArrayList<MovieInterface>();
    //use a new arrayList to store all valid movies
    //first, regain all the movies that has the indicated genre
    for(int i =0;i<Genres.size();i++) {
      for(int j =0;j<hashGenre.get(Genres.get(i)).size();j++) {
        if(!Movies.contains(hashGenre.get(Genres.get(i)).get(j))) {
          Movies.add(hashGenre.get(Genres.get(i)).get(j));
        }
      }
    }

    for(MovieInterface movie: Movies) {
      boolean hasRating = false;
      //check if its rating is selected
      for(int i=0;i<Ratings.size();i++) {
        if((Math.floor(movie.getAvgVote())+"").equals(Ratings.get(i)+".0")) {
          hasRating = true;
        }
      }
      boolean hasGenre = true;
      for(int i =0;i<Genres.size();i++) {
        //check if any of its genre is selected
        if(!movie.getGenres().contains(Genres.get(i)))
          hasGenre = false;
      }
      //if both are true, the movie can stay
      if(Genres.size()!=0) {
        if(hasRating && hasGenre)
          newMovies.add(movie);
      }
    }
    Movies = newMovies;
  }
  
  /**
   * Not select a genre
   * @param genre the type of genre need to be removed
   */
  @Override
  public void removeGenre(String genre) {
    Genres.remove(genre);
    update();
  }

  /**
   * Not select a rating
   * @param rating the type of rating need to be removed
   */
  @Override
  public void removeAvgRating(String rating) {
    Ratings.remove(rating);
    update();
  }

  /**
   * Get all selected genres
   * @return the list of selected genres
   */
  @Override
  public List<String> getGenres() {
    return Genres;
  }
  /**
   * Get all selected ratings
   * @return the list of selected ratings
   */
  @Override
  public List<String> getAvgRatings() {
    return Ratings;
  }
  
  /**
   * Get size of selected movies
   * @return the size of selected movies
   */
  @Override
  public int getNumberOfMovies() {
    return Movies.size();
  }
  
  /**
   * Get all possible genres
   * @return the size of selected movies
   */
  @Override
  public List<String> getAllGenres() {
    return AllGenre;
  }
  /**
   * Get top 3 selected movies starting from given index
   * @param statringIndex the index in the movies list to begin with
   * @return the list of top movies
   */
  @Override
  public List<MovieInterface> getThreeMovies(int startingIndex) {
    if(Movies.size()<=0 || startingIndex>= Movies.size())
      return new ArrayList<MovieInterface>();
    List<MovieInterface> sublist = Movies.subList(startingIndex, Movies.size());
    Collections.sort(sublist);
    return sublist.subList(0, Math.min(3, sublist.size()));
  }
}
