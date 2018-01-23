package main.java;

import main.java.net.Server;

import java.net.SocketException;
import java.net.UnknownHostException;

import static main.java.config.Properties.SERVER_ADDRESS;
import static main.java.config.Properties.SERVER_PORT;

public class RunServerInstance {
    public static void main(String[] args) {
        try {
            Server serverInstance = new Server();
            System.out.println(String.format("Listening @%s:%d", SERVER_ADDRESS, SERVER_PORT));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
}
