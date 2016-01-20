package Help;

/**Queue 
* @author Khalil Stemmler
* November 24th, 2014
* This interface provides the necessary method definitions required for the implementation of a Queue ADT
*/

public interface Queue {
	public void add(Request req);
	public Request leave();
	public boolean isEmpty();
	public int length();
}
