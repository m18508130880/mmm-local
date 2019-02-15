package util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CheckCode {
	public final static int WIDTH = 60;
    public final static int HEIGHT = 20;
    public static void  CreateCheckCode(HttpServletRequest request, HttpServletResponse response, String pSid) throws ServletException, IOException
    {
		response.setContentType("image/jpeg"); 
		ServletOutputStream sos = response.getOutputStream(); 
		// �����������Ҫ�����ͼƬ 
		response.setHeader("Pragma","No-cache"); 
		response.setHeader("Cache-Control","no-cache"); 
		response.setDateHeader("Expires", 0); 
		// �����ڴ�ͼ�󲢻����ͼ��������          
		BufferedImage image = 
			new BufferedImage(CheckCode.WIDTH, CheckCode.HEIGHT, BufferedImage.TYPE_INT_RGB);
		Graphics2D  g = image.createGraphics(); 
		// �����������֤�� 
		char [] rands = CheckCode.generateCheckCode(); 
		// ����ͼ�� 
		drawBackground(g); 
		drawRands(g,rands); 

     	// ����ͼ�� �Ļ��� ���̣� ���ͼ�� 
     	g.dispose(); 
     	// ��ͼ��������ͻ��� 
     	ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
     	ImageIO.write(image, "JPEG", bos); 
     	byte [] buf = bos.toByteArray(); 
     	response.setContentLength(buf.length); 
     	// ��������Ҳ��д�ɣ� bos.writeTo(sos); 
     	sos.write(buf); 
     	bos.close(); 
     	sos.close(); 
     	
     	// ����ǰ��֤����뵽 Session �� 
     	CurrStatus currStatus = new CurrStatus();
     	currStatus.setCheckCode(new String(rands));
     	currStatus.setResult("");
     	request.getSession().setAttribute("CurrStatus_" + pSid, currStatus);
    } 
    
	public static char [] generateCheckCode() 
    { 
       // ������֤����ַ��� 
       String chars = "0123456789";
       char [] rands = new char[4]; 
       for(int i=0; i<4; i++) 
       { 
              int rand = (int)(Math.random() * 10); 
              if(i ==0 || i == 2)
              {
            	  rand = rand%3;
              }
              else
              {
            	  if(rands[i-1]== '0' && rand == 0)
            		  rand++;
              }
              rands[i] = chars.charAt(rand); 
       }
       return rands; 
    } 
    public static void drawRands(Graphics g , char [] rands) 
    { 
       g.setColor(Color.darkGray); 
       g.setFont(new Font(null,Font.ITALIC|Font.BOLD,18)); 
       // �ڲ�ͬ�ĸ߶��������֤���ÿ���ַ�          
       g.drawString("" + rands[0] + rands[1],1,18); 
//       g.drawString("" + rands[1],16,15); 
       g.drawString("" + rands[2]+ rands[3],32,18); 
//       g.drawString("" + rands[3],46,16); 
       //System.out.println(rands); 
    } 
    public static void drawBackground(Graphics g) 
	{ 
    	// ������ 
    	g.setColor(new Color(0x87CEEB)); 
    	g.fillRect(0, 0, WIDTH, HEIGHT); 
    	// ������� 120 �����ŵ� 
    	for(int i=0; i<222; i++) 
    	{ 
    		int x = (int)(Math.random() * WIDTH); 
    		int y = (int)(Math.random() * HEIGHT); 
    		int red = (int)(Math.random() * 255); 
    		int green = (int)(Math.random() * 255); 
    		int blue = (int)(Math.random() * 255); 
    		g.setColor(new Color(red,green,blue));        
    		g.drawOval(x,y,2,0); 
    	} 
	} 
}
