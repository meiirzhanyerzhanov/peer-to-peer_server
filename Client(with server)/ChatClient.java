package check;

import java.net.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatClient implements Runnable
{  private Socket socket              = null;
private Thread thread              = null;
   private DataInputStream  console   = null;
   private DataOutputStream streamOut = null;
   DataInputStream streamIn = null;
   public ChatClientThread client    = null;
   public String msgfromserver="";
   
  
        LinkedList<String> list = new LinkedList<String>();
        
   public ChatClient(String serverName, int serverPort)
   {  System.out.println("Establishing connection. Please wait ...");
      try
      {  socket = new Socket(serverName, serverPort);
         System.out.println("Connected: " + socket);
         start();
      }
      catch(UnknownHostException uhe)
      {  System.out.println("Host unknown: " + uhe.getMessage());
      }
      catch(IOException ioe)
      {  System.out.println("Unexpected exception: " + ioe.getMessage());
      }
     // this.run();
      
      /*String line = "";
      while (!line.equals("server exit"))
      {  try
         {  line = console.readLine();
            streamOut.writeUTF(line);
            streamOut.flush();
            String msgfromserver = streamIn.readUTF();
            System.out.println(msgfromserver);
         }
         catch(IOException ioe)
         {  System.out.println("Sending error: " + ioe.getMessage());
         }
      }*/
     
      
   }
   public String test(){
       String ip = null;
       String ip2=null;
    try {
       Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface iface = interfaces.nextElement();
            // filters out 127.0.0.1 and inactive interfaces
            if (iface.isLoopback() || !iface.isUp())
                continue;

            Enumeration<InetAddress> addresses = iface.getInetAddresses();
            while(addresses.hasMoreElements()) {
                InetAddress addr = addresses.nextElement();
                
                ip = addr.getHostAddress();
                if(!ip.startsWith("f")){
                    ip2=ip;
                }
                if(iface.getDisplayName().startsWith("802.11n Wireless LAN")){
                   // return ip;
                }
                
                
                System.out.println(iface.getDisplayName() + " " + ip);
            }
        
        }
        return ip2;
    } catch (SocketException e) {
        throw new RuntimeException(e);
    }
  
  
   }
   
  
   public LinkedList getfiles(){
        String dir = System.getProperty("user.dir");
    File folder = new File(dir+"\\Myfiles");
        File[] listOfFiles = folder.listFiles();
       System.out.println(dir+"\\Myfiles");
        for (File file : listOfFiles) {
    if (file.isFile()) {
        InetAddress i = null;
        String ii=file.getName();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        StringBuilder sb = new StringBuilder();
        
        String[] str = ii.split("\\.");
        
        String str2 = "";
        str2+=str[0]+",";
        str2+=str[1]+ ",";
        str2+=file.length() + ",";
        str2+=sdf.format(file.lastModified())+ ",";
     
        try {
            i=InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        
        sb.append(test());
        sb.append(",");
        sb.append(2222);
        
        str2+=sb.toString();
        //System.out.println("sgsdfsdfsdfsdf" +ii + "         asdasd");
        list.add(str2);
    }
}
        return list;
    }
   
  
   
@Override
   public void run()
   {
      try
         {  streamOut.writeUTF("Hello");
            streamOut.flush();
            
         }
         catch(IOException ioe)
         {  System.out.println("Sending error: " + ioe.getMessage());
            stop();
         }
       
       while (thread != null)
      {  try
         {  streamOut.writeUTF(console.readLine());
            streamOut.flush();
         }
         catch(IOException ioe)
         {  System.out.println("Sending error: " + ioe.getMessage());
            stop();
         }
      }
   }
   public void sendtoserver(String msg){
       try
         {  streamOut.writeUTF(msg);
            streamOut.flush();
         }
         catch(IOException ioe)
         {  System.out.println("Sending error: " + ioe.getMessage());
            stop();
         }
   }
   
   public void sendtoserverlist(LinkedList msg){
       try
         {  streamOut.writeUTF(msg.toString());
            streamOut.flush();
         }
         catch(IOException ioe)
         {  System.out.println("Sending error: " + ioe.getMessage());
            stop();
         }
   }
    public void handle(String msg)
   {  if (msg.equals(".bye"))
      {  System.out.println("Good bye. Press RETURN to exit ...");
         stop();
      }
      else
       msgfromserver =msg;
         System.out.println(msg);
   }
    Socket getsocket(){
        return socket;
        
    }
   
   public void start() throws IOException
   {  console   = new DataInputStream(System.in);
      streamOut = new DataOutputStream(socket.getOutputStream());
      if (thread == null)
      {  client = new ChatClientThread(this, socket);
         thread = new Thread(this);                   
         thread.start();
      }
   }
   public void stop()
   {  if (thread != null)
      {  thread.stop();  
         thread = null;
      }
      try
      {  if (console   != null)  console.close();
         if (streamOut != null)  streamOut.close();
         if (socket    != null)  socket.close();
      }
      catch(IOException ioe)
      {  System.out.println("Error closing ..."); }
      client.close();  
      client.stop();
   }
   public static void main(String args[])
   {  ChatClient client = null;
     
         client = new ChatClient("localhost", 2222);
   }
}

 