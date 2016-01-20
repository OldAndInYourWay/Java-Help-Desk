package Help;

import java.awt.Color;
import java.io.Serializable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import BasicIO.BinaryDataFile;
import BasicIO.BinaryOutputFile;
import Forms.CheckedForm;
import Forms.FieldType;

/**TechDesk
* @author Khalil Stemmler
* November 28th, 2014
* The purpose of this class is to abstract a TechDesk. The TechDesk class is a runnable class that receives Request objects from the buffer and
* displays the contents of the object. The TechDesk runs a single life cycle.
*/

public class TechDesk implements Serializable, Runnable  {
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
	public TechDesk(Buffer q){
		queue = q;
		form = new CheckedForm("TechDesk", Color.RED, 800, 10);
		date = form.addTextField("Date", FieldType.DATE);
		time = form.addTextField("Time", FieldType.TIME);
		number = form.addTextField("Request #", FieldType.INTEGER);
		area = form.addTextArea("Text");
		date.setEditable(false);
		time.setEditable(false);
		number.setEditable(false);
		area.setEditable(false);
	}

        /** The run method, inherited from the Runnable class begins the life cycle of the DeskDesk class. Its purpose is to continuously
         *  receive Request objects using the leave() method and display them within the form. The add method can be blocked if no 
         *  space is currently in the Buffer. The HelpDesk life cycle ends when the user chooses to QUIT and the shouldQuit() method returns true.
         **/
        
	@Override
	public void run() {
		isRunning  = true;	
		System.out.println("The TECHDESK is now running");
		A: while(true){
		Request storedRequest = queue.leave(); //this statement could be blocked
		
		//Display what is on screen
		if(!form.shouldQuit()){
		date.setText(storedRequest.date);
		date.setForeground(Color.BLACK);    //look into why this has to happen
		time.setText(storedRequest.time);
		number.setText(storedRequest.requestNumber);
		area.setText(storedRequest.description);
		}
		
		//Allow the user to accept() what is on screen and move onto the next one
			form.resetForm();
			//Since the TechDesk form doesnt have to deal with input formatting, we will assume that all the input that has been passed is good
			form.setGoodInput(true);
			if(form.shouldQuit()){
				form.setVisible(false);
				System.out.println();
				System.out.println("TechDesk says - Bye bye!");
				break A;
			}
			  while(!form.accept() && !form.shouldQuit()){
				//Waiting for the form to be validated. If OK is pressed, it will break this cycle and add items to the logQueue, otherwise System.exit(0)
				try {
					Thread.sleep(3000);
                                        System.out.println("TECHDESK is waiting for user input");
				} catch (InterruptedException e) {		
				}
				  if(form.shouldQuit()){
					form.setVisible(false);
					System.out.println();
					System.out.println("TechDesk says - Bye bye!");
					break A;
				}
			}
			form.clear();
		}
		//END OF THE EVENT LOOP
		isRunning = false;
	}
	
	/** This method returns the CheckedForm object that is used for the user input of the HelpDesk
	 ** @return CheckedForm 	this method returns the CheckedForm object that has been abstracted in the HelpDesk implementation
         **/
        
	public CheckedForm getForm(){
		return form;
	}
}
