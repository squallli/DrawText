package PrintBitmap;

import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.Attribute;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.PrinterName;

import com.google.gson.Gson;

public class PrintBitmap {
	
	static Gson gson = null;
	static PrintInfo pi = null;
	static LabelInfo li = null;
	static PrintStatusDB PSD = null;
	static Runnable oneRunnable = null;
	static Thread oneThread = null;
	
	public PrintBitmap() 
	{
	}
	
	public static void main(String[] args){
		//Gson to Json
		Gson gson = new Gson();

		String printname = "";

		String orgString = "Select * from PRINTLOG Where Flag='0'";
		String[] strErrMag = new String[20];
		pi = new PrintInfo();
		PSD = new PrintStatusDB();
		ArrayList<Hashtable> PrintDataAll = DBAccess.ExecuteSelectQuery(orgString, strErrMag);

		for (Hashtable object: PrintDataAll) {

			if(object.get("ERRMSG").toString() != ""){
				
			}else{
			
				System.out.println(object);
				System.out.println(object.get("PRINTDATA"));

				pi.setPRINTDATE(object.get("PRINTDATE").toString());
				pi.setPRINTTIME(object.get("PRINTTIME").toString());
				pi.setPRINTNAME(object.get("PRINTNAME").toString());
				pi.setPRINTIP(object.get("PRINTIP").toString());
				pi.setQTY(BigDecimal.valueOf((double) object.get("QTY")));
			
				// JSON to Java object, read it from a Json String.
				printname = object.get("PRINTNAME").toString();
				if(printname.trim() == ""){

					PSD.UpDatePrintDB(pi.getPRINTDATE(), pi.getPRINTTIME(), pi.getPRINTIP(), "Print Name Error¡C", "2");
					continue;
				}
				

				
				//Print Qty
				if(pi.getQTY().intValueExact() < 1){

					PSD.UpDatePrintDB(pi.getPRINTDATE(), pi.getPRINTTIME(), pi.getPRINTIP(), "Print Qty Error¡C", "2");
					continue;
				}
				
				try{
					li = gson.fromJson(object.get("PRINTDATA").toString(), LabelInfo.class);
					System.out.println(li.retObj);
				}catch (Exception e) {
					e.toString();
					System.out.println("gson Exception " + e.toString());

					PSD.UpDatePrintDB(pi.getPRINTDATE(), pi.getPRINTTIME(), pi.getPRINTIP(), "gson Exception", "2");
					continue;
				}
				
				oneRunnable = new PrintThread(li , pi);
				oneThread = new Thread(oneRunnable);
				oneThread.start();
			
				try {
					oneThread.join();

					PSD.UpDatePrintDB(pi.getPRINTDATE(), pi.getPRINTTIME(), pi.getPRINTIP(), "", "1");
				} catch (InterruptedException e) {

					e.printStackTrace();
					System.out.println("InterruptedException");

					PSD.UpDatePrintDB(pi.getPRINTDATE(), pi.getPRINTTIME(), pi.getPRINTIP(), "Interrupted Exception", "2");
				}

			
				oneRunnable = null; 
				oneThread = null;

				System.gc(); 
			}
		}
	    //System.exit(0);
	}
}

class PrintThread implements Runnable{
	private static PrintService printService = null;
	private static PrintService[] printServices;
	private static PageFormat pageFormat = null;
	private static PrintUtil.Page PUPage = null;
	private static LabelInfo sLi = null;
	private static PrintInfo sPi = null;
	public PrintThread(LabelInfo Li , PrintInfo pi){
		sLi = Li;
		sPi = pi;
	}

	public void run(){
		PUPage = new PrintUtil.Page();

		String printname = sPi.getPRINTNAME();
		PrinterJob printerjob = PrinterJob.getPrinterJob();
	
		PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet();
		printServiceAttributeSet.add((Attribute) new PrinterName(printname, null));
		printServices = PrintServiceLookup.lookupPrintServices(null, printServiceAttributeSet);
			
		// If you want to adjust heigh and width etc. of your paper.
		pageFormat = new PageFormat(); 
		Paper paper = pageFormat.getPaper();
			
		//Set Page Format
		double width = 4d * 72d;
	    double height = 1.8d * 72d;
	    paper.setSize(width, height);
	    pageFormat.setOrientation(PageFormat.PORTRAIT);
	    pageFormat.setPaper(paper);
			
	    Book pBook = new Book();
	    pBook.append(PUPage, pageFormat);
	        
	    try {
	        printService = printServices[0];
			printerjob.setPrintService(printService); // Try setting the printer you want
		} catch (ArrayIndexOutOfBoundsException e) {
			System.err.println("Error: No printer named '" + printname + "', using default printer.");
			pageFormat = printerjob.defaultPage(); // Set the default printer instead.
		} catch (PrinterException exception) {
			System.err.println("Printing error: " + exception);
		}
	        
	    printerjob.setPageable(pBook);
	        
	    try {
	       	printerjob.setCopies(sPi.getQTY().intValueExact());
		    printerjob.print(); // Actual print command
		} catch (PrinterException exception) {
			   System.err.println("Printing error: " + exception);
		}
	  
	}
}

