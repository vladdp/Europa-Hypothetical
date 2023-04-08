package main;

import java.awt.Color;
import java.awt.Graphics;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main extends JPanel {
	private static final long serialVersionUID = 1L;
	
	public static final int WIDTH = 14;
	public static final int LENGTH = 15;
	
	public static final int DIST = 5; // physical distance between height sensors (5 cm)
	
	public static float sensorF[][] = new float[WIDTH][LENGTH]; // sensor near the front
	public static float sensorB[][] = new float[WIDTH][LENGTH]; // sensor near the back
	public static float map[][] = new float[WIDTH][LENGTH+DIST]; // both sensors combined
	public static float h;
	
	private JFrame frame;
	private JPanel panel;
	
	public Main() {
		readDataFromFile();
		
		// creates a new array that includes data from both height sensors
		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < LENGTH + DIST; y++) {
				if (y < DIST) {
					map[x][y] = sensorB[x][y];
				} else if (y < LENGTH) {
					map[x][y] = (sensorF[x][y-DIST] + sensorB[x][y]) / 2;
				} else {
					map[x][y] = sensorF[x][y-DIST];
				}
			}
		}
		
		float min = getMinValue(map);
		float max = getMaxValue(map);
		float diff = max - min; 
		float increment = diff / 15;
		
		String minText = String.valueOf(min);
		String maxText = String.valueOf(max);
		
		Color d[] = new Color[15];
		d[0] = new Color(0, 0, 180);
		int blue = 255;
		int red = 0;
		int green = 0;
		
		// assigns 15 colors to represent different heights
		for (int i = 1; i < 15; i++) {
			d[i] = new Color(red, green, blue);
			red += 17;
			green += 17;
		}
		
		frame = new JFrame();
		panel = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				
				// these two lines draw the min and max values
				g.drawString(minText + " cm", 140, 40);
				g.drawString(maxText + " cm", 300, 40);
				
				// this loop draws the legend
				for (int i = 0; i < 15; i++) {
					g.setColor(d[i]);
					g.fillRect(290-10*i, 50, 10, 20);
				}
				
				// this loop checks the height at (x, y), assigns
				// a color and draws it
				for (int x = 0; x < 14; x++) {
					for (int y = 0; y < LENGTH + DIST; y++) {
						h = map[x][y];
						
						if (h <= min + increment) {
							g.setColor(d[14]);
						} else if (h <= min + 2 * increment) {
							g.setColor(d[13]);
						} else if (h <= min + 3 * increment) {
							g.setColor(d[12]);
						} else if (h <= min + 4 * increment) {
							g.setColor(d[11]);
						} else if (h <= min + 5 * increment) {
							g.setColor(d[10]);
						} else if (h <= min + 6 * increment) {
							g.setColor(d[9]);
						} else if (h <= min + 7 * increment) {
							g.setColor(d[8]);
						} else if (h <= min + 8 * increment) {
							g.setColor(d[7]);
						} else if (h <= min + 9 * increment) {
							g.setColor(d[6]);
						} else if (h <= min + 10 * increment) {
							g.setColor(d[5]);
						} else if (h <= min + 11 * increment) {
							g.setColor(d[4]);
						} else if (h <= min + 12 * increment) {
							g.setColor(d[3]);
						} else if (h <= min + 13 * increment) {
							g.setColor(d[2]);
						} else if (h <= min + 14 * increment) {
							g.setColor(d[1]);
						} else {
							g.setColor(d[0]);
						}
						
						g.fillRect(340-20*x, 100+20*y, 20, 20);
					}
				}
			}
		};
		
		frame.add(panel);
		frame.setSize(500, 600);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	// this function returns the smallest height reading
	public static float getMinValue(float[][] data) {
		float minValue = data[0][0];
        for (int j = 0; j < data.length; j++) {
            for (int i = 0; i < data[j].length; i++) {
                if (data[j][i] < minValue ) {
                    minValue = data[j][i];
                }
            }
        }
        return minValue ;
	}
	
	// this function returns the highest height reading
	public static float getMaxValue(float[][] data) {
		float maxValue = data[0][0];
        for (int j = 0; j < data.length; j++) {
            for (int i = 0; i < data[j].length; i++) {
                if (data[j][i] > maxValue) {
                    maxValue = data[j][i];
                }
            }
        }
        return maxValue;
	}
	
	// this function reads in the data from the text file
	// and stores it to the sensor arrays
	private void readDataFromFile() {
		try {
			File sensorData = new File("Sensor_Data2_Mod.txt");
			Scanner reader = new Scanner(sensorData);
			
			int map_x;
			int map_y;
			float h1, h2;
			
			for (int y = 0; y < LENGTH; y++) {
				for (int x = 0; x < WIDTH; x++) {
					map_x = Integer.parseInt(reader.next());
					map_y = Integer.parseInt(reader.next());
					h1 = Float.parseFloat(reader.next());
					h2 = Float.parseFloat(reader.next());
					
					sensorF[map_x][map_y] = h1;
					sensorB[map_x][map_y] = h2;
				}
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}	
	}
	
	public static void main(String[] args) {
		new Main();
	}
}