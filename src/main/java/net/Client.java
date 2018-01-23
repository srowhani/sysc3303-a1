package main.java.net;

import main.java.net.AbstractRequest;
import main.java.net.error.MalformedRequestException;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;

import static main.java.config.Properties.BUFFER_SIZE;
import static main.java.config.Properties.INTERMEDIATE_HOST_ADDRESS;
import static main.java.config.Properties.INTERMEDIATE_HOST_PORT;

public class Client {
    private DatagramSocket datagramSocket;

    public Client () throws SocketException {
        datagramSocket = new DatagramSocket();
    }

    public void send (AbstractRequest request) throws IOException, MalformedRequestException, NullPointerException {
        byte[] content = request.getBody();
        // Client prints out the information it has put in the packet
        System.out.println(request);
        System.out.println(Arrays.toString(content));
        // Client sends the packet to a well-known port
        DatagramPacket datagramPacket = new DatagramPacket(
                content, content.length, InetAddress.getByName(INTERMEDIATE_HOST_ADDRESS), INTERMEDIATE_HOST_PORT);
        datagramSocket.send(datagramPacket);
        // The client waits on its datagram socket
        awaitMessage();
    }

    private byte[] awaitMessage () throws IOException, NullPointerException {
        // Allocate buffer to read in from datagramSocket
        byte[] buffer = new byte[BUFFER_SIZE];
        DatagramPacket datagramPacket = new DatagramPacket(buffer, BUFFER_SIZE);

        datagramSocket.receive(datagramPacket);

        byte[] response = datagramPacket.getData();

        if (response == null) {
            throw new NullPointerException();
        }
        // When it receives datagram packet, it prints out the byte array received
        System.out.println("response: " + Arrays.toString(response));
        return response;
    }
}
