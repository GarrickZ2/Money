package finalproject.server;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import finalproject.db.ClientDB;
import finalproject.db.DBInterface;
import finalproject.entities.Person;

/**
 * @author zzx
 */
public class Server extends JFrame implements Runnable {

	public static final int DEFAULT_PORT = 8001;
	private static final int FRAME_WIDTH = 600;
	private static final int FRAME_HEIGHT = 800;
	final int AREA_ROWS = 10;
	final int AREA_COLUMNS = 40;
	private Socket socket;
	private ObjectInputStream in;
	private PrintWriter out;
	private int port;

	ClientDB clientDB;

	//GUI Component
	JPanel up;
	JPanel down;
	JPanel main;
	JLabel db;
	JLabel dbName;
	JButton query;
	JTextArea area;

	public Server() throws IOException, SQLException {
		this(DEFAULT_PORT, "server.db");
	}
	
	public Server(String dbFile) throws IOException, SQLException {
		this(DEFAULT_PORT, dbFile);
	}

	public Server(int port, String dbFile) throws IOException, SQLException {
		clientDB = new ClientDB(dbFile);
		//set menu
		this.port = port;
		JMenuBar br = new JMenuBar();
		JMenu menu = createFileMenu();
		br.add(menu);
		this.setJMenuBar(br);
		//up zone
		up = new JPanel(new GridLayout(2, 1));
		JPanel panel1 = new JPanel(new FlowLayout());
		db = new JLabel("DB:");
		dbName = new JLabel(dbFile);
		panel1.add(db);
		panel1.add(dbName);
		up.add(panel1);

		JPanel panel2 = new JPanel(new FlowLayout());
		query = new JButton("Query DB");
		query.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String original = area.getText();
				ArrayList<Person> persons = clientDB.selectAll();
				String head = "first\tlast\tage\tcity\tsend\tid\n";
				String spe = "-----\t-----\t-----\t-----\t-----\t-----\n";
				String content = "";
				for(Person each:persons){
					content = content + each.getFirstName() + "\t" + each.getLastName() + "\t" + each.getAge() + "\t"
							+ each.getCity() + "\t" + each.getSent() + "\t" + each.getId() + "\n";
				}
				area.setText(original + head + spe + content);
			}
		});
		panel2.add(query);
		up.add(panel2);

		//down zone
		down = new JPanel(new BorderLayout());
		area = new JTextArea("");
		area.setEditable(false);
		down.add(area);

		main = new JPanel(new BorderLayout());
		main.add(up, BorderLayout.NORTH);
		main.add(down, BorderLayout.CENTER);

		this.add(main);
		this.setTitle("Server");
		this.setSize(500, 800);
		this.setSize(Server.FRAME_WIDTH, Server.FRAME_HEIGHT);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Thread t = new Thread(this);
		t.start();
	}

	public JMenu createFileMenu()
	{
		JMenu menu = new JMenu("File");
		menu.add(createFileExitItem());
		return menu;
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

	public static void main(String[] args) {

		Server sv;
		try {
			sv = new Server("server.db");
			sv.setVisible(true);
		} catch (IOException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			ServerSocket s = new ServerSocket(port);
			while (true) {
				String con = area.getText();
				area.setText(con + "\nListening on port " + port + "\n");
				socket = s.accept();
				try {
					con = area.getText();
					area.setText(con + "Connecting Successfully\n");
					in =new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
					out = new PrintWriter(new BufferedWriter(
							new OutputStreamWriter(socket.getOutputStream())
					), true);
					while (true) {
						Person person = (Person)in.readObject();
						out.println("Success");
						clientDB.insertPerson(person);
					}
				}catch (Exception e){
					con = area.getText();
					area.setText(con + "\nClosing Connection" + "\n");
					socket.close();
				}
			}
		}catch (IOException ignored){}
	}
}
