package Forms;
import java.awt.*;
import java.awt.event.*;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.NoSuchElementException;

import javax.swing.*;

/**CheckedForm 
	* @author Khalil Stemmler
	* November 28th, 2014
	* The purpose of this class is to extend functionality from the Java Swing library's JFrame class and construct a revised version of a 
	* CheckedForm class. The CheckedForm class is used to check forms of a specified type and neglect to accept a form with erroneous data.
	* This implementation verifies the forms by implementing the ActionLister and FocusListener interfaces. The ActionListener provides functionality to check 
	* and verify data typed within the forms as it has been submitted while the FocusListener verifies data as a form has been focused on.
	* 
	* EDIT:
	* Functionality has been added to support the monitoring of a Thread
*/

public class CheckedForm extends JFrame implements ActionListener, FocusListener {
	
	//INSTANCE VARIABLES
	JButton ok = new JButton("OK");
	JButton quit = new JButton("Quit");
	JPanel uncheckedFields;
	JPanel checkedFields;
	Node fieldsList;
	TextAreasNode textAreasList;
	Boolean goodInput = false;
	Boolean accept = false;
	Boolean shouldQuit = false;
	Thread monitoredThread; //added functionality to monitor a Thread 
	
	//CONSTRUCTOR
	public CheckedForm(String name, Color col, int xLocation, int yLocation){
		super(name);
		fieldsList = null;
		this.setLocation(xLocation, yLocation);
		this.getContentPane().setBackground(col);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(500, 400);
		this.setResizable(false);
		setLayout(new BorderLayout());
		
		//Checked Fields
		GridLayout grid = new GridLayout(0, 4);
		checkedFields = new JPanel();
		checkedFields.setLayout(grid);
		this.add(checkedFields, BorderLayout.NORTH);
		
		//Unchecked Fields
		FlowLayout flow = new FlowLayout();
		uncheckedFields = new JPanel();
		uncheckedFields.setLayout(flow);
		this.add(uncheckedFields, BorderLayout.CENTER);
		
		//Buttons
		JPanel buttons = new JPanel();
		buttons.setLayout(new FlowLayout());
		ok.addActionListener(new ConfirmOrQuit());
		quit.addActionListener(new ConfirmOrQuit());
		buttons.add(ok);
		buttons.add(quit);
		this.add(buttons, BorderLayout.SOUTH);
			
		this.setVisible(true);	
		
	}
	
	/** This method is responsible for adding a new JTextField to the form. In addition to adding the JTextField to the form, a reference is also
	 ** kept by using the addFieldNode() method.
     ** @param String 		the name value for the textField's information being added to the node
     ** @param FieldType 	the type of field being added to the form
     **/
	
	public JTextField addTextField(String label, FieldType kind){
		JTextField field = new JTextField();
		
		if(kind == FieldType.DATE){
			checkedFields.add(new JLabel(label));
			checkedFields.add(field);
			//System.out.println("adding type Date");
		}
		if(kind == FieldType.TIME){
			checkedFields.add(new JLabel(label));
			checkedFields.add(field);
			//System.out.println("adding type Time");
		}
		if(kind == FieldType.CURRENCY){
			checkedFields.add(new JLabel(label));
			checkedFields.add(field);
			//System.out.println("adding type Currency");
		}
		if(kind == FieldType.DECIMAL){
			checkedFields.add(new JLabel(label));
			checkedFields.add(field);
			//System.out.println("adding type Decimal");
		}
		if(kind == FieldType.INTEGER){
			checkedFields.add(new JLabel(label));
			checkedFields.add(field);
			//System.out.println("adding type Integer");
		}
		if(kind == FieldType.PERCENT){
			checkedFields.add(new JLabel(label));
			checkedFields.add(field);
			//System.out.println("adding type Percent");
		}
		field.addActionListener(this);
		field.addFocusListener(this);
		addFieldNode(field, label, kind);
		this.setVisible(true);
		return field; //this is returned so that we can keep a reference to this object
	}
	
	/** This method is responsible for adding a new JTextArea to the form. In addition to adding the JTextArea to the form, a reference is also
	 ** kept by using the addAreaNode() method.
     ** @param String 		the name value for the textArea's information being added to the node
     **/
	
	public JTextArea addTextArea(String label){
		JTextArea area = new JTextArea(5, 30);
			uncheckedFields.add(new JLabel(label));
			uncheckedFields.add(area);
			this.setVisible(true);
			addAreaNode(area, label);
			return area;
	}
	
	/** This method is responsible for the dynamic addition of a JTextArea's name and reference. Keeping a reference to the JTextArea to
	 ** the form is useful to be able to perform functions such as "clear()" and emptyCheck().
	 ** Standard walking pointers method is used to add to the end of the linked list "textAreasList".
	 ** @param JTextArea 	the JTextArea reference being reserved to the node
     ** @param String 		the name value for the area's information being added to the node
     **/
	
