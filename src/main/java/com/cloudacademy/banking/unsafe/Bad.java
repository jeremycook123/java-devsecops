package com.cloudacademy.banking.unsafe;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.io.File;
import java.security.KeyPairGenerator;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Bad {
    static final Logger logger = LogManager.getLogger(Bad.class);

    public int field;

    Bad(int field) {
      this.field = field;
    }

    public Bad deserialize(Socket sock) {
        // unsafe deserialization
        try(ObjectInputStream in = new ObjectInputStream(sock.getInputStream())) {
            return (Bad)in.readObject(); // unsafe
        }
        catch(IOException | ClassNotFoundException ex) {
            return null;
        }
    }

    public void connect() throws UnknownHostException, IOException {
        // hardcoded IP address
        String ip = "192.168.12.42";
        Socket socket = new Socket(ip, 6667);
    }

    public void files() {
        // read write to publicly accessible file
        File file1 = new File("/tmp/sensitive.txt");
    }

    public void cryptoKey() throws NoSuchAlgorithmException {
        // weak crypto RSA 1024
        KeyPairGenerator keyPairGen1 = KeyPairGenerator.getInstance("RSA");
        keyPairGen1.initialize(1024);
    }

    public void doSomethingAndLog(String message) {
        // uses log4j-core 2.13.0: CVE-2021-44228
        try {
            throw new Exception("boom!!");
        } catch (Exception e) {
            logger.error("Failed to do something" + e.getMessage());
        }
    }

    public String generateSecretToken() {
        // predictable pseudorandom number generator
        Random r = new Random();
        return Long.toHexString(r.nextLong());
    }
}