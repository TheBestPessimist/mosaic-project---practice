package fileIO;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

/**
 * 
 * Class used to read from a text file.
 * @author The Best Pessimist
 *
 */
public class ReadFromFile {
	
	private String fileToUse;
	private FileInputStream fis;
	private InputStreamReader isr;
	private BufferedReader infile;
	
	/**
	 * Constructor. Just give the file path to read from.
	 * It will be opened in text mode
	 * @param aux The path to the file 
	 * @throws FileNotFoundException In case the file is unavailable
	 */
	public ReadFromFile (String inputFile) throws FileNotFoundException
	{
		fileToUse = inputFile;
		fis = new FileInputStream (fileToUse);
		isr = new InputStreamReader (fis);
		infile = new BufferedReader (isr);
	}
	

	/**
	 * The function is used to return the handle to the BufferedReader, in order to use it's
	 * functions. 
	 * @return Handle to the BufferedReader
	 */
	public BufferedReader use ()
	{
		return infile;
	}
	
}
