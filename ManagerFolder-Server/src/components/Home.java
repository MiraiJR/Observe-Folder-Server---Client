package components;

import Models.Client;
import java.awt.BorderLayout;
import java.util.ArrayList;
import javax.swing.JFrame;

public class Home extends JFrame {

    private ArrayList<Client> clients;
    private MainLeft main_left;
    private MainRight main_right;

    public Home() {
        clients = new ArrayList<>();
        initComponents();
        init();
    }

    public ArrayList<Client> getClients() {
        return clients;
    }

    public void setClients(ArrayList<Client> clients) {
        this.clients = clients;
        this.main_left.setClients(clients);
        this.main_right.setClients(clients);
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

        main_left = new MainLeft();
        add(main_left, BorderLayout.LINE_START);
        main_right = new MainRight();
        add(main_right, BorderLayout.CENTER);
    }
}
