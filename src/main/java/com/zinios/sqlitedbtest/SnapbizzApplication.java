package com.zinios.sqlitedbtest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class SnapbizzApplication {

    public static void main(String[] args) {
        Connection localDbConnection = null;
        String gloablDbLocaltion = "/Users/abhishek.ramkrishna/Desktop/gb.db";
        String localDbLocation = "/Users/abhishek.ramkrishna/Desktop/snapv2.db";
        try {
            // create a database connection
            Class.forName("org.sqlite.JDBC");
            localDbConnection = DriverManager.getConnection("jdbc:sqlite:" + localDbLocation);
            localDbConnection.prepareStatement("ATTACH DATABASE \"" + gloablDbLocaltion + "\" AS  gb KEY 'sn@pb1zz@123'").execute();
            Statement statement = localDbConnection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
//            ResultSet rs = statement.executeQuery("select * from ITEMS");
//            while (rs.next()) {
//                // read the result set
//                System.out.println("NAME = " + rs.getString("NAME"));
//            }

            int rowsCount = 0;
            /*
            * getting layer 1  
             */
            String query = "SELECT * FROM ITEMS,PRODUCTS WHERE ITEMS.PRODUCT_CODE=PRODUCTS.PRODUCT_CODE";
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                System.out.println("NAME = " + rs.getString("NAME") + " Product Code = " + rs.getString("PRODUCT_CODE"));
                rowsCount++;
            }
            System.out.println("End of layer 1");
            /*
            * getting layer 2   
             */
            query = "select * from gb.PRODUCTS,main.ITEMS where main.ITEMS.PRODUCT_CODE=GB.PRODUCTS.BARCODE";
            rs = statement.executeQuery(query);
            while (rs.next()) {
                System.out.println("NAME = " + rs.getString("NAME") + " Product Code = " + rs.getString("PRODUCT_CODE"));
                rowsCount++;
            }
            System.out.println("End of layer 2");
            /*
            * getting layer 3   
             */
            query = "select * from main.ITEMS where main.ITEMS.PRODUCT_CODE <= 0";
            rs = statement.executeQuery(query);
            while (rs.next()) {
                System.out.println("NAME = " + rs.getString("NAME") + " Product Code = " + rs.getString("PRODUCT_CODE"));
                rowsCount++;
            }
            System.out.println("End of layer 3");

            System.out.println("Total Rows:" + rowsCount);

        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (localDbConnection != null) {
                    localDbConnection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e);
            }
        }
    }

}
