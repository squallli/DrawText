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
    	//�o�̬O�C�L���D��
        public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) {
        	if (pageIndex >= 1) {
   	         return Printable.NO_SUCH_PAGE; //���A�~��L
        	}
            Graphics2D g2d = (Graphics2D) graphics;
           
           //�]�w�C�L�����I�b�i�C�L�ϰ쪺���W��   
            g2d.translate((int)2 , (int)0);
            g2d.setClip(0, 0, (int)pageFormat.getImageableWidth(), (int)pageFormat.getImageableHeight());
            
            /*�~�W*/
    	    g2d.setPaint(Color.black);
    	    Font myFont = new Font("�L�n������",Font.BOLD,12);
    	    g2d.setFont(myFont);
    	    g2d.drawString("�j�_�L�w�l���V�󥻬~�v����S450g",0,12);
    	    
    	   /*�W��*/
    	    myFont = new Font("�L�n������",Font.PLAIN,9);
    	    g2d.setFont(myFont);
    	    g2d.drawString("�W��G450g/�~",0,24);
    	    
            /*���W��*/
    	    myFont = new Font("�L�n������",Font.PLAIN,9);
    	    g2d.setFont(myFont);
    	    g2d.drawString("���G",0,36);
    	    
    	    /*���-�ƶq*/
    	    myFont = new Font("�L�n������",Font.PLAIN,11);
    	    g2d.setFont(myFont);
    	    g2d.drawString("99",26,36);
           
    	    /*���-�ƶq*/
    	    myFont = new Font("�L�n������",Font.PLAIN,9);
    	    g2d.setFont(myFont);
    	    g2d.drawString("�~",43, 36);
    	    
    	    /*�����@*/
    	    myFont = new Font("Arial",Font.PLAIN,6);
    	    g2d.setFont(myFont);
    	    g2d.drawString("0420",0, 44);
   
    	    /*�����G*/
    	    myFont = new Font("Arial",Font.PLAIN,6);
    	    g2d.setFont(myFont);
    	    g2d.drawString("100.099",42, 44);
    	    
    	    /*���X��m*/
    	    CreateEan13();
    	    try {
				ean13.Print.Scale(0.8f);
			} catch (Exception e) {
				// TODO �۰ʲ��ͪ� catch �϶�
				e.printStackTrace();
			}
    	    ean13.DrawEan13Barcode(g2d, new Point(0, 48));

    	    myFont = new Font("OCR-B-Seagull",Font.BOLD,6);
    	    g2d.setFont(myFont);
    	    g2d.drawString("4710627843008",0, 69);
    	    
    	    /*�����T*/
    	    myFont = new Font("Arial",Font.PLAIN,6);
    	    g2d.setFont(myFont);
    	    g2d.drawString("118419",0, 76);
    	    
    	    /*�����|*/
    	    myFont = new Font("Arial",Font.PLAIN,6);
    	    g2d.setFont(myFont);
    	   g2d.drawString("P1708",44, 76);
    	    
    	    /*������*/
    	    myFont = new Font("Arial",Font.PLAIN,6);
    	    g2d.setFont(myFont);
    	    g2d.drawString("90. 1. 2.10",0, 83);

    	    /*����*/
    	    myFont = new Font("Arial",Font.BOLD,68);
    	    g2d.setFont(myFont);
    	   g2d.drawString("199",85, 75);
    	    
    	    /*���*/
    	    myFont = new Font("�L�n������",Font.PLAIN,9);
    	    g2d.setFont(myFont);
    	    g2d.drawString("�C100g 44.2��",128, 95);
    	    
    	    /*�P�P*/
    	    myFont = new Font("�L�n������",Font.BOLD,20);
    	    g2d.setFont(myFont);
    	    g2d.drawString("�P",70, 52);
    	    g2d.drawString("�P",70, 72);
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
			// TODO �۰ʲ��ͪ� catch �϶�
			e.printStackTrace();
		}
        
        //if (txtChecksumDigit.Text.Length > 0)
           // ean13.ChecksumDigit = txtChecksumDigit.Text;
    }
    
    /**
     * ��X�@�˪��L����W��
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
     * �̷ӱ����X�i�Ϊ��L���
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