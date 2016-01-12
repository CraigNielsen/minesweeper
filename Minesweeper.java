//======================CRAIG FERGUSON
//======================MINESWEEPER
//======================SEPT 9 2012
//=====FRGCRA003======================

/*
The game has 4 difficulty levels 
starts on easy
if win, automatically gets harder
if loose, resets and try same difficulty again
can reset the if you get lost in all the flags

everything is well thought out. 
can right click to flag and then rightclick again to unflag
checks for win after every click
good incentive to finish the final level as there is a hidden .gif
*/


import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;


public class Minesweeper extends JFrame implements ActionListener, MouseListener
{
		String diffic;
		int size=10;
		int numMines;
		boolean won=false;
		boolean lost=false;
		boolean[][] mines= new boolean[size][size];
		boolean[][] clicked= new boolean[size][size];
		int[][] cells= new int[size][size];
		JFrame window;
		JPanel panel= new JPanel();
		JButton[][] buttons= new JButton[size][size];
		boolean[][] flagged= new boolean[size][size];
		int totFlagged= 0;

			//========================================================================main method
		
		public static void main(String[] args){
			 
			
			new Minesweeper(5, "EASY");
			
		}
		//============================================================================constructor---------
		
		public Minesweeper(int noOfMines, String diffic){
		
		
			
			numMines=noOfMines;
			this.diffic=diffic;
			
		//----window frame---
			window= new JFrame();
			window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			window.setSize(50*size,50*size);
			window.setTitle("MINESWEEPER");
			window.setLocationRelativeTo(null);
			
		//--MENU--
			
			JMenuBar bar= new JMenuBar();
			JMenu diff= new JMenu("Difficulty");
			JMenu curDifficulty= new JMenu("current: "+diffic);
			JMenu restart= new JMenu("........RESET GAME........");
			
			JMenuItem confirm=new JMenuItem("CONFIRM");
			confirm.addActionListener(this);
			JMenuItem cancel=new JMenuItem("CANCEL");			
			
			JMenuItem hard= new JMenuItem("HARD");
			hard.addActionListener(this);
			JMenuItem easy= new JMenuItem("EASY");
			easy.addActionListener(this);
			JMenuItem med= new JMenuItem("MEDIUM");
			med.addActionListener(this);
			JMenuItem cust= new JMenuItem("RUSSIAN ROULETTE");
			cust.addActionListener(this);
			
			
			restart.add(confirm);
			restart.add(cancel);
			
			diff.add(easy);
			diff.add(med);
			diff.add(hard);
			diff.add(cust);
			
			
			bar.add(diff);
			bar.add(curDifficulty);
			bar.add(Box.createHorizontalGlue());
			bar.add(restart);
			
			panel.setLayout(new GridLayout(size,size));
			initialise();
			// window.setVisible(true);
					
			
		
// 			add buttons
			
			
			for (int i=0; i<size ; i++){
				for (int j=0 ; j<size ; j++){
					panel.add( buttons[i][j] );
					
				}
			}
			window.setJMenuBar(bar);
			window.add(panel, BorderLayout.CENTER);
			populateMines();
			populateCells();
					/////add buttons--------------------
			
			
			window.setVisible(true);
		
		}
		//----------------------------------
		

		
		//=======================================================================Mouse listener
		
			public void mouseEntered ( MouseEvent e ) {}
			public void mouseExited ( MouseEvent e ) {}
			public void mousePressed ( MouseEvent e ) {}
			
			public void mouseReleased ( MouseEvent e ) {}
			public void mouseClicked ( MouseEvent e ) {
				
				if (SwingUtilities.isRightMouseButton(e)){
					
					//find adress
					JButton b=(JButton) e.getSource();//share an address
					
						String index=(String) b.getClientProperty("index");
			
						int row= Integer.parseInt("" + index.charAt(2));// get row and column of button pressed
						int column= Integer.parseInt("" + index.charAt(6));
						
						
					
						if (b.isEnabled() && (!clicked[row][column]) ){b.setIcon(new ImageIcon( "flag2.gif") );}
					
					
						
		
						
						if (flagged[row][column] && (!clicked[row][column] ))
						
						{flagged[row][column]=false;totFlagged--;b.setIcon(null);check();return;}
						
						if (!flagged[row][column]&& (!clicked[row][column]))
						
						{flagged[row][column]=true;totFlagged++;}
						check();
						
						
					}
				}
		
