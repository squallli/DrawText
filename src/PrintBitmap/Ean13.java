package PrintBitmap;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.Formatter;

public class Ean13
{
    // Left Hand Digits.
    private String[] _aOddLeft = { "0001101", "0011001", "0010011", "0111101", 
									  "0100011", "0110001", "0101111", "0111011", 
									  "0110111", "0001011" };

    private String[] _aEvenLeft = { "0100111", "0110011", "0011011", "0100001", 
									   "0011101", "0111001", "0000101", "0010001", 
									   "0001001", "0010111" };

    // Right Hand Digits.
    private String[] _aRight = { "1110010", "1100110", "1101100", "1000010", 
									"1011100", "1001110", "1010000", "1000100", 
									"1001000", "1110100" };

    private String _sQuiteZone = "000000000";
    private String _sLeadTail = "101";
    private String _sSeparator = "01010";
    //private String _sCountryCode = "00";

    public class PrintInfo
    {
    	private String _sName;
    	private float _fMinimumAllowableScale = 0.8f;
        private float _fMaximumAllowableScale = 1.2f;
        private float _fWidth = 99.29f;//32.29f;
        private float _fHeight = 19.00f;//25.93f;
        private float _fFontSize = 6.0f;
        private float _fScale = 1.0f;
    	private String _sManufacturerCode;
    	private String _sProductCode;
    	private String _sChecksumDigit;
    	private String _sCountryCode;

    	public void setPrintname(String Printname) {
    	     this._sName = Printname;
    	}
    	
    	public String getPrintname() {
   	     	 return this._sName;
    	}
    	
    	public float MinimumAllowableScale() {
  	     	 return this._fMinimumAllowableScale;
    	}
    	
    	public float MaximumAllowableScale() {
 	     	 return this._fMaximumAllowableScale;
    	}
    	
    	public float Width() {
	     	 return this._fWidth;
    	}
    	
    	public float Height() {
	     	 return this._fHeight;
    	}
    	
    	public float FontSize() {
	     	 return this._fFontSize;
    	}
    	
    	public void Scale(float Scale) throws Exception{
    		if (Scale < this._fMinimumAllowableScale || Scale > this._fMaximumAllowableScale)
                throw new Exception("Scale value out of allowable range.  Value must be between " +
                		String.valueOf(this._fMinimumAllowableScale) + " and " +
                		String.valueOf(this._fMaximumAllowableScale));
    		this._fScale = Scale;
    	}
    	
    	public float getScale() {
	     	 return this._fScale;
    	}
    	
    	public void setCountryCode(String CountryCode) {
    		 while (CountryCode.length() < 2)
             {
    			 CountryCode = "0" + CountryCode;
             }
             _sCountryCode = CountryCode;
    	}
    	
    	public String getCountryCode() {
	     	 return this._sCountryCode;
    	}
    	
    	public void setManufacturerCode(String ManufacturerCode) {
    		 this._sManufacturerCode = ManufacturerCode;
    	}
    	
    	public String getManufacturerCode() {
	     	 return this._sManufacturerCode;
    	}
    	
    	public void setProductCode(String ProductCode) {
   		 this._sProductCode = ProductCode;
    	}
   	
    	public String getProductCode() {
	     	 return this._sProductCode;
    	}
    	
    	public void setChecksumDigit(String ChecksumDigit)throws Exception {
    		int iValue = Integer.parseInt(ChecksumDigit);
            if (iValue < 0 || iValue > 9)
                throw new Exception("The Check Digit most be between 0 and 9.");
            this._sChecksumDigit = ChecksumDigit;
       	}
    	
    	public String getChecksumDigit() {
	     	 return this._sChecksumDigit;
    	}
    }

    PrintInfo Print = new PrintInfo();
    
    public Ean13()
    {
    }
    
