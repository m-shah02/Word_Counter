package Word_Counter;

import java.io.*;
import java.net.*;

public class UdpClient {
	public static void main(String[] args) throws IOException {
		DatagramSocket socket = new DatagramSocket();
		
		// send hello
		String msg = "Hello";
		byte[] buf = msg.getBytes();	// we need raw bytes
		InetAddress address = InetAddress.getByName("127.0.0.1");
		DatagramPacket packetSend = new DatagramPacket(buf, buf.length, address, 17777);
		
		System.out.println("Press any key to send packet."); System.in.read();	// simply waits for a key
		socket.send(packetSend);
		
		// wait for response
		byte[] bufRecv = new byte[256];
		DatagramPacket packetRecv = new DatagramPacket(bufRecv, bufRecv.length);
		System.out.println("Waiting for reply");
		socket.receive(packetRecv);
		System.out.println("Got packet from " + packetRecv.getAddress() + ":" + packetRecv.getPort() + " of length " + packetRecv.getLength());
		String received = new String(packetRecv.getData());	// Convert back from byte array to string
		System.out.println("Received: " + received);
    
        socket.close();
        System.out.println("Done.");
	}
}