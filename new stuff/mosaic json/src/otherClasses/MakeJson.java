package otherClasses;
import org.apache.log4j.Logger;
import net.sf.json.JSONObject;

/**
 * This class will create the JSON object from a bean.
 * @author The Best Pessimist
 *
 */
public class MakeJson {

	static Logger log = Logger.getLogger(InterpretGroovy.class.getClass());
	
	/**
	 * Create the JSON object from a bean.
	 * @author The Best Pessimist 
	 * @param obj of type Bean.
	 * @return JSONObject representing the bean.
	 */
	public static  JSONObject makeJson (Object obj)
	{
		JSONObject jObj = null;
		try{
			jObj = JSONObject.fromObject(obj);
		}
		catch (Exception ex)
		{
			log.fatal("invalid bean.\n" + ex.getMessage() + "\nThe program will now exit.");
			System.exit(-1337);
		}
		return jObj;
	}
}
