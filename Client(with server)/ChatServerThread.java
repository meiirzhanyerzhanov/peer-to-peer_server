/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package check;

import java.net.*;
import java.io.*;
import java.util.Random;

public class ChatServerThread extends Thread
{  private Socket          socket   = null;
   private ChatServer      server   = null;
   ChatClient      client   = null;
   private int             ID       = -1;
   private DataInputStream streamIn =  null;
   private DataOutputStream streamOut =  null;
   String msg2="";
  
   String username;
   
   
   
   public ChatServerThread(ChatServer _server, Socket _socket)
   {  server = _server;  socket = _socket;  ID = socket.getPort();
   }
   public void send(String msg)
   {   try
       {  streamOut.writeUTF(msg);
          streamOut.flush();
       }
       catch(IOException ioe)
       {  System.out.println(ID + " ERROR sending: " + ioe.getMessage());
          server.remove(ID);
          stop();
       }
   }
   public int getID()
   {  return ID;
   }
@Override
   public void run()
   {  
   System.out.println("Server Thread " + ID + " running.");
 /*  int check = 0, check2=0;
  while(check2==0 && check==0){
   try{
      
       String msgclient = streamIn.readUTF();
       StringTokenizer msg = new StringTokenizer(msgclient, "<");
       int count = msg.countTokens();
       if(count < 2){
        streamOut.writeUTF("Invalid. Please first authorize");
       streamOut.flush();
       }
       else{
       msg2 = msg.nextToken();
       
       if(!msg2.startsWith("server hello")){
           check2 =0;
       }
       else{
           check2=1;
       }
       
       msg2 = msg.nextToken();
       username="<"+msg2;
       if(!server.groups.checkunique(username)){
           
           check=1;
       }
       else{
           check=0;
       }
       if(check2==1 && check==1){
       
       server.groups.saveusernames(username);
       streamOut.writeUTF("hi " + username);
       streamOut.flush();
       }
       else{
            check=0;
       check2=0;
        streamOut.writeUTF("Invalid command or username already taken");
       streamOut.flush();
       
       }
       }
       
   }
   catch(IOException e){
      
       
   }
  }
  */
       for(;;)
      { 
          try{
       
       String msgclient = streamIn.readUTF();
       String[] msg = msgclient.split(" ");
       if(msgclient.startsWith("DOWNLOAD:")){
           Random rand = new Random();
           int  n = rand.nextInt(100) + 1;
           if(n<50){
               
           
           String[] msgfrom = msgclient.split(" ");
           String filen=msgfrom[1];
           filen+="."+msgfrom[2];
           filen=filen.replaceAll(",", "");
           System.out.println(msgclient);
           System.out.println(filen);
           
           try{
String str="";  
  

System.out.println("Sending File: "+filen);
streamOut.writeUTF(filen);  
streamOut.flush();  
String dir = System.getProperty("user.dir");
File f=new File(dir+"\\Myfiles\\"+filen);
System.out.println("HeERERERER:     "+f.getAbsolutePath());
FileInputStream fin=new FileInputStream(f);
long sz=(int) f.length();

byte b[]=new byte [1024];

int read;

streamOut.writeUTF(Long.toString(sz)); 
streamOut.flush(); 

System.out.println ("Size: "+sz);
System.out.println ("Buf size: "+socket.getReceiveBufferSize());

while((read = fin.read(b)) != -1){
    streamOut.write(b, 0, read); 
    streamOut.flush(); 
}
fin.close();

System.out.println("..ok"); 
 
 
 
System.out.println("Send Complete");
 
}
catch(Exception e)
{
	e.printStackTrace();
	System.out.println("An error occured");
}
       
       }
           else{
               streamOut.writeUTF("NO");   
           streamOut.flush();
           }
       }
    
      else{
          streamOut.writeUTF(" ");   
           streamOut.flush();
      }
       
       
   }
   catch(IOException e){
       
   }
      }
   }
   public void open() throws IOException
   {  streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
   streamOut = new DataOutputStream(socket.getOutputStream());
   }
   public void close() throws IOException
   {  if (socket != null)    socket.close();
      if (streamIn != null)  streamIn.close();
   }
  /* public String begin() {
       try{
       String msgclient = streamIn.readUTF();
       StringTokenizer msg = new StringTokenizer(msgclient, "<");
       msg2 = msg.nextToken();
       msg2 = msg.nextToken();
       streamOut = new DataOutputStream(socket.getOutputStream());
       streamOut.writeUTF("Hello:" + msg2);
       streamOut.flush();
       
       
   }
   catch(IOException e){
       
   }
       return msg2;
   }*/
}