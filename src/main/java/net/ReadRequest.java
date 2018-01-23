package main.java.net;

public class ReadRequest extends AbstractRequest {

    public ReadRequest(String filename) {
       mode(RequestMode.ASCII);
       filename(filename);
    }

    @Override
    public RequestType getType() {
        return RequestType.READ;
    }

    @Override
    public byte[] getSignature () {
        return new byte[]{ 0, 1 };
    }
}
