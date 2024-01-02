import java.rmi.Remote;

public interface IServer extends Remote {
    public void echoClientMessage(String message);
}
