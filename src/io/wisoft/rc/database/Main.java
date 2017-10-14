package io.wisoft.rc.database;


public class Main {
  public static void main(String[] args) {
    System.out.println("Hello !! RC festival database server");
    System.out.println("                                    - Version 1.0 -");
    System.out.println("Try connecting database ...");
    Connect.database();
    System.out.println("Try connecting main server ...");
    Connect.server();
    ServerCommunication serverCommunication = new ServerCommunication();
    serverCommunication.receive();

    while (true) {
      try {
        Thread.sleep(3000);
        ServerCommunication.send("Hello Server!!");
        System.out.println("Sending...");
      } catch (InterruptedException ignore){}
    }
  }
}
