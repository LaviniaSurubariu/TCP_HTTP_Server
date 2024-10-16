package eu.deic.ie;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPHTTPServer {
    public static void main(String[] args) {
        ServerSocket servSock = null;
        try {
            int port = Integer.parseInt(args[0]);
            servSock = new ServerSocket(port);
            System.out.println("Java Web Server DICE is listening in port: " + port);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        while (true) {

            OutputStream os = null;
            PrintWriter out = null;
            InputStream is = null;
            BufferedReader in = null;

            try {

                Socket connClientObj = servSock.accept();

                is = connClientObj.getInputStream();
                in = new BufferedReader(new InputStreamReader(is));
                os = connClientObj.getOutputStream();
                out = new PrintWriter(os, true);

                String inputLine = "";
                String outputLine = "";
                StringBuffer procLine = new StringBuffer();

                while (((inputLine = in.readLine()) != null) && (inputLine.length() > 1)) {
                    procLine.append(inputLine + "\r\n");
                } //end while HTTP 1.1
                System.out.println("Client: " + connClientObj.toString() + " -> \n" + procLine.toString());

                HTTPSeminarProtocol objHTTPparser = new HTTPSeminarProtocol();
                outputLine = objHTTPparser.processInput(procLine.toString());
                out.println(outputLine);

            } catch (IOException ioe) {
                ioe.printStackTrace();
            } finally {
                if (out != null) {
                    out.close();
                }
            }
        }
//
//        try {
//            if (servSock != null) {
//                servSock.close();
//            }
//        } catch (IOException ioe) {
//            ioe.printStackTrace();
//        }
    }
}

class HTTPSeminarProtocol {

    public String processInput(String theInput) {
        String theOutput = "";

        byte[] buffResp = new byte[4096];
        if (theInput.indexOf("GET") != 0) {
            theOutput = "HTTP/1.1 200 OK\r\nContent-Length: 19\r\nNU STIU COMANDA\r\n\r\n";
        } else {
            String fileName = theInput.substring(theInput.indexOf("/") + 1, theInput.indexOf("HTTP/"));
            String fileExt = fileName.substring(fileName.indexOf(".") + 1);
            String contentType = "";
            String fileContent = "";

            if (fileExt.compareToIgnoreCase("txt") == 0) contentType = "text/html";
            if (fileExt.compareToIgnoreCase("html") == 0) contentType = "text/html";
            if (fileExt.compareToIgnoreCase("htm") == 0) contentType = "text/html";
            if (fileExt.compareToIgnoreCase("gif") == 0) contentType = "image/gif";

            if (fileExt.compareToIgnoreCase("class") == 0) {
                contentType = "text/html";
                //RUN java class in REFLECTION mode => "SERVLET CONTAINER"
            } else {
                try {
                    int bread = 0;
                    FileInputStream fis = new FileInputStream(fileName);
                    while ((bread = fis.read(buffResp)) != -1) {
                        fileContent += new String(buffResp, 0, bread);
                    }
                    fis.close();
                    theOutput = "HTTP/1.1 200 OK \r\nContent-Type:" + contentType + "\r\nContent-Length:"+(fileContent.length()+2) + "\r\n\r\n" + fileContent + "\r\n";

                } catch (IOException ioec) {
                    ioec.printStackTrace();
                    theOutput = "HTTP/1.1 404 \r\n\r\n";
                }
            }
        }

        return theOutput;
    }
}