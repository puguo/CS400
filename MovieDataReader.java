import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/**
 * This class processes a .CSV file through a FileReader to produce a list of 
 * the movies that the file stores. Uses the default constructor. The only
 * public member is the readDataSet() method.
 * @author Kevin Kristensen
 */
public class MovieDataReader implements MovieDataReaderInterface {
	
	// The array of properties that must be present
	private static final String[] KEY_PROPERTIES = {"title", "year", "genre", 
								"director", "description", "avg_vote"};
	
	// The array of properties that are present
	private String[] properties;
	
	// The indices in properties corresponding to the KEY_PROPERTIES
	private int[] indicesOfKeyProperties;
	
	// The list of movies to store
	private List<MovieInterface> movieList;
	
	/**
	 * Processes the .CSV file read from given FileReader to produce a list of 
	 * movies stored therein.
	 * @param inputFileReader the FileReader object created from the .CSV file
	 * @return the list of movies extracted from the file
	 */
	public List<MovieInterface> readDataSet(Reader inputFileReader) 
			   throws IOException, DataFormatException {
		// Initialize reader and Movie list
		BufferedReader reader = new BufferedReader(inputFileReader);
		movieList = new ArrayList<MovieInterface>();
		
		// Process first row of CSV file
		String firstLine = reader.readLine();
		if (firstLine == null) { // Test that the first line is not null
			String msg = "The file is empty.";
			throw new DataFormatException(msg);
		}
		properties = firstLine.split(",");
		genIndicesOfKeyProperties(properties); // Throws a DataFormatException
		
		String nextLine = reader.readLine();
		while (nextLine != null) {
			ArrayList<String> lineReformat = processLine(nextLine);
			if(lineReformat.size() != properties.length) {
				String msg = "Line has incorrect number of columns.";
				throw new DataFormatException(msg);
			}
			Movie mv = createMovie(lineReformat);
			movieList.add(mv);
			nextLine = reader.readLine();
		}
		return movieList;
	}
	
	/**
	 * Creates a movie object from the array of strings corresponding to the
	 * properties by extracting the 'key' properties.
	 * @param lineReformat
	 * @return
	 */
	private Movie createMovie(ArrayList<String> lineReformat) {
		String title = lineReformat.get(indicesOfKeyProperties[0]);
		int year = 
				  Integer.parseInt(lineReformat.get(indicesOfKeyProperties[1]));
		String[] genre = lineReformat.get(indicesOfKeyProperties[2]).split(",");
		ArrayList<String> genres = new ArrayList<String>();
		for (String s : genre) {
			genres.add(s.trim());
		}
		String director = lineReformat.get(indicesOfKeyProperties[3]);
		String description = lineReformat.get(indicesOfKeyProperties[4]);
		float avg_vote = 
				  Float.parseFloat(lineReformat.get(indicesOfKeyProperties[5]));
		return new Movie(title, year, genres, director, description,
								avg_vote);
	}
	
	/**
	 * Returns an array of strings, each string corresponding to a property.
	 * Note that it is a precondition that the line is not null.
	 * @param line the line from the file to process
	 * @return the array of strings corresponding to the properties
	 */
	private ArrayList<String> processLine(String line) {
		String[] data = line.split(",");
		ArrayList<String> dataReformat = new ArrayList<String>();
		boolean combineStrings = false;
		String dummy = "";
		for (String s : data) {
			if (combineStrings) {
				if (s.endsWith("\"")) {
					dummy += "," + s.substring(0,s.length()-1);
					dataReformat.add(dummy);
					combineStrings = false;
				} else {
					dummy += "," + s;
				}
			} else { // not currently combining Strings
				if (s.startsWith("\"")) {
					dummy = s.substring(1);
					combineStrings = true;
				} else {
					dataReformat.add(s);
				}
			}
		}
		return dataReformat;
	}
	
	/**
	 * Returns the indices of the 'key' properties in the list of properties
	 * gathered from the first line in the file.
	 * @param properties the String array of movie properties
	 * @return the indices of the 'key' properties in the input array of 
	 *         properties
	 * @throws DataFormatException if there are repetitions of the 'key'
	 *         property names, or if any of the 'key' property names do not 
	 *         appear
	 */
	private void genIndicesOfKeyProperties(String[] properties) 
												   throws DataFormatException {
		boolean[] found = new boolean[KEY_PROPERTIES.length];
		indicesOfKeyProperties = new int[KEY_PROPERTIES.length];
		for (int j = 0; j < properties.length; j++) {
			for (int i = 0; i < KEY_PROPERTIES.length; i++) {
				if (properties[j].equals(KEY_PROPERTIES[i])) {
					if (found[i]) {
						String msg = "Repeated property names in first line.";
						throw new DataFormatException(msg);
					} else {
						found[i] = true;
						indicesOfKeyProperties[i] = j;
					}
				}
			}
		}
		for (Boolean isFound : found) {
			if (!isFound) {
				String msg = "Key property name not found.";
				throw new DataFormatException(msg);
			}
		}
	}
	
}
