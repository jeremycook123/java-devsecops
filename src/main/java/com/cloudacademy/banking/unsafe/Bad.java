package com.cloudacademy.banking.unsafe;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Bad {
    public int field;

    Bad(int field) {
      this.field = field;
    }

    public Bad deserialize(Socket sock) {
        try(ObjectInputStream in = new ObjectInputStream(sock.getInputStream())) {
            return (Bad)in.readObject(); // unsafe
        }
        catch(IOException | ClassNotFoundException ex) {
            return null;
        }
    }

    public void connect() {
        String ip = "192.168.12.42";
        Socket socket = new Socket(ip, 6667);
    }

    public void files() {
        File file1 = new File("/tmp/sensitive.txt");
        Path path = Paths.get("/tmp/sensitive.txt");
    }
}