package com.cloudacademy.banking.unsafe;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

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
}