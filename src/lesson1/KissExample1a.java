package lesson1;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

/**
 * Part of a GUI with six JButtons performing related tasks
 * 
 * Note all of the repetition – this is very WET code!
 */
public class KissExample1a extends JFrame implements ActionListener {
	JButton jButton1;
	JButton jButton2;
	JButton jButton3;
	JButton jButton4;
	JButton jButton5;
	JButton jButton6;
	
	JLabel indicator1;
	JLabel indicator2;
	JLabel indicator3;
	JLabel indicator4;
	JLabel indicator5;
	JLabel indicator6;
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new KissExample1a();
			}
		});
	}
	
	public KissExample1a() {
		super();
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLayout(new GridLayout(6,2));

		jButton1 = new JButton();
		this.add(jButton1);
		jButton1.setBorder(new BevelBorder(BevelBorder.RAISED));
		jButton1.setBackground(Color.blue);
		jButton1.setForeground(Color.white);
		jButton1.setText("button1");
		jButton1.setPreferredSize(new Dimension(100,30));
		jButton1.setVisible(true);
		jButton1.addActionListener(this);
		
		indicator1 = new JLabel();
		this.add(indicator1);
		indicator1.setVisible(false);
		indicator1.setForeground(Color.blue);
		indicator1.setPreferredSize(new Dimension(60,30));
		indicator1.setHorizontalAlignment(SwingConstants.CENTER);
		indicator1.setText("icon1");
		
		jButton2 = new JButton();
		this.add(jButton2);
		jButton2.setBorder(new BevelBorder(BevelBorder.RAISED));
		jButton2.setBackground(Color.blue);
		jButton2.setForeground(Color.white);
		jButton2.setText("button2");
		jButton2.setPreferredSize(new Dimension(100,30));
		jButton2.setVisible(true);
		jButton2.addActionListener(this);
		
		indicator2 = new JLabel();
		this.add(indicator2);
		indicator2.setVisible(false);
		indicator2.setForeground(Color.blue);
		indicator2.setPreferredSize(new Dimension(60,30));
		indicator2.setHorizontalAlignment(SwingConstants.CENTER);
		indicator2.setText("icon2");
		
		jButton3 = new JButton();
		this.add(jButton3);
		jButton3.setBorder(new BevelBorder(BevelBorder.RAISED));
		jButton3.setBackground(Color.blue);
		jButton3.setForeground(Color.white);
		jButton3.setText("button3");
		jButton3.setPreferredSize(new Dimension(100,30));
		jButton3.setVisible(true);
		jButton3.addActionListener(this);
		
		indicator3 = new JLabel();
		this.add(indicator3);
		indicator3.setVisible(false);
		indicator3.setForeground(Color.blue);
		indicator3.setPreferredSize(new Dimension(60,30));
		indicator3.setHorizontalAlignment(SwingConstants.CENTER);
		indicator3.setText("icon3");
		
		jButton4 = new JButton();
		this.add(jButton4);
		jButton4.setBorder(new BevelBorder(BevelBorder.RAISED));
		jButton4.setBackground(Color.blue);
		jButton4.setForeground(Color.white);
		jButton4.setText("button4");
		jButton4.setPreferredSize(new Dimension(100,30));
		jButton4.setVisible(true);
		jButton4.addActionListener(this);
		
		indicator4 = new JLabel();
		this.add(indicator4);
		indicator4.setVisible(false);
		indicator4.setForeground(Color.blue);
		indicator4.setPreferredSize(new Dimension(60,30));
		indicator4.setHorizontalAlignment(SwingConstants.CENTER);
		indicator4.setText("icon4");
		
		jButton5 = new JButton();
		this.add(jButton5);
		jButton5.setBorder(new BevelBorder(BevelBorder.RAISED));
		jButton5.setBackground(Color.blue);
		jButton5.setForeground(Color.white);
		jButton5.setText("button5");
		jButton5.setPreferredSize(new Dimension(100,30));
		jButton5.setVisible(true);
		jButton5.addActionListener(this);
		
		indicator5 = new JLabel();
		this.add(indicator5);
		indicator5.setVisible(false);
		indicator5.setForeground(Color.blue);
		indicator5.setPreferredSize(new Dimension(60,30));
		indicator5.setHorizontalAlignment(SwingConstants.CENTER);
		indicator5.setText("icon5");
		
		jButton6 = new JButton();
		this.add(jButton6);
		jButton6.setBorder(new BevelBorder(BevelBorder.RAISED));
		jButton6.setBackground(Color.blue);
		jButton6.setForeground(Color.white);
		jButton6.setText("button6");
		jButton6.setPreferredSize(new Dimension(100,30));
		jButton6.setVisible(true);
		jButton6.addActionListener(this);
		
		indicator6 = new JLabel();
		this.add(indicator6);
		indicator6.setVisible(false);
		indicator6.setForeground(Color.blue);
		indicator6.setPreferredSize(new Dimension(60,30));
		indicator6.setHorizontalAlignment(SwingConstants.CENTER);
		indicator6.setText("icon6");
		
		this.pack();
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton btn = (JButton) e.getSource();
		if (btn == jButton1) {
			indicator1.setVisible(true);
			indicator2.setVisible(false);
			indicator3.setVisible(false);
			indicator4.setVisible(false);
			indicator5.setVisible(false);
			indicator6.setVisible(false);
		} else if (btn == jButton2) {
			indicator1.setVisible(false);
			indicator2.setVisible(true);
			indicator3.setVisible(false);
			indicator4.setVisible(false);
			indicator5.setVisible(false);
			indicator6.setVisible(false);
		} else if (btn == jButton3) {
			indicator1.setVisible(false);
			indicator2.setVisible(false);
			indicator3.setVisible(true);
			indicator4.setVisible(false);
			indicator5.setVisible(false);
			indicator6.setVisible(false);
		} else if (btn == jButton4) {
			indicator1.setVisible(false);
			indicator2.setVisible(false);
			indicator3.setVisible(false);
			indicator4.setVisible(true);
			indicator5.setVisible(false);
			indicator6.setVisible(false);
		} else if (btn == jButton5) {
			indicator1.setVisible(false);
			indicator2.setVisible(false);
			indicator3.setVisible(false);
			indicator4.setVisible(false);
			indicator5.setVisible(true);
			indicator6.setVisible(false);
		} else if (btn == jButton6) {
			indicator1.setVisible(false);
			indicator2.setVisible(false);
			indicator3.setVisible(false);
			indicator4.setVisible(false);
			indicator5.setVisible(false);
			indicator6.setVisible(true);
		}		
	}
}