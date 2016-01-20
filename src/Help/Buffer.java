package Help;

import BasicIO.BinaryDataFile;
import BasicIO.BinaryOutputFile;
import java.io.Serializable;    

/**Buffer
* @author Khalil Stemmler
* November 28th, 2014
* The purpose of this class is provide a monitored buffer with the implementation of a Queue so that Requests may be stored in aux storage
* before either being passed to the TechDesk class for review or saved to disk. The implementation is a contiguous Queue.
*/

public class Buffer implements Queue, Serializable {
    //INSTANCE VARIABLES
	private Request[] arrayBuffer;
	private final int MAX_SIZE;
	private int count;
	private int front;
	private int rear;
	
        //CONSTRUCTOR
	public Buffer(int size){
		MAX_SIZE = size;
		arrayBuffer = new Request[MAX_SIZE];
		front = 0;
		rear = 0;
		count = 0;
	}

        /** This method adds a Request object to the Queue. Being synchronized adds safety so that when the method is invoked, 
         ** the state of the arrayBuffer will not change. Therefore, it is deterministic so that only one of either add or leave methods
         ** can be executing at one time. The add method blocks the calling thread while count is equal to maxSize, preventing QueueOverflow
	 ** @param Request 	the Request object to be added into the Queue
         **/
        
	public synchronized void add(Request req) {
	//qualifying this method with a synchronized clause makes it so that only one of the synch methods can be running
		try{
			while(count == MAX_SIZE){
				System.out.print("ADD - ");
				wait();	//when a Thread tries to add a request, if the queue is full, we will wait
				Thread.currentThread().sleep(4000);
				System.out.println("HelpDesk blocked at add method");
			}
				arrayBuffer[rear] = req;
				rear = (rear + 1) % MAX_SIZE;
				count++;
				notifyAll();
				System.out.println("We just added an item to the buffer - COUNT: " + count);
		}	
		 catch (InterruptedException e){
			 System.out.println("The add function has been interrupted");
		 }
	}
        
         /** This method removes a Request object from the Queue. Being synchronized adds safety so that when the method is invoked, 
         ** the state of the arrayBuffer will not change. Therefore, it is deterministic so that only one of either add or leave methods
         ** can be executing at one time. The leave method blocks the calling thread while count is 0, preventing QueueUnderflow
	 ** @return Request 	the next Request object within the Queue
         **/

	public synchronized Request leave() {
		Request temp = null; 
		try{
			while(count == 0){
				System.out.print("LEAVE - ");
				wait();
				Thread.currentThread().sleep(4000);
				System.out.println("TechDesk blocked at leave method");
			}
				System.out.println("Removing an item from the queue");
				temp = arrayBuffer[front];
				front = (front + 1) % MAX_SIZE;
				count--;
				notifyAll();
				System.out.println("We just removed an item from the buffer - COUNT: " + count);
		} catch (InterruptedException e){
			System.out.println("The leave function has been interrupted");
		}
		return temp;
	}
        
        /** This function returns the truth value of count being 0
	 ** @return Boolean     truth value of count being 0
         **/

	public boolean isEmpty() {
		return count == 0;
	}
        
        /** This method returns the count of the Queue
	 ** @return int     the count of the Queue (total number of items within the Queue)
         **/

	public int length() {
		return count;
	}
        
        /** This method pre-populates the Queue within Request objects read in from a BinaryDataFile
	 ** @param BinaryDataFile 	the file to read in containing Request objects
         **/

	public void setup(BinaryDataFile in) {
		Request tempReq = null;
		A: while(true){
			System.out.println("reading");
		try{
		tempReq = (Request) in.readObject();
                if(in.isDataError()){ //if something other than a correct data file was read in
                    break A;
                }
		System.out.println("reading in object #: " + tempReq.requestNumber);
		} catch (NullPointerException e){ //when we are at the end of the file
			System.out.println("END OF FILE");
			break A;
		}
		arrayBuffer[rear] = tempReq;
		rear = (rear + 1) % MAX_SIZE;
		count++;
		}
	}
        
        /** This method writes all the Request within the Queue to the BinaryData file.
	 ** @param BinaryDataFile 	the file write all Queue data to
         **/
	
	public void finish(BinaryOutputFile out){
		while(arrayBuffer.length != 0){
			Request temp = arrayBuffer[front];
			front = (front + 1) % MAX_SIZE;
			count--;
			out.writeObject(temp); //write to Data file
		}
	}

}
