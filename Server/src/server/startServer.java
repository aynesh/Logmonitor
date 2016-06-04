/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 *
 * @author Aynesh
 */
public class startServer extends Thread {
    
     static final int PORT = 6500;
     private ServerSocket serverSocket = null;
     private Socket socket = null;
     private int sleepInterval;
     private myFrame frame;
     private int filesCount;
     private String filePathTemp[]=new String[100];
     private String outputPath;
     
     startServer(myFrame frameIn,int Interval)
    {
          this.sleepInterval= Interval;
          this.frame=frameIn;
    }
      
     public void readFile() throws FileNotFoundException, IOException
     {
         FileReader fin = new FileReader("C:\\Users\\Aynesh\\Desktop\\Config.txt");
         BufferedReader data = new BufferedReader(fin);
         String line;
         int i=1;
         outputPath= data.readLine().substring(5);
         line = data.readLine();
         filesCount=Integer.parseInt(line.substring(6));
         for(i=1;i<=filesCount;i++) {
             filePathTemp[i-1]=data.readLine();
         }
         data.close();
     }
             
     
     
    @Override
    public void run()
    {
              
        
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server Started ");
            this.readFile();
                        }  catch(SocketTimeoutException s){
         System.out.println("Socket timed out!");     
        }
        catch (IOException e) {
        }
        while (true) {
            try {
                socket = serverSocket.accept();
                frame.addIPAddress(socket.getInetAddress());
                new threadedServer(socket,frame,filePathTemp,filesCount,sleepInterval,outputPath).start();
                
            } catch (IOException e) {
            }

            
        }
    }
    
}
