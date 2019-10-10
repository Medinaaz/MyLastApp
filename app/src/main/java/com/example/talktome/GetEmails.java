package com.example.talktome;

import android.widget.Toast;

import java.io.*;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class GetEmails {

    public GetEmails() {}
    Message[] messages;
    //
    // inspired by :
    // http://www.mikedesjardins.net/content/2008/03/using-javamail-to-read-and-extract/
    //

    public Message[] doit() throws MessagingException, IOException {
        Folder folder = null;
        try {
            //create properties field
            Properties properties = new Properties();

            properties.put("mail.pop3.host", "smtp.gmail.com");
            properties.put("mail.pop3.port", "587");
            properties.put("mail.pop3.starttls.enable", "true");
            Session emailSession = Session.getDefaultInstance(properties);

            //create the POP3 store object and connect with the pop server
            final Store store = emailSession.getStore("pop3s");

            new Thread() {
                @Override
                public void run() {
                    try {
                        store.connect("pop.gmail.com","yakuphanbilgic@gmail.com", "kosvkeonriszprry");

                        while(!store.isConnected()){

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();

            //create the folder object and open it
            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);

            // retrieve the messages from the folder in an array and print it
            messages = emailFolder.getMessages();

            store.close();
            return messages;
        }
        finally {
            if (folder != null) { folder.close(true); }
        }
    }

    public static void main(String args[]) throws Exception {
    }
}