import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Multiplayer {
	
	private Socket socket;
	private DataOutputStream dos;
	private DataInputStream dis;
	private String ip;
	private int port;
	private boolean is_server;
	
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
	
	private void initializeServer(String ip, int port) {
		try {
			ServerSocket serverSocket = new ServerSocket(port, 8, InetAddress.getByName(ip));
			listenForServerRequest(serverSocket);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
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
	
	public boolean getIs_server() {
		return is_server;
	}
	
	public void sendMessage (int i) {
		try {
			dos.writeInt(i);
		} catch (IOException e) {
	//		e.printStackTrace();
			System.exit(1);
		}
	}
	
	public int readMessage () {
		try {
			return dis.readInt();
		} catch (IOException e) {
	//		e.printStackTrace();
			System.exit(1);
			return 0;
		}
	}
}
