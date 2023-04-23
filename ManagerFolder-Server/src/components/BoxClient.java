package components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import Models.Client;
import Service.ObserveFolder;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class BoxClient extends JButton implements ActionListener, MouseListener {

    Client client;
    Boolean is_active;
    JPopupMenu menu;
    JButton option_1, option_2, option_3, option_4, option_5;
    Thread thread_observe_folder;
    ObserveFolder observer_folder;

    public BoxClient(Client client) {
        this.client = client;
        is_active = false;
        initComponents();
        setOpaque(true);
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs;
        g2.setPaint(new Color(143, 151, 164));
        g2.fillRect(0, 0, getWidth(), getHeight());

        super.paintComponent(grphcs);
    }

    private void initComponents() {
        this.setText(client.getName());

        if (client.getState()) {
            this.setBackground(new Color(37, 205, 68));
        } else {
            this.setBackground(new Color(143, 151, 164));
        }
        
        this.is_active = client.getState();
        this.setForeground(Color.WHITE);

        option_1 = new JButton("Thiêt lập quan sát");
        option_1.setBackground(Color.WHITE);
        option_1.setBorder(new EmptyBorder(5, 5, 5, 5));
        option_1.addActionListener(this);
        option_1.addMouseListener(this);

        option_2 = new JButton("Xem thông báo");
        option_2.setBackground(Color.WHITE);
        option_2.setBorder(new EmptyBorder(5, 5, 5, 5));
        option_2.addActionListener(this);
        option_2.addMouseListener(this);

        option_3 = new JButton("Ngừng quan sát");
        option_3.setBackground(Color.WHITE);
        option_3.setBorder(new EmptyBorder(5, 5, 5, 5));
        option_3.addActionListener(this);
        option_3.addMouseListener(this);

        option_4 = new JButton("Đổi thư mục quan sát");
        option_4.setBackground(Color.WHITE);
        option_4.setBorder(new EmptyBorder(5, 5, 5, 5));
        option_4.addActionListener(this);
        option_4.addMouseListener(this);

        option_5 = new JButton("Đổi tên");
        option_5.setBackground(Color.WHITE);
        option_5.setBorder(new EmptyBorder(5, 5, 5, 5));
        option_5.addActionListener(this);
        option_5.addMouseListener(this);

        addActionListener(this);
        this.addMouseListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this) {
            if (!is_active) {
                setBackground(new Color(37, 205, 68));
            } else {
                setBackground(new Color(143, 151, 164));
            }

            is_active = !is_active;
            client.setState(is_active);
        }
        
        if(e.getSource() == option_2) {
            client.setIs_view(true);
        }

        if (e.getSource() == option_1) {
            JFileChooser file_chooser = new JFileChooser(new File(client.getObserve_dir()));
            file_chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int return_val = file_chooser.showOpenDialog(this);
            if (return_val == JFileChooser.APPROVE_OPTION) {
                String dir = file_chooser.getSelectedFile().getAbsolutePath();

                try {
                    // thiết lập quan sát
                    observer_folder = new ObserveFolder(dir, client);
                    thread_observe_folder = new Thread(observer_folder);
                    thread_observe_folder.start();

                    // đánh dấu là đang quan sát cho client
                    setBackground(new Color(37, 205, 68));
                    repaint();
                    is_active = true;
                    client.setState(is_active);
                } catch (IOException ex) {
                    Logger.getLogger(BoxClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        if (e.getSource() == option_4) {
            observer_folder.stop();

            JFileChooser file_chooser = new JFileChooser(new File(client.getObserve_dir()));
            file_chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int return_val = file_chooser.showOpenDialog(this);
            if (return_val == JFileChooser.APPROVE_OPTION) {
                String dir = file_chooser.getSelectedFile().getAbsolutePath();

                try {
                    // thiết lập quan sát
                    observer_folder = new ObserveFolder(dir, client);
                    thread_observe_folder = new Thread(observer_folder);
                    thread_observe_folder.start();

                    // đánh dấu là đang quan sát cho client
                    setBackground(new Color(37, 205, 68));
                    repaint();
                    is_active = true;
                    client.setState(is_active);
                } catch (IOException ex) {
                    Logger.getLogger(BoxClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        if (e.getSource() == option_3) {
            // ngừng quan sát
            observer_folder.stop();

            // đánh dầy là ngừng quan sát cho client
            setBackground(new Color(143, 151, 164));
            repaint();
            is_active = false;
            client.setState(is_active);
        }

        if (e.getSource() == option_5) {
            String name = JOptionPane.showInputDialog("Nhập tên ...", this.getText());
            client.setName(name);

            this.setText(name);
            repaint();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            if (is_active) {
                menu = new JPopupMenu();
                menu.setLayout(new GridLayout(4, 1));
                menu.setOpaque(true);
                menu.setBackground(Color.BLACK);
                menu.add(option_2);
                menu.add(option_4);
                menu.add(option_5);
                menu.add(option_3);
            } else {
                menu = new JPopupMenu();
                menu.setLayout(new GridLayout(2, 1));
                menu.setOpaque(true);
                menu.setBackground(Color.BLACK);
                menu.add(option_1);
                menu.add(option_5);
            }

            menu.show(this, e.getX(), e.getY());
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (e.getSource() == option_1) {
            option_1.setBackground(Color.CYAN);
        }

        if (e.getSource() == option_2) {
            option_2.setBackground(Color.CYAN);
        }

        if (e.getSource() == option_3) {
            option_3.setBackground(Color.CYAN);
        }

        if (e.getSource() == option_4) {
            option_4.setBackground(Color.CYAN);
        }

        if (e.getSource() == option_5) {
            option_5.setBackground(Color.CYAN);
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (e.getSource() == option_1) {
            option_1.setBackground(Color.WHITE);
        }

        if (e.getSource() == option_2) {
            option_2.setBackground(Color.WHITE);
        }

        if (e.getSource() == option_3) {
            option_3.setBackground(Color.WHITE);
        }

        if (e.getSource() == option_4) {
            option_4.setBackground(Color.WHITE);
        }

        if (e.getSource() == option_5) {
            option_5.setBackground(Color.WHITE);
        }
    }
}
