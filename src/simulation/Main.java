package simulation;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TimerTask;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Main {
	private static SimIterativeRobot robot;
	public static final int FRAME_WIDTH = 700;
	public static final int FRAME_HEIGHT = 700;
	public static final double PIXELS_PER_INCH =  2.5;
	public static final double FRAMES_PER_SECOND = 30;
	public static Random r = new Random();
	private static int frames = 0;
	public static Map<Object, Object> debug = new HashMap<>();
	public static void startSim() {
		JFrame frame = new JFrame("simulation");
		JComponent drawer = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				
				for (double x = 0; x < FRAME_WIDTH; x += PIXELS_PER_INCH * 12) {
					g.drawLine((int) x, 0, (int) x, FRAME_HEIGHT);
				}
				for (double y = 0; y < FRAME_HEIGHT; y += PIXELS_PER_INCH * 12) {
					g.drawLine(0, (int) y, FRAME_WIDTH, (int) y);
				}
				int xDebug = 20;
				for (Object d : debug.keySet()) {
					g.drawString(d.toString() + ": " + debug.get(d).toString(), 20, xDebug);
					xDebug += 20;
				}
				robot.paintComponent(g);
			}
		};
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        robot = new Robot();
        //Display the window.
        frame.getContentPane().add(drawer);
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setVisible(true);
        java.util.Timer t2 = new java.util.Timer();
        t2.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				robot.update();
				frames++;
				frame.repaint();
			}
		}, 0, 10);
        //t.start();
	}
	public static void main(String[] args) {
	    SwingUtilities.invokeLater(Main::startSim);
	}
	
	public static int getFrames() {
		return frames;
	}

	public static double getTime() {
		return Main.getFrames() / Main.FRAMES_PER_SECOND;
	}

}
