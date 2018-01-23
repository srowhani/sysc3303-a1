package main.java;

import main.java.net.IntermediateHost;

import java.net.SocketException;
import java.net.UnknownHostException;

import static main.java.config.Properties.INTERMEDIATE_HOST_ADDRESS;
import static main.java.config.Properties.INTERMEDIATE_HOST_PORT;

public class RunIntermediateHostInstance {
    public static void main(String[] args) {
        IntermediateHost intermediateHostInstance = null;
        try {
            intermediateHostInstance = new IntermediateHost();
            System.out.println("Intermediate Host instantiated");
            System.out.println(String.format("Listening %s:%d", INTERMEDIATE_HOST_ADDRESS, INTERMEDIATE_HOST_PORT));
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }
}