	private void addAreaNode(JTextArea area, String name){
		if(textAreasList == null){
			textAreasList = new TextAreasNode(area, name, null);
		} else {
			TextAreasNode p = textAreasList;
			TextAreasNode q = null;
			while(p != null){
				q = p;
				p = p.next;
			}
			q.next = new TextAreasNode(area, name, null);
			textAreasList.size++;
		}
	}
	
	/** This method is responsible for the dynamic addition of a JTextField's String and Type information. Reserving the name tied to a Type is important
	 ** for performing specific operations such matching the ActionListener's getSource() method. 
	 ** Standard walking pointers method is used to add to the end of the linked list "fieldsList".
	 ** @param JTextField 	the JTextField reference being reserved to the node
     ** @param String 		the name value for the field's information being added to the node
     ** @param FieldType	 	the type value for the field's information being added to the node
     **/
	
	private void addFieldNode(JTextField field, String name, FieldType kind){
		if(fieldsList == null){
			fieldsList = new Node(field, kind, name, null);
		} else {
			Node p = fieldsList;
			Node q = null;
			while(p != null){
				q = p;
				p = p.next;
			}
			q.next = new Node(field, kind, name, null);
			fieldsList.size++;
		}
	}
	
	/** This method is responsible for the dynamic addition of a JTextField's String and Type information. Reserving the name tied to a Type is important
	 ** for performing specific operations such matching the ActionListener's getSource() method. 
	 ** Standard walking pointers method is used to add to the end of the linked list "fieldsList".
	 ** @return JTextField	the target field
     ** @param String 	the name of the target field
     **/
	
	private JTextField getField(String target){
		Node p = fieldsList;
		while((p != null) && (p.name.compareTo(target) != 0)){
			p = p.next;
		}
		if(p == null){
			throw new NoSuchElementException();
		} else {
			return p.field;
		}
	}
	
	/** This method is used when the information within the fields has been checked and the types are correct (the types are correct since formatCheck() accepted the values). 
	 ** It returns the formatted type using a corresponding type format object. Incorrect data typed within the field will set the foreground to be RED and will set goodInput to
	 ** be false: therefore, the form will not be accepted.
     ** @param String 			the name value for the field to be formatted and returned
     ** @param FieldType		the type value for the field to be formatted and returned
     ** @see formatCheck
     **/
	
	private boolean formatCheck(String name, FieldType kind){
		DateFormat dateChecker = DateFormat.getDateInstance(DateFormat.SHORT);
		DateFormat timeChecker = DateFormat.getTimeInstance(DateFormat.SHORT);
		NumberFormat currencyChecker = NumberFormat.getNumberInstance();
		NumberFormat integerFormat = NumberFormat.getIntegerInstance();
		NumberFormat percentageFormat = NumberFormat.getPercentInstance();
		DecimalFormat decimalFormat = new DecimalFormat("###,###,###,###.00");
		Boolean goodFormat = true;
		if(kind == FieldType.DATE){
			try {
				dateChecker.parse(getField(name).getText());
				getField(name).setForeground(Color.BLACK);
			} catch (ParseException e) {
				getField(name).setForeground(Color.RED);
				goodFormat = false;
			}
		} 
		
		if(kind == FieldType.TIME){
			try {
				timeChecker.parse(getField(name).getText());
				getField(name).setForeground(Color.BLACK);
			} catch (ParseException e) {
				getField(name).setForeground(Color.RED);
				goodFormat = false;
			}
		} 
		
		if(kind == FieldType.CURRENCY){
			try {
				currencyChecker.parse(getField(name).getText());
				getField(name).setForeground(Color.BLACK);
			} catch (ParseException e) {
				getField(name).setForeground(Color.RED);
				goodFormat = false;
			}
		} 
		
		if(kind == FieldType.DECIMAL){
			try {
				decimalFormat.parse(getField(name).getText());
				getField(name).setForeground(Color.BLACK);
			} catch (ParseException e) {
				getField(name).setForeground(Color.RED);
				goodFormat = false;
			}
		} 
		
		if(kind == FieldType.INTEGER){
			try {
				integerFormat.parse(getField(name).getText());
				getField(name).setForeground(Color.BLACK);
			} catch (ParseException e) {
				getField(name).setForeground(Color.RED);
				goodFormat = false;
			}
		} 
		
		if(kind == FieldType.PERCENT){
			try {
				percentageFormat.parse(getField(name).getText());
				getField(name).setForeground(Color.BLACK);
			} catch (ParseException e) {
				getField(name).setForeground(Color.RED);
				goodFormat = false;
			}
		} 
		
		//The boolean acceptance that is returned from this method is a comparison of if all fields are non empty and the fields have good input.
		if((goodFormat) && (!emptyCheck())){
			System.out.println("FORMAT/EMPTY CHECK - The input is now good");
			return true;
		} else {
			System.out.println("FORMAT/EMPTY CHECK - The input is bad");
			return false;
		}
	}
	
