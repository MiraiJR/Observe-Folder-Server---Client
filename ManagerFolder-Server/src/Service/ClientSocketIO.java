package Service;

import Models.Client;
import java.io.DataInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientSocketIO implements Runnable {

    private final Client client;

    public ClientSocketIO(Client client) throws IOException {
        this.client = client;

    }

    @Override
    public void run() {
        while (true) {
            try {
                DataInputStream din = new DataInputStream(client.getSocket().getInputStream());
                byte type_msg = din.readByte();

                if (type_msg == 0) {
                    String data = din.readUTF();
                    if (data.equals("exit")) {
                        String cur_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
                        this.client.addNotify(client.getName() + " - " + cur_time + " - " + " đã ngắt kết nối!");
                        ServerSocketIO.removeClient(client);
                    }
                } else if (type_msg == 1) {
                    String data = din.readUTF();
                    client.setObserve_dir(data);
                }
            } catch (IOException ex) {
                try {
                    client.getSocket().close();
                } catch (IOException ex1) {
                    Logger.getLogger(ClientSocketIO.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        }
    }
}
