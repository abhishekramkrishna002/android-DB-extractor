package com.zinios.sqlitedbtest;

import java.util.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class ExtractDB {

    public final static String sdkLocation = "/Users/abhishek.ramkrishna/Library/Android/sdk";
    public final static String platformToolsLocation = sdkLocation + "/platform-tools";
    public final static String workingDir = "/Users/abhishek.ramkrishna/Desktop";
    public final static String Storage_Dir_1_5 = "/Users/abhishek.ramkrishna/Desktop/databases/1.5";
    public final static String Storage_Dir_2_0 = "/Users/abhishek.ramkrishna/Desktop/databases/2.0";
    public final static String SNAPBILLING_1_5_LOCAL_DB = "/data/data/com.snapbizz.snapbilling/databases/SnapBizz.db";
    public final static String SNAPBILLING_2_0_LOCAL_DB = "/data/data/com.snapbizz.snapbillingv2/databases/snapbizzv2.db";
    public final static String SNAPBILLING_2_0_GLOABL_DB = "/data/data/com.snapbizz.snapbillingv2/databases/global.db";

    public static void main(String[] args) {
        try {
            givePermissions();
            getDBForSnapbillingDB(new Command(new String[]{platformToolsLocation + "/adb", "pull", SNAPBILLING_1_5_LOCAL_DB},Storage_Dir_1_5));
            getDBForSnapbillingDB(new Command(new String[]{platformToolsLocation + "/adb", "pull", SNAPBILLING_2_0_LOCAL_DB},Storage_Dir_2_0));
            getDBForSnapbillingDB(new Command(new String[]{platformToolsLocation + "/adb", "pull", SNAPBILLING_2_0_GLOABL_DB},Storage_Dir_2_0));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
                System.out.println("Sucessfully pulled all the databases");
        }
    }

    public static void getDBForSnapbillingDB(Command command) throws Exception {
        Runtime rt = Runtime.getRuntime();
        ProcessBuilder pb = new ProcessBuilder(command.instruction);
        pb.directory(new File(command.workingDir));
        Process proc = pb.start();
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
        String consoleMessage = null;
        while ((consoleMessage = stdInput.readLine()) != null) {
            System.out.println(consoleMessage);
        }
        while ((consoleMessage = stdError.readLine()) != null) {
            System.out.println(consoleMessage);
        }
        stdInput.close();
        stdError.close();
        
    }
    
    public static void givePermissions() throws Exception
    {
        Runtime rt = Runtime.getRuntime();
        ProcessBuilder pb = new ProcessBuilder(new String[]{platformToolsLocation+"/adb","shell"});
        pb.directory(new File(workingDir));
        Process proc = pb.start();
        
        OutputStream outputStream=proc.getOutputStream();
        outputStream.write("su\n".getBytes());
        outputStream.write("chmod 777 data\n".getBytes());
        outputStream.write("cd data\n".getBytes());
        outputStream.write("chmod 777 data\n".getBytes());
        outputStream.write("cd data\n".getBytes());
        outputStream.write("chmod 777 com.snapbizz.snapbilling\n".getBytes());
        outputStream.write("cd com.snapbizz.snapbilling\n".getBytes());
        outputStream.write("chmod -R 777 databases\n".getBytes());
        outputStream.write("cd /data/data\n".getBytes());
        outputStream.write("chmod 777 com.snapbizz.snapbillingv2\n".getBytes());
        outputStream.write("cd com.snapbizz.snapbillingv2\n".getBytes());
        outputStream.write("chmod -R 777 databases\n".getBytes());
        outputStream.write("exit\n".getBytes());
        outputStream.write("exit\n".getBytes());
        outputStream.flush();
        outputStream.close();
//        BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
//        BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
//        String consoleMessage = null;
//        while ((consoleMessage = stdInput.readLine()) != null) {
//            System.out.println(consoleMessage);   
//        }
//        while ((consoleMessage = stdError.readLine()) != null) {
//            System.out.println(consoleMessage);
//        }
//        stdInput.close();
//        stdError.close(); 
//        
        
    }

    
}
