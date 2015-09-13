/*
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.nju.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import edu.nju.main.JMineSweeper;

public class WebEndDialog {

	
	private JDialog dialog;
	private MyWinPanel picWinPanel;
	private MyFailPanel picFailPanel;
	private JPanel LabelPanel;
	private DescribeTextPanel textPanel;
	private JPanel btnPanel;
	
    private JLabel mineNum;
    private JLabel Result;
    private JLabel CORH;
    
	private JButton okBtn;
	
	private int mineFlagByHost=0;
	private int mineFlagByClient=0;
	private boolean isWinner = false;
	//记录的是客户赢了还是主机赢了
	private boolean isHost=false;
	static final String win = "Win";
	static final String fail= "Fail";
	
	public WebEndDialog(JFrame parent,boolean isHost,boolean isWinner) {   
		super();
		setIsHost(isHost);
		setIsWinner(isWinner);
		initialization(parent);
		mineFlagByClient=JMineSweeper.ClientFlag;
		mineFlagByHost=JMineSweeper.HostFlag;
		
	}
	
	public WebEndDialog(JFrame parent) {   
		super();
		initialization(parent);
		
	}

	public boolean show() {
		dialog.setVisible(true);
		return true;
	}
	
	public void setmineFlagByClient (int a){
		this.mineFlagByClient=a;
	}
	
	public void setmineFlagByHost (int a){
		this.mineFlagByHost=a;
	}
	
	public void setIsWinner (boolean isWinner){
		this.isWinner=isWinner;
	}

	public void setIsHost(boolean isHost){
		this.isHost=isHost;
	}
	private void initialization(JFrame parent) {
        
		dialog = new JDialog(parent, "WebPK", true);
		CORH = new JLabel("C/H");
		Result = new JLabel("Result");
		mineNum= new JLabel("Flag");
		
		CORH.setFont(new Font("Verdana", Font.BOLD, 15));
		Result.setFont(new Font("Verdana", Font.BOLD, 15));
		mineNum.setFont(new Font("Verdana", Font.BOLD, 15));
        
		picWinPanel = new MyWinPanel();
		picFailPanel = new MyFailPanel();
		LabelPanel = new JPanel();
		textPanel = new DescribeTextPanel();
		btnPanel = new JPanel();

		
		okBtn = new JButton("OK");
		okBtn.setFont(new Font("Verdana", Font.BOLD, 15));
		okBtn.setBounds(100, 115, 70, 23);
		okBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
			}
		});
		
		LabelPanel.setLayout(new FlowLayout(FlowLayout.CENTER,50,10));
		LabelPanel.add(CORH);
		LabelPanel.add(Result);
		LabelPanel.add(mineNum);
		
		
	    
		btnPanel.add(okBtn,BorderLayout.CENTER);
	    
		dialog.setLayout(new BorderLayout());
		dialog.add(LabelPanel,BorderLayout.NORTH);
		
		
		if(this.isWinner){
			picWinPanel.setBounds(0, 100, 280, 300);
			textPanel.add(picWinPanel);
		   dialog.add(picWinPanel);
		}else{
			picFailPanel.setBounds(0, 100, 280, 300);
			textPanel.add(picFailPanel);
			dialog.add(picFailPanel);
		}
		dialog.add(textPanel,BorderLayout.CENTER);
		dialog.add(btnPanel,BorderLayout.SOUTH);
		dialog.setBounds(parent.getLocation().x + 50,
				parent.getLocation().y + 50,300, 500);
	}

	private class DescribeTextPanel extends JPanel {

		DescribeTextPanel() {
			super();
			setBounds(0, CORH.getY()+CORH.HEIGHT, LabelPanel.WIDTH, 50);
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.setFont(new Font("Verdana", Font.BOLD, 15));
			g.setColor(Color.black);
			System.out.println("isHost : "+isHost);
			if((isHost&isWinner)|((!isHost)&(!isWinner))){
				System.out.println("主机获胜！！！！");
				g.drawString("Host", CORH.getX(), CORH.HEIGHT+20);
				g.drawString(win,  Result.getX(), CORH.HEIGHT+20);
				g.drawString(String.valueOf(mineFlagByHost), mineNum.getX(), CORH.HEIGHT+20);
				g.drawString("Client", CORH.getX(), CORH.HEIGHT+50);
			    g.drawString(fail,  Result.getX(), CORH.HEIGHT+50);
				g.drawString(String.valueOf(mineFlagByClient), mineNum.getX(), CORH.HEIGHT+50);
			}else {
			System.out.println("客户获胜！！！！！");
				g.drawString("Host", CORH.getX(), CORH.HEIGHT+20);
				g.drawString(fail,  Result.getX(), CORH.HEIGHT+20);
				g.drawString(String.valueOf(mineFlagByHost), mineNum.getX(), CORH.HEIGHT+20);
				g.drawString("Client", CORH.getX(), CORH.HEIGHT+50);
			    g.drawString(win,  Result.getX(), CORH.HEIGHT+50);
				g.drawString(String.valueOf(mineFlagByClient), mineNum.getX(), CORH.HEIGHT+50);
			}
			//repaint();
		}
	}
    
	
	private class MyWinPanel extends JPanel {
	    @Override
	    protected void paintComponent(Graphics g) {
	    	// TODO Auto-generated method stub
	    	super.paintComponent(g);
	    	g.drawImage(Images.win, 2,0, 283, 300, null);
	    	//repaint();
	    	
	    }
	}
	
	private class MyFailPanel extends JPanel {
	    @Override
	    protected void paintComponent(Graphics g) {
	    	// TODO Auto-generated method stub
	    	super.paintComponent(g);
	    	g.drawImage(Images.lose1, 2,0, 283, 300, null);
	    	//repaint();
	    	
	    }
	}
	
//  public static void main (String args[]){
//	  WebEndDialog w = new WebEndDialog(new JFrame(),true,false);
//	  w.show();
//  }

	
}