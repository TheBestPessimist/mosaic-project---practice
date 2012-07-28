package otherClasses;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Vector;
import org.apache.log4j.Logger;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import fileIO.ReadFromFile;
import Beans.FromMachineBean;
import Beans.GigaBean;
import Beans.ToMachineBean;

/**
 * assertions are made before parsing to the bean, because if i have different types, 
 * then i will get who_knows_what values 
 */

/**
 * 
 * @author The Best Pessimist
 * 
 */

public class PopulateBean {
	private GigaBean gigaBean;
	public static JSONObject jsonObject;
	private Vector<String> singleNodeVariablesVerify = new Vector<String>();
	private Vector<String> allNodesVariablesVerify = new Vector<String>();

	private Logger log = Logger.getLogger(PopulateBean.class.getClass());
//	this is used for the logger
	
	/**
	 * Constructor
	 * 
	 * @throws Exception
	 */
	public PopulateBean() {
		gigaBean = new GigaBean();
		try {
			this.jsonObject = parseJson();
		} catch (JSONException jex) {
			log.fatal("Invalid JSON string.\n" + jex.getMessage());
			log.fatal("Exiting program...");
			System.exit(-1);
		}
		// created the jsonObject

		prepareVerifyVariables();

		/*
		 * in order to use the GigaBean object, i have to split the big json
		 * object into 2 objects: 1 for the from-machine bean, and 1 for the
		 * to-machine bean
		 */
		createFromMachine(jsonObject);
		createToMachine(jsonObject);
	}

	
	/**
	 * Get the single node variables and the all nodes variables from the 2
	 * files supplied in the docs folder.
	 */
	private void prepareVerifyVariables() {

		try {
			ReadFromFile inFile = new ReadFromFile(
					"docs/single_task_variables.txt");
			// read from the file

			String aux = "";
			while (inFile.use().ready()) {
				aux += inFile.use().readLine();
			}
			String aux2[];
			aux2 = aux.split("(\\s*)([,])(\\s*)");
			// aici am regex care ma scapa de combinatiile spatiu virgula
			// spatiu(nici unul sau mai multe)

			for (String i : aux2)
				singleNodeVariablesVerify.add(i);
			// add the strings to the vector
		} catch (IOException ioe) {
			// In case no reading could be made from that particular file
			log.error("Could not read the single_task_variables.txt file. "
					+ ioe.getMessage() + "\nThe program will now exit.");
			System.exit(-1);
		}

		// read the other file
		try {
			ReadFromFile inFile = new ReadFromFile(
					"docs/all_tasks_variables.txt");
			// read from the file

			String aux = "";
			while (inFile.use().ready()) {
				aux += inFile.use().readLine();
			}

			String aux2[] = aux.split("(\\s*)([,])(\\s*)");
			// aici am regex care ma scapa de combinatiile spatiu virgula
			// spatiu(nici unul sau mai multe)

			for (String i : aux2)
				allNodesVariablesVerify.add(i);
			// add the strings to the vector
		} catch (IOException ioe) {
			// In case no reading could be made from that particular file
			log.error("Could not read the all_tasks_variables.txt file. "
					+ ioe.getMessage() + "\nThe program will now exit.");
			System.exit(-1);
		}

	}

	
	/**
	 * Parses the JSON object from a file.
	 * 
	 * @(Must be modified!!!!)
	 * @return a new JSONObject
	 * @throws IOException
	 */
	private JSONObject parseJson() {
		try {
			ReadFromFile input = new ReadFromFile("docs/infile.json");
			BufferedReader bufferedReader = input.use();
			String aux = "";
			while (bufferedReader.ready()) {
				aux += (char) bufferedReader.read();
			}

			return new JSONObject().fromObject(aux);
		} catch (IOException ioe) {
			// In case the json can't be parsed.
			log.fatal("Could not parse the json data file. " + ioe.getMessage()
					+ "\nThe program will now exit.");
			System.exit(-1337);
			return null;
			// this is unreachable code, but it has to be put in order to
			// respect the function definition
		}

	}

	
	/**
	 * creates the json object which will then be used to populate the
	 * {@link FromMachineBean}
	 * 
	 * @throws Exception
	 */
	private void createFromMachine(JSONObject jsonObject) {
		// do all the verifications first
		assert jsonObject.containsKey("from_machine");
		// assume i have the from-machine object
		JSONObject fromMachine = jsonObject.getJSONObject("from_machine");
		
		if (fromMachine.isNullObject()) {
			log.fatal("from_machine key not found in json."
					+ "\nThe program will now exit.");
			System.exit(-1);
		}
		boolean isToMachine = false;
		checkArrays(fromMachine, isToMachine);
		// assume i have the 2 arrays and they have correct data

		// ###### all verifications completed at this point ###########

		FromMachineBean fmb = new FromMachineBean();
		fmb = (FromMachineBean) JSONObject.toBean(fromMachine,	FromMachineBean.class);
		gigaBean.setFrom_machine(fmb);
		// gigaBean has the from machine part!
	}

	
	/**
	 * creates the json object which will then be used to populate the
	 * {@link ToMachineBean}
	 * 
	 * @param jsonObject
	 *            The JSON object that contains the 2 arrays
	 * @throws Exception
	 *             The JSON arrays do not have valid data
	 */
	private void createToMachine(JSONObject jsonObject) {
		// doing all the verifications first...
		assert jsonObject.containsKey("to_machine");
		// assuming there is the to-machine object
		JSONObject toMachine = jsonObject.getJSONObject("to_machine");

		if (toMachine.isNullObject()) {
			log.fatal("to_machine key not found in json."
					+ "\nThe program will now exit.");
			System.exit(-1);
		}
		boolean isToMachine = true;
		checkArrays(toMachine, isToMachine);

		// assuming the 2 arrays exist and have good data

		ToMachineBean tmb = (ToMachineBean) toMachine.toBean(toMachine,ToMachineBean.class);
		gigaBean.setTo_machine(tmb);
		// gigaBean has now the to-machine object
	}

	
	/**
	 * 
	 * @param jObj
	 *            The JSON object that contains the 2 arrays
	 * @param isToMachine 
	 * @throws Exception
	 *             The JSON arrays do not have valid data
	 */
	private void checkArrays(JSONObject jObj, boolean isToMachine) {
		// assume array single_task_variables exists
		JSONArray array = jObj.getJSONArray("single_task_variables");

		int i;
		for (i = 0; i < array.size(); ++i)
			if (!singleNodeVariablesVerify.contains(array.get(i).toString())) {
				log.fatal("" + new Exception(	"Invalid single_task_variables! \nThe program will now exit.").getMessage());
				System.exit(-1337);
			}
		// all data in this array is valid
		
		
		if (isToMachine){
			// assume the array all_tasks_variables exists
			array = jObj.getJSONArray("all_tasks_variables");
	
			try {
				for (i = 0; i < array.size(); ++i)
					if (!allNodesVariablesVerify.contains(array.get(i).toString()))
						throw (new Exception("Invalid all_tasks_variables!"));
				// all data in the array is valid
				
			} catch (Exception e) {
				log.fatal("Invalid data array. " + e.getMessage()
						+ "\nThe program will now exit.");
				System.exit(-1337);
			}
		}
	}

	
	/**
	 * @return the gigaBean
	 */
	public GigaBean getGigaBean() {
		return gigaBean;
	}

	
	/**
	 * @param gigaBean
	 *            the gigaBean to set
	 */
	public void setGigaBean(GigaBean gigaBean) {
		this.gigaBean = gigaBean;
	}

}