
import java.io.*;
import java.net.*;
/**
 *
 * @author Aynesh
 */
public class Client {

     static final int PORT = 6500;
     private int filesCount=0;



     
    public static void main(String[] args) throws InterruptedException {
        
      String serverName = "localhost";
      Socket client;
      
      //To find the ipaddress
      
         try
         {
         FileReader fin = new FileReader("C:\\ClientConfig.txt");
         BufferedReader data = new BufferedReader(fin);
         serverName= data.readLine().substring(10);
         data.close();
         fin.close();
         }
         catch(FileNotFoundException e )
         {
             System.out.println("Server's IP address not found please put a text in C drive in ClientConfig.txt ");
             System.out.println("format ipaddress=a.b.c.d ");
         }
         catch(IOException e)
         {
             System.out.println("Unknown error !");
         }
      
      while(true)
      {
         System.out.println("Connecting to " + serverName + " on port " + PORT);
         try {
                
             client = new Socket(serverName, PORT);
             upload fileClient = new upload(client);
             fileClient.intialize();
             fileClient.checkUTF();   
         } catch (UnknownHostException ex) {
                System.out.println("Host down !");
         } catch (IOException ex) {
                System.out.println("Server error !");
                Thread.sleep(15000);
        }
          
      }
    } 

}
