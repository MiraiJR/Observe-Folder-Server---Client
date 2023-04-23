/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package components;

import Models.Client;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

public class MainRight extends JPanel implements ActionListener, Observer {

    public JLabel label;
    public JTextArea notify;
    private ArrayList<Client> clients;
    private final ArrayList<String> content_notifies;
    private JButton view_all;

    public MainRight() {
        this.clients = new ArrayList<>();
        this.content_notifies = new ArrayList<>();
        initComponents();
        setOpaque(false);
    }

    public ArrayList<Client> getClients() {
        return clients;
    }

    public void setClients(ArrayList<Client> clients) {
        this.clients = clients;
        for (Client client : this.clients) {
            client.addObserver(this);
        }
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs;
        g2.setPaint(new Color(128, 126, 122));
        g2.fillRect(0, 0, getWidth(), getHeight());

        super.paintComponent(grphcs);
    }

    private void initComponents() {
        this.setLayout(new BorderLayout());

        label = new JLabel("Thông báo!", SwingConstants.CENTER);
        label.setFont(new Font("Dialog", Font.BOLD, 20));
        label.setPreferredSize(new Dimension(getWidth(), 100));
        label.setForeground(Color.WHITE);
        notify = new JTextArea();
        notify.setLineWrap(true);
        notify.setEditable(false);
        view_all = new JButton("Xem tất cả thông báo");
        view_all.setBackground(new Color(0, 255, 78));
        view_all.setPreferredSize(new Dimension(getWidth(), 100));
        view_all.addActionListener(this);

        add(label, BorderLayout.PAGE_START);
        add(notify, BorderLayout.CENTER);
        add(view_all, BorderLayout.PAGE_END);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == view_all) {
            this.notify.setText(String.join("\n", this.content_notifies));
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        Client changed_client = (Client) o;
        if (arg.toString().equals("true")) {
            this.notify.setText(String.join("\n", changed_client.getNotifies()));
        } else {
            this.content_notifies.add((String) arg);

            this.notify.setText(String.join("\n", this.content_notifies));
        }
    }
}