	/** This method is used to confirm if any of the fields that have been created for the CheckedForm are empty. If any of the fields
	 ** are empty, then the method will return true.
     ** @return Boolean		boolean value of if ANY of the fields are empty
     **/
	
	public Boolean emptyCheck(){
		Node p = fieldsList;
		while(p != null){
			if(p.field.getText().compareTo("") == 0){
				return true;
			}
			p = p.next;
		}
		return false;
	}
	
	/** The focusGained method is an implemented method from the FocusListener interface. When focus is obtained on a component, 
	 ** a focus event is triggered.
     ** @param FocusEvent	the event triggered
     **/

		@Override
		public void focusGained(FocusEvent e) {
			//DO Nothing
		}
	
   /** The focusLost method is an implemented method from the FocusListener interface. When focus is lost on a component, 
	** a focus event is triggered.
	** @param FocusEvent	the event triggered
	**/

		@Override
		public void focusLost(FocusEvent e) {
			Node p = fieldsList;

			while(e.getSource() != p.field){
				p = p.next;
			}
			goodInput = formatCheck(p.name, p.kind);
		}
	
		/** The actionPerformed method is an implemented method from the ActionListener interface. When a field has been accepted (TAB or ENTER
		 ** pressed), the actionPerformed method triggers an ActionEvent. This method is specifically used for the checking of Format.
	     ** @param ActionEvent	the event triggered
	     **/	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Node p = fieldsList;

		while(e.getSource() != p.field){
			p = p.next;
		}
			goodInput = formatCheck(p.name, p.kind);	
	}
	
    /** The ConfirmOrQuit class is a class to abstract the life cycle of the CheckedForm. It is specifically used for handing the
     ** OK and QUIT buttons. The actionPerformed method is an implemented method from the ActionListener interface. This implementation is used for
     ** the acceptance of OK and QUIT buttons. The design choice to separate form actions checking and button actions checking adds to Readability.
     ** @param ActionEvent	the event triggered
     **/	
	
	class ConfirmOrQuit implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			//Thread-4 is HelpDesk thread
			//Thread-5 is TechDesk thread
			
			System.out.println(monitoredThread.getName());
			if(e.getSource().equals(ok)){
				if(goodInput){
					accept = true;
					goodInput = false;
				}
			} else if(e.getSource().equals(quit)){
				System.out.println("QUIT");
				shouldQuit = true;
				if(monitoredThread.getName().compareTo("Thread-5") == 0){
				monitoredThread.interrupt();
				System.out.println("TechDesk wants to close");
			}
			}
		}	
	}
	
	/** The clear method is used to reset all data in the forms for subsequent submissions after an OK action has been accepted. The 
	 ** clear method takes advantage of the fieldsList dynamic list holding all JTextFields and the textAreasList dynamic list holding all
	 ** JTextAreas.
     **/

	public void clear() {
		Node p = fieldsList;
		while(p != null){
			p.field.setText("");
			p = p.next;
		}
		TextAreasNode q = textAreasList;
		while(q != null){
			q.area.setText("");
			q = q.next;
		}
	}
	
	/** The resetForm method is used after the approval of an OK action event; it resets the two main Boolean values responsible for the 
	 ** advances in the program's "paused" state.
     **/
	
	public void resetForm() {
		goodInput = false;
		accept = false;
	}
	
	/** This accessor method returns the Boolean value of the accept variable.
	 ** @return Boolean 	the Boolean value of the accept variable
     **/

	public boolean accept(){
		return accept;
	}
	
	/** This accessor method returns the Boolean value of the shouldQuit variable.
	 ** @return Boolean 	the Boolean value of the shouldQuit variable
     **/
	
	public boolean shouldQuit(){
		return shouldQuit;
	}
        
        /** This mutator method sets the truth value of GoodInput which can be used to override the acceptance of text within the forms.
         *  This method could be dangerous if not used with caution. In the program, this method is only used for the TechDesk where userInput
         *  is not a prerequisite for continuation.
	 ** @return Boolean 	the Boolean value of the goodInput variable
         **/
	
	public void setGoodInput(boolean truth){
		goodInput = truth;
	}
        
        /** This method is added functionality to support the monitoring of a Thread. 
	 ** @return Thread      the Thread to be monitored
         **/
	
	public void setMonitoredThread(Thread t){
		monitoredThread = t;
	}

}
