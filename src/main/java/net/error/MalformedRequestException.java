package main.java.net.error;

public class MalformedRequestException extends Exception {
    @Override
    public String getMessage () {
        return "Request not properly instantiated";
    }
}
