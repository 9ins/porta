package org.chaostocosmos.net.porta;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DBSessionTest {

    ExecutorService executorService;
    String url, dbUser, dbPasswd;
    long startMills, endMillis;
    long totalRow = 0;

    public DBSessionTest(int threadCount, String url, String dbUser, String dbPasswd) throws ClassNotFoundException {
        this.url = url;
        this.dbUser = dbUser;
        this.dbPasswd = dbPasswd;
        this.executorService = Executors.newFixedThreadPool(threadCount);
        Class.forName("oracle.jdbc.driver.OracleDriver");
    }

    public void generate(int num, long delay, String query) throws InterruptedException, SQLException {
        startMills = System.currentTimeMillis();
        for(int i=0; i<num; i++) {
            SelectQueryTask task = new SelectQueryTask(query);
            this.executorService.execute(task);
            Thread.sleep(delay);
        }
        this.executorService.shutdown();
    } 

    class SelectQueryTask extends Thread {

        Connection dbConnection;
        java.sql.Statement stmt;
        String query;
        //long startMills;

        public SelectQueryTask(String query) throws SQLException {
            //this.startMills = System.currentTimeMillis();
            this.query = query;
            this.dbConnection = DriverManager.getConnection(url, dbUser, dbPasswd);
        }

        public void run() {
            try {
                this.stmt = this.dbConnection.createStatement();
                ResultSet rs = this.stmt.executeQuery(this.query);
                while(rs.next()) {
                    totalRow++;
                    String result = rs.getString(1)+"  "+rs.getString(2)+"  "+rs.getString(3);
                    System.out.println("[RESULT] "+result+"  ROW: "+totalRow);
                }
                rs.close();
                this.stmt.close();
                this.dbConnection.close();
                //System.out.println("Elapse time millis: "+(System.currentTimeMillis()-startMills));
            } catch(SQLException e) {
                e.printStackTrace();
            }
            long elapse = System.currentTimeMillis() - startMills;
            //System.out.println("Total Elapse Seconds: "+elapse/1000);
        }
    }

    public static void main(String[] args) throws InterruptedException, SQLException, ClassNotFoundException {        
        String url = "jdbc:oracle:thin:@localhost:1521:cdb1";
        //String url = "jdbc:oracle:thin:@192.168.1.157:1521:cdb1";
        String user = "innoquartz";
        String passwd = "znjcmdlsh12";
        String query = "select * from innoquartz.job_drill_simple";
        DBSessionTest dbTest = new DBSessionTest(1, url, user, passwd);
        dbTest.generate(100, 0, query);
    }
}