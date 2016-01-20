package Help;

import java.awt.Color;
import java.io.Serializable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import BasicIO.BinaryDataFile;
import BasicIO.BinaryOutputFile;
import Forms.*;

/**HelpDesk
* @author Khalil Stemmler
* November 28th, 2014
* The purpose of this class is to abstract a HelpDesk. The HelpDesk class is a runnable class that uses the CheckedForm to obtain sufficient user input
* and then creates a Request item containing form data and sends it off to the Buffer for auxiliary storage. The HelpDesk runs a single life cycle.
*/

public class HelpDesk implements Serializable, Runnable {
	//INSTANCE VARIABLES
	CheckedForm form;
	JTextField date;
	JTextField time;
	JTextField number;
	JTextArea area;
	BinaryDataFile in;
	BinaryOutputFile bin;
	private Buffer queue;
	boolean isRunning;
	
	//CONSTRUCTOR
	public HelpDesk(Buffer q){
		queue = q;
		form = new CheckedForm("HelpDesk", Color.CYAN, 10, 10);
		date = form.addTextField("Date", FieldType.DATE);
		time = form.addTextField("Time", FieldType.TIME);
		number = form.addTextField("Request #", FieldType.INTEGER);
		area = form.addTextArea("Text");
		}
		
        /** The run method, inherited from the Runnable class begins the life cycle of the HelpDesk class. Its purpose is to continuously
         *  create Request objects from parsed user input and insert them into the buffer using the add() method. The add method can be blocked if no 
         *  space is currently in the Buffer. The HelpDesk life cycle ends when the user chooses to QUIT and the shouldQuit() method returns true.
         **/
        
	@Override
	public void run() {
		isRunning  = true;
		System.out.println("The HELPDESK is now running");
		A: while(true){
			form.resetForm();
			//WAIT FOR USER INPUT
			 while((!form.accept() && (!form.shouldQuit())) || form.emptyCheck()){
				//Waiting for the form to be validated. If OK is pressed, it will break this cycle and add items to the logQueue, otherwise System.exit(0)
				 //System.out.println("HelpDesk currently waiting for user prompt");  
					try {
						Thread.sleep(3000);
                                                System.out.println("HELPDESK is waiting for user input");
					} catch (InterruptedException e) {
						
					}
				if(form.shouldQuit()){
					form.setVisible(false);
					System.out.println();
					System.out.println("HelpDesk says - Bye bye!");
					break A;
				}
			} //end of the main event loop
			  //ITEM CREATION
			  //Read in form information
				String dateString = date.getText();
				String timeString = time.getText();
				String numberInt = number.getText();
				String areaText = area.getText();
				
				//Create Request Object
				Request req = new Request(dateString, timeString, numberInt, areaText);
				
				//SYNCHRONIZED METHOD (Can get blocked and sit on this line of code)
			    queue.add(req);
			    form.clear();
	}	
		isRunning = false;
	}
        
        /** This method returns the CheckedForm object that is used for the user input of the HelpDesk
	 ** @return CheckedForm 	this method returns the CheckedForm object that has been abstracted in the HelpDesk implementation
         **/
	
	public CheckedForm getForm(){
		return form;
	}
	
}
