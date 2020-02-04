/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ft;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import static oracle.jrockit.jfr.events.Bits.intValue;

public class ChatServerThread extends Thread
{  private Socket          socket   = null;
   private ChatServer      server   = null;
   boolean i = false;
   private int             ID       = -1;
   private DataInputStream streamIn =  null;
   private DataOutputStream streamOut =  null;
   String msg2="";
   public String Ip;
   //int up
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
       for(;;)
      { 
          try{
       
       String input = streamIn.readUTF();
                    // NOTE: if you want to check server can read input, uncomment next line and check server file console.
                    System.out.println(input);
                        if(input.startsWith("[")){
                           // System.out.println("Wewen amy mal");
                    String filename=null;
                    String filetype;
                    String values;
                    String filesize;
                    String filelast;
                    String port;
                    String[] tok = input.split("\\[");
                    
                    tok[1] = tok[1].replace("=", ",");
                    tok[1] = tok[1].replace(" ", "");
                    tok[1] = tok[1].replace("]","");
                    tok[1] = tok[1] + ",zhyndygoi";
                    int j=0;
                    String[] tokens = tok[1].split(",");
                    LinkedList<LinkedList<String>> hye = new LinkedList<LinkedList<String>>();
                    while(!tokens[j].equals("zhyndygoi")){
                           filename=tokens[j];
                          // System.out.println(filename);
                           j++;
                           filetype=tokens[j];
                          // System.out.println(filetype);
                           j++;
                           filesize=tokens[j];
                          // System.out.println(filesize);
                           j++;
                           filelast=tokens[j];
                         //  System.out.println(filelast);
                           j++;
                           Ip=tokens[j];
                          // System.out.println(Ip);
                           j++;
                           port=tokens[j];
                          // System.out.println(port);
                           j++;
                    LinkedList<String> list = new LinkedList<String>();
                    LinkedList<LinkedList<String>> hell = new LinkedList<LinkedList<String>>();
                    LinkedList<LinkedList<String>> hell2 = new LinkedList<LinkedList<String>>();
                    Stack<LinkedList<String>> st1 = new Stack<LinkedList<String>>();
                    
                    if(!server.peers.containsKey(Ip)){
                    server.peers.put(Ip, 0);
                   
                    }
                  
                    list.add(filetype);
                    list.add(filesize);
                    list.add(filelast);
                    list.add(Ip);
                    list.add(port);
                   // System.out.println(list.toString());
                   
                             
                            
                    if(!server.Records.containsKey(filename)){
                        hell.add(list);
                   server.Records.put(filename,hell);
                   server.linko.put(list, filename);
                   if(!hye.contains(list)){
                   hye.push(list);
                   System.out.println(hye.toString());
                   server.ipoc.put(Ip, hye);
                   } }else{
                      // LinkedList<LinkedList<String>> list2 = new LinkedList<LinkedList<String>>();
                       ArrayList<LinkedList<String>> list2 = new ArrayList<LinkedList<String>>();
                       if(!hye.contains(list)){
                   hye.push(list);
                   System.out.println(hye.toString());
                   server.ipoc.put(Ip, hye);
                   }
                      hell=server.Records.get(filename);
                       server.Records.remove(filename);
                       hell.add(list);
                       server.Records.put(filename,hell);
                       server.linko.put(list,filename);
                    }
                    server.linko.put(list,filename);
                  //  list.clear();
                    }
                    i=true;
                   // System.out.println(server.Records.toString()); 
                    //System.out.println(server.ipoc.toString());
                    System.out.println(server.peers.toString());
                }
                        if(input.equalsIgnoreCase("hello")){ 
                        streamOut.writeUTF("Hi");
                         streamOut.flush();
                            
                        }
                        
                        if(input.equals("Bye") && i==true){
                            
                            LinkedList<LinkedList<String>> k = server.ipoc.get(Ip);
                            int size=k.size();
                            System.out.println(size);
                            while(size!=0){
                                LinkedList<String> temp = k.pop();
                              String koy = server.linko.get(temp);
                              LinkedList<LinkedList<String>> wws=server.Records.get(koy);
                              wws.remove(temp);
                              server.Records.remove(koy);
                              server.Records.put(koy, wws);
                              size--;
                            }
                            System.out.println(server.Records.toString());
                        }
                        if(input.indexOf("SEARCH")!=-1 && i==true){
                            String lastWord = input.substring(input.lastIndexOf(" ")+1);
                            System.out.println(lastWord);
                            
                            LinkedList<LinkedList<String>> res = server.Records.get(lastWord);
                            if(res!=null && !"[]".equals(res.toString())){
                            streamOut.writeUTF(res.toString());
                            streamOut.flush();
                            }else{
                            streamOut.writeUTF("Not Found");
                            streamOut.flush();
                        }
                        }
                            
                        
                        if(input.indexOf("SCORE of")!=-1 && i==true){
                           String[] tokens = input.split(" ");
                           System.out.println("token1:"+tokens[0]);
                                   System.out.println(" token2" + tokens[1]);
                                 //  System.out.println(" token3" + tokens[2]);
                           String ip = tokens[2];
                           int up = Integer.parseInt(tokens[3]);
                           if(server.NumUp.containsKey(ip)){
                               int uploads = server.NumUp.get(ip);
                           uploads=uploads+up;
                           server.NumUp.put(ip, uploads);
                           }else{
                               server.NumUp.put(ip, up);
                           }
                           if(server.NumReq.containsKey(ip)){
                           int requests = server.NumReq.get(ip);
                           requests++;
                           server.NumReq.put(ip, requests);
                           }else{
                               server.NumReq.put(ip,1);
                           }
                           int score = intValue(server.NumUp.get(ip)*100/server.NumReq.get(ip));
                           server.peers.put(ip, score);
                           System.out.println(server.peers.toString());
                        }
                       
                            
                            
                 
   }catch(IOException e){
       
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
  
}