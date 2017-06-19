package PrintBitmap;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.print.PageFormat;
import java.awt.print.Printable;

import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.PrintRequestAttributeSet;

public class PrintUtil {
    private PrintService printService;
    private boolean hasUI = false; 
	private static Ean13 ean13 = null;
	
    public static class Page implements Printable {
    	//這裡是列印的主體
        public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) {
        	if (pageIndex >= 1) {
   	         return Printable.NO_SUCH_PAGE; //不再繼續印
        	}
            Graphics2D g2d = (Graphics2D) graphics;
           
           //設定列印的原點在可列印區域的左上角   
            g2d.translate((int)2 , (int)0);
            g2d.setClip(0, 0, (int)pageFormat.getImageableWidth(), (int)pageFormat.getImageableHeight());
            
            /*品名*/
    	    g2d.setPaint(Color.black);
    	    Font myFont = new Font("微軟正黑體",Font.BOLD,12);
    	    g2d.setFont(myFont);
    	    g2d.drawString("古寶無患子橄欖草本洗髮精華露450g",0,12);
    	    
    	   /*規格*/
    	    myFont = new Font("微軟正黑體",Font.PLAIN,9);
    	    g2d.setFont(myFont);
    	    g2d.drawString("規格：450g/瓶",0,24);
    	    
            /*單位名稱*/
    	    myFont = new Font("微軟正黑體",Font.PLAIN,9);
    	    g2d.setFont(myFont);
    	    g2d.drawString("單位：",0,36);
    	    
    	    /*單位-數量*/
    	    myFont = new Font("微軟正黑體",Font.PLAIN,11);
    	    g2d.setFont(myFont);
    	    g2d.drawString("99",26,36);
           
    	    /*單位-數量*/
    	    myFont = new Font("微軟正黑體",Font.PLAIN,9);
    	    g2d.setFont(myFont);
    	    g2d.drawString("瓶",43, 36);
    	    
    	    /*未知一*/
    	    myFont = new Font("Arial",Font.PLAIN,6);
    	    g2d.setFont(myFont);
    	    g2d.drawString("0420",0, 44);
   
    	    /*未知二*/
    	    myFont = new Font("Arial",Font.PLAIN,6);
    	    g2d.setFont(myFont);
    	    g2d.drawString("100.099",42, 44);
    	    
    	    /*條碼位置*/
    	    CreateEan13();
    	    try {
				ean13.Print.Scale(0.8f);
			} catch (Exception e) {
				// TODO 自動產生的 catch 區塊
				e.printStackTrace();
			}
    	    ean13.DrawEan13Barcode(g2d, new Point(0, 48));

    	    myFont = new Font("OCR-B-Seagull",Font.BOLD,6);
    	    g2d.setFont(myFont);
    	    g2d.drawString("4710627843008",0, 69);
    	    
    	    /*未知三*/
    	    myFont = new Font("Arial",Font.PLAIN,6);
    	    g2d.setFont(myFont);
    	    g2d.drawString("118419",0, 76);
    	    
    	    /*未知四*/
    	    myFont = new Font("Arial",Font.PLAIN,6);
    	    g2d.setFont(myFont);
    	   g2d.drawString("P1708",44, 76);
    	    
    	    /*未知五*/
    	    myFont = new Font("Arial",Font.PLAIN,6);
    	    g2d.setFont(myFont);
    	    g2d.drawString("90. 1. 2.10",0, 83);

    	    /*價格*/
    	    myFont = new Font("Arial",Font.BOLD,68);
    	    g2d.setFont(myFont);
    	   g2d.drawString("199",85, 75);
    	    
    	    /*單價*/
    	    myFont = new Font("微軟正黑體",Font.PLAIN,9);
    	    g2d.setFont(myFont);
    	    g2d.drawString("每100g 44.2元",128, 95);
    	    
    	    /*促銷*/
    	    myFont = new Font("微軟正黑體",Font.BOLD,20);
    	    g2d.setFont(myFont);
    	    g2d.drawString("促",70, 52);
    	    g2d.drawString("銷",70, 72);
            return Printable.PAGE_EXISTS;
        }
    }
    
    private static void CreateEan13()
    {
    	ean13 = new Ean13();
        ean13.Print.setCountryCode("212");
        ean13.Print.setManufacturerCode("9198");
        ean13.Print.setProductCode("00145");
        try {
			ean13.Print.setChecksumDigit("8");
		} catch (Exception e) {
			// TODO 自動產生的 catch 區塊
			e.printStackTrace();
		}
        
        //if (txtChecksumDigit.Text.Length > 0)
           // ean13.ChecksumDigit = txtChecksumDigit.Text;
    }
    
    /**
     * 找出一樣的印表機名稱
     * @param printname
     * @return
     */
    public PrintService findPrintServicesName(String printname){
        PrintService printservice = null;
        PrintService services[] = this.findPrintServices(null, null);
        if (services.length == 0){
            return null;
        }
        for(int i = 0 ; i < services.length ; i++){
            PrintService ps = services[i];
            if(printname.equalsIgnoreCase(ps.getName())){
                printservice = ps;
            }
        }
        return printservice;
    }
    
    /**
     * 依照條件找出可用的印表機
     * @param docflavor
     * @param pras
     * @return
     */
    public PrintService[] findPrintServices(DocFlavor docflavor, PrintRequestAttributeSet pras){
        PrintService[] services = PrintServiceLookup.lookupPrintServices(docflavor, pras);
        return services;
    }

    public PrintService getPrintService() {
        return printService;
    }

    public void setPrintService(PrintService printService) {
        this.printService = printService;
    }

    public boolean isHasUI() {
        return hasUI;
    }

    public void setHasUI(boolean hasUI) {
        this.hasUI = hasUI;
    }
}