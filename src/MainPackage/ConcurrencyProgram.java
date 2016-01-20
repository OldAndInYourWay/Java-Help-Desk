package MainPackage;

import BasicIO.BinaryDataFile;
import BasicIO.BinaryOutputFile;
import Help.*;

/**ConcurrencyProgram
* @author Khalil Stemmler
* November 28th, 2014
* This class is the entry point for the Concurrency Program that prepares the buffer and opens up the HelpDesk and TechDesk forms.
* After setup, the class runs until both the HelpDesk and TechDesk threads are dead in which case it then writes all existing data within the Buffer to disk.
*/

public class ConcurrencyProgram {
	
	public ConcurrencyProgram(){
		//Creation of objects
		Buffer buffer = new Buffer(5);
		HelpDesk producer = new HelpDesk(buffer);
		TechDesk consumer = new TechDesk(buffer);
		
		//Creation of Threads
		Thread producerThread = new Thread(producer);
		Thread consumerThread = new Thread(consumer);
		
		//Information Passing to GUI to perform specific tasks for specific threads
		producer.getForm().setMonitoredThread(producerThread);
		consumer.getForm().setMonitoredThread(consumerThread); 
		
		//Read In Our Buffer's Previous Data
		System.out.println("Initializing the Buffer");
		BinaryDataFile in = new BinaryDataFile();
		buffer.setup(in);
		
		producerThread.start(); //start essentially just calls the run method (and does some stuff behind the scenes)
		consumerThread.start();
		
		//Guarded block that runs until completion
		while(producerThread.isAlive() || consumerThread.isAlive()){
			
		}
		
		//Finalizing the program and printing out
		System.out.println("Saving the Buffer");
		BinaryOutputFile out = new BinaryOutputFile();
		buffer.finish(out);
		System.exit(0);
	}

	public static void main(String args []){
		new ConcurrencyProgram();
	}
}
