package Forms;

import javax.swing.JTextArea;

/**CheckedForm 
* @author Khalil Stemmler
* November 10th, 2014
* This TextAreasNode class is used to dynamically allocate information for each JTextArea added within the CheckedForm.
*/

public class TextAreasNode{
	//INSTANCE VARIABLES
	JTextArea area;
	String name;
	TextAreasNode next;
	int size;
	
	//CONSTRUCTOR
	TextAreasNode(JTextArea area, String name, TextAreasNode next){
		this.area = area;
		this.name = name;
		this.next = next;
		size = 1;
	}
	
	/** This accessor method gets the size of the TextAreasNode
	 ** @return int 	size of the Node (get)
     **/
	
	public int getSize(){
		return size;
	}
	
	/** This method sets the size of the TextAreasNode
	 ** @param int		size of the Node (set)
     **/
	
	public void setSize(int size){
		this.size = size;
	}
}
