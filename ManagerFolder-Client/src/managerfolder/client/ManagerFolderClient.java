package managerfolder.client;

import Service.ClientSocketIO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

public class ManagerFolderClient extends JFrame {

    private static Socket s;
    private static final JButton reconnect = new JButton("Kết nối server");
    private static final JLabel label = new JLabel("", SwingConstants.CENTER);
    private static DataOutputStream dout = null;
    private static ManagerFolderClient manager_folder_client;

    public ManagerFolderClient() {
        initComponents();
        init();
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Manage Folder Server");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 1080, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 720, Short.MAX_VALUE)
        );

        pack();
    }

    private void init() {
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) throws IOException {
        EventQueue.invokeLater(() -> {
            manager_folder_client = new ManagerFolderClient();
            manager_folder_client.setVisible(true);

            manager_folder_client.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                    if (JOptionPane.showConfirmDialog(manager_folder_client,
                            "Bạn muốn thoát ứng dụng?", "Đóng ứng dụng?",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                        if (s != null) {
                            try {
                                // client đóng ứng dụng thì gửi thông báo đến server
                                sendMsg("exit", 0);

                                System.exit(0);
                            } catch (IOException ex) {
                                Logger.getLogger(ManagerFolderClient.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                }
            });
            try {
                s = new Socket("localhost", 3000);
                ClientSocketIO client = new ClientSocketIO(s);
                new Thread(client).start();

                label.setText("Đã kết nối với server!");
                manager_folder_client.add(label);
                // gửi thư mục mà client đang được đặt cho server
                String dir = System.getProperty("user.dir");
                sendMsg(dir, 1);
            } catch (IOException except) {
                label.setText("Kết nối server thất bại!");
                manager_folder_client.add(label, BorderLayout.CENTER);
                reconnect.setPreferredSize(new Dimension(100, 100));
                reconnect.setBackground(new Color(68, 210, 168));
                reconnect.addActionListener((ActionEvent e) -> {
                    try {
                        connectServer();
                    } catch (IOException ex) {
                        Logger.getLogger(ManagerFolderClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
                manager_folder_client.add(reconnect, BorderLayout.PAGE_END);
                if (s != null) {
                    try {
                        s.close();
                    } catch (IOException ex) {
                        Logger.getLogger(ManagerFolderClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    JOptionPane.showMessageDialog(null, "Server ngừng hoạt động!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Server không hoạt động!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }

    private static void connectServer() throws IOException {
        try {
            s = new Socket("localhost", 3000);
            ClientSocketIO client = new ClientSocketIO(s);
            new Thread(client).start();

            manager_folder_client.remove(label);
            manager_folder_client.remove(reconnect);
            label.setText("Đã kết nối với server");
            manager_folder_client.add(label); // gửi thư mục mà client đang được đặt cho server
            manager_folder_client.validate();
            manager_folder_client.repaint();
            String dir = System.getProperty("user.dir");
            sendMsg(dir, 1);
        } catch (IOException error) {
            if (s != null) {
                try {
                    s.close();
                } catch (IOException ex) {
                    Logger.getLogger(ManagerFolderClient.class.getName()).log(Level.SEVERE, null, ex);
                }
                JOptionPane.showMessageDialog(null, "Server ngừng hoạt động!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Server không hoạt động!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private static void sendMsg(String msg, int type) throws IOException {
        dout = new DataOutputStream(s.getOutputStream());
        dout.writeByte(type);
        dout.writeUTF(msg);
        dout.flush();
    }

    public static void disconnectGUI() {
        JOptionPane.showMessageDialog(null, "Server đã ngừng hoạt động!", "Thông báo", JOptionPane.WARNING_MESSAGE);
        manager_folder_client.remove(label);
        label.setText("Kết nối server thất bại!");
        manager_folder_client.add(label, BorderLayout.CENTER);
        reconnect.setPreferredSize(new Dimension(100, 100));
        reconnect.setBackground(new Color(68, 210, 168));
        reconnect.addActionListener((ActionEvent e) -> {
            try {
                connectServer();
            } catch (IOException ex) {
                Logger.getLogger(ManagerFolderClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        manager_folder_client.add(reconnect, BorderLayout.PAGE_END);
        manager_folder_client.validate();
        manager_folder_client.repaint();
    }
}
