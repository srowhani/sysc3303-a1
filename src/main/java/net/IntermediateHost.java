package main.java.net;

import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.function.Function;

import static main.java.config.Properties.*;

public class IntermediateHost {
    private Timer timer;
    private DatagramSocket receiveDatagramSocket;
    private DatagramSocket responseSocket;

    public IntermediateHost () throws SocketException, UnknownHostException{
        // Create a datagram socket to use to receive
        receiveDatagramSocket = new DatagramSocket(
                INTERMEDIATE_HOST_PORT, InetAddress.getByName(INTERMEDIATE_HOST_ADDRESS));
        // Create a datagram socket to talk with server
        responseSocket = new DatagramSocket();
        // repeat the following forever
        timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    receive();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }, 10, 10);
    }

    public void receive () throws IOException, NullPointerException {
        byte[] buffer = new byte[BUFFER_SIZE];
        DatagramPacket clientDatagramPacket = new DatagramPacket(buffer, buffer.length);
        // The host waits to receive a request
        receiveDatagramSocket.receive(clientDatagramPacket);

        byte[] received = clientDatagramPacket.getData();

        if (received == null) {
            throw new NullPointerException();
        }
        // The host prints out the information it has received
        print(received);
        // the host forms a packet to send containing exactly what it received
        DatagramPacket serverDatagramPacket = new DatagramPacket(received, received.length, InetAddress.getByName(SERVER_ADDRESS), SERVER_PORT);

        // the host prints out this information
        System.out.println("serverDatagramPacket.data" + Arrays.toString(serverDatagramPacket.getData()));
        System.out.println("serverDatagramPacket.length" + serverDatagramPacket.getLength());
        System.out.println("serverDatagramPacket.address" + serverDatagramPacket.getAddress());
        System.out.println("serverDatagramPacket.port" + serverDatagramPacket.getPort());
        // the host sends this packet to server
        responseSocket.send(serverDatagramPacket);

        // now begins awaiting response (note requires java8)
        awaitResponse(response -> {
            try {
                forwardResponseToClient(
                        clientDatagramPacket.getAddress(), clientDatagramPacket.getPort(), response);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    private void awaitResponse(Function<byte[], Void> onComplete) throws IOException {
        byte[] responseBuffer = new byte[BUFFER_SIZE];
        DatagramPacket datagramPacket = new DatagramPacket(responseBuffer, responseBuffer.length);

        // awaits response from server
        responseSocket.receive(datagramPacket);

        // prints data gram info post receive
        System.out.println("datagramPacket.data" + Arrays.toString(datagramPacket.getData()));
        System.out.println("datagramPacket.length" + datagramPacket.getLength());
        System.out.println("datagramPacket.address" + datagramPacket.getAddress());
        System.out.println("datagramPacket.port" + datagramPacket.getPort());

        byte[] received = datagramPacket.getData();

        if (received == null) {
            throw new NullPointerException();
        }
        // give cute little callback
        onComplete.apply(received);
    }

    private void forwardResponseToClient(InetAddress client, int port, byte[] received) throws IOException {
        DatagramPacket returnToSender = new DatagramPacket(received, received.length, client, port);
        responseSocket.send(returnToSender);
    }

    private List<Byte> dump(byte[] content) {
        List<Byte> byteList = new ArrayList<>();
        for (byte b : content) {
            byteList.add(b);
        }
        return byteList;
    }

    private void print (byte[] content) {
        int index = 2;
        List<Byte> received = dump(content);
        String signature = Arrays.toString(received.subList(0, 2).toArray());
        String filename = "";
        do {
            filename += (char) content[index];
        } while (content[++index] != 0);

        index += 1;

        String mode = "";
        do {
            mode += (char) content[index];
        } while (content[++index] != 0);

        System.out.println("sig: " + signature);
        System.out.println("filename: " + filename);
        System.out.println("mode: " + mode);
        System.out.println("raw: " + Arrays.toString(content));
    }
}
