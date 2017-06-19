package PrintBitmap;

public class PrintStatusDB {

	public void UpDatePrintDB(String _PRINTDATE , String _PRINTTIME , String _PRINTIP , String _ERRMSG , String _FLAG){
		String Sql = "Update PRINTLOG Set FLAG='" + _FLAG + "',ERRMSG='" + _ERRMSG + 
				"' Where PRINTDATE='" + _PRINTDATE + 
				"' and PRINTTIME = '" + _PRINTTIME + 
				"' and PRINTIP = '" + _PRINTIP + "'";
		String[] err = new String[1];
		DBAccess.ExcuteQuery(Sql, err);
	}
}
