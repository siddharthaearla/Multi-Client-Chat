//E.SIDDHARTHA
//20154114

import java.io.*;
import java.net.*;
import java.util.*;

class ThreadClientSend implements Runnable 
{

String d;
DataOutputStream out=null;
Scanner sc = new Scanner(System.in);
public ThreadClientSend(DataOutputStream out)
{
    this.out=out;
}

public void run()
{

  while(true)
  {
    try
    {     
      d=sc.nextLine();

      out.writeUTF(d);
    }
    catch(IOException e){}
  }
}
}

class ThreadClientReceive implements Runnable 
{

String m;
DataInputStream in=null;

public ThreadClientReceive(DataInputStream in)
{
    this.in=in;
}

public void run()
{
    while(true)
    {
      try
      {

        m=in.readUTF();
        System.out.println("server: "+m);

      }               
      catch(IOException e){System.out.println(e);}
    
    }
    
}
}


public class Client
{


  public static void main(String[] args)
{

try{

//s1 receiving in
// s2 sending  out

   Socket s1=new Socket("localhost",1030);
   Socket s2=new Socket("localhost",1031);
   
DataInputStream in=new DataInputStream(s1.getInputStream());
DataOutputStream out=new DataOutputStream(s2.getOutputStream());

new Thread(new ThreadClientSend(out)).start();
new Thread(new ThreadClientReceive(in)).start();

}
catch(Exception e){System.out.println(e);}
}
}

