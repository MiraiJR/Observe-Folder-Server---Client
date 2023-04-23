package Models;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;

public class Client extends Observable {

    Socket socket;
    Boolean state;
    String observe_dir;
    String name;
    ArrayList<String> notifies;
    Boolean is_view;

    public Client() {
        this.state = false;
        this.notifies = new ArrayList<>();
        this.is_view = false;
    }

    public Client(Socket socket) {
        this.socket = socket;
        this.state = false;
        this.notifies = new ArrayList<>();
        this.is_view = false;
    }

    public Client(Socket socket, String observe_dir, String name) {
        this.socket = socket;
        this.state = false;
        this.observe_dir = observe_dir;
        this.name = name;
        this.notifies = new ArrayList<>();
        this.is_view = false;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public String getObserve_dir() {
        return observe_dir;
    }

    public void setObserve_dir(String observe_dir) {
        this.observe_dir = observe_dir;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getNotifies() {
        return notifies;
    }

    public void setNotifies(ArrayList<String> notifies) {
        this.notifies = notifies;
    }

    public void addNotify(String notify) {
        this.notifies.add(notify);
        setChanged();
        notifyObservers(notify);
    }

    public Boolean getIs_view() {
        return is_view;
    }

    public void setIs_view(Boolean is_view) {
        this.is_view = is_view;
        setChanged();
        notifyObservers(is_view);
    }
}
