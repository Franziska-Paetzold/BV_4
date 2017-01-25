// BV Ue04 WS2016/17 Vorgabe
//
// Copyright (C) 2015 by Klaus Jung

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.*;
import java.awt.*;
import java.io.File;

public class ImageAnalysis extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private static final String author = "Lai, Pätzold";		// TODO: type in your name here
	private static final String initialFilename = "mountains.png";
	private static final File openPath = new File(".");
	private static final int border = 10;
	private static final int maxWidth = 910; 
	private static final int maxHeight = 910; 
	private static final int graySteps = 256;
	
	private static JFrame frame;
	
	private ImageView imgView;						// image view
	private HistoView histoView = new HistoView();	// histogram view
	private StatsView statsView = new StatsView();	// statistics values view
	private JSlider brightnessSlider;				// brightness Slider
	
	// TODO: add an array to hold the histogram of the loaded image
	public int[] histogram = new int[graySteps];
	
	// TODO: add an array that holds the ARGB-Pixels of the originally loaded image
	public int[] argbOrigPixels;
	
	// TODO: add an array that holds the ARGB-Pixels of the newly loaded image
	public int[] argbNewPixels;
	
	// TODO: add a contrast slider
	private JSlider contrastSlider;
	private JSlider quantificationSlider;
	
	private JLabel statusLine;				// to print some status text
	
	/**
	 * Constructor. Constructs the layout of the GUI components and loads the initial image.
	 */
	public ImageAnalysis() {
        super(new BorderLayout(border, border));
        
        // load the default image
        File input = new File(initialFilename);
        
        if(!input.canRead()) input = openFile(); // file not found, choose another image
        
        imgView = new ImageView(input);
        imgView.setMaxSize(new Dimension(maxWidth, maxHeight));
        
        // TODO: initialize the original ARGB-Pixel array from the loaded image
        argbOrigPixels = imgView.getPixels();
        argbNewPixels = imgView.getPixels().clone();
        
		// load image button
        JButton load = new JButton("Open Image");
        load.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		File input = openFile();
        		if(input != null) {
        			imgView.loadImage(input);
        			imgView.setMaxSize(new Dimension(maxWidth, maxHeight));
        			
        	        // TODO: initialize the original ARGB-Pixel array from the newly loaded image
        	        argbOrigPixels = imgView.getPixels();
        	        argbNewPixels = imgView.getPixels().clone();
        			
        			frame.pack();
	                processImage();
        		}
        	}        	
        });
         
        JButton reset = new JButton("Reset Slider");
        reset.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		brightnessSlider.setValue(0);
        		
        		// TODO: reset contrast slider
        		contrastSlider.setValue(100);
        		quantificationSlider.setValue(10);
        		imgView.setPixels(argbOrigPixels);
        		processImage();
	    	}        	
	    });
         
        JButton autocontrast = new JButton("Autocontrast");
        autocontrast.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		processImageAutocontrast();
//        		histoView.setHistogram(statsView.autocontrast());
        		
	    	}        	
	    });
        
        // some status text
        statusLine = new JLabel(" ");
        
        // top view controls
        JPanel topControls = new JPanel(new GridBagLayout());
        topControls.add(load);
        topControls.add(reset);
        topControls.add(autocontrast);
        
        // center view
        JPanel centerControls = new JPanel();
        JPanel rightControls = new JPanel();
        rightControls.setLayout(new BoxLayout(rightControls, BoxLayout.Y_AXIS));
        centerControls.add(imgView);
        rightControls.add(histoView);
        rightControls.add(statsView);
        centerControls.add(rightControls);
        
        // bottom view controls
        JPanel botControls = new JPanel();
        botControls.setLayout(new BoxLayout(botControls, BoxLayout.Y_AXIS));
        
        // brightness slider
        brightnessSlider = new JSlider(-graySteps, graySteps, 0);
		TitledBorder titBorder = BorderFactory.createTitledBorder("Brightness");
		titBorder.setTitleColor(Color.GRAY);
        brightnessSlider.setBorder(titBorder);
        brightnessSlider.addChangeListener(new ChangeListener() {
        	public void stateChanged(ChangeEvent e) {
        		processImage();				
        	}        	
        });
        
        // TODO: setup contrast slider

        // contrast slider
