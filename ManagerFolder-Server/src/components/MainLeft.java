package components;

import Models.Client;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class MainLeft extends JPanel implements ActionListener {

    private JLabel label;
    private JPanel panel;
    private ArrayList<Client> clients;

    public MainLeft() {
        clients = new ArrayList<>();
        initComponents();
        setOpaque(false);
    }

    public ArrayList<Client> getClients() {
        return clients;
    }

    public void setClients(ArrayList<Client> clients) {
        this.clients = clients;
        this.remove(1);
        paintPanel();
        this.add(panel, 1);
        validate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs;
        g2.setPaint(new Color(242, 195, 163));
        g2.fillRect(0, 0, getWidth(), getHeight());

        super.paintComponent(grphcs);
    }

    private void initComponents() {
        this.setPreferredSize(new Dimension(300, getHeight()));
        label = new JLabel("Danh sách clients đang kết nối!", SwingConstants.CENTER);
        label.setFont(new Font("Dialog", Font.BOLD, 20));
        label.setPreferredSize(new Dimension(300, 100));
        paintPanel();
        add(label, BorderLayout.PAGE_START);
        add(panel, 1);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    private void paintPanel() {
        panel = new JPanel();
        int number_row = clients.size() % 2 == 0 ? clients.size() / 2 : (clients.size() / 2) + 1;
        
        GridLayout layout = new GridLayout(number_row, 2);
        layout.setHgap(50);
        layout.setVgap(50);
        panel.setLayout(layout);
        panel.setBackground(new Color(242, 195, 163));

        for (Client client : clients) {
            BoxClient box_client = new BoxClient(client);
            box_client.setPreferredSize(new Dimension(100, 100));
            panel.add(box_client);
        }
    }
}