    public Ean13(String mfgNumber, String productId)
    {
    	Print.setCountryCode("00");
    	Print.setManufacturerCode(mfgNumber);
    	Print.setProductCode(productId);
        this.CalculateChecksumDigit();
    }
    
    public Ean13(String countryCode, String mfgNumber, String productId)
    {
    	Print.setCountryCode(countryCode);
    	Print.setManufacturerCode(mfgNumber);
    	Print.setProductCode(productId);
        this.CalculateChecksumDigit();
    }
    
    public Ean13(String countryCode, String mfgNumber, String productId, String checkDigit)
    {
    	Print.setCountryCode(countryCode);
    	Print.setManufacturerCode(mfgNumber);
    	Print.setProductCode(productId);
    	
    	try {
			Print.setChecksumDigit(checkDigit);
		} catch (Exception e) {
			// TODO 自動產生的 catch 區塊
			e.printStackTrace();
		}
    }
    
    public void DrawEan13Barcode(Graphics2D g, Point pt)
    {
    	Formatter formatter = new Formatter();
    	Formatter formatter2 = new Formatter();
        float width = Print.Width() * Print.getScale();
        float height = Print.Height() * Print.getScale();
        
        //	EAN13 Brocade should be a total of 113 modules wide.
        float lineWidth = width / 113f;

        // Save the GraphicsState.
        //Drawing2D.GraphicsState gs = g.Save();

        // Set the PageUnit to Inch because all of our measurements are in inches.
        //g = System.Drawing.GraphicsUnit.Millimeter;

        // Set the PageScale to 1, so a millimeter will represent a true millimeter.
        //g.PageScale = 1;

        //Brush brush = new System.Drawing.SolidBrush(System.Drawing.Color.Black);

        float xPosition = 0;

        StringBuilder strbEAN13 = new StringBuilder();
        StringBuilder sbTemp = new StringBuilder();

        float xStart = pt.x;
        float yStart = pt.y;
        //float xEnd = 0;  
        Font font = new Font("Arial", Math.round(Print.FontSize() * Print.getScale()), Font.ITALIC);
      
        // Calculate the Check Digit.
        this.CalculateChecksumDigit();
    
        formatter.format("%s%s%s%s", 
        		Print.getCountryCode(),
            	Print.getManufacturerCode(),
            	Print.getProductCode(),
            	Print.getChecksumDigit());
            	
        sbTemp.append(formatter);


        String sTemp = String.valueOf(sbTemp);

        String sLeftPattern = "";

        // Convert the left hand numbers.
        sLeftPattern = ConvertLeftPattern(sTemp.substring(0, 7));
        
        
        // Build the UPC Code.
        formatter2.format("%s%s%s%s%s%s%s", 
        		this._sQuiteZone, this._sLeadTail,
                sLeftPattern,
                this._sSeparator,
                ConvertToDigitPatterns(sTemp.substring(7), this._aRight), this._sLeadTail,this._sQuiteZone);
        
        strbEAN13.append(formatter2);
        formatter.close();
        
        String sTempUPC = String.valueOf(strbEAN13);
        
        FontMetrics metrics = g.getFontMetrics(font);
        int hgt = metrics.getHeight();
        int adv = metrics.stringWidth(sTempUPC);
        Dimension size = new Dimension(adv+2, hgt+2);
        float fTextHeight = size.height;

        boolean bStart = false;

        // Draw the brocade lines.
        for (int i = 0; i < strbEAN13.length(); i++)
        {
            if (sTempUPC.substring(i, i+1).equals("1"))
            {
                if (xStart == pt.x && !bStart)
                {
                    xPosition = xStart = pt.x;
                    bStart = true;
                    //xStart = xPosition;
                }

                // Save room for the UPC number below the bar code.
                if ((i > 12 && i < 55) || (i > 57 && i < 101)){
                    // Draw space for the number
                	g.setColor(Color.BLACK);
                	g.setStroke(new BasicStroke(lineWidth));
            	    g.draw(new Line2D.Float(xPosition, yStart,xPosition, yStart + (height - fTextHeight)));
            	    
                    //g.FillRectangle(brush, xPosition, yStart, lineWidth, height - fTextHeight);
                }else{
                    // Draw a full line.
                	g.setColor(Color.BLACK);
                	g.setStroke(new BasicStroke(lineWidth));
            	    g.draw(new Line2D.Float(xPosition, yStart,xPosition, yStart + height));
            	   
                    //g.FillRectangle(brush, xPosition, yStart, lineWidth, height);
                }
            }

            xPosition += lineWidth;
           // xEnd = xPosition;
        }
    }
    
    public BufferedImage CreateBitmap()
    {
        float tempWidth = (Print.Width() * Print.getScale()) * 100;
        float tempHeight = (Print.Height() * Print.getScale()) * 100;

        BufferedImage bmp = new BufferedImage((int)tempWidth, (int)tempHeight , BufferedImage.TYPE_INT_RGB);

        Graphics g = bmp.getGraphics();
        //this.DrawEan13Barcode(g, new Point(0, 0));
        g.dispose();
        return bmp;
    }
    
    private String ConvertLeftPattern(String sLeft)
    {
        switch (sLeft.substring(0, 1))
        {
            case "0":
                return CountryCode0(sLeft.substring(1));

            case "1":
                return CountryCode1(sLeft.substring(1));

            case "2":
                return CountryCode2(sLeft.substring(1));

            case "3":
                return CountryCode3(sLeft.substring(1));

            case "4":
                return CountryCode4(sLeft.substring(1));

            case "5":
                return CountryCode5(sLeft.substring(1));

            case "6":
                return CountryCode6(sLeft.substring(1));

            case "7":
                return CountryCode7(sLeft.substring(1));

            case "8":
                return CountryCode8(sLeft.substring(1));

            case "9":
                return CountryCode9(sLeft.substring(1));

            default:
                return "";
        }
    }
    
    private String CountryCode0(String sLeft)
    {
        // 0 Odd Odd  Odd  Odd  Odd  Odd 
        return ConvertToDigitPatterns(sLeft, this._aOddLeft);
    }
    
    private String CountryCode1(String sLeft)
    {
        // 1 Odd Odd  Even Odd  Even Even 
        StringBuilder sReturn = new StringBuilder();
        // The two lines below could be replaced with this:
        // sReturn.Append( ConvertToDigitPatterns( sLeft.Substring( 0, 2 ), this._aOddLeft ) );
        sReturn.append(ConvertToDigitPatterns(sLeft.substring(0, 1), this._aOddLeft));
        sReturn.append(ConvertToDigitPatterns(sLeft.substring(1, 2), this._aOddLeft));
        sReturn.append(ConvertToDigitPatterns(sLeft.substring(2, 3), this._aEvenLeft));
        sReturn.append(ConvertToDigitPatterns(sLeft.substring(3, 4), this._aOddLeft));
        // The two lines below could be replaced with this:
        // sReturn.Append( ConvertToDigitPatterns( sLeft.Substring( 4, 2 ), this._aEvenLeft ) );
        sReturn.append(ConvertToDigitPatterns(sLeft.substring(4, 5), this._aEvenLeft));
        sReturn.append(ConvertToDigitPatterns(sLeft.substring(5, 6), this._aEvenLeft));
        return String.valueOf(sReturn);
    }
    
    private String CountryCode2(String sLeft)
    {
        // 2 Odd Odd  Even Even Odd  Even 
        StringBuilder sReturn = new StringBuilder();
        sReturn.append(ConvertToDigitPatterns(sLeft.substring(0, 1), this._aOddLeft));
        sReturn.append(ConvertToDigitPatterns(sLeft.substring(1, 2), this._aOddLeft));
        sReturn.append(ConvertToDigitPatterns(sLeft.substring(2, 3), this._aEvenLeft));
        sReturn.append(ConvertToDigitPatterns(sLeft.substring(3, 4), this._aEvenLeft));
        sReturn.append(ConvertToDigitPatterns(sLeft.substring(4, 5), this._aOddLeft));
        sReturn.append(ConvertToDigitPatterns(sLeft.substring(5, 6), this._aEvenLeft));
        return String.valueOf(sReturn);
    }
    
