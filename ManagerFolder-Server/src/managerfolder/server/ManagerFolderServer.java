package managerfolder.server;

import Service.ServerSocketIO;
import components.Home;
import java.awt.EventQueue;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class ManagerFolderServer {

    private static Home main_frame;

    public ManagerFolderServer() {
        main_frame = new Home();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            ManagerFolderServer managerFolderServer = new ManagerFolderServer();
            main_frame.setVisible(true);
            main_frame.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                    if (JOptionPane.showConfirmDialog(main_frame,
                            "Bạn muốn thoát ứng dụng?", "Đóng ứng dụng?",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                        try {
                            ServerSocketIO.sendAll("server-shutdown", 0);
                        } catch (IOException ex) {
                            Logger.getLogger(ManagerFolderServer.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            });
            
            Thread socket_thread = new Thread(new ServerSocketIO(main_frame));
            socket_thread.start();
        });

    }
}
