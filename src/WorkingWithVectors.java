import java.util.Vector;


public class WorkingWithVectors {

	/**
	 * @param args
	 * 
	 * Simple using of class Vector. 
	 * Nothing to do here!
	 * 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		Vector<Integer> vecti = new Vector<Integer>();	
//		Vector trebuie sa primeasca o clasa, de aceea am folosit "Integer", 
//		si nu int.  
		
		for (int i = 1; i <=123456; ++i)
			vecti.add( (2*(7*i+486*i/2)%191*i) ); 	// se vede k e facuta de mine :D
		
		for (int i = 1; i < 123456; ++i)
			System.out.println (vecti.get(i)); 		// il afisez

	}

}
