package main.java.net;

import main.java.net.error.MalformedRequestException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

public abstract class AbstractRequest {
    private RequestMode requestMode;
    private String filename;

    protected enum RequestType {
        READ,
        WRITE,
        INVALID
    }

    public enum RequestMode {
        ASCII,
        OCTET
    }

    public AbstractRequest mode (RequestMode m) {
        this.requestMode = m;
        return this;
    }
    public AbstractRequest filename (String filename) {
        this.filename = filename;
        return this;
    }

    public RequestMode getMode() {
        return requestMode;
    }

    public String getFilename() {
        return filename;
    }

    public abstract RequestType getType();

    public abstract byte[] getSignature();

    public byte[] getBody() throws IOException, MalformedRequestException {
        if (filename == null || requestMode == null) {
            throw new MalformedRequestException();
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        byteArrayOutputStream.write(getSignature());
        byteArrayOutputStream.write(getFilename().getBytes());
        byteArrayOutputStream.write(0);
        byteArrayOutputStream.write(getMode().name().getBytes());
        byteArrayOutputStream.write(0);

        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public String toString () {
        return "{%s, %s, %d, %s, %d}".format(
            Arrays.toString(getSignature()),
            filename,
            0,
            getMode().name(),
            0
        );
    }
}
