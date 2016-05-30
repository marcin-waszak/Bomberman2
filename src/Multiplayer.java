import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * A class used for initializing server and making a Server-Client connection,
 * and providing methods responsible for sending and receiving messages sent via
 * TCP/IP protocol.
 * 
 * Although there is strict differentiation between server and client in this class,
 * the overall program is using a peer to peer communication model while maintaining data 
 * and sending messages.
 */
public class Multiplayer {
	
	/** Socket required for communication between players*/
	private Socket socket;
	/** DataStream in which users write messages to other players*/
	private DataOutputStream dos;
	/** DataStream in which users receive messages from other players*/
	private DataInputStream dis;
	/** IP address of the server*/
	private String ip;
	/** Game port*/
	private int port;
	/** True if multiplayer object is a server*/
	private boolean is_server;
	
	/**
	 * Constructor method for Multiplayer class.
	 * Asks user for IP address and port required to connect to server.
	 * If there isn't any server running with those parameters, this
	 * calls the method to initialize one.
	 */
	public Multiplayer() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Please input the IP: ");
		ip = scanner.nextLine();
		System.out.println("Please input the port: ");
		port = scanner.nextInt();
		while (port < 1 || port > 65535) {
			System.out.println("The port you entered was invalid, please input another port: ");
			port = scanner.nextInt();
		}
		if (!connect(ip, port)) {
			initializeServer(ip, port);
		}
	}
	
	/**
	 * Initializes server.
	 * After doing so starts listening for server requests.
	 * @param ip	IP address of server
	 * @param port	Game port
	 */
	private void initializeServer(String ip, int port) {
		try {
			ServerSocket serverSocket = new ServerSocket(port, 8, InetAddress.getByName(ip));
			listenForServerRequest(serverSocket);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Tries to make a TCP/IP connection with server.
	 * 
	 * If successful, creates Output and Input streams used
	 * for sending and receiving messages between server and client.
	 * And also disables Nagle's algorithm.
	 * 
	 * If fails, returns false to constructor method from which this was called and
	 * constructor starts initializing server.
	 * 
	 * @param ip	IP address of server
	 * @param port	Game port
	 * @return		Boolean value telling if connection was made
	 */
	private boolean connect(String ip, int port) {
		try {
			socket = new Socket(ip, port);
			socket.setTcpNoDelay(true);
			dos = new DataOutputStream(socket.getOutputStream());
			dis = new DataInputStream(socket.getInputStream());
			System.out.println("Successfully connected to the server.");
			is_server = false;
			return true;
		} catch (IOException e) {
			System.out.println("Unable to connect to the address: " + ip + " port: " + port + "\nStarting a server");
			return false;
		}
	}
	
	/**
	 * Method called from server.
	 * 
	 * Waits for client making a request. Then accepts it,
	 * and creates Output and Input streams used for sending and
	 * receiving messages between server and client. Also disables
	 * Nagle's algorithm.
	 * 
	 * @param serverSocket	Reference to server
	 */
	private void listenForServerRequest(ServerSocket serverSocket) {
		try {
			socket = serverSocket.accept();
			socket.setTcpNoDelay(true);
			dos = new DataOutputStream(socket.getOutputStream());
			dis = new DataInputStream(socket.getInputStream());
			System.out.println("CLIENT HAS REQUESTED TO JOIN, AND WE HAVE ACCEPTED");
			is_server = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns boolean value indicating whether the caller 
	 * is server or client.
	 * @return True if the caller is indeed a server.
	 */
	public boolean getIs_server() {
		return is_server;
	}
	
	/**
	 * Method used for sending message to other player.
	 * Exception is thrown when the other player exits program.
	 * This then kills itself thread.
	 * @param i	Integer type message to send
	 */
	public void sendMessage (int i) {
		try {
			dos.writeInt(i);
		} catch (IOException e) {
			System.exit(0);
		}
	}
	
	/**
	 * Method used for receiving message from the other player.
	 * Exception is thrown when the other player exits program.
	 * This then kills itself thread.
	 * @return Integer type message received from other player
	 */
	public int readMessage () {
		try {
			return dis.readInt();
		} catch (IOException e) {
			System.exit(0);
			return 0;
		}
	}
}
