package test;

import java.io.*;
import java.net.*;

import javax.net.ssl.*;

public class Test {
    public static void main( String[] args ) throws IOException {
         
    	int portNumber = 17777;
    	String html = "<p id='out'></p><h1>not today</h1>"
    			+ "<script>const now = new Date();" 	// soem JavaScript
    			+ "const currentDateTime = now.toLocaleString(); "
    			+ "document.getElementById(\"out\").innerHTML = currentDateTime;"
    			+ "</script>"
    			+ "<img src='https://media.tenor.com/giB0jG3Ofu4AAAAC/cat-dancing-led-light-rainbow.gif'</img>";
    	
    	//System.setProperty("javax.net.debug", "all");		//for debugging

        //tell the execution environment where the keystore (and the certificate) is
        System.setProperty( "javax.net.ssl.keyStore", "C:\\snslab\\wk09.jks" );
        System.setProperty( "javax.net.ssl.keyStorePassword", "abcd" );

        SSLServerSocketFactory ssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        SSLServerSocket ss = (SSLServerSocket) ssf.createServerSocket( portNumber );
        
        ss.setEnabledProtocols( new String[]{"TLSv1.3", "TLSv1.2"} );
        
    	Socket s = ss.accept();

		BufferedReader in = new BufferedReader( new InputStreamReader( s.getInputStream() ) );
		PrintWriter out = new PrintWriter( s.getOutputStream(), true );	// outgoing
		
        //wait for the browser's HTTP request
        String line;
        while ( true ) {
        	line = in.readLine();
            if (line.isEmpty()) break;	//break when an empty line is sent
        	System.out.println( "recv: " + line );
        }
        
        //send the HTTP response (rather blidnly -- this response is sent no matter what comes from the client)
        System.out.println( "sending HTTP response:" );
        String response = "HTTP/1.1 200 OK\n"
        		+ "Content-Length: " + html.length() + "\n"
        		+ "\n"	// add an empty line as per HTTP specs
        		+ html;
        out.println( response );
}}