		//-------------------	
		//=======================================================================ACTION LISTENER=======
		public void actionPerformed(ActionEvent e){
			//===========check for menu buttons using getActionCommand()---
			String tempS= e.getActionCommand();
			
			
			if(tempS.equals("HARD")){refresh(17,"HARD");return;}
			if(tempS.equals("EASY")){refresh(5,"EASY");return;}
			if(tempS.equals("MEDIUM")){refresh(13,"MEDIUM");return;}
			if(tempS.equals("CONFIRM")){refresh();return;}
			if(tempS.equals("RUSSIAN ROULETTE")){refresh(20,"RUSSIAN ROULETTE");return;}
			
			//------------------------------------------------------------
			check();
			JButton b=(JButton)e.getSource();  //share an address
			String index=(String) b.getClientProperty("index");
			
			
			int row= Integer.parseInt("" + index.charAt(2));// get row and column of button pressed
			int column= Integer.parseInt("" + index.charAt(6));
			
			
			if (flagged[row][column]){flagged[row][column]=false;totFlagged--;check();buttons[row][column].setText("");buttons[row][column].setIcon(null);}
			
			if (mines[row][column]){    //if a mine
			  	explode();
			}  
			else if (cells[row][column]>0){   // if a number
				b.setText("" + cells[row][column]);
				// b.setBackground(Color.GRAY);	
				b.setForeground(Color.BLACK);	
				clicked[row][column]=true;
			}
			
			else if (cells[row][column]==0){  //if zero
				
				zeroClear(row,column);
			}
			
		check();	
		}
	// =========================================================================Zero Clear
	
		public void zeroClear(int i, int j){
						clicked[i][j]=true;
						buttons[i][j].setBackground(Color.BLACK);
						buttons[i][j].setEnabled(false);
						
						
						boolean left= (j-1 >= 0);    //checks if in bounds
						boolean right= (j+1 < size);
						boolean up= (i-1 >= 0);
						boolean down= (i+1 < size);
						
						if (left){                  //checks the left etc..using recursion
									if (cells[i][j-1]==0 && (!clicked[i][j-1]) && (!flagged[i][j-1]) ){
											
											buttons[i][j-1].setText("");
											buttons[i][j-1].setBackground(Color.BLACK);
											buttons[i][j-1].setForeground(Color.BLACK);
											clicked[i][j-1]=true;
											zeroClear(i,j-1);}
									//below else if line makes sure no zeros are showing
									else if (cells[i][j-1]==0 && (!flagged[i][j-1])) {buttons[i][j-1].setText("");}	
									else if ((!flagged[i][j-1])){buttons[i][j-1].setText("" + cells[i][j-1]);clicked[i][j-1]=true;}
													
						}
						if (right){	
										
									if (cells[i][j+1]==0 && (!clicked[i][j+1])&& (!flagged[i][j+1])){
											
											buttons[i][j+1].setText("");
											buttons[i][j+1].setBackground(Color.BLACK);
											buttons[i][j+1].setForeground(Color.BLACK);
											clicked[i][j+1]=true;
											zeroClear(i,j+1);
											}
									
									else if (cells[i][j+1]==0 && (!flagged[i][j+1])) {buttons[i][j+1].setText("");}		
									else if ((!flagged[i][j+1])) {buttons[i][j+1].setText(""+cells[i][j+1]);clicked[i][j+1]=true;}
						}
						if (up){	
										
									if (cells[i-1][j]==0 && (!clicked[i-1][j]) && (!flagged[i-1][j])){
									
											buttons[i-1][j].setText("");
											buttons[i-1][j].setBackground(Color.BLACK);
											buttons[i-1][j].setForeground(Color.BLACK);
											clicked[i-1][j]=true;
											zeroClear(i-1,j);}
											
											
									else if (cells[i-1][j]==0 && (!flagged[i-1][j])) {buttons[i-1][j].setText("");}
									else if ((!flagged[i-1][j])){buttons[i-1][j].setText(""+cells[i-1][j]);clicked[i-1][j]=true;}
						}
						if (down){	
										
									if (cells[i+1][j]==0 && (!clicked[i+1][j]) && (!flagged[i+1][j])){
											
											buttons[i+1][j].setText("");
											buttons[i+1][j].setBackground(Color.BLACK);
											buttons[i+1][j].setForeground(Color.BLACK);
											clicked[i+1][j]=true;
											zeroClear(i+1,j);}
											
									else if (cells[i+1][j]==0 && (!flagged[i+1][j])) {buttons[i+1][j].setText("");}		
									else if ((!flagged[i+1][j])){buttons[i+1][j].setText(""+cells[i+1][j]);clicked[i+1][j]=true;}
						}
		}
	
	
		//======================================================================  initialise
		
