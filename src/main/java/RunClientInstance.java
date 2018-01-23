package main.java;

import main.java.net.AbstractRequest;
import main.java.net.Client;
import main.java.net.ReadRequest;
import main.java.net.WriteRequest;
import main.java.net.error.MalformedRequestException;

import java.io.IOException;
import java.net.SocketException;

public class RunClientInstance {
    public static void main(String[] args) {
        try {
            Client clientInstance = new Client();
            System.out.println("Client Instantiated");
            // alternate 5 read / write requests
            clientInstance.send(new ReadRequest("test1.txt"));
            clientInstance.send(new WriteRequest("eg1.txt"));
            clientInstance.send(new ReadRequest("test2.txt"));
            clientInstance.send(new WriteRequest("eg2.txt"));
            clientInstance.send(new ReadRequest("test3.txt"));
            clientInstance.send(new WriteRequest("eg3.txt"));
            clientInstance.send(new ReadRequest("test4.txt"));
            clientInstance.send(new WriteRequest("eg4.txt"));
            clientInstance.send(new ReadRequest("test5.txt"));
            clientInstance.send(new WriteRequest("eg5.txt"));

            // 1 invalid req
            clientInstance.send(new AbstractRequest() {
                @Override
                public AbstractRequest mode(AbstractRequest.RequestMode m) {
                    return super.mode(m);
                }

                @Override
                public AbstractRequest.RequestType getType() {
                    return RequestType.INVALID;
                }

                @Override
                public byte[] getSignature() {
                    return new byte[] {9, 9}; // invalid sig
                }
            }.mode(AbstractRequest.RequestMode.ASCII).filename("invalid.txt"));

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MalformedRequestException e) {
            e.printStackTrace();
        }
    }
}
