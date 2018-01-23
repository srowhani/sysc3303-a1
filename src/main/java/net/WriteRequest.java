package main.java.net;


public class WriteRequest extends AbstractRequest {

    public WriteRequest(String filename) {
        mode(RequestMode.ASCII);
        filename(filename);
    }

    @Override
    public RequestType getType() {
        return RequestType.WRITE;
    }

    @Override
    public byte[] getSignature() {
        return new byte[]{ 0, 2 };
    }
}
