package edu.nju.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.text.AttributedCharacterIterator;

import javax.swing.*;

import edu.nju.model.impl.GameModelImpl;




public class PCwinDialog {
	
	private JDialog dialog;
	private MyPanel panel;
	private JLabel label1;
	private JLabel label2;
	private  JPanel LabelPanel;
	private int time=0;
	public PCwinDialog(JFrame parent) {   
		super();
		setTime(GameModelImpl.time);
		initialization(parent);
		time=0;
	}

	public boolean show() {
		dialog.setVisible(true);
		return false;
	}
	public void setTime(int time){
		this.time= time;
	}
	public void initialization(JFrame parent){
		dialog = new JDialog(parent, "PCwin", true);
		panel = new MyPanel();
		LabelPanel = new JPanel();
		label1=new JLabel("Your time: ");
		label2= new JLabel(String.valueOf(time));
		label1.setFont(new Font("Verdana", Font.PLAIN, 14));
		label2.setFont(new Font("Verdana", Font.PLAIN, 14));
		LabelPanel.setLayout(new FlowLayout(FlowLayout.CENTER,10,10));
		LabelPanel.add(label1);
		LabelPanel.add(label2);
		dialog.add(panel,BorderLayout.CENTER);
		dialog.add(LabelPanel,BorderLayout.SOUTH);
		dialog.setBounds(parent.getLocation().x + 50,
				parent.getLocation().y + 50, 250, 330);
	}
	
	private class MyPanel extends JPanel {
	    @Override
	    protected void paintComponent(Graphics g) {
	    	// TODO Auto-generated method stub
	    	super.paintComponent(g);
	    	g.drawImage(Images.win, 0,0, 250, 320, null);
	    	g.setColor(Color.BLACK);
	    	
	    }
	}
	 
	
}
