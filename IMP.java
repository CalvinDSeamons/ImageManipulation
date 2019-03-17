/*
 *Calvin Seamons (Methods created)
 *Hunter Llyod (GUI Framework created)
 *
 */



//########################## Note for Grader ################################3//
//For the histogram and equlizer function you have to "add the effect" then run histogram, then press start to populate the grpahs
//Once that is complete you can press equalize to see the equalize effect of the manipulated photo.
//Otherwise the image will pull old data from the graph 
//to use in the equalization calculation. Lastly pressing start builds the data in the graphs. 
//###############################################################################//


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.awt.image.PixelGrabber;
import java.awt.image.MemoryImageSource;
import java.util.prefs.Preferences;

class IMP implements MouseListener{
   //Here be all me global variables!!
   JFrame frame;
   JPanel mp;
   JButton start;
   JScrollPane scroll;
   JMenuItem openItem, exitItem, resetItem;
   Toolkit toolkit;
   File pic;
   ImageIcon img;
   //Here is the backup for the image reset.
   ImageIcon backup;
   int colorX, colorY;
   int [] pixels;
   int [] results;
   int redHArray[] = new int[256];
   int blueHArray[] = new int[256];
   int greenHArray[] = new int[256];  

   //Instance Fields you will be using below
   
   //This will be your height and width of your 2d array
   int height=0, width=0, superHeight=0, superWidth=0;
   
   //your 2D array of pixels
    int picture[][];
    int newPic[][];
    int resetPicture[][];
    int backuppic[][];

    /* 
     * In the Constructor I set up the GUI, the frame the menus. The open pulldown 
     * menu is how you will open an image to manipulate. 
     */
   IMP()
   {
      toolkit = Toolkit.getDefaultToolkit();
      frame = new JFrame("Image Processing Software by Calvin Seamons");
      JMenuBar bar = new JMenuBar();
      JMenu file = new JMenu("File");
      JMenu functions = getFunctions();
      frame.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent ev){quit();}});
      		openItem = new JMenuItem("Open");
      		openItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent evt){ handleOpen(); }});
      		resetItem = new JMenuItem("Reset");
      		resetItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent evt){ uberReset(); }});     
      		exitItem = new JMenuItem("Exit");
      		exitItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent evt){ quit(); }});
      file.add(openItem);
      file.add(resetItem);
      file.add(exitItem);
      bar.add(file);
      bar.add(functions);
      frame.setSize(1200, 900);
      mp = new JPanel();
      mp.setBackground(new Color(0, 0, 0));
      scroll = new JScrollPane(mp);
      frame.getContentPane().add(scroll, BorderLayout.CENTER);
      JPanel butPanel = new JPanel();
      butPanel.setBackground(Color.black);
      start = new JButton("start");
      start.setEnabled(true);
      butPanel.add(start);
      frame.getContentPane().add(butPanel, BorderLayout.SOUTH);
      frame.setJMenuBar(bar);
      frame.setVisible(true);      
   }
   
   /* 
    * This method creates the pulldown menu and sets up listeners to selection of the menu choices. 
    * If the listeners are activated they call the methods 
    * for handling the choice, fun1, fun2, fun3, fun4, etc. etc. 
    */
   
  private JMenu getFunctions()
  {
     JMenu fun = new JMenu("Functions");
     
     JMenuItem firstItem  = new JMenuItem("Function 1: Blue Hue"            );
     JMenuItem secondItem = new JMenuItem("Function 2: Gray-Scale"          );
     JMenuItem thirdItem  = new JMenuItem("Function 1: Rotate Image"        );
     JMenuItem forthItem  = new JMenuItem("Function 7: Locate Orange Colors");
     JMenuItem sevenItem  = new JMenuItem("Function 5: Histogram"           );
     JMenuItem fithItem   = new JMenuItem("Function 3: Blur Image"          );
     JMenuItem sixthItem  = new JMenuItem("Function 4: Edge Detection"      );
     JMenuItem eightItem  = new JMenuItem("Function 6: Equalizer"           );
     //Here i am adding new functions for the lab.

    
     firstItem.addActionListener(new ActionListener(){
     	@Override
        public void actionPerformed(ActionEvent evt){fun1();}});
   
     secondItem.addActionListener(new ActionListener(){
	@Override
	public void actionPerformed(ActionEvent evt1){fun4();}});

     thirdItem.addActionListener(new ActionListener(){
	@Override
	public void actionPerformed(ActionEvent evt2){fun3();}});

     forthItem.addActionListener(new ActionListener(){
	@Override
	public void actionPerformed(ActionEvent evn4){fun5();}});

     fithItem.addActionListener(new ActionListener(){
	@Override
	public void actionPerformed(ActionEvent evn5){fun6();}});
       
     sixthItem.addActionListener(new ActionListener(){
	@Override
	public void actionPerformed(ActionEvent env6){fun7();}});

     sevenItem.addActionListener(new ActionListener(){
	@Override
	public void actionPerformed(ActionEvent env7){fun8();}});

     eightItem.addActionListener(new ActionListener(){
	@Override
	public void actionPerformed(ActionEvent env7){fun9();}});

     fun.add(thirdItem);
     fun.add(secondItem);
     fun.add(fithItem);
     fun.add(sixthItem);
     fun.add(sevenItem);
     fun.add(eightItem);
     fun.add(forthItem);

     return fun;   
     }
  
  /*
   * This method handles opening an image file, breaking down the picture to a one-dimensional array and then drawing the image on the frame. 
   * You don't need to worry about this method. 
   */
    private void handleOpen(){  
    	img = new ImageIcon();
     	JFileChooser chooser = new JFileChooser();
      	Preferences pref = Preferences.userNodeForPackage(IMP.class);
      	String path = pref.get("DEFAULT_PATH", "");
      	chooser.setCurrentDirectory(new File(path));
     	int option = chooser.showOpenDialog(frame);
     	if(option == JFileChooser.APPROVE_OPTION){
        	pic = chooser.getSelectedFile();
        	pref.put("DEFAULT_PATH", pic.getAbsolutePath());
       		img = new ImageIcon(pic.getPath());
      		}
    	width = img.getIconWidth();
     	height = img.getIconHeight(); 
	superWidth = img.getIconWidth();
	superHeight = img.getIconHeight();
     	JLabel label = new JLabel(img);
     	label.addMouseListener(this);
     	pixels = new int[width*height];  
     	results = new int[width*height];  
     	Image image = img.getImage();
     	PixelGrabber pg = new PixelGrabber(image, 0, 0, width, height, pixels, 0, width );
     	try{
        	pg.grabPixels();
     	   }
	catch(InterruptedException e){
        	System.err.println("Interrupted waiting for pixels");
          	return;
       	}  
     	for(int i = 0; i<width*height; i++)
        	results[i] = pixels[i];  
     		turnTwoDimensional();
     		mp.removeAll();
     		mp.add(label); 
     		mp.revalidate();
  }



  
  /*
   * The libraries in Java give a one dimensional array of RGB values for an image, I thought a 2-Dimensional array would be more usefull to you
   * So this method changes the one dimensional array to a two-dimensional. 
   */
  private void turnTwoDimensional()
  {
     picture = new int[height][width];
     resetPicture = new int[height][width];
     backuppic = new int[height][width];
     for(int i=0; i<height; i++){
       for(int j=0; j<width; j++){
          picture[i][j] = pixels[i*width+j];
     	  resetPicture[i][j] = pixels[i*width+j];
	  backuppic[i][j] = pixels[i*width+j];
       }
     }
  }
  /*
   *  This method takes the picture back to the original picture
   */
  private void reset()
  {
        for(int i = 0; i<width*height; i++)
             pixels[i] = results[i]; 
       Image img2 = toolkit.createImage(new MemoryImageSource(width, height, pixels, 0, width)); 

      JLabel label2 = new JLabel(new ImageIcon(img2));    
       mp.removeAll();
       mp.add(label2);
     
       mp.revalidate();
       
    }


  private void uberReset(){

    width = superWidth;
    height = superHeight;
    Image image = img.getImage();
      JLabel label = new JLabel(new ImageIcon(image));

    PixelGrabber pg = new PixelGrabber(image, 0, 0, width, height, pixels, 0, width );
    try{
      pg.grabPixels();
    }catch(InterruptedException e)
    {
      System.err.println("Interrupted waiting for pixels");
      return;
    }
    for(int i = 0; i<width*height; i++)
    results[i] = pixels[i];
    turnTwoDimensional();
    mp.removeAll();
    mp.add(label);

    mp.revalidate();
    mp.repaint();

  }




  /*
   * This method is called to redraw the screen with the new image. 
   */
  private void resetPicture()
  {
       for(int i=0; i<height; i++)
       for(int j=0; j<width; j++)
          pixels[i*width+j] = picture[i][j];
      Image img2 = toolkit.createImage(new MemoryImageSource(width, height, pixels, 0, width)); 

      JLabel label2 = new JLabel(new ImageIcon(img2));    
       mp.removeAll();
       mp.add(label2);
     
       mp.revalidate(); 
   
    }

  private void resetNewPic(int[][] x){
	//this resets the new picture from the manipulated functions. 
	for(int i=0;i<width;i++)
	for(int j=0;j<height;j++)
	pixels[i*height+j] = x[i][j];

	Image img2 = toolkit.createImage(new MemoryImageSource(height,width, pixels, 0, height));

        JLabel label2 = new JLabel(new ImageIcon(img2));
        mp.removeAll();
        mp.add(label2);

        mp.revalidate();
	mp.repaint();
  }

  private void rotate(int[][] x){
	//rotate swaps the heifght and width so 
	//the swaped photo can load properly in tjhe correct dimensions
	int temp = height;
	height = width;
	width = temp;
	picture = new int[height][width];
	for(int i = 0; i<height; i++){
		for(int j = 0; j<width; j++){
		
		picture[i][j] = x[i][j];

		}
	}
	
	resetPicture();
	mp.repaint();
  }
    /*
     * This method takes a single integer value and breaks it down doing bit manipulation to 4 individual int values for A, R, G, and B values
     */
  private int [] getPixelArray(int pixel)
  {
      int temp[] = new int[4];
      temp[0] = (pixel >> 24) & 0xff;
      temp[1]   = (pixel >> 16) & 0xff;
      temp[2] = (pixel >>  8) & 0xff;
      temp[3]  = (pixel      ) & 0xff;
      return temp;
      
    }
    /*
     * This method takes an array of size 4 and combines the first 8 bits of each to create one integer. 
     */
  private int getPixels(int rgb[])
  {
         int alpha = 0;
         int rgba = (rgb[0] << 24) | (rgb[1] <<16) | (rgb[2] << 8) | rgb[3];
        return rgba;
  }
  
  public void getValue()
  {
      int pix = picture[colorY][colorX];
      int temp[] = getPixelArray(pix);
      System.out.println("Color value " + temp[0] + " " + temp[1] + " "+ temp[2] + " " + temp[3]);
    }
  
  /**************************************************************************************************
   * This is where you will put your methods. Every method below is called when the corresponding pulldown menu is 
   * used. As long as you have a picture open first the when your fun1, fun2, fun....etc method is called you will 
   * have a 2D array called picture that is holding each pixel from your picture. 
   *************************************************************************************************/
   /*
    * Example function that just removes all red values from the picture. 
    * Each pixel value in picture[i][j] holds an integer value. You need to send that pixel to getPixelArray the method which will return a 4 element array 
    * that holds A,R,G,B values. Ignore [0], that's the Alpha channel which is transparency, we won't be using that, but you can on your own.
    * getPixelArray will breaks down your single int to 4 ints so you can manipulate the values for each level of R, G, B. 
    * After you make changes and do your calculations to your pixel values the getPixels method will put the 4 values in your ARGB array back into a single
    * integer value so you can give it back to the program and display the new picture. 
    */
  private void fun1()
  {
     
    for(int i=0; i<height; i++)
       for(int j=0; j<width; j++)
       {   
          int rgbArray[] = new int[4];
         
          //get three ints for R, G and B
          rgbArray = getPixelArray(picture[i][j]);
         
        
           rgbArray[1] = 0;
           //take three ints for R, G, B and put them back into a single int
           picture[i][j] = getPixels(rgbArray);
        } 
     resetPicture();
  }



 

	private void fun4(){
		//super simple gray scale.
		//
		//I average the RGB based on the luminosity scale and add them across all channels.
		int rgb[] = new int[4];
		for(int i=0; i<height; i++){
			for(int j=0; j<width; j++){
				rgb = getPixelArray(picture[i][j]);
				
				int total =(int) (rgb[1]*.21)+(int)(rgb[2]*.72)+(int)(rgb[3]*.07);
				rgb[1] = (total);
				rgb[2] = (total);
				rgb[3] = (total);

				picture[i][j] = getPixels(rgb);
			}
			resetPicture();
		}
	}	


	private void fun3(){
		System.out.println("Trying to rotate photo");
		newPic = new int[width][height];
		//this funtion rotates the photo by shifting the pixels into a new photo
		//that has and inverse and swap of the hight and width.
		for(int i =0; i<height; i++)
			for(int j =0; j<width; j++){
				newPic[j][(height-1)-i] = picture[i][j];
			}	
		rotate(newPic);




	


  } 

   private void fun5(){
	//this function find orange and colors everything else black. 
	int rgb[] = new int[4];
	for(int i = 0; i<height; i++){
		for(int j = 0; j<width; j++){
			//really basic color tracker. this takes the picture and turn it black or white 
			//based on a rgb threshold i set the determine if a given color is present. 
			rgb = getPixelArray(picture[i][j]);
			
			if(rgb[1] > 245 && rgb[2]<150){
				rgb[1] = 255;
				rgb[2] = 255;
				rgb[3] = 255;
			}else{
				rgb[1] = 0;
				rgb[2] = 0;
				rgb[3] = 0;
			}
			picture[i][j] = getPixels(rgb);
		}
	}
		
	resetPicture();
   }

  private void fun6(){
	int rgb[] = new int[4];
        int blurPic[][] = new int[height][width];
	//This function blurs the photo.
	//it works by suming the outside edge then avergaing it and adding that value to the center.
	for(int i = 2; i < height-3; i++){
		for(int j = 2; j < width-3; j++){
			rgb = getPixelArray(picture[i][j]);
			int red =rgb[1];
			int green =rgb[2]; 
			int blue =rgb[3];
			int r1 = rgb[1];
			int g1 = rgb[2];
			int b1 = rgb[3];
			for(int o = -2; o < 3; o++){
				for(int p = -2; p<3; p++){
				
				rgb = getPixelArray(picture[i-o][j-p]);
				red += rgb[1];
				green += rgb[2];
				blue += rgb[3];

				}	

			}
		rgb[1] = (red)/27;
		rgb[2] = (green)/27;
		rgb[3] = (blue)/27;
		blurPic[i][j] = getPixels(rgb);	
		}
	}
	
	for(int b=0; b<height; b++){
		for(int c=0; c<width;c++){
		picture[b][c] = blurPic[b][c];
		}
	}

	resetPicture();
  }

  private void drawHistogram(){
    //This is code hunter gave us that i tweeked for the 3 histogram panels we needed to create.    
    JFrame redFrame = new JFrame("Red");
    redFrame.setSize(305, 600);
    redFrame.setLocation(600, 0);
    JFrame greenFrame = new JFrame("Green");
    greenFrame.setSize(305, 600);
    greenFrame.setLocation(905, 0);
    JFrame blueFrame = new JFrame("blue");
    blueFrame.setSize(305, 600);
    blueFrame.setLocation(1210, 0);
    MyPanel redPanel = new MyPanel();
    MyPanel greenPanel = new MyPanel();
    MyPanel bluePanel = new MyPanel();
    redFrame.getContentPane().add(redPanel, BorderLayout.CENTER);
    greenFrame.getContentPane().add(greenPanel, BorderLayout.CENTER);
    blueFrame.getContentPane().add(bluePanel, BorderLayout.CENTER);
    redFrame.setVisible(true);
    greenFrame.setVisible(true);
    blueFrame.setVisible(true);
    start.setEnabled(true);
    start.addActionListener(new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent evt){

        redPanel.drawing(redHArray, height, 0);
        greenPanel.drawing(greenHArray, height, 1);
        bluePanel.drawing(blueHArray, height, 2);
      }
    });
  }


  private void fun7(){
	//the calls below gray the image then blur it 3x
	fun4();
	fun6();
	fun6();
	fun6();
	
	//here i init all the rgb channels i use for my calculations
	int rgb0[] = new int[4], rgb1[] = new int[4], rgb2[] = new int[4], rgb3[] = new int[4], rgb4[] = new int[4];
	int rgb5[] = new int[4], rgb6[] = new int[4], rgb7[] = new int[4], rgb8[] = new int[4], rgb9[] = new int[4];
	int rgb10[] = new int[4], rgb11[] = new int[4], rgb12[] = new int[4], rgb13[] = new int[4], rgb14[] = new int[4];
	int rgb15[] = new int[4], rgb16[] = new int[4];
	int edge[][] = new int[height][width];
	
	//go throw the loop and take all the outside pixels from the center and sum them together.
	for(int i = 2; i<height-3; i++){
		for(int j = 2; j<width-3; j++){
			//these arrays all are assign the specifc grid layout to edge detect.
			rgb0  = getPixelArray(picture[i][j]);
			rgb1  = getPixelArray(picture[i+2][j-2]);
			rgb2  = getPixelArray(picture[i+2][j-1]);  
			rgb3  = getPixelArray(picture[i+2][j]); 
			rgb4  = getPixelArray(picture[i+2][j+1]);
                        rgb5  = getPixelArray(picture[i+2][j+2]);
                        rgb6  = getPixelArray(picture[i+1][j-2]);
                        rgb7  = getPixelArray(picture[i+1][j+2]);
			rgb8  = getPixelArray(picture[i][j-2]);
                        rgb9  = getPixelArray(picture[i][j+2]);
                        rgb10 = getPixelArray(picture[i-1][j-2]);
                        rgb11 = getPixelArray(picture[i-1][j+2]);
			rgb12 = getPixelArray(picture[i-2][j-2]);
                        rgb13 = getPixelArray(picture[i-2][j-1]);
                        rgb14 = getPixelArray(picture[i-2][j]);
                        rgb15 = getPixelArray(picture[i-2][j+1]);
			rgb16 = getPixelArray(picture[i-2][j+2]);
			//middle is itself * 16
			int middleTotal = ((rgb0[1])*16);
			// edges are summed (16 in total) 
			int edgeTotal = (-1*(rgb1[1]))+(-1*(rgb2[1]))+(-1*(rgb3[1]))+(-1*(rgb4[1]))+
					(-1*(rgb5[1]))+(-1*(rgb6[1]))+(-1*(rgb7[1]))+(-1*(rgb8[1]))+
					(-1*(rgb9[1]))+(-1*(rgb10[1]))+(-1*(rgb11[1]))+(-1*(rgb12[1]))+
					(-1*(rgb13[1]))+(-1*(rgb14[1]))+(-1*(rgb15[1]))+(-1*(rgb16[1]));
			//we the ncalculate this distance. based on that we determine if its an edge or not. 
			int result = middleTotal+edgeTotal;
			//System.out.println("Middle Total: "+ middleTotal + " Edge Total: " + edgeTotal + " Result: " + result);
			if(result > -18 && result < 18){
				rgb0[1] = 0;
				rgb0[2] = 0;
				rgb0[3] = 0;
				edge[i][j] = getPixels(rgb0);
			}
			else{
				rgb0[1] = 255;
				rgb0[2] = 255;
				rgb0[3] = 255;
				edge[i][j] = getPixels(rgb0);
			}		        	
			
		}
	}
	int rgb[] = new int[4];
	//these loops set the channles based on the results and set either a black or white image. 
	for(int g = 0; g<height; g++){
		for(int h = 0; h<width; h++){
			rgb = getPixelArray(edge[g][h]);

			if(g < 10 || h < 10 || g > height-10 || h > width-10){

			rgb[1] = 0;
			rgb[2] = 0;
			rgb[3] = 0;
			edge[g][h] = getPixels(rgb);

			}



		}

	}


	for(int a =0; a<height; a++){
		for(int b = 0; b<width; b++){
			picture[a][b] = edge[a][b];
		}
	}
	resetPicture();
}

