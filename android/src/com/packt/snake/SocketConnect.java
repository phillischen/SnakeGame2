package com.packt.snake;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class SocketConnect extends Thread {
    private static SocketConnect myConnect;
    private String ipAdress = "192.168.0.5";
    //private String ipAdress = "192.168.43.228";
    private int port = 8080;
    private DataInputStream input;
    private DataOutputStream output;
    private String username = "player";
    public boolean quit = false;

    private Socket mSocket;
    //private SnakeGame game;
    private MyAssetsManager myAm = new MyAssetsManager();
    //private Context mycontext;
    private String userlist = "", skinlist = "";
    private ArrayList<String> userarray = new ArrayList<String>();
    private ArrayList<Integer> skinarray;
    private boolean waiting = false;
   // public Intent startGameIntent;

    private SocketConnect(){
        //mycontext = context;
    }

    public static SocketConnect get(){
        if (myConnect == null){
            myConnect = new SocketConnect();
        }
        return myConnect;
    }

    public void renewSocket(){
        //quit = true;
        myConnect = new SocketConnect();
    }

    public void setAssetManager(MyAssetsManager manager){
        myAm = manager;
    }

    public void setIpAdress(String ip, int port){
        ipAdress = ip;
        this.port = port;
    }


    public int initConnect(){
        try {

                System.out.println("create a new socket");
                mSocket = new Socket(ipAdress, port);
                input = new DataInputStream(mSocket.getInputStream());
                output = new DataOutputStream(mSocket.getOutputStream());
            //mSocket = new Socket(ipAdress, port);
            //System.out.println("connected");

            System.out.println("use Current socket");
            JSONObject join = new JSONObject();
            join.put("type", "askjoin");
            join.put("username", username);
            join.put("skin",1);
            output.writeUTF(join.toString());
            String message = input.readUTF();
            System.out.println("response = "+message);
            JSONObject respond = new JSONObject(message);

            if (respond.getString("type").equals("join")){
                if (respond.getBoolean("agree")){
                    Log.d("MYTAG","initConnect"+respond.getString("usernamelist"));
                    //myAm.userlist = respond.getString("usernamelist");
                    userlist = userlist + respond.getString("usernamelist");
                    String[] x = userlist.split("\n");
                    for (String i : x){
                        userarray.add(i);
                    }
                    //userarray.add(respond.getString("usernamelist"));
                    return 1;
                } else{
                    return  respond.getInt("reason");
                    //2 - duplicate; 3 - on play
                }
            } else return 4;//other message, ignore


        } catch (IOException e) {
            Log.e("MYTAG","initConnect ERROR",e);
            return 5; //3 - connection failed
        } catch (Exception ex){
            Log.e("MYTAG","initConnect ERROR",ex);
            return 5;
        }
    }


    @Override
    public void run(){
        try {
            while (!mSocket.isClosed()) {
                if (input.available() > 0){
                    String message = input.readUTF();
                    System.out.println("thread receive msg: "+message);
                    JSONObject respond = new JSONObject(message);
                    String mtype = respond.getString("type");
                    switch (mtype){
                        case "move":
                            int[] param = myAm.userdata.get(respond.getString("username"));
                            param[3] = respond.getInt("direction");
                            myAm.userdata.put(respond.getString("username"),param);
                            break;
                        case "join":
                            userlist = userlist + respond.getString("newuser")+"\n";
                            userarray.add(respond.getString("newuser"));
                            skinlist = skinlist + respond.getInt("skin");
                            break;
                        case "ready":
                            System.out.println("===========socekt receive: ready");
                            myAm.play = true;
                            break;
                        case "quit":
                            userarray.remove(respond.getString("username"));
                            userlist = "";
                            for (String x: userarray){
                                userlist = userlist+x+"\n";
                            }
                            System.out.println("someone quit "+ userlist);
                            break;
                    }
                } if (quit){
                    //sendQuitRequest();
                    JSONObject m = new JSONObject();
                    m.put("type", "quit");
                    output.writeUTF(m.toString());
                    System.out.println("quit!!");
                    quit = false;
                    userlist = "";
                    skinlist = "";
                    break;
                } if (waiting){
                    //sendQuitRequest();
                    JSONObject m = new JSONObject();
                    m.put("type", "ready");
                    output.writeUTF(m.toString());
                    System.out.println("ready");
                    waiting = false;
                } if (myAm.newDirection){
                    JSONObject m = new JSONObject();
                    m.put("type", "move");
                    m.put("direction",myAm.direction);
                    m.put("username",myAm.myUsername);
                    output.writeUTF(m.toString());
                    System.out.println("send move");
                    myAm.newDirection = false;
                }

            }
            //System.out.println("============break success");
            input.close();
            output.close();
            mSocket.close();
            } catch (Exception e) {
                //e.printStackTrace();
                Log.e("MYTAG","socket thread ERROR",e);
            }


    }

    public String getUserlist() {
        return userlist;
    }

    public String getSkinlist() {
        return skinlist;
    }

    public boolean isWaiting() {
        return waiting;
    }

    public void setWaiting(boolean waiting) {
        this.waiting = waiting;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setQuit(boolean quit) {
        this.quit = quit;
    }

    public MyAssetsManager getMyAm() {
        return myAm;
    }

    public void dumpSingleData(){
        myAm.myUsername = username;
        int[] param = new int[]{0,1,0,0,0};
        int[] param2 = new int[]{0,1,0,0,0};
        myAm.userdata.put(username,param);
        myAm.userdata.put("AI",param2);
    }

    public void dumpMultiData(){  //put final data to star the game to asset manager
        myAm.myUsername = username;
        myAm.userlist = userlist.split("\n");
        for (String x : userarray){
            int[] param = new int[]{0,0,0,0,0};//[skin,speed,score,length,ready]
            //System.out.println("====================user" + x+" is dumped");
            myAm.userdata.put(x,param);
        }
    }

    public ArrayList<String> getUserarray() {
        return userarray;
    }
}
