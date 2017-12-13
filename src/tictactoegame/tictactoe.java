package tictactoegame;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class tictactoe extends JFrame {
	public static final int rows = 3;
	public static final int cols = 3;
	
	public static final int cellSize = 100;
	public static final int canvasWidth = cellSize * cols;
	public static final int canvasHeight = cellSize * rows;
	public static final int gridWidth = 10;
	public static final int gridWidthHalf = gridWidth / 2;
	
	public static final int cellPadding = cellSize / 5;
	public static final int pieceSize = cellSize - (cellPadding * 2);
	public static final int pieceStrokeWidth = 10;
	
	public enum GameStatus {
		playing, tie, p1Won, p2Won
	}
	
	private GameStatus currentStatus;
	
	public enum Piece {
		empty, p1, p2
	}
	
	private Piece currentPiece;
	
	private Piece[][] board;
	private DrawCanvas canvas;
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new tictactoe();
			}
		});
	}
	
	public tictactoe() {
		canvas = new DrawCanvas();
		canvas.setPreferredSize(new Dimension(canvasWidth, canvasHeight));
		
		canvas.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int mouseX = e.getX();
				int mouseY = e.getY();
				
				int rowClick = mouseY / cellSize;
				int colClick = mouseX / cellSize;
				
				if (currentStatus == GameStatus.playing) {
					if (rowClick >= 0 && rowClick < rows && colClick >= 0 && colClick < cols && board[rowClick][colClick] == Piece.empty) {
						board[rowClick][colClick] = currentPiece;
						updateGame(currentPiece, rowClick, colClick);
						if (currentPiece == Piece.p1) {
							currentPiece = Piece.p2;
						} else {
							currentPiece = Piece.p1;
						}
					}
				} else {
					initGame();
				}
				
				repaint();
			}
		});
		
		Container c = getContentPane();
		c.setLayout(new BorderLayout());
		c.add(canvas, BorderLayout.CENTER);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setTitle("The Best Tic Tac Toe Game Ever");
		setVisible(true);
		
		board = new Piece[rows][cols];
		initGame();
	}
	
	public void initGame() {
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				board[r][c] = Piece.empty;
			}
		}
		currentStatus = GameStatus.playing;
		currentPiece = Piece.p1;
	}
	
	public void updateGame(Piece piece, int r, int c) {
		if (hasWon(piece, r, c)) {
			if (piece == Piece.p1) {
				currentStatus = GameStatus.p1Won;
			} else {
				currentStatus = GameStatus.p2Won;
			}
		} else if (isTie()) {
			currentStatus = GameStatus.tie;
		}
	}
	
	public boolean isTie() {
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				if (board[r][c] == Piece.empty) {
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean hasWon(Piece piece, int r, int c) {
		int count = 0;
		for (int i = 0; i < 3; i++) {
			if (board[i][c] == piece) {
				count++;
			}
		}
		if (count == 3) {
			return true;
		} else {
			count = 0;
		}
		for (int j = 0; j < 3; j++) {
			if (board[r][j] == piece) {
				count++;
			}
		}
		if (count == 3) {
			return true;
		} else {
			count = 0;
		}
		if (r == c) {
			for (int k = 0; k < 3; k++) {
				if (board[k][k] == piece) {
					count++;
				}
			}
			if (count == 3) {
				return true;
			} else {
				count = 0;
			}
		}
		if (r + c == 2) {
			for (int l = 0; l < 3; l++) {
				if (board[l][2-l] == piece) {
					count++;
				}
			}
			if (count == 3) {
				return true;
			}
		}
		return false;
	}

	class DrawCanvas extends JPanel {
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			setBackground(new Color(232, 238, 247));
			
			g.setColor(new Color(151, 156, 163));
			for (int r = 1; r < rows; r++) {
				g.fillRoundRect(0, cellSize * r - gridWidthHalf, canvasWidth - 1, gridWidth, gridWidth, gridWidth);
			}
			for (int c = 1; c < cols; c++ ) {
				g.fillRoundRect(cellSize * c - gridWidthHalf, 0, gridWidth, canvasHeight - 1, gridWidth, gridWidth);
			}
			
			Graphics2D g2d = (Graphics2D)g;
			g2d.setStroke(new BasicStroke(pieceStrokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < cols; c++) {
					int x1 = c * cellSize + cellPadding;
					int y1 = r * cellSize + cellPadding;
					if (board[r][c] == Piece.p1) {
						g2d.setColor(new Color(120, 163, 232));
						int x2 = (c + 1) * cellSize - cellPadding;
						int y2 = (r + 1) * cellSize - cellPadding;
						g2d.drawLine(x1, y1, x2, y2);
						g2d.drawLine(x2, y1, x1, y2);
					} else if (board[r][c] == Piece.p2) {
						g2d.setColor(new Color(247, 138, 113));
						g2d.drawOval(x1, y1, pieceSize, pieceSize);
					}
				}
			}
			
			
		}
	}
}