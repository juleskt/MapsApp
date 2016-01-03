import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

//Simple java TCP socket server to handle queries
public class MyServer 
{
 	public static void main(String[] args) throws IOException
 	{
 		//Initialize objects
  		ServerSocket serverSocket = null;
  		Socket socket = null;
  		DataInputStream dataInputStream = null;
  		DataOutputStream dataOutputStream = null;
  		TwoDTree tree = new TwoDTree();
		Location test;

		Scanner sc = new Scanner(new FileReader("locations.txt"));
		 
		sc.nextLine();
		String state = "";
		String county = "";
		double lat;
		double lng;
		while (sc.hasNextLine()) 
		{
				String input = sc.nextLine();
				String split[] = input.split("\\s+");

				int len = split.length;

				state = split[0];

				for(int x = 1; x < len-2; x++)
				{
					county+= split[x];
				}
				
				lat = Double.parseDouble(split[len-2]);
				lng = Double.parseDouble(split[len-1]);		

				test = new Location(state, county, lat,lng);
                                tree.insert(test);
				county = "";
		}
  
  		try 
  		{
		   serverSocket = new ServerSocket(8888);
		   System.out.println("Listening :8888");
		} 
  		catch (IOException e) 
  		{
   			// TODO Auto-generated catch block
   			e.printStackTrace();
  		}
  
 		 while(true)
  		 {
    		try 
   			{
			    socket = serverSocket.accept();

			    dataInputStream = new DataInputStream(socket.getInputStream());
			    dataOutputStream = new DataOutputStream(socket.getOutputStream());

			    System.out.println("Connection from: " + socket.getInetAddress());
			    
			    String rec = dataInputStream.readUTF(); 

			    String split[] = rec.split("\\s+");

			    if(split.length == 2)
			    {
			    	System.out.println("Query: Nearest 10");
			    	double latt = Double.parseDouble(split[0]);
					double lngg = Double.parseDouble(split[1]);

					test = new Location(latt,lngg);
					ArrayList<IPoint> tenn = tree.nearest10(test);
					String toSend = "";
		
					for (IPoint a : tenn) 
					{
						toSend += a.toString() + " ";
					}
					
				System.out.println(tenn.size());

				    dataOutputStream.writeUTF(toSend);

				}
				else
				{
					System.out.println("Query: Rectangular bounds");
					
					String  superSplitOne[] = split[1].split(",");
					String superSplitTwo[] = split[3].split(",");

					double southLat = Double.parseDouble(superSplitOne[0].substring(1));
					double westLng = Double.parseDouble(superSplitOne[1].substring(0,superSplitOne[1].length()-2));
					double northLat = Double.parseDouble(superSplitTwo[0].substring(1));
					double eastLng = Double.parseDouble(superSplitTwo[1].substring(0,superSplitTwo[1].length()-3));

					Rectangle range = new Rectangle(southLat, westLng, northLat, eastLng);
					String toSend = "";

					ArrayList<IPoint> inrange = tree.search(range);
		
					for (IPoint a : inrange)
					{
						toSend+= a.toString() + " ";
					}
					
					System.out.println(inrange.size());
				    dataOutputStream.writeUTF(toSend);
				}
 			} 
   			catch (IOException e) 
   			{
    			// TODO Auto-generated catch block
    			e.printStackTrace();
   			}
   			finally
   			{
    			if( socket!= null)
    			{
     				try 
     				{
      					socket.close();
      					System.out.println("Closed\n");
     				} 
     				catch (IOException e) 
     				{
      					// TODO Auto-generated catch block
      					e.printStackTrace();
     				}
    			}
    
    			if( dataInputStream!= null)
    			{
     				try 
     				{
      					dataInputStream.close();
     				} 
     				catch (IOException e) 
     				{
      					// TODO Auto-generated catch block
      					e.printStackTrace();
     				}
    			}
    
    			if( dataOutputStream!= null)
    			{
     				try 
     				{
      					dataOutputStream.close();
     				} 
     				catch (IOException e) 
     				{
      					// TODO Auto-generated catch block
      					e.printStackTrace();
     				}
    			}
   			}
  		}
 	}
}
