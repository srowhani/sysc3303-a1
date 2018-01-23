package main.java.net;

import main.java.net.error.InvalidRequestTypeException;
import main.java.net.error.MalformedRequestException;

import java.io.IOException;
import java.net.*;
import java.util.*;

import static main.java.config.Properties.BUFFER_SIZE;
import static main.java.config.Properties.SERVER_ADDRESS;
import static main.java.config.Properties.SERVER_PORT;

public class Server {
    Timer timer;
    private DatagramSocket datagramSocket;
    public Server () throws UnknownHostException, SocketException {
        // server creates a datagram socket to use to receive
        datagramSocket = new DatagramSocket(SERVER_PORT, InetAddress.getByName(SERVER_ADDRESS));
        // repeat the following forever
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    listen();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 10, 10);
    }

    private void listen() throws IOException {
        // alloc buffer for listen
        byte[] buffer = new byte[BUFFER_SIZE];
        DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);

        datagramSocket.receive(datagramPacket);

        byte[] received = datagramPacket.getData();

        if (received == null) {
            throw new NullPointerException();
        }

        AbstractRequest.RequestType requestType = processInput(received);
        byte[] response = new byte[]{ 0 };
        if (requestType == AbstractRequest.RequestType.READ) {
            // if packet is valid read req send back 0 3 0 1
            response = new byte[]{ 0, 3, 0, 1 };
        } else if (requestType == AbstractRequest.RequestType.WRITE) {
            // if packet is valid write req send back 0 4 0 0
            response = new byte[]{ 0, 4, 0, 0 };
        } 

        DatagramPacket responsePacket = new DatagramPacket(
                response, response.length, datagramPacket.getAddress(), datagramPacket.getPort());

        // server prints out response packet info
        System.out.println("responsePacket.address" + responsePacket.getAddress());
        System.out.println("responsePacket.port" + responsePacket.getPort());
        System.out.println("responsePacket.data" + Arrays.toString(responsePacket.getData()));

        // creates a datagram socket just for this response
        DatagramSocket temporaryResponseSocket = new DatagramSocket();
        // sends via the new socket to the port it received from request form
        temporaryResponseSocket.send(responsePacket);
        // closes new socket it just created
        temporaryResponseSocket.close();
    }

    private AbstractRequest.RequestType processInput(byte[] received) {
        try {
            validate(received);
            AbstractRequest.RequestType requestType = parseRequestType(received);
            return requestType;
        } catch (InvalidRequestTypeException e) {
            e.printStackTrace();
        } catch (MalformedRequestException e) {
            e.printStackTrace();
            timer.cancel();
            timer = null;
            datagramSocket.close();
        }
        return null;
    }

    private void validate(byte[] received) throws MalformedRequestException {
        int index = 2;
        List<Byte> byteList = new ArrayList<>();
        for (byte b : received) {
            byteList.add(b);
        }

        String signature = Arrays.toString(byteList.subList(0, 2).toArray());
        System.out.println(signature);
        if (!signature.equals("[0, 1]") && !signature.equals("[0, 2]")) {
            throw new MalformedRequestException();
        }

        String filename = "";
        do {
            filename += (char) received[index];
        } while (received[++index] != 0);


        index += 1;

        String mode = "";
        do {
            mode += (char) received[index];
        } while (received[++index] != 0);
        // the case where these two are empty is where they havent been zero padded
        // therefore not correct format so non valid
        if (filename == "" || mode == "") {
            throw new MalformedRequestException();
        }
        System.out.println("parsed as valid");
        System.out.println("sig: " + signature);
        System.out.println("filename" + filename);
        System.out.println("mode" + mode);
        System.out.println("raw bytes: " + Arrays.toString(received));
    }

    private AbstractRequest.RequestType parseRequestType(byte[] received) throws InvalidRequestTypeException {
        String in = received[0] + "" +  received[1];
        System.out.println(in);
        if (in.equals("01")) {
            return AbstractRequest.RequestType.READ;
        } else if (in.equals("02")) {
            return AbstractRequest.RequestType.WRITE;
        } else {
            throw new InvalidRequestTypeException();
        }
    }


}
