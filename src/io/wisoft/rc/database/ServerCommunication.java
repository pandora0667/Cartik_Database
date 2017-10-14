package io.wisoft.rc.database;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class ServerCommunication {
  private static final Charset charset = Charset.forName("UTF-8");
  private static final int BUFFER_SIZE = 1000;

  public void receive() {
    Runnable runnable = this::threadReceive;
    new Thread(runnable).start();
  }

  private void threadReceive() {
    try {
      //noinspection InfiniteLoopStatement
      while(true) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(BUFFER_SIZE);
        int isClose = Connect.getSocketChannel().read(byteBuffer);
        if (isClose == -1) {
          throw new IOException();
        }

        byteBuffer.flip();
        String msg = charset.decode(byteBuffer).toString();
        System.out.println(msg);
      }
    } catch (IOException e) {
      System.out.println("Data receive Error : " + e);
      Connect.closeSocketChannel();
    } catch (NullPointerException e) {
      System.out.println("Unknown Error : " + e);
    }
  }

  public static void send(final String msg) {
    try {
      ByteBuffer byteBuffer = charset.encode(msg);
      Connect.getSocketChannel().write(byteBuffer);
    } catch (IOException e) {
      System.out.println("Data transfer failed");
      System.out.println("Error Code : " + e);
      System.out.println("Connection to the server is terminated and reconnected");
      Connect.server();
    }
  }
}
