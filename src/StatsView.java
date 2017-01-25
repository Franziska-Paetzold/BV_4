

import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;


public class StatsView extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final String[] names = { "Min:", "Max:", "Average:", "Median:", "Variance:", "Entropy:" }; 
	private static final int rows = names.length;
	private static final int border = 2;
	private static final int columns = 2;
	private static final int graySteps = 256;
	
	private JLabel[] infoLabel = new JLabel[rows];
	private JLabel[] valueLabel = new JLabel[rows];
	
	private int median = 0;
	double average = 0.0; 
	double varianz = 0.0;
	double entropie = 0.0;

	
	private int[] histogram = null;
	
	public StatsView() {
		super(new GridLayout(rows, columns, border, border));
		TitledBorder titBorder = BorderFactory.createTitledBorder("Statistics");
		titBorder.setTitleColor(Color.GRAY);
		setBorder(titBorder);
		for(int i = 0; i < rows; i++) {
			infoLabel[i] = new JLabel(names[i]);
			valueLabel[i] = new JLabel("-----");
			add(infoLabel[i]);
			add(valueLabel[i]);
		}
	}
	
	private void setValue(int column, int value) {
		valueLabel[column].setText("" + value);
	}
	
	private void setValue(int column, double value) {
		valueLabel[column].setText(String.format(Locale.US, "%.2f", value));
	}
	
	public boolean setHistogram(int[] histogram) {
		if(histogram == null || histogram.length != graySteps) {
			return false;
		}
		this.histogram = histogram;
		update();
		return true;
	}
	
	public boolean update() {
		if(histogram == null) {
			return false;
		}

		// TODO: calculate and display statistic values
		int pixelsSum = getPixelsSum();

		setValue(0, 0);
		setValue(1, 3.1415);
		
		setValue(0, min());
		setValue(1, max());
		setValue(2, average());
		setValue(3, median());
		setValue(4, variance(pixelsSum));
		setValue(5, entropy(pixelsSum));

		return true;
	}
	
	private int getPixelsSum(){
		int pixelsSum = 0;
		
		for(int i=0; i<histogram.length; i++) {
			pixelsSum += histogram[i];
		}
		return pixelsSum;
	}
	
	private int min() {
		/*
		int min;
		if(histogram[0] < histogram[1])
		{
			min=histogram[0];
		}
		else
		{
			min=histogram[1];
		}
		
		for (int j=2; j<256; j++)
		{
			if(histogram[j] < min)
			{
				min = histogram[j];
			}
		}
		return min;*/
		
		int min =0;
		for(int i=0; i<histogram.length; i++)
		if (histogram[i]!=0)
		{
			min=i;
			return min;
		}
		return min;
	}
	
	private int max() {
		int max =0;
		for(int i=255; i>=0; i--)
		if (histogram[i]!=0)
		{
			max=i;
			return max;
		}
		return max;
	}

	private double average() {
		int min = min();
		int max = max();
		double temp = 0;
		int sum=0;
		
		for(int i = min; i<=max; i++){
			temp += i*histogram[i];
			sum+=histogram[i];
		}
		average = temp/sum;
		return average;
		}

	private int median() {
		
		int min= min();
		int max= max();
//		
//		int median = (min + max)/2;
//		return median;
		
		ArrayList<Integer> medianListe = new ArrayList<Integer>();
		
		for(int i=min; i<=max; i++)

		
{
			int j=0;
			while (j<=histogram[i])
					{medianListe.add(i);
					j++;
					}
			}
		int help=medianListe.size()/2;
		median=medianListe.get(help);
		return median;
	}

	private double variance(int pixelsSum) {
		// TODO: variance
		int min = min();
		int max = max();
		double var = 0;
		double average = average();
		
		for(int i = min; i <= max; i++){
			var += Math.pow(i-average, 2)*histogram[i];
		}
		var = var/pixelsSum;
		return var;
	}

	private double entropy(int pixelsSum) {
		//TODO: entropy
		double entropy = 0.0;
		for(int i=0; i < histogram.length; i++) {
			
			double p = (histogram[i] / (double) pixelsSum);
			if (p > 0){
			entropy += p * (Math.log(p) / Math.log(2.0));
			}
		}
		return -entropy;
	}
	
//	public int[] autocontrast()
//	{
//		int temp = max() - min();
//		double offset = 255/ (double) temp;
//		int[] histogramNew = new int[256];
//		for(int i=min(); i<=max(); i++)
//		{
//			int help = (int)(offset*(i-min()));
//			System.out.println(help + " " + i);
//			histogramNew[help]= histogram[i];
//		}
//		setHistogram(histogramNew);
//		return histogramNew;
//	}
	
	private int minP()
	{
		int counter =0;
		for(int i=0; i < histogram.length; i++) {
			if (counter <= getPixelsSum()*0.01)
					{
						counter += histogram[i];
					}
			else
			{
				return i;
			}
		}
		return 0;
	}
	
	private int maxP()
	{
		int counter =0;
		for(int i=0; i < histogram.length; i++) {
			if (counter <= getPixelsSum()*0.99)
					{
						counter += histogram[i];
					}
			else
			{
				return i;
			}
		}
		return 255;
	}
	
	public int autocontrastNew(int i)
	{
		int temp = maxP() - minP();
		//System.out.println(maxP());
		double offset = 255/ (double) temp;
		
		int help = (int)(offset*(i-min()));
		return help;

	}
}