    private String CountryCode3(String sLeft)
    {
        // 3 Odd Odd  Even Even Even Odd 
        StringBuilder sReturn = new StringBuilder();
        sReturn.append(ConvertToDigitPatterns(sLeft.substring(0, 1), this._aOddLeft));
        sReturn.append(ConvertToDigitPatterns(sLeft.substring(1, 2), this._aOddLeft));
        sReturn.append(ConvertToDigitPatterns(sLeft.substring(2, 3), this._aEvenLeft));
        sReturn.append(ConvertToDigitPatterns(sLeft.substring(3, 4), this._aEvenLeft));
        sReturn.append(ConvertToDigitPatterns(sLeft.substring(4, 5), this._aEvenLeft));
        sReturn.append(ConvertToDigitPatterns(sLeft.substring(5, 6), this._aOddLeft));
        return String.valueOf(sReturn);
    }
    
    private String CountryCode4(String sLeft)
    {
        // 4 Odd Even Odd  Odd  Even Even 
        StringBuilder sReturn = new StringBuilder();
        sReturn.append(ConvertToDigitPatterns(sLeft.substring(0, 1), this._aOddLeft));
        sReturn.append(ConvertToDigitPatterns(sLeft.substring(1, 2), this._aEvenLeft));
        sReturn.append(ConvertToDigitPatterns(sLeft.substring(2, 3), this._aOddLeft));
        sReturn.append(ConvertToDigitPatterns(sLeft.substring(3, 4), this._aOddLeft));
        sReturn.append(ConvertToDigitPatterns(sLeft.substring(4, 5), this._aEvenLeft));
        sReturn.append(ConvertToDigitPatterns(sLeft.substring(5, 6), this._aEvenLeft));
        return String.valueOf(sReturn);
    }
    
    private String CountryCode5(String sLeft)
    {
        // 5 Odd Even Even Odd  Odd  Even 
        StringBuilder sReturn = new StringBuilder();
        sReturn.append(ConvertToDigitPatterns(sLeft.substring(0, 1), this._aOddLeft));
        sReturn.append(ConvertToDigitPatterns(sLeft.substring(1, 2), this._aEvenLeft));
        sReturn.append(ConvertToDigitPatterns(sLeft.substring(2, 3), this._aEvenLeft));
        sReturn.append(ConvertToDigitPatterns(sLeft.substring(3, 4), this._aOddLeft));
        sReturn.append(ConvertToDigitPatterns(sLeft.substring(4, 5), this._aOddLeft));
        sReturn.append(ConvertToDigitPatterns(sLeft.substring(5, 6), this._aEvenLeft));
        return String.valueOf(sReturn);
    }
    
    private String CountryCode6(String sLeft)
    {
        // 6 Odd Even Even Even Odd  Odd 
        StringBuilder sReturn = new StringBuilder();
        sReturn.append(ConvertToDigitPatterns(sLeft.substring(0, 1), this._aOddLeft));
        sReturn.append(ConvertToDigitPatterns(sLeft.substring(1, 2), this._aEvenLeft));
        sReturn.append(ConvertToDigitPatterns(sLeft.substring(2, 3), this._aEvenLeft));
        sReturn.append(ConvertToDigitPatterns(sLeft.substring(3, 4), this._aEvenLeft));
        sReturn.append(ConvertToDigitPatterns(sLeft.substring(4, 5), this._aOddLeft));
        sReturn.append(ConvertToDigitPatterns(sLeft.substring(5, 6), this._aOddLeft));
        return String.valueOf(sReturn);
    }
    
    private String CountryCode7(String sLeft)
    {
        // 7 Odd Even Odd  Even Odd  Even
        StringBuilder sReturn = new StringBuilder();
        sReturn.append(ConvertToDigitPatterns(sLeft.substring(0, 1), this._aOddLeft));
        sReturn.append(ConvertToDigitPatterns(sLeft.substring(1, 2), this._aEvenLeft));
        sReturn.append(ConvertToDigitPatterns(sLeft.substring(2, 3), this._aOddLeft));
        sReturn.append(ConvertToDigitPatterns(sLeft.substring(3, 4), this._aEvenLeft));
        sReturn.append(ConvertToDigitPatterns(sLeft.substring(4, 5), this._aOddLeft));
        sReturn.append(ConvertToDigitPatterns(sLeft.substring(5, 6), this._aEvenLeft));
        return String.valueOf(sReturn);
    }
    
    private String CountryCode8(String sLeft)
    {
        // 8 Odd Even Odd  Even Even Odd 
        StringBuilder sReturn = new StringBuilder();
        sReturn.append(ConvertToDigitPatterns(sLeft.substring(0, 1), this._aOddLeft));
        sReturn.append(ConvertToDigitPatterns(sLeft.substring(1, 2), this._aEvenLeft));
        sReturn.append(ConvertToDigitPatterns(sLeft.substring(2, 3), this._aOddLeft));
        sReturn.append(ConvertToDigitPatterns(sLeft.substring(3, 4), this._aEvenLeft));
        sReturn.append(ConvertToDigitPatterns(sLeft.substring(4, 5), this._aEvenLeft));
        sReturn.append(ConvertToDigitPatterns(sLeft.substring(5, 6), this._aOddLeft));
        return String.valueOf(sReturn);
    }
    
    private String CountryCode9(String sLeft)
    {
        // 9 Odd Even Even Odd  Even Odd 
        StringBuilder sReturn = new StringBuilder();
        sReturn.append(ConvertToDigitPatterns(sLeft.substring(0, 1), this._aOddLeft));
        sReturn.append(ConvertToDigitPatterns(sLeft.substring(1, 2), this._aEvenLeft));
        sReturn.append(ConvertToDigitPatterns(sLeft.substring(2, 3), this._aEvenLeft));
        sReturn.append(ConvertToDigitPatterns(sLeft.substring(3, 4), this._aOddLeft));
        sReturn.append(ConvertToDigitPatterns(sLeft.substring(4, 5), this._aEvenLeft));
        sReturn.append(ConvertToDigitPatterns(sLeft.substring(5, 6), this._aOddLeft));
        return String.valueOf(sReturn);
    }
    
    private String ConvertToDigitPatterns(String inputNumber, String[] patterns)
    {
        StringBuilder sbTemp = new StringBuilder();
        int iIndex = 0;
        for (int i = 0; i < inputNumber.length(); i++)
        {
            iIndex = Integer.parseInt(inputNumber.substring(i, i+1));
            sbTemp.append(patterns[iIndex]);
        }
        return String.valueOf(sbTemp);
    }
    
    public void CalculateChecksumDigit()
    {
        String sTemp = Print.getCountryCode() + Print.getManufacturerCode() + Print.getProductCode();
        int iSum = 0;
        int iDigit = 0;

        // Calculate the checksum digit here.
        for (int i = sTemp.length(); i >= 1; i--)
        {
            iDigit = Integer.parseInt(sTemp.substring(i - 1, i));
            if (i % 2 == 0)
            {	// odd
                iSum += iDigit * 3;
            }
            else
            {	// even
                iSum += iDigit * 1;
            }
        }

        int iCheckSum = (10 - (iSum % 10)) % 10;
        
        try {
			Print.setChecksumDigit(String.valueOf(iCheckSum));
		} catch (Exception e) {
			// TODO 自動產生的 catch 區塊
			e.printStackTrace();
		}
    }
}
