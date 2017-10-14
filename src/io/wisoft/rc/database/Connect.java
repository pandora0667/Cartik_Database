package io.wisoft.rc.database;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

import static io.wisoft.rc.database.ServerCommunication.send;

public class Connect {
  // TODO DATABASE, Main SERVER Connect
  private static Connection connection = null;
  private static File inFile = new File("/home/jusk2/Development/JAVA/RC_Database", "DatabaseAccount.txt");
  private static HashMap<String, String> account = new HashMap<>();
  private static SocketChannel socketChannel = null;
  private static final String IP = "192.168.1.33";
  private static final int PORT = 5001;

  private static void readDatabaseAccount() {
    try(BufferedReader buffer = new BufferedReader(new FileReader(inFile))) {
      String line;
      while ((line = buffer.readLine()) != null ) {
        int idx = line.indexOf("-");
        String key = line.substring(0, idx);
        String value = line.substring(idx+1);
        account.put(key, value);
      }
    } catch (FileNotFoundException e) {
      System.out.println("Database account file not found!! ");
    } catch (IOException e) {
      System.out.println("IO Exception!!");
    }
  }

  public static Connection database() {
    if (account.isEmpty()) {
      readDatabaseAccount();
    }

    try {
      connection = DriverManager.getConnection(account.get("url"), account.get("username"), account.get("password"));
      System.out.println("-- DATABASE ACCESS --");
    } catch (SQLException e) {
      System.out.println("DATABASE CONNECTION FAIL");
      System.out.println("ERROR : " + e);
    }

    return connection;
  }

  public static void server() {
    try {
      socketChannel = SocketChannel.open();
      socketChannel.configureBlocking(true);
      socketChannel.connect(new InetSocketAddress(IP, PORT));
      System.out.println("-- SERVER ACCESS --");
      send("Database");
    } catch (IOException e) {
      System.out.println("Main server Connect ERROR!!");
      System.out.println("Error Code : " + e);
    }
  }

  public static SocketChannel getSocketChannel() {
    return socketChannel;
  }

  public static void closeSocketChannel() {
    try {
      socketChannel.close();
    } catch (IOException ignore){}
  }

//    public void init() {
//    try {
//      Class.forName("org.postgresql.Driver");
//    } catch (ClassNotFoundException e) {
//      e.printStackTrace();
//    }
//  }
}