private void fun8(){
	//function 8 populates the histogram. it goes throw and +1 the channel based on its value. 
	int x = 0;
    	for(int i=0; i<height; i++)
    	for(int j=0; j<width; j++){
      		int rgbArray[] = new int[4];
      		rgbArray = getPixelArray(picture[i][j]);
      		int red = rgbArray[1];
      		int green = rgbArray[2];
      		int blue = rgbArray[3];
      		redHArray[red] += 1;
      		greenHArray[green] += 1;
      		blueHArray[blue] += 1;
    }
    drawHistogram();
  }

private void fun9(){
	//This is the equalizer function.
	int totalRed = 0;
	int totalGreen = 0;
	int totalBlue = 0;
	int pixelsTotal = width*height;
	int equal[][] = new int[height][width];
	float[] dataR = new float[pixelsTotal];
	float[] dataG = new float[pixelsTotal];
	float[] dataB = new float[pixelsTotal];
	int rgb[] = new int[4];
	//it takes the histogram data and sums it and divides by total pixels in the photo.
	for(int i = 0; i< 255; i++){
		totalRed += redHArray[i];
		totalGreen += greenHArray[i];
		totalBlue += blueHArray[i];
		dataR[i] = (totalRed*255)/pixelsTotal;
		dataG[i] = (totalGreen*255)/pixelsTotal;
		dataB[i] = (totalBlue*255)/pixelsTotal;
	}

	for(int j =0; j<height; j++){
		for(int k=0; k<width; k++){
			rgb = getPixelArray(picture[j][k]);
			int redVal = (int)dataR[rgb[1]];
			int greenVal = (int)dataG[rgb[2]];
			int blueVal = (int)dataB[rgb[3]];
			//here we set the rgb chanel to the calculated value of chanel with the equalizer put into effect.
			rgb[1] = redVal;
			rgb[2] = greenVal;
			rgb[3] = blueVal;
			equal[j][k] = getPixels(rgb);

		}

	}

	for(int a = 0; a<height; a++){
		for(int b = 0; b<width; b++){
			
			picture[a][b] = equal[a][b];

		}

	}
	//these resets the histogams for future use. 
	for(int m = 0; m<256; m++){
	redHArray[m] = 0;
	greenHArray[m] = 0;
	blueHArray[m] = 0;
	}

	resetPicture();
	fun8();






}
    
  private void quit()
  {  
     System.exit(0);
  }

    @Override
   public void mouseEntered(MouseEvent m){}
    @Override
   public void mouseExited(MouseEvent m){}
    @Override
   public void mouseClicked(MouseEvent m){
        colorX = m.getX();
        colorY = m.getY();
        System.out.println(colorX + "  " + colorY);
        getValue();
        start.setEnabled(true);
    }
    @Override
   public void mousePressed(MouseEvent m){}
    @Override
   public void mouseReleased(MouseEvent m){}
   
   public static void main(String [] args)
   {
      IMP imp = new IMP();
   }
 
}
