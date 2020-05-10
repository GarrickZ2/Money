package finalproject.client;

import java.awt.*;
import java.util.ArrayList;
import java.sql.*;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;

import finalproject.client.ClientInterface.ComboBoxItem;
import finalproject.db.ClientDB;
import finalproject.db.DBInterface;
import finalproject.entities.Person;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;


/**
 * @author zzx
 */
public class ClientInterface extends JFrame {

	private static final long serialVersionUID = 1L;

	public static final int DEFAULT_PORT = 8001;
	
	private static final int FRAME_WIDTH = 600;
	private static final int FRAME_HEIGHT = 400;
	final int AREA_ROWS = 10;
	final int AREA_COLUMNS = 40;

	ClientDB clientDB;
	JComboBox<ComboBoxItem> peopleSelect;
	JFileChooser jFileChooser;
	Socket socket;
	int port;


	//GUI Component
	JPanel main;
	JPanel up;
	JPanel down;
	JTextArea area;
	JLabel title1;
	JLabel dbName;
	JLabel title2;
	JLabel connName;
	JButton open;
	JButton close;
	JButton send;
	JButton query;

	public ClientInterface() {
		this(DEFAULT_PORT);
	}
	
	public ClientInterface(int port) {
		jFileChooser = new JFileChooser();
		this.port = port;
		JMenu menu = createFileMenu();
		JMenuBar br = new JMenuBar();
		br.add(menu);
		this.setJMenuBar(br);
		//up zone
		up = new JPanel(new GridLayout(5, 1));
//
		JPanel panel1 = new JPanel(new FlowLayout());
		title1 = new JLabel("Activate DB:");
		dbName = new JLabel("<None>");
		panel1.add(title1);
		panel1.add(dbName);
		up.add(panel1);

		JPanel panel2 = new JPanel(new FlowLayout());
		title2 = new JLabel("Activate Connection:");
		connName = new JLabel("<None>");
		panel2.add(title2);
		panel2.add(connName);
		up.add(panel2);

		JPanel jPanel5 = new JPanel(new FlowLayout());
		peopleSelect = new JComboBox<>();
		ComboBoxItem none = new ComboBoxItem(-1, "<Empty>");
		peopleSelect.addItem(none);
		jPanel5.add(peopleSelect);
		up.add(jPanel5);

		JPanel panel3 = new JPanel(new FlowLayout());
		open = new JButton("Open Connection");
		close = new JButton("Close Connection");
		panel3.add(open);
		panel3.add(close);
		up.add(panel3);

		JPanel panel4 = new JPanel(new FlowLayout());
		send = new JButton("Send Data");
		query = new JButton("Query DB Data");
		query.addActionListener(new QueryListener());
		panel4.add(send);
		panel4.add(query);
		up.add(panel4);

		// down zone
		down = new JPanel(new BorderLayout());
		area = new JTextArea();
		area.setEditable(false);
		down.add(area);

		main = new JPanel(new BorderLayout());
		main.add(up, BorderLayout.NORTH);
		main.add(down, BorderLayout.CENTER);

		this.add(main);
		this.setTitle("Client");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(500, 800);
		this.setVisible(true);

	}
	
	class QueryListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			printInfo();
		}
	}

	private void printInfo(){
		area.setText("");
		ArrayList<Person> persons = clientDB.selectAll();
		String head = "first\tlast\tage\tcity\tsend\tid\n";
		String spe = "-----\t-----\t-----\t-----\t-----\t-----\n";
		String content = "";
		for(Person each:persons){
			content = content + each.getFirstName() + "\t" + each.getLastName() + "\t" + each.getAge() + "\t"
					+ each.getCity() + "\t" + each.getSent() + "\t" + each.getId() + "\n";
		}
		area.setText(head + spe + content);
	}
   public JMenu createFileMenu()
   {
      JMenu menu = new JMenu("File");
      menu.add(createFileOpenItem());
      menu.add(createFileExitItem());
      return menu;
   }
   
   
   private void fillComboBox() throws SQLException {
	   
	   ArrayList<ComboBoxItem> l = getNames();
	   for(ComboBoxItem item: l){
	   	peopleSelect.addItem(item);
	   }
   }

   private void clearCombBox(){
		peopleSelect.removeAllItems();
		ComboBoxItem none = new ComboBoxItem(-1, "<Empty>");
		peopleSelect.addItem(none);
   }

   private JMenuItem createFileExitItem() {
		JMenuItem item = new JMenuItem("Exit");
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		return item;
   }


	private JMenuItem createFileOpenItem() {
	   JMenuItem item = new JMenuItem("Open DB");

	   item.addActionListener(new OpenDBListener());
	   return item;
   }
   
	class SendButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

	        try {
				
	        	// responses are going to come over the input as text, and that's tricky,
	        	// which is why I've done that for you:
				BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				
				// now, get the person on the object dropdownbox we've selected
				ComboBoxItem personEntry = (ComboBoxItem)peopleSelect.getSelectedItem();
				
				// That's tricky which is why I have included the code. the personEntry
				// contains an ID and a name. You want to get a "Person" object out of that
				// which is stored in the database
				
				// Send the person object here over an output stream that you got from the socket.
				ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
				Person person = clientDB.selectByID(personEntry.getId()+"");
				os.writeObject(person);
				os.flush();

				String response = br.readLine();
				if (response.contains("Success")) {
					System.out.println("Success");
					// what do you do after we know that the server has successfully
					// received the data and written it to its own database?
					// you will have to write the code for that.
				} else {
					System.out.println("Failed");
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
//			} catch (SQLException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
	        
			
		}
		
	}
	
	private ArrayList<ComboBoxItem> getNames() throws SQLException {
		ArrayList<Person> peoples = clientDB.selectAll();
		ArrayList<ComboBoxItem> items = new ArrayList<>();
		for(Person each : peoples){
			if(each.getSent() == 0) {
				ComboBoxItem item = new ComboBoxItem(Integer.parseInt(each.getId()),
						each.getFirstName() + " " + each.getLastName());
				items.add(item);
			}
	   	}
	   return items;
   }
	
	// a JComboBox will take a bunch of objects and use the "toString()" method
	// of those objects to print out what's in there. 
	// So I have provided to you an object to put people's names and ids in
	// and the combo box will print out their names. 
	// now you will want to get the ComboBoxItem object that is selected in the combo box
	// and get the corresponding row in the People table and make a person object out of that.
	class ComboBoxItem {
		private int id;
		private String name;
		
		public ComboBoxItem(int id, String name) {
			this.id = id;
			this.name = name;
		}
		
		public int getId() {
			return this.id;
		}
		
		public String getName() {
			return this.name;
		}
		
		@Override
		public String toString() {
			return this.name;
		}
	}
	
	/* the "open db" menu item in the client should use this ActionListener */
	class OpenDBListener implements ActionListener
	  {
		 @Override
		 public void actionPerformed(ActionEvent event)
		 {
			int returnVal = jFileChooser.showOpenDialog(getParent());
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				System.out.println("You chose to open this file: " + jFileChooser.getSelectedFile().getAbsolutePath());
				String dbFileName = jFileChooser.getSelectedFile().getAbsolutePath();
				try {
					clientDB = new ClientDB(dbFileName);
					peopleSelect.removeAllItems();
					fillComboBox();
				} catch (Exception e ) {
					System.err.println("error connection to db: "+ e.getMessage());
					clearCombBox();
					e.printStackTrace();
				}
			}
		 }
	  }

	public static void main(String[] args) {
		ClientInterface ci = new ClientInterface();
	}
}
