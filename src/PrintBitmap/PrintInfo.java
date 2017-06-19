package PrintBitmap;

import java.math.BigDecimal;

public class PrintInfo {
	 private String PRINTDATE;
	 private String PRINTTIME;
	 private String PRINTNAME;
	 private String PRINTIP;
	 private BigDecimal QTY;
	 
	 public void setPRINTDATE(String _PRINTDATE){
		 this.PRINTDATE = _PRINTDATE;
	 }
	 
	 public String getPRINTDATE(){
		 return this.PRINTDATE;
	 }
	 
	 public void setPRINTTIME(String _PRINTTIME){
		 this.PRINTTIME = _PRINTTIME;
	 }
	 
	 public String getPRINTTIME(){
		 return this.PRINTTIME;
	 }
	 
	 public void setPRINTNAME(String _PRINTNAME){
		 this.PRINTNAME = _PRINTNAME;
	 }
	 
	 public String getPRINTNAME(){
		 return this.PRINTNAME;
	 }
	 
	 public void setPRINTIP(String _PRINTIP){
		 this.PRINTIP = _PRINTIP;
	 }
	 
	 public String getPRINTIP(){
		 return this.PRINTIP;
	 }
	 
	 public void setQTY(BigDecimal _QTY){
		 this.QTY = _QTY;
	 }
	 
	 public BigDecimal getQTY(){
		 return this.QTY;
	 }
}
	
