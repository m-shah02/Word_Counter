package Word_Counter;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UdpServerRun {
	public static void main(String[] args) throws IOException {
		
		System.out.println("Starting server...");

		DatagramSocket socket = new DatagramSocket(17777);
		byte[] bufRecv = new byte[256];
		DatagramPacket packet = new DatagramPacket(bufRecv, bufRecv.length);
		
		while(true) {
			socket.receive(packet);
			System.out.println("Got packet...");
			
			//handle packet
			MultithreadUdpServer srv = new MultithreadUdpServer(socket, packet);
			srv.start();
		}
	}
}