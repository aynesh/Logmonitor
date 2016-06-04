
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author Aynesh
 */
public class upload {
    
    private OutputStream outToServer;
    private InputStream inFromServer;
    private DataInputStream in;
    private int bytesCount[];
    private Socket client;
    private BufferedOutputStream bout= null;
    private String filePath[];
    private DataOutputStream out;
    int filesCount;
    private int i[];
    upload(Socket accept) throws IOException
    {
            this.client = accept;
            outToServer = client.getOutputStream();
            inFromServer = client.getInputStream();
            filePath= new String[100];
            bytesCount = new int[100];
            i=new int[100];
    }
    
    public void checkUTF() throws IOException
    {
         System.out.println("Just connected to " + client.getRemoteSocketAddress());
         in = new DataInputStream(inFromServer);
         int k;
         while(true) {
         String question = in.readUTF();
         System.out.println("Command received for upload - "+ question);
         k=Integer.parseInt(question);
         question = in.readUTF();
         if(filePath[k].equals(question))
            {
                try
                {
                this.uploadFile(filePath[k],k);
                }catch(IOException e)
                {
                    e.printStackTrace();
                }
            }
         this.verifySuccess();
       
        }
    }
 
    void intialize() throws IOException
    {
             in = new DataInputStream(inFromServer);
             out = new DataOutputStream(outToServer);
             String tempData;
             int k;
             while(true)
             {
                 if(in.readUTF().equals("start"))
                 {
                     tempData=in.readUTF();
                     if(tempData.contains("files="))
                             {
                                 filesCount=Integer.parseInt(tempData.substring(6));
                             }
                     for(k=0;k<filesCount;k++)
                     {
                     filePath[k]=in.readUTF();
                     }
                     out.writeUTF("end");
                     System.out.println("Sent list of log files");
                     for(k=0;k<filesCount;k++)
                     {
                         System.out.println(filePath[k]);
                     }
                     break;
                 }
                 
             }
             
    }
    
     void verifySuccess() throws IOException
     {
         out = new DataOutputStream(outToServer);
         in = new DataInputStream(inFromServer);    
         while(true){
            if(in.readUTF().equals("Verify Success"))
                break;
                 
         }
         out.writeUTF("Success");
         System.out.println("Success");
     }
    
    
    public void uploadFile(String filePath,int k) throws IOException
    {
                i[k]++;
                System.out.println("File transfer initiated . . . ");
                bout = new BufferedOutputStream(client.getOutputStream());
                File myFile = new File(filePath);
                
                byte mybytearray[] = new byte[(int) myFile.length()];
                FileInputStream fis = null;

                try {
                    fis = new FileInputStream(myFile);
                } catch (FileNotFoundException ex) {
                      System.out.println("File not found !");
                }
                BufferedInputStream bis = new BufferedInputStream(fis);
                System.out.println("This is "+i[k]+"th run");
                try {
                    
                    if(i[k]==1)
                    {
                    bis.read(mybytearray, 0, mybytearray.length);
                    bytesCount[k]=mybytearray.length;
                    System.out.println("Bytes : "+bytesCount[k]);
                    out.writeUTF(Integer.toString(bytesCount[k]));
                    bout.write(mybytearray, 0, mybytearray.length);
                    System.out.println("Writing for the first time");
                    }
                    else
                    {     
                    bis.read(mybytearray, 0, mybytearray.length);
                        if(mybytearray.length!=bytesCount[k]&&i[k]!=1)
                        {
                          Calendar cal = Calendar.getInstance();
                          cal.getTime();
                          SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                          String temp = String.format("%n***** The log updated at : %s **** %n",sdf.format(cal.getTime()));
                          out.writeUTF(Integer.toString(mybytearray.length-bytesCount[k]+temp.getBytes().length));
                          bout.write(temp.getBytes());
                          bout.write(mybytearray, bytesCount[k], mybytearray.length-bytesCount[k]);
                          bytesCount[k]=mybytearray.length;
                          System.out.println("Writing for the "+i[k]+"th time Bytes Count");
                        }
                        else
                        {
                            out.writeUTF("0");
                        }
                                    
                    }

                    bout.flush();                    

                } catch (IOException ex) {

                    System.out.println("Unknown error");
                }
      }
  
}