//      contrastSlider = new JSlider(-50,100,0); // range taken from photoshop
        contrastSlider = new JSlider(0,200,100);
		TitledBorder titBorderC = BorderFactory.createTitledBorder("Contrast");
		titBorderC.setTitleColor(Color.GRAY);
		contrastSlider.setBorder(titBorderC);
		contrastSlider.addChangeListener(new ChangeListener() {
        	public void stateChanged(ChangeEvent e) {
        		processImage();				
        	}        	
        });
		
		quantificationSlider = new JSlider(10,200,10);
		TitledBorder titBorderQ = BorderFactory.createTitledBorder("Quantification");
		titBorderC.setTitleColor(Color.GRAY);
		quantificationSlider.setBorder(titBorderQ);
		quantificationSlider.addChangeListener(new ChangeListener() {
        	public void stateChanged(ChangeEvent e) {
        		processImage();				
        	}        	
        });
		
        botControls.add(contrastSlider);
        botControls.add(brightnessSlider);
        botControls.add(quantificationSlider);
        statusLine.setAlignmentX(Component.CENTER_ALIGNMENT);
        botControls.add(statusLine);
        
        // add to main panel
        add(topControls, BorderLayout.NORTH);
        add(centerControls, BorderLayout.CENTER);
        add(botControls, BorderLayout.SOUTH);
               
        // add border to main panel
        setBorder(BorderFactory.createEmptyBorder(border,border,border,border));
        
        // perform the initial rotation
        processImage();
	}
	

	/**
	 * Set up and show the main frame.
	 */
	private static void createAndShowGUI() {
		// create and setup the window
		frame = new JFrame("Image Analysis - " + author);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JComponent contentPane = new ImageAnalysis();
        contentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(contentPane);

        // display the window
        frame.pack();
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        frame.setLocation((screenSize.width - frame.getWidth()) / 2, (screenSize.height - frame.getHeight()) / 2);
        frame.setVisible(true);
	}

	/**
	 * Main method. 
	 * @param args - ignored. No arguments are used by this application.
	 */
	public static void main(String[] args) {
        // schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
	}
	
	
	/**
	 * Open file dialog used to select a new image.
	 * @return The selected file object or null on cancel.
	 */
	private File openFile() {
		// file open dialog
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Images (*.jpg, *.png, *.gif)", "jpg", "png", "gif");
        chooser.setFileFilter(filter);
        chooser.setCurrentDirectory(openPath);
        int ret = chooser.showOpenDialog(this);
        if(ret == JFileChooser.APPROVE_OPTION) return chooser.getSelectedFile();
        return null;		 
	}
	
	
	protected void processImage() {
		long startTime = System.currentTimeMillis();

		// TODO: add your processing code here
		int changed[] = argbNewPixels.clone();
		int brightness = brightnessSlider.getValue();
		double contrast = contrastSlider.getValue()/100.0;
		double quantification = quantificationSlider.getValue()/10.0;
//		int[] g = new int[maxHeight*maxWidth];//Graustufe des Pixels
//		int[] q= new int[g.length]; //Quantisierungsindex
//		int[]gq = new int[q.length]; //quantisierte Graustufte
		int g=0; //Graustufte des Pixels
		int q=0; // Quntisierungsindex
		int gq=0; //quantisiserte Graustufe
				
		histogram = new int[graySteps]; // histogram with 256 grey scale values
		
		for (int i = 0; i < changed.length; i++) {
				int j = changed[i] & 0xFF;
				
				
				// formula taken from GDM
				//f(j) = contrast * (j-127.5) + 127.5 + brightness
				int newPix = (int) Math.round((contrast * (j - 127.5 + brightness) + 127.5));
				
				
				
				//Quantisierung
//				g[i]=newPix;
//				q[i] =  g[i]/delta;
				g= newPix;
				
				double delta=quantification;
				//System.out.println(quantification);
				q=(int) ((g/delta)+0.5);
				gq= (int) ((q*delta)+0.5);
				
				newPix=gq;
				
				// handle over- and underflow
				if (newPix>255) newPix = 255;
				else if (newPix<0) newPix = 0;
				
				changed[i] = (0xFF << 24) |(newPix<<16) | (newPix << 8) | newPix;
				histogram[newPix]++;
			}

		imgView.setPixels(changed);
		histoView.setHistogram(histogram);
		statsView.setHistogram(histogram);
		
		System.out.println(contrast + " " + brightness);
		
		imgView.applyChanges();
		histoView.update();
		statsView.update();

		// show processing time
		long time = System.currentTimeMillis() - startTime;
		statusLine.setText("Processing time = " + time + " ms.");
	}
	
	protected void processImageAutocontrast() {
		long startTime = System.currentTimeMillis();

		// TODO: add your processing code here
		int changed[] = argbNewPixels.clone();
				
		histogram = new int[graySteps]; // histogram with 256 grey scale values
		
		for (int i = 0; i < changed.length; i++) {
				int j = changed[i] & 0xFF;
				
				int newPix = statsView.autocontrastNew(j);
				
				// handle over- and underflow
				if (newPix>255) newPix = 255;
				else if (newPix<0) newPix = 0;
				
				changed[i] = (0xFF << 24) |(newPix<<16) | (newPix << 8) | newPix;
				histogram[newPix]++;
			}

		imgView.setPixels(changed);
		histoView.setHistogram(histogram);
		statsView.setHistogram(histogram);
		
		imgView.applyChanges();
		histoView.update();
		statsView.update();

		// show processing time
		long time = System.currentTimeMillis() - startTime;
		statusLine.setText("Processing time = " + time + " ms.");
	}
	

}