		public void initialise(){
		
		for (int i=0; i<size ; i++){
				for (int j=0 ; j<size ; j++){
					mines[i][j]=false;
					clicked[i][j]=false;
					flagged[i][j]=false;
					cells[i][j]=0;
					buttons[i][j]= new JButton();
					buttons[i][j].addActionListener(this);
					buttons[i][j].addMouseListener(this);
					buttons[i][j].putClientProperty("index","r:"+i+" "+"c:"+j);
				}} 
		
		}
		
	

		//======================================================================POPULATE MINES
		public void populateMines(){
		
		int needed = numMines ;
		while ( needed > 0 ) {
			int x = ( int ) Math.floor ( Math.random()* size ) ;
			int y = ( int ) Math.floor ( Math.random()* size ) ;
			if ( !mines[x][y] ) {
				mines [x][y] = true ;
				needed -- ;
			}
		}
		}
		//======================================================================check
		public void check(){
			int count=0;
			for (int i=0; i<size ; i++ ){     //i is rows j is columns
			
				for (int j=0; j<size ; j++){
					if (clicked[i][j] || flagged[i][j]){
						count++;
						if (totFlagged==numMines && count==(size*size)){
						
									if (diffic.equals("RUSSIAN ROULETTE")){
							JOptionPane.showMessageDialog ( null ,
							new ImageIcon( "hell.gif") , "AW HELL NAW!!!?  WELL DONE!" ,
							JOptionPane.INFORMATION_MESSAGE, new ImageIcon( "WIN77.gif") ) ;refresh();break;}

							JOptionPane.showMessageDialog ( null ,
							new ImageIcon( "Clap.gif") , "CONGRATULATIONS!" ,
							JOptionPane.INFORMATION_MESSAGE, new ImageIcon( "WIN.gif") ) ;
					
						if (diffic.equals("EASY")){refresh(13,"MEDIUM");break;}
						if (diffic.equals("MEDUIM")){refresh(17,"HARD");break;}
						if (diffic.equals("HARD")){refresh(20,"RUSSIAN ROULETTE");break;}
						
						
						
						}
						else {continue;}	
						}else {break;}
					
					
					
					
					
				}}
			
		}
		
		
		//=====================================================================Explode 
		public void explode(){
			for (int i=0; i<size ; i++ ){     //i is rows j is columns
				for (int j=0; j<size ; j++){
						
						if (mines[i][j]){
							Icon imgIcon = new ImageIcon("ex2.gif");
							buttons[i][j].setIcon(imgIcon);
							//buttons[i][j].setText("");
							//buttons[i][j].setBackground(Color.RED);
							
						}
				}
			//showMessageDialog(Component parent, Object message, String title, int messageType, Icon icon)	
		}
		JOptionPane.showMessageDialog ( null ,
					new ImageIcon( "Excloud.gif") , "" ,
					JOptionPane.INFORMATION_MESSAGE, new ImageIcon( "unlucky.gif") ) ;
		refresh();
		}
		//=====================================================================  refresh 
		
		public void refresh(){
				window.setVisible(false);
				new Minesweeper(numMines,diffic);
		}
		
		public void refresh(int numMin, String difficul){
				window.setVisible(false);
				new Minesweeper(numMin,difficul);
		}
		
		//=====================================================================custom
		
		public void custom(){
			
			
		}
		
     //======================================================================POPULATE cells

		public void populateCells(){
			int count=0;
			for (int i=0; i<size ; i++ ){     //i is rows j is columns
				for (int j=0; j<size ; j++) {
						
						boolean left= (j-1 >= 0);    //checks if in bounds
						boolean right= (j+1 < size);
						boolean up= (i-1 >= 0);
						boolean down= (i+1 < size);
						
						//count the mines surrounding the JButton cell				
						
						if (left){
								if (mines[i][j-1]){count++;}
									if (up){
										if (mines[i-1][j-1]){count++;}}
									if (down){
										if (mines[i+1][j-1]){count++;}}
						}
						if (right){
								if (mines[i][j+1]){count++;}
									if (up){
										if (mines[i-1][j+1]){count++;}}
									if (down){
										if (mines[i+1][j+1]){count++;}}
						}
						if (up){
								if (mines[i-1][j]){count++;}}
						if (down){
								if (mines[i+1][j]){count++;}}
						
						//give value of count to cell
						cells[i][j]=count;
						count=0;					
						
				}
			}
		}

} 