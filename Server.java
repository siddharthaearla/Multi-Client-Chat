//E.SIDDHARTHA
//20154114

import java.util.*;
import java.io.*;
import java.net.*;

class Customer
{
  static DataInputStream cusdis=null;
  static DataOutputStream cusdos=null;
  static Socket sk=null;
  static int portnum;
  public Customer(DataInputStream dis,DataOutputStream dos,Socket sk)
  {
    this.cusdis=dis;
    this.cusdos=dos;
    this.sk=sk;
    this.portnum=sk.getPort();
  }
}

class ThreadServerSend implements Runnable 
{

String d;
ServerScanning scan=null;
DataOutputStream out=null;
int per;
Scanner sc = new Scanner(System.in);

public ThreadServerSend(ServerScanning scan)
{
    this.scan=scan;
    this.per=0;
}

public void update(ServerScanning scan)
{
  this.scan=scan;
  this.per=1;
}

public void resume()
{
  this.per=0;
}
public void run()
{
while(true)
{
    try
    {   
      if(this.per==1)
      {
        return;
      }
     d="";
     d=sc.nextLine();
     if("".equals(d))
     continue;

      System.out.println("send to :: ");
      int sendid=sc.nextInt();

       Customer temp=scan.clientMap.get(sendid);
       Socket sock=temp.sk;

       System.out.println(sock.getPort());
       DataOutputStream dos=new DataOutputStream(sock.getOutputStream());      



      dos.writeUTF(d);      

    }
    catch(IOException e){System.out.println(e);}
  }
}
}

class ThreadServerReceive implements Runnable 
{

String m;
DataInputStream in=null;
int recid;
public ThreadServerReceive(DataInputStream in,int recid)
{
    this.in=in;
    this.recid=recid;
}

public void run()
{
 while(true){   
      try
      {

        m=in.readUTF();
        System.out.println("client"+recid+": "+m);

      }               
      catch(IOException e){System.out.println(e);}
    
    
 }   
}


}


class ServerScanning implements Runnable
{
     Map<Integer,Customer> clientMap = new HashMap<Integer,Customer>();
     Map<Integer,Integer> portMap = new HashMap<Integer,Integer>();

     Map<DataInputStream,Integer> distoidMap=new HashMap<DataInputStream,Integer>();
     Map<DataOutputStream,Integer> dostoidMap=new HashMap<DataOutputStream,Integer>();
     int mapsize=0;
     Scanner sc = new Scanner(System.in);

 static ServerSocket ss1=null;
 static ServerSocket ss2=null;

   public ServerScanning(ServerSocket ss1,ServerSocket ss2)
   {
    this.ss1=ss1;
    this.ss2=ss2;
   }
   
   
 public void run()
 {
 
     ThreadServerSend sersend=new ThreadServerSend(this);
     
  while(true)
  {
    try
    {
      
      Socket s1=null;
      Socket s2=null;

                    s1=ss1.accept();
                    s2=ss2.accept();
      System.out.println(s1);
      if(s1==null || s2==null)
       continue;
     // System.out.println("connected");
      DataInputStream dis=new DataInputStream(s2.getInputStream());  
      DataOutputStream dos=new DataOutputStream(s1.getOutputStream());
    
    int tp=s1.getPort();
    Customer cus=new Customer(dis,dos,s1);
    //System.out.println(mapsize);
    this.clientMap.put(mapsize,cus);
    this.portMap.put(mapsize,tp);
    this.distoidMap.put(dis,mapsize);
    this.dostoidMap.put(dos,mapsize);
    this.mapsize++;  
      
    dos=null;

    
    sersend.update(this);
    sersend.resume();
    new Thread(sersend).start();
    //System.out.println("started");
    new Thread(new ThreadServerReceive(dis,this.mapsize-1)).start(); 

   }
    
    catch(Exception e){System.out.println(e);}
 }
 }
 

 
}

  
public class Server
{

private static ObjectOutputStream out;
private static ObjectInputStream in;
private static Socket sout;
private static Socket sin;
private static ServerSocket ss1;
private static ServerSocket ss2;


  public static void main(String[] args)
  {
     
     try
     {
     
       ServerSocket ss1=new ServerSocket(1030);
       ServerSocket ss2 = new ServerSocket(1031);
       
       Socket s1=null;
       Socket s2=null;


    new Thread(new ServerScanning(ss1,ss2)).start();  
      

         }catch(Exception e){System.out.println(e);}

  }
  
}

