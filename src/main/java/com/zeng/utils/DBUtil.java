package com.zeng.utils;


import com.zeng.bean.UFile;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

public class DBUtil {

    /* the default framework is embedded */
    private String framework = "embedded";
    private String protocol = "jdbc:derby:";
    private Connection conn = null;
    private ArrayList<Statement> statements = new ArrayList<Statement>();
    private PreparedStatement psInsert;
    private PreparedStatement psDelete;
    private PreparedStatement psUpdate;
    private PreparedStatement psSelect;
    private Statement s;
    private ResultSet rs = null;

    /**
     *  打印输出信息
     *  @param message  信息的内容
     */
    private void reportFailure(String message) {
        System.err.println("\nData verification failed:");
        System.err.println('\t' + message);
    }
    /**
     *  打印异常的信息
     * @param e    异常的信息
     */
    public static void printSQLException(SQLException e)
    {
        // Unwraps the entire exception chain to unveil the real cause of the
        // Exception.
        while (e != null)
        {
            System.err.println("\n----- SQLException -----");
            System.err.println("  SQL State:  " + e.getSQLState());
            System.err.println("  Error Code: " + e.getErrorCode());
            System.err.println("  Message:    " + e.getMessage());
            // for stack traces, refer to derby.log or uncomment this:
            //e.printStackTrace(System.err);
            e = e.getNextException();
        }
    }

    /**
     * 进行数据库的连接
     */
    public void conn(){

        try {
            Properties props = new Properties(); //connection properties
            props.put("user","user1");
            props.put("password","user1");
            conn = DriverManager.getConnection(protocol+"derbyDB;create=true",props);
            s = conn.createStatement();
            statements.add(s);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * 创建数据库
     * @param  dbName   需要创建的数据库名字
     */
    public void createDB(String dbName){
        try {
            conn();
            System.out.println("Connected to and created database "+dbName);
            // We want to control transactions manually. Autocommit is on by
            // default in JDBC
            conn.setAutoCommit(true);
            /* Creating a statement object that we can use for running various
             * SQL statements commands against the database.*/
            //We create a table
            s.execute("create table location(uuid varchar(50),fileSize integer,fileType varchar(40),primeName varchar(100),createDate varchar(40),SaveAddr varchar(200),security varchar(200))");
            System.out.println("Created table localtion");

        } catch (SQLException e) {
            System.out.println("数据表已存在！");
            //e.printStackTrace();
        }
    }
    /**
     *  对数据库进行插入操作
     * @param  uFile   需要插入的文件信息
     */
    public String insertT(UFile uFile) {
        //and add a few rows...

        try {
            conn();
            psInsert = conn.prepareStatement("insert into location values(?,?,?,?,?,?,?)");
            statements.add(psInsert);

            psInsert.setString(1, uFile.getUuid());
            psInsert.setInt(2, uFile.getFileSize());
            psInsert.setString(3, uFile.getFileType());
            psInsert.setString(4, uFile.getPrimeName());
            psInsert.setString(5, uFile.getCreateDate());
            psInsert.setString(6, uFile.getSaveAddr());
            psInsert.setString(7, uFile.getSecurity());
            System.out.println("Inserted SUCCESS.");

            psInsert.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "SUCCESS";
    }
    /**
     *  删除表中元素
     * @param  uuid    需要删除文件的id
     */
    public void deleteT(int uuid){
        //and delete a few rows...
        try {
            psDelete = conn.prepareStatement("delete from  location where uuid=?");
            statements.add(psDelete);

            psDelete.setInt(1, uuid);
            psDelete.executeUpdate();
            System.out.println("Delete SUCCESS.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     *  寻找指定uuid的元素信息
     * @param uuid    需要查找的uuid
     */
    public UFile queryOne(String uuid){
        conn();
        UFile temp = new UFile();
        try {
            psSelect = conn.prepareStatement("SELECT * FROM location WHERE uuid=?");
            statements.add(psSelect);
            psSelect.setString(1,uuid);

            rs = psSelect.executeQuery();
            if(rs.next()){
                temp.setUuid(rs.getString(1));
                temp.setFileSize(rs.getInt(2));
                temp.setFileType(rs.getString(3));
                temp.setPrimeName(rs.getString(4));
                temp.setCreateDate(rs.getString(5));
                temp.setSaveAddr(rs.getString(6));
                temp.setSecurity(rs.getString(7));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("query one date success.");
        return temp;
    }

    /**
     *  查找整张表的元数据信息
     */
    public List<UFile> queryAll(){
        List<UFile> list = new ArrayList();
        try {
            conn();
            rs = s.executeQuery("SELECT * FROM location ORDER BY uuid");
            while(rs.next()){
                UFile temp = new UFile();
                temp.setUuid(rs.getString(1));
                temp.setFileSize(rs.getInt(2));
                temp.setFileType(rs.getString(3));
                temp.setPrimeName(rs.getString(4));
                temp.setCreateDate(rs.getString(5));
                temp.setSaveAddr(rs.getString(6));
                temp.setSecurity(rs.getString(7));
                list.add(temp);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("query all the date success.");
        return list;
    }

    /**
     * 删除表及数据库信息
     */
    public void closeDB(){
        if (framework.equals("embedded"))
        {
            try
            {
                conn();
                // delete the table
                s.execute("drop table location");
                System.out.println("Dropped table location");

                // the shutdown=true attribute shuts down Derby
                DriverManager.getConnection("jdbc:derby:;shutdown=true");

                // To shut down a specific database only, but keep the
                // engine running (for example for connecting to other
                // databases), specify a database in the connection URL:
                //DriverManager.getConnection("jdbc:derby:" + dbName + ";shutdown=true");
            }
            catch (SQLException se)
            {
                if (( (se.getErrorCode() == 50000)
                        && ("XJ015".equals(se.getSQLState()) ))) {
                    // we got the expected exception
                    System.out.println("Derby shut down normally");
                    // Note that for single database shutdown, the expected
                    // SQL state is "08006", and the error code is 45000.
                } else {
                    // if the error code or SQLState is different, we have
                    // an unexpected exception (shutdown failed)
                    System.err.println("Derby did not shut down normally");
                    printSQLException(se);
                }
            }
        }

        // release all open resources to avoid unnecessary memory usage

        // ResultSet
        try {
            if (rs != null) {
                rs.close();
                rs = null;
            }
        } catch (SQLException sqle) {
            printSQLException(sqle);
        }

        // Statements and PreparedStatements
        int i = 0;
        while (!statements.isEmpty()) {
            // PreparedStatement extend Statement
            Statement st = (Statement)statements.remove(i);
            try {
                if (st != null) {
                    st.close();
                    st = null;
                }
            } catch (SQLException sqle) {
                printSQLException(sqle);
            }
        }

        //Connection
        try {
            if (conn != null) {
                conn.close();
                conn = null;
            }
        } catch (SQLException sqle) {
            printSQLException(sqle);
        }
    }
}
