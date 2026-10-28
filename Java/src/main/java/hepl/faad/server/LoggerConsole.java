package hepl.faad.server;

public class LoggerConsole implements Logger{
    @Override
    public void Trace(String message)
    {
        System.out.println(message);
    }
}
