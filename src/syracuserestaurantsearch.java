import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.sql.*;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
public class syracuserestaurantsearch {
	
	//Instance Variables
	public JFrame frame;
	public JLabel header;
	public JLabel inf;
	public JLabel zip;
	public JTextField zipcode;
	public JButton go;
	public Connection C=null;
	public Statement st=null;
	public Statement st1=null;
	public JTextArea result;
	public JScrollPane pane;
	
	//Default Constructor
	public syracuserestaurantsearch() throws IOException
	{
		GUI();
	}
	
	
	
	public static void main(String[] args) throws IOException
	{
		syracuserestaurantsearch s=new syracuserestaurantsearch();
	}
	
	
	
	public void GUI() throws IOException
	{
		//Setting the Jframe And backgroundImage
		
		
		frame=new JFrame("Syracuse Restaurant Search");
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);
	    ImageIcon img=new ImageIcon(ImageIO.read(new File("C:/Users/User/eclipseworkspace/Project/src/restaurant.jpeg")).getScaledInstance(1100,750, Image.SCALE_SMOOTH));
		frame.setContentPane(new JLabel(img));
		frame.setLayout(null);
		
		frame.addWindowListener(new WindowAdapter() {
	         public void windowClosing(WindowEvent windowEvent){
	            System.exit(0);
	         }        
	      });
		
		
		//Setting the Header
		
		
		header=new JLabel("SYRACUSE RESTAURANT LOCATOR",JLabel.CENTER);
		header.setFont(new Font("serif",Font.ITALIC,44));
		Color c=Color.decode("#B0C4DE");
		Color c1=Color.decode("#008B8B");
		Border border=BorderFactory.createLineBorder(c1, 5);
		header.setBorder(border);
		header.setForeground(c);
		header.setBounds(150, 20, 740, 70);
		frame.add(header);
		
		
		//Setting the information
		
		
		inf=new JLabel("Please Enter The ZipCode Where You Want To Find Restaurants");
		inf.setFont(new Font("serif",Font.ITALIC,32));
		Color c2=Color.decode("#B0C4DE");
		inf.setForeground(c2);
		inf.setBounds(100, 130, 850, 130);
		frame.add(inf);
		
		
		
		//zip Label
		
		
		zip=new JLabel("ZIPCODE");
		zip.setFont(new Font("serif",Font.ITALIC,40));
		Color c3=Color.decode("#8B0000");
		zip.setForeground(c3);
		zip.setBounds(420, 190, 550, 150);
		frame.add(zip);
		
		
		
		//zipcode textfield to take the input
		
		
		zipcode=new JTextField(5);
		zipcode.setFont(new Font("serif",Font.CENTER_BASELINE,22));
		zipcode.setBounds(420, 290, 80, 40);
		frame.add(zipcode);
		
		
		//GO Button
		
		
		go=new JButton("GO");
		go.setFont(new Font("serif",Font.BOLD,20));
		Color c4=Color.decode("#8B008B");
		go.setForeground(c4);
		go.setBounds(520, 289, 70, 43);
		
		
		//result textarea
		
		
		result=new JTextArea();
		result.setEditable(false);
	    pane=new JScrollPane(result);
	    pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	    pane.setBounds(320, 350, 380, 300);
			
		go.addActionListener(new ActionListener(){
			
			@SuppressWarnings("unchecked")
			public void actionPerformed(ActionEvent e)
			{
				
				
				//To clear the result textfield to load new Output
				
				result.setText("");
	            
				
				
				//Making connection to DBMS
				
				try
				{
					Class.forName("com.mysql.jdbc.Driver");
					System.out.println("Connecting to Database");
					C=DriverManager.getConnection("jdbc:mysql://localhost:3306/syracuse?user=root&password=ahmedkhan50&useUnicode=true&characterEncoding=UTF-8");
					System.out.println("Successful Connection");
				}
				
				catch(Exception e1)
				{
					e1.getMessage();
				}
				
				
				//checking if Appropriate Zipcode corresponding to Syracuse is Entered
				
				
				String s=zipcode.getText();
				char[] err=s.toCharArray();
				int len=err.length;
				ArrayList a=new ArrayList();
				
				String query1="SELECT DISTINCT zipcode from syracuserestaurants";
			    try {
					st1=C.createStatement();
					ResultSet res1=st1.executeQuery(query1);
					
					while(res1.next())
					{
						String zipcode1=res1.getString("zipcode");
						a.add(zipcode1);
					}
				    	
					
				} 
			    catch (SQLException e4) {
					
					e4.printStackTrace();
				}
				
			    
			    //Comparing the zipcode entered with list of all valid zipcodes in my database
			    
				Boolean contains=a.contains(s);
				
			    //Displaying message to user by JOptionPane if zipcode entered by the user is not appropriate type
				
				
				try
				{
					if(len<5||len>5||err[0]!='1'||err[1]!='3'||err[2]!='2'||!contains)
					{
						
						JOptionPane.showMessageDialog(frame,"<HTML><font color='red'>Please Enter A Valid ZipCode For City SYRACUSE</font></HTML>","Invalid Zipcode",JOptionPane.ERROR_MESSAGE);
							
					}
					
					
					//Searching the database to extract all restaurants for the appropriate zipcode entered By user
					
					
					else
					{
						
					String query="SELECT restaurantname,address,cuisines FROM syracuserestaurants WHERE zipcode="+s+" "+"ORDER BY restaurantname ASC";
				    st=C.createStatement();
					ResultSet res=st.executeQuery(query);
					while(res.next())
					{
						
						String restaurantname=res.getString("restaurantname");
						String address=res.getString("address");
						String cuisines=res.getString("cuisines");
						
						result.append(restaurantname+'\n'+"Address:"+address+'\n'+"Cuisines:"+cuisines+'\n'+'\n');
						result.setCaretPosition(0);
						result.setBackground(Color.cyan);
						result.setFont(new Font("snas-serif",Font.LAYOUT_LEFT_TO_RIGHT,15));
						Color c4=Color.decode("#A52A2A");
						result.setForeground(c4);
						
					}
					
				    }
				
				}
				catch(Exception e2)
				{
					e2.getMessage();
				}
				
				finally
				{
					try {
						if(st!=null)
						{
						st.close();
						}
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						if(C!=null)
						{
						C.close();
						}
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				frame.add(pane);//display the result when button is clicked after entering zipcode
			}
			
			
			
		});
		frame.add(go);
		frame.setVisible(true);
	}
										
	}


