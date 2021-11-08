import java.util.List;
import java.util.zip.DataFormatException;
import java.io.IOException;
import java.io.Reader;

public interface MovieDataReaderInterface {
	
	public List<MovieInterface> readDataSet(Reader inputFileReader) throws  IOException, DataFormatException;

}
