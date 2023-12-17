package Word_Counter;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class MultithreadUdpServer extends Thread {

	DatagramSocket socket;
	DatagramPacket packet;

	public void run() {
    	System.out.println(Thread.currentThread().getName() + ": " + packet.getAddress() + ":" + packet.getPort());
        String received = new String(packet.getData());
	    System.out.println("received: " + received);
	}

	public MultithreadUdpServer(DatagramSocket s, DatagramPacket p) {
		socket = s;
		packet = p;
	}

}