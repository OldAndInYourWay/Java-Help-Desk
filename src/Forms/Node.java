package Forms;

import javax.swing.JTextField;

/**CheckedForm 
* @author Khalil Stemmler
* November 10th, 2014
* This Node class is used to dynamically allocate information for each JTextField added within the CheckedForm.
*/

public class Node {
	//INSTANCE VARIABLES
	JTextField field;
	String name;
	FieldType kind;
	Node next;
	int size;
	
	//CONSTRUCTOR
	public Node(JTextField field, FieldType kind, String name, Node next){
		this.field = field;
		this.kind = kind;
		this.name = name;
		this.next = next;
		size = 1;
	}
	
	/** This accessor method gets the size of the Node
	 ** @return int 	size of the Node (get)
     **/
	
	public int getSize(){
		return size;
	}
	
	/** This method sets the size of the Node
	 ** @param int		size of the Node (set)
     **/
	
	public void setSize(int size){
		this.size = size;
	}

}
