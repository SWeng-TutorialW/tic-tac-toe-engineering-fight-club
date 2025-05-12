package il.cshaifasweng.OCSFMediatorExample.server;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App 
{
	
	private static SimpleServer server;
    public static void main( String[] args ) throws IOException
    {
        System.out.println("Server started and listening for connections...");
        server = new SimpleServer(3025);
        server.listen();
    }
}
