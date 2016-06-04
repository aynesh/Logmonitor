/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;


import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;



public class threadedServer extends Thread 
{
    
     protected Socket Client;
     private byte aByte[];
     private int bytesRead;
     private InputStream Input;
     private myFrame reference;
     private DataInputStream in;
     private DataOutputStream out;
     private int i[]=new int[100];
     private String filePath[];
     private int filesCount=2;
     private int sleepInterval=5000;
     private String fileNames[];
     private String outputPath;
     
     public threadedServer(Socket clientSocket, myFrame ref,String path[],int Count,int Interval,String oPath) {
        this.Client = clientSocket;
        this.reference=ref;
        filePath=path;
        filesCount=Count;
        sleepInterval=5000;
        fileNames=new String[filesCount];
        outputPath=oPath;
    }
     
     public void sendCommand(String filePath,int k) throws IOException
     {          
                 System.out.println("This is "+i[k]++ +" th run ");
                 out.writeUTF(Integer.toString(k));
                 out.writeUTF(filePath);
     }
     
     public void transferFile(int k) throws IOException
     {
                 aByte = new byte[1];
                 ByteArrayOutputStream baos = new ByteArrayOutputStream();
                 Input = Client.getInputStream();
                 if (Input != null) {
                            System.out.println("file extraction intiated . . . ");
                            FileOutputStream fos = null;
                            BufferedOutputStream bos = null;
                            try {
                                    if(i[k]==1)
                                    {
                                    fos = new FileOutputStream(outputPath+Client.getInetAddress()+"_"+fileNames[k] );
                                    }
                                    else
                                    {
                                    fos = new FileOutputStream(outputPath+Client.getInetAddress()+"_"+fileNames[k],true );
                                    }    
                                    
                                    bos = new BufferedOutputStream(fos);
                                    int temp = Integer.parseInt(in.readUTF());
                                    System.out.println("available "+temp);
                                    int l=0;
                                    
                                    for(l=0;l<temp;l++)
                                    {
                                        try
                                        {
                                            Input.read(aByte);
                                        } catch(IOException exx)
                                        {
                                            System.out.println("Data transfer error ! ");
                                            Thread.sleep(25);
                                        }
                                         baos.write(aByte);
                                    }
                                    
                                    bos.write(baos.toByteArray());                                        
                                    bos.flush();
                                    bos.close();
                                    System.out.println("File Transfer successfull . . . ");

                                  } catch(Exception e) {
                                   System.out.println("File transfer failed . . . . ");
                                  }

                    }
     }
     
     
     void verifySuccess() throws IOException
     {
         out = new DataOutputStream(Client.getOutputStream());
         in = new DataInputStream(Client.getInputStream());    
         out.writeUTF("Verify Success");
         String answer;
         while(true){
             try
             {
             answer = in.readUTF();
            if(answer.equals("Success")) {
                 break;
            }
            
             }
             catch(IOException e)
             {
                 System.out.println("error");
                 e.printStackTrace();
                 break;
                 
             }
                 
         }
         System.out.println("Success");
     }
     
     void intialize() throws IOException
     {
         int k;  
         String tempData[];

         out = new DataOutputStream(Client.getOutputStream());
         in = new DataInputStream(Client.getInputStream());                 
         out.writeUTF("start");
         out.writeUTF("files="+filesCount);
         for(k=0;k<filesCount;k++)
         {
             out.writeUTF(filePath[k]);
         }
         while(true)
         {
             if(in.readUTF().equals("end")) {
                 break;
             }
         }

         for(k=0;k<filesCount;k++)
         {
             try
             {
             tempData=filePath[k].split("\\\\");
             fileNames[k]=tempData[tempData.length-1];
             }catch (Exception e)
             {
                 System.out.println("Unknown error");
             }
         }
         
         
     }
     
     
    @Override
     public void run()
     {
        int k=0;
        try
        {
            this.intialize();
            while(true){
                
                for(k=0;k<filesCount;k++)
                {
                    this.sendCommand(filePath[k],k);   
                    
                    try {
                    Thread.sleep(sleepInterval);
                    } catch (InterruptedException ex) {
                    System.out.println("Unknown interuppt");
                    }
                                  
                    this.transferFile(k);                    
                    this.verifySuccess();
                }     

                
                
            }

                
        } catch(IOException e) {
        System.out.println("Data transfer error ! ");
        reference.refreshList(Client.getInetAddress());
        e.printStackTrace();
        }
     }
}


