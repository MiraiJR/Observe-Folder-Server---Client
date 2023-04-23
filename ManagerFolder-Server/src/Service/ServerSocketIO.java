package Service;

import Models.Client;
import components.Home;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerSocketIO implements Runnable {

    private final int port = 3000;
    private ServerSocket server_socket;
    private static ArrayList<Client> sockets;
    private static Home main_frame;

    public ServerSocketIO(Home main_frame) {
        ServerSocketIO.sockets = new ArrayList<>();
        ServerSocketIO.main_frame = main_frame;
    }

    public ArrayList<Client> getSockets() {
        return sockets;
    }

    @Override
    public void run() {
        try {
            server_socket = new ServerSocket(port);
            while (true) {
                Socket s = server_socket.accept();

                Client client = new Client(s, "", "Client " + s.getPort());
                ClientSocketIO client_socket = new ClientSocketIO(client);

                new Thread(client_socket).start();

                sockets.add(client);
                ServerSocketIO.main_frame.setClients(sockets);
            }
        } catch (IOException ex) {
            System.out.println(ex);
            Logger.getLogger(ServerSocketIO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void addClient(Client client) {
        ServerSocketIO.sockets.add(client);
    }

    public static void removeClient(Client client) {
        ServerSocketIO.sockets.removeIf(cli -> cli == client);
        ServerSocketIO.main_frame.setClients(ServerSocketIO.sockets);
    }
    
    public static void sendAll(String msg, int type) throws IOException {
        for(Client client:  ServerSocketIO.sockets) {
            DataOutputStream dout = new DataOutputStream(client.getSocket().getOutputStream());
            dout.writeByte(type);
            dout.writeUTF(msg);
            dout.flush();
        }
    }
}
