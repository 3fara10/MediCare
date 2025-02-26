package org.example.domain;

import java.io.*;

public class IdGenerator {
    private static IdGenerator instance ;
    private final String fileName;
    private int lastId;

    private IdGenerator(String fileName) {
        this.fileName = fileName;
        this.lastId=readLastId();
    }
    public static IdGenerator getInstance(String fileName) {
        //happy flow :)
        //handle it in weekend
        if (instance == null) {
            instance = new IdGenerator(fileName);
        }
        return instance;
    }

    private int readLastId() {
        int id=100;

        try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line=br.readLine();
            if(line!=null) {
                id=Integer.parseInt(line);
            }
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
        return id;
    }

    public int generateID(){
        lastId++;

        try(BufferedWriter bw=new BufferedWriter(new FileWriter(fileName))){
            bw.write(Integer.toString(lastId));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return lastId;
    }
}

