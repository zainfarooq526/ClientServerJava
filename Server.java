import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.scene.control.ListView;

import static java.lang.Character.isDigit;
/*
 * Clicker: A: I really get it    B: No idea what you are talking about
 * C: kind of following
 */

public class Server{

	int count = 1;
	ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
	TheServer server;
	private Consumer<Serializable> callback;
	Info sPackage;

	ArrayList<Integer> temp = new ArrayList<>();


	Server(Consumer<Serializable> call){

		callback = call;
		server = new TheServer();
		sPackage = new Info();
		server.start();
	}


	public class TheServer extends Thread{

		public void run() {

			try(ServerSocket mysocket = new ServerSocket(5555);){
		    System.out.println("Server is waiting for a client!");


		    while(true) {
				System.out.println("before");
				ClientThread c = new ClientThread(mysocket.accept(), count);
				System.out.println("after");

				callback.accept("client has connected to server: " + "client #" + count);
				clients.add(c);
				sPackage.P.add("client " + count);
				c.start();
				count++;
			    }
			}//end of try
				catch(Exception e) {
					System.out.println(e);
					callback.accept("Server socket did not launch");
				}
			}//end of while
		}
	

		class ClientThread extends Thread{

			Socket connection;
			int count;
			ObjectInputStream in;
			ObjectOutputStream out;
			
			ClientThread(Socket s, int count){
				this.connection = s;
				this.count = count;	
			}

			public synchronized void updateClients() {
				temp = new ArrayList<Integer>();

				if (sPackage.addClient.size() == 0) {
					for(int i = 0; i < clients.size(); i++) {
						temp.add(i+1);
					}
				} else {
					temp = sPackage.addClient;
				}

				for(int i = 0; i < temp.size(); i++) {
					System.out.println(temp.size());
					System.out.println(clients.size());
					System.out.println(temp);


					ClientThread t = clients.get(temp.get(i));
					try {
					 t.out.writeObject(sPackage);
					}
					catch(Exception e) {
						System.out.println(e);
					}
				}


			}
			
			public void run(){
					
				try {
					in = new ObjectInputStream(connection.getInputStream());
					out = new ObjectOutputStream(connection.getOutputStream());
					connection.setTcpNoDelay(true);
				}
				catch(Exception e) {
					System.out.println("Streams not open");
				}


				sPackage.Message = "new client on server: client #"+count;
				updateClients();


				 while(true) {
					    try {
					    	Info data = (Info) in.readObject();
							sPackage = data;
							sPackage.Message = "client:" + count + "send:" + sPackage.Message;
							callback.accept(sPackage);
							updateClients();
					    	}
					    catch(Exception e) {
							sPackage.Message = "Client #"+count+" has left the server!";
					    	updateClients();
					    	clients.remove(this);
							sPackage.P.remove("client " + count);
					    	break;
					    }
					}
				}//end of run
			
			
		}//end of client thread

}


	
	

	
