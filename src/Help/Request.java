package Help;
import java.io.Serializable;

/**Request
* @author Khalil Stemmler
* October 19th, 2014
* This Request class is the object that will be passed through the HelpDesk's queue object and stored in persistent data. The Request objects will
* be read in at the beginning of a HelpDesk invocation and written to a file at the end. The instance variables mirror the fields in the HelpDesk object; they have been parsed
* down to String and int variables for printing.
*/

public class Request implements Serializable {
	//INSTACE VARIABLES
	//This will be the object that we will be populating the queue with
	String date;
	String time;
	String requestNumber;
	String description;
	
	//CONSTRUCTOR
	public Request(String date, String time, String requestNumber, String description){
		this.date = date;
		this.time = time;
		this.requestNumber = requestNumber;
		this.description = description;
	}
	
	/** Gets the date variable for this
     ** @return String		date
     **/
	
	public String getDate(){
		return date;
	}
	
	/** Gets the time variable for this
     ** @return String		time
     **/
	
	public String getTime(){
		return time;
	}
	
	/** Gets the requestNumber variable for this
     ** @return int		requestNumber
     **/
	
	public String requestNumber(){
		return requestNumber;
	}
	
	/** Gets the description variable for this
     ** @return String		description variable
     **/
	
	public String getDescription(){
		return description;
	}
}