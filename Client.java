import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;



public class Client extends Thread{

	
	Socket socketClient;
	
	ObjectOutputStream out;
	ObjectInputStream in;
	
	private Consumer<Serializable> callback;

	Info pack;
	
	Client(Consumer<Serializable> call){
	
		callback = call;
	}
	
	public void run() {
		
		try {
		socketClient= new Socket("127.0.0.1",5555);
	    out = new ObjectOutputStream(socketClient.getOutputStream());
	    in = new ObjectInputStream(socketClient.getInputStream());
	    socketClient.setTcpNoDelay(true);
		pack = new Info();
		}
		catch(Exception e) {}
		
		while(true) {
			 
			try {
			pack = (Info) in.readObject();
			pack.addClient.clear();
			callback.accept(pack.Message);
			}
			catch(Exception e) {}
		}
	
    }
	
	public void send(String msg) {
		try {
			pack.Message = msg;
			out.writeObject(pack);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}




}
