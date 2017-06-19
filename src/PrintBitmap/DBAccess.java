package PrintBitmap;

import oracle.jdbc.pool.OracleConnectionPoolDataSource;
import oracle.jdbc.pool.OracleDataSource;
import oracle.jdbc.pool.OracleConnectionCacheManager;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Properties;

import javax.sql.PooledConnection;

public class DBAccess {

	public static String ConnectionString = "jdbc:oracle:thin:@192.168.0.46:1521:orcl";
	public static String UserName = "rfid02";
	
//	public static String ConnectionString = "jdbc:oracle:thin:@APPL.egat.com.tw:1521:APPL";
//	public static String UserName = "SJT0";
	
	private static String GetPwd()
	{

		return "rfid02";
		
//		DateFormat dateFormat = new SimpleDateFormat("yyMM");
//		Date date = new Date();
//		return "SJT0" + dateFormat.format(date);
	}
	public static Boolean ExcuteQueries(String[] sql,String[] err)
	{
		err[0] = "";
		Boolean ret = false;
		Connection conn = null;
		Statement statement = null;
		PooledConnection pc = null;
		try {
			
			
			OracleConnectionPoolDataSource ocpds = new OracleConnectionPoolDataSource();
		    ocpds.setURL(ConnectionString);
		    ocpds.setUser(UserName);
		    ocpds.setPassword(GetPwd());
		    
		    pc = ocpds.getPooledConnection();
		    conn = pc.getConnection();
		    conn.setAutoCommit(false);
		    statement = conn.createStatement();
		    for(String s:sql)
		    {
		    	statement.executeUpdate(s);
		    }
		    conn.commit();
			ret = true;
			
		} catch (Exception e) {
			err[0] = e.toString();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				err[0] = e1.toString();
			}
			
			ret = false;
		}
		finally{
			try {
				pc.close();
				statement.close();
				conn.setAutoCommit(true);
				if(conn != null) conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return ret;
	}
	
	public static Boolean ExcuteQuery(String Sql,String[] err)
	{
		err[0] = "";
		Boolean ret = false;
		Connection conn = null;
		Statement statement = null;
		PooledConnection pc= null;
		
		try {
			
			OracleConnectionPoolDataSource ocpds = new OracleConnectionPoolDataSource();
		    ocpds.setURL(ConnectionString);
		    ocpds.setUser(UserName);
		    ocpds.setPassword(GetPwd());
		    
		    pc = ocpds.getPooledConnection();
		    conn = pc.getConnection();
		    
			statement = conn.createStatement();
			statement.executeUpdate(Sql);
			ret = true;
			
		} catch (Exception e) {
			
			err[0] = e.toString();
			ret = false;
		}
		finally
		{
			try {
				pc.close();
				statement.close();
				conn.setAutoCommit(true);
				if( conn != null) conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return ret;
	}
	
	
	public static ArrayList<Hashtable> ExecuteSelectQuery(String Sql,String[] err) 
	{
		ArrayList<Hashtable> Table = new ArrayList<Hashtable>();
		Hashtable rows = null;
	
		err[0] = "";
		Connection conn = null;
		Statement stat = null;
		PooledConnection pc = null;
		
		try {
			err[0] = "";

			OracleConnectionPoolDataSource ocpds = new OracleConnectionPoolDataSource();
		    ocpds.setURL(ConnectionString);
		    ocpds.setUser(UserName);
		    ocpds.setPassword(GetPwd());
		    
		    pc = ocpds.getPooledConnection();
		    
		    conn = pc.getConnection();
			    
		    stat=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
		    ResultSet rs = stat.executeQuery(Sql);
		    ResultSetMetaData rsmd = null;
		    
		    String s = "";
			while(rs.next()){
				rsmd = rs.getMetaData();
				rows = new Hashtable();
				for(int i=1;i<=rsmd.getColumnCount();i++)
			    {
					if(rsmd.getColumnType(i) == Types.VARCHAR || 
						  rsmd.getColumnType(i) == Types.CHAR ||
						  rsmd.getColumnType(i) == Types.NVARCHAR)
					{
						s = rs.getString(i);
						if(s == null)
							rows.put(rsmd.getColumnName(i),"");
						else {
							rows.put(rsmd.getColumnName(i),rs.getString(i));
						}
					}
					else if(rsmd.getColumnType(i) == Types.INTEGER)
						rows.put(rsmd.getColumnName(i),rs.getInt(i));
					else if(rsmd.getColumnType(i) == Types.NUMERIC)
						rows.put(rsmd.getColumnName(i),rs.getDouble(i));
					else if(rsmd.getColumnType(i) == Types.TIMESTAMP)
					{
						if(rs.getTimestamp(i) == null)
							rows.put(rsmd.getColumnName(i), "");
						else 
							rows.put(rsmd.getColumnName(i), rs.getTimestamp(i));
					}
						
					else if(rsmd.getColumnType(i) == Types.BLOB)
						if(rs.getBlob(i) == null)
							rows.put(rsmd.getColumnName(i),"");
						else
						{
							Blob b = rs.getBlob(i);
							byte[] bdata = b.getBytes(1, (int) b.length());
							rows.put(rsmd.getColumnName(i),bdata);
						}
							
			    }
				Table.add(rows);
			}
			return Table;
		} catch (Exception e1) {
			err[0] = e1.toString();
			return null;
		}
		finally{
			try {
				pc.close();
				stat.close();
				if(conn != null)
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}
