/*
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.nju.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

public class RecordDialog {

	/**
	 *  
	 */ 
	
  	private JDialog dialog;
	private JPanel panel;
	private JPanel textPanel;
	private JPanel btnPanel;
    private JLabel rate;
    private JLabel bestTime;
    private JLabel level;
	private JButton okBtn;
	private JButton clearBtn;
	private JSeparator line;
	private double rates[]={0,0,0,0};
	private int bestTimes[]={999,999,999,999};
	private String[] levels = { "Easy", "Hard", "Hell","Custom" };
	
	
	public RecordDialog(JFrame parent) {   
		super();
		initialization(parent);
	}

	public boolean show() {
		
		dialog.setVisible(true);
		return true;
	}
	
	public void setRatesAndTimes (double []rates ,int [] times){
		this.rates=rates;
		this.bestTimes= times;
	}
	
	

	private void initialization(JFrame parent) {
        rates = new double [4];
        bestTimes = new int [4];
		dialog = new JDialog(parent, "record", true);
		rate = new JLabel();
		bestTime = new JLabel();
		level= new JLabel();
		
        rate.setText("win rate(%)");
        bestTime.setText("best time(s)");
        level.setText("level");
        
		okBtn = new JButton("OK");
		okBtn.setFont(new Font("Monospaced", Font.PLAIN, 12));
		okBtn.setBounds(100, 115, 70, 23);
		okBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
			}
		});

		clearBtn = new JButton("Clear");
		clearBtn.setFont(new Font("Monospaced", Font.PLAIN, 12));
		clearBtn.setBounds(192, 115, 70, 23);
		clearBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				int length = levels.length;
				for (int i = 0; i < length; i++) {
					rates[i] = 0;
					bestTimes[i] = 999;
				}
				textPanel.repaint();
			}
		});

		line = new JSeparator();
		line.setBounds(10, 120, 240, 10);

		panel = new JPanel();
		//这里！
		textPanel = new DescribeTextPanel();
		btnPanel=new JPanel();
		
		panel.setLayout(new FlowLayout(FlowLayout.CENTER,20,10));

		panel.add(rate);
		panel.add(bestTime);
		panel.add(level);
		
		panel.add(textPanel);
		
	  //btnPanel.add(line);
		btnPanel.add(okBtn);
		btnPanel.add(clearBtn);
		

	
		dialog.setLayout(new BorderLayout());
		dialog.add(panel,BorderLayout.NORTH);
		dialog.add(textPanel,BorderLayout.CENTER);
	
		dialog.add(btnPanel,BorderLayout.SOUTH);
		dialog.setBounds(parent.getLocation().x + 50,
				parent.getLocation().y + 50, 250, 220);

		

	}

	private class DescribeTextPanel extends JPanel {

		DescribeTextPanel() {
			super();
			setBounds(0, rate.getY()+rate.HEIGHT, panel.WIDTH, 120);
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.setFont(new Font("Monospaced", Font.PLAIN, 12));
			g.setColor(Color.black);
			int length = levels.length;
			
			for (int i = 0; i < length; i++) {
				g.drawString(String.valueOf(rates[i]), rate.getX(), rate.HEIGHT+10+i*20);
				g.drawString(String.valueOf(bestTimes[i]),bestTime.getX(), rate.HEIGHT+10+i*20);
				g.drawString(levels[i],level.getX(), rate.HEIGHT+10+i*20);
			}
			//repaint();
		}
	}



}