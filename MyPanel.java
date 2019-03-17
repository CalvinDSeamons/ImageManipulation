
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class MyPanel extends JPanel
{
 
int startX, flag, startY, endX, endY;

    BufferedImage grid;
    Graphics2D gc;

	public MyPanel()
	{
	   startX = startY = 0;
           endX = endY = 100;
 	}

     public void clear()
    {
       grid = null;
       repaint();
    }
    public void paintComponent(Graphics g)
    {  
         super.paintComponent(g);
         Graphics2D g2 = (Graphics2D)g;
         if(grid == null){
            int w = this.getWidth();
            int h = this.getHeight();
            grid = (BufferedImage)(this.createImage(w,h));
            gc = grid.createGraphics();

         }
         g2.drawImage(grid, null, 0, 0);
     }
    public void drawing(int[] x, int height, int z)
    {
        if(z==0){
	gc.setColor(Color.RED);
	}
	else if(z==1){
	gc.setColor(Color.GREEN);
	}
	else if(z==2){
	gc.setColor(Color.BLUE);
	}

	for(int i =0; i<256;i++){

        gc.drawLine(i+10, 500, i+10, 500-(x[i]/3));
	}
        repaint();
	
    }
   
}
