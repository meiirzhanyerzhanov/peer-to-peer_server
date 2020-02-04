/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ft;

import java.net.*;
import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class ChatServer implements Runnable
{  private ServerSocket     server = null;
   private Thread           thread = null;
   private ChatServerThread clients[] = new ChatServerThread[50];
   public Map<String, LinkedList<LinkedList<String>>> Records;
   public Map<String,Integer> peers;
   public Map<LinkedList<String>, String> linko;
   public Map<String, LinkedList<LinkedList<String>>> ipoc;
   public Map<String,Integer> NumUp;
   public Map<String,Integer> NumReq;
   
  
   private int clientCount = 0;
   public ChatServer(int port)
   {  try
      {  
          Records=new HashMap<String,LinkedList<LinkedList<String>>>();
          peers =new HashMap<String,Integer>();
          linko = new HashMap<LinkedList<String>, String>();
          ipoc = new HashMap<String, LinkedList<LinkedList<String>>>();
          NumUp=new HashMap<String,Integer>();
          NumReq= new HashMap<String,Integer>();
          
          System.out.println("Binding to port " + port + ", please wait  ...");
         server = new ServerSocket(port);  
         System.out.println("Server started: " + server);
        
         start();
         
      }
      catch(IOException ioe)
      {  System.out.println(ioe); }
   }
   
@Override
   public void run()
   {  while (thread != null)
      {  try
         {  System.out.println("Waiting for a client ..."); 
            addThread(server.accept());
         }
         catch(IOException ie)
         {  System.out.println("Acceptance Error: " + ie); }
      }
   }

   
   

   public void addThread(Socket socket)
   {  System.out.println("Client accepted: " + socket);
         clients[clientCount] = new ChatServerThread(this, socket);
         try
         {  clients[clientCount].open(); 
            clients[clientCount].start();  
            clientCount++; }
         catch(IOException ioe)
         {  System.out.println("Error opening thread: " + ioe); } }
   public void start()
   {  if (thread == null)
      {  thread = new Thread(this); 
         thread.start();
      }
   }
   public void stop()
   {  if (thread != null)
      {  thread.stop(); 
         thread = null;
      }
   }
   
   public static void main(String args[]){ 
    ChatServer server = null;
   
         server = new ChatServer(4444);
   }
}