package check;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File; 
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class ExampleGUI extends JFrame implements ActionListener{

   
    private JButton search;  //Buttons
    private JButton dload;
    private JButton close;  
     private static final String host = "10.101.18.237";
    private static final int portNumber = 4444;
    private DataOutputStream streamOut = null;
   private DataInputStream streamIn = null;
    private JList jl;   // List that will show found files
    private JLabel label; //Label "File Name
    private JTextField tf,tf2; // Two textfields: one is for typing a file name, the other is just to show the selected file
    DefaultListModel listModel; // Used to select items in the list of found files
    ChatClient client = new ChatClient(host, portNumber);
   ChatClientThread clientthread = null;
        
        
   String ip[] = null;
            String port[]=null;
            String type[] = null;
            String size[] = null;
            String fileName="";
    String str[]={"Info1","Info2","Info3","Info4","Info5"}; // Files information
    
   /*  private void sendtoserver(String msg) {
        try
       { 
            streamOut = new DataOutputStream(client.getsocket().getOutputStream());
            streamOut.writeUTF(msg);
            streamOut.flush();
       }
       catch(IOException ioe)
       {  System.out.println(" ERROR sending: " + ioe.getMessage());
  
       }
        
    }*/
    public ExampleGUI(){
        super("Example GUI");
        setLayout(null);
        setSize(500,600);
        
        label=new JLabel("File name:");
        label.setBounds(50,50, 80,20);
        add(label);
        
        tf=new JTextField();
        tf.setBounds(130,50, 220,20);
        add(tf);
        
        search=new JButton("Search");
        search.setBounds(360,50,80,20);
        search.addActionListener(this);
        add(search);
     
        listModel = new DefaultListModel();
        jl=new JList(listModel);
        
        JScrollPane listScroller = new JScrollPane(jl);
        listScroller.setBounds(50, 80,300,300);
        
        add(listScroller);
        
        dload=new JButton("Download");
        dload.setBounds(200,400,130,20);
        dload.addActionListener(this);
        add(dload);
     
        tf2=new JTextField();
        tf2.setBounds(200,430,130,20);
        add(tf2);
        
        close=new JButton("Close");
        close.setBounds(360,470,80,20);
        close.addActionListener(this);
        add(close);
        
        setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e){
        
            
        if(e.getSource()==search){ //If search button is pressed show 25 randomly generated file info in text area 
            listModel.removeAllElements();
            fileName=tf.getText();
            client.sendtoserver("SEARCH: " + fileName);
            Random r=new Random();
           
            String msg = null;
            try {
                msg = client.client.getin().readUTF();
            } catch (IOException ex) {
                Logger.getLogger(ExampleGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(msg.startsWith("Not Found")){
                listModel.insertElementAt(msg, 0);
            }
            msg=msg.replaceAll("\\[", "");
            msg=msg.replaceAll("\\]", "");
            String[] msg2=msg.split(",");
            
             ip= new String[msg2.length/5+1];
             port= new String[msg2.length/5+1];
              type= new String[msg2.length/5+1];
             size= new String[msg2.length/5+1];
            //listModel.insertElementAt(msg2.length, 0);
            int i,l;
            for(i=0; i<(msg2.length/5);i++){
                msg=" ";
                for(l=0;l<5;l++){
                    msg+=msg2[l +i*5];
                }
            type[i]=msg2[0+i*5].replaceAll(" ", "");
            size[i]=msg2[1+i*5].replaceAll(" ", "");
            ip[i]=msg2[3+i*5].replaceAll(" ", "");
            port[i]=msg2[4+i*5].replaceAll(" ", "");
           System.out.println("ip : "+ip[i] + "  port:  " + port[i]+ "\n");
            listModel.insertElementAt(fileName+msg, i);
            }
            
            
            /*for (int i = 0; i < 25; i++) {
                listModel.insertElementAt(fileName+" "+str[r.nextInt(str.length)],i);
            }*/
        } 
        else if(e.getSource()==dload){   //If download button is pressed get the selected value from the list and show it in text field
          //  String ipv4 = ip[jl.getSelectedIndex()];
           // int portnum = Integer.parseInt(port[jl.getSelectedIndex()]);
           ChatClient clientB = new ChatClient(ip[jl.getSelectedIndex()], Integer.parseInt(port[jl.getSelectedIndex()]));
           
           clientB.sendtoserver("DOWNLOAD: "+fileName+", "+type[jl.getSelectedIndex()]+", "+size[jl.getSelectedIndex()]);
           
           try{
                
            
            String msg;
            String filename;
           
	
        try {
                sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(ExampleGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        msg=clientB.msgfromserver;
        
        if(msg.startsWith("NO")){
            client.sendtoserver("SCORE of "+ip[jl.getSelectedIndex()]+" 0");
           
            tf2.setText("NO");
        }
        else{
            client.sendtoserver("SCORE of "+ip[jl.getSelectedIndex()]+" 1");
        System.out.println("Receving file: "+fileName+type[jl.getSelectedIndex()]);
	filename="client"+fileName+"."+type[jl.getSelectedIndex()]; 
	System.out.println("Saving as file: "+filename);
        long sz=Long.parseLong(clientB.client.getin().readUTF());
System.out.println ("File Size: "+(sz/(1024*1024))+" MB");
byte b[]=new byte [1024];
System.out.println("Receving file..");


FileOutputStream fos=new FileOutputStream(new File(filename),true);

long bytesRead;
do
{
bytesRead = clientB.client.getin().read(b, 0, b.length);
fos.write(b,0,b.length);
}while(!(bytesRead<1024));
System.out.println("Completed");
tf2.setText(ip[jl.getSelectedIndex()]+" downloaded");
           }} 
           catch (FileNotFoundException ex) {
                Logger.getLogger(ExampleGUI.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ExampleGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
          
            
            
        }
        else if(e.getSource()==close){ //If close button is pressed exit
            client.sendtoserver("Bye");
            System.exit(0);
        }
      
    }
    public String gethost(){
        return host;
    }
    public int getport(){
        return portNumber;
    }
    
    public static void main(String[] args) {
        
  
       //client.getfiles();
        ExampleGUI ex=new ExampleGUI();
        ex.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close the window if x button is pressed
      //  ChatClient client = new ChatClient(host, portNumber);
       // client.sendtoserver("Hello");
        


        
    }
}