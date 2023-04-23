package Service;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import managerfolder.client.ManagerFolderClient;

public class ClientSocketIO implements Runnable {

    private final Socket socket;
    private final AtomicBoolean running = new AtomicBoolean(false);

    public ClientSocketIO(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        running.set(true);
        while(running.get()) {
            try {
                DataInputStream din = new DataInputStream(socket.getInputStream());
                byte type = din.readByte();
                
                if(type == 0) {
                    String data = din.readUTF();
                    
                    System.out.println(data);
                    if(data.equals("server-shutdown")) {
                        running.set(false);
                        ManagerFolderClient.disconnectGUI();
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(ClientSocketIO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void stop() {
        running.set(false);
    }
}
