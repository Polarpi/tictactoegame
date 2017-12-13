package megatictactoe;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class megatictactoe extends JFrame {
	public static final int rows = 9;
	public static final int cols = 9;
	public static final int boardRows = 3;
	public static final int boardCols = 3;
	
	public static final int cellSize = 75;
	public static final int gridWidth = 8;
	public static final int megaGridWidth = 16;
	public static final int gridWidthHalf = gridWidth / 2;
	public static final int canvasPadding = 10;
	public static final int canvasWidth = cellSize * boardCols + gridWidth * 2 + canvasPadding * 2;
	public static final int canvasHeight = cellSize * boardRows + gridWidth * 2 + canvasPadding * 2;
	
	public static final int cellPadding = cellSize / 5;
	public static final int pieceSize = cellSize - (cellPadding * 2);
	public static final int pieceStrokeWidth = 10;
	
	public static final int boardWidth = canvasWidth * 3 + megaGridWidth * 2;
	public static final int boardHeight = canvasHeight * 3 + megaGridWidth * 2;
	public static final int megaCellPadding = canvasWidth / 10;
	public static final int megaPieceSize = canvasWidth - (megaCellPadding * 2);
	
	public enum MegaGameStatus {
		playing, tie, p1Won, p2Won
	}
	
	private MegaGameStatus currentStatus;
	
	public enum MegaPiece {
		empty, p1, p2
	}
	
	private tictactoe[][] boards;
	private MegaPiece[][] megaBoard;
	
	private DrawCanvas canvas;
	private JLabel label;
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new megatictactoe();
			}
		});
	}
	
	public megatictactoe() {
		canvas = new DrawCanvas();
		canvas.setPreferredSize(new Dimension(boardWidth, boardHeight));
		
		canvas.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int mouseX = e.getX();
				int mouseY = e.getY();
				
				int rowClick = mouseY / (boardWidth / 9);
				int colClick = mouseX / (boardHeight / 9);
				
				int megaRowClick = rowClick / boardRows;
				int megaColClick = colClick / boardCols;
				
				if (currentStatus == MegaGameStatus.playing) {
					if (boards[megaRowClick][megaColClick].getCurrentStatus() == tictactoe.GameStatus.playing) {
						if (rowClick >= 0 && rowClick < rows && colClick >= 0 && colClick < cols 
								&& boards[megaRowClick][megaColClick].getPiece(rowClick % 3, colClick % 3) 
								== tictactoe.Piece.empty) {
								boards[megaRowClick][megaColClick].setPiece(rowClick % 3, colClick % 3);
								boards[megaRowClick][megaColClick].updateGame(boards[megaRowClick][megaColClick].getCurrentPiece(), rowClick % 3, colClick % 3);
								if (boards[megaRowClick][megaColClick].getCurrentPiece() == tictactoe.Piece.p1) {
									boards[megaRowClick][megaColClick].setCurrentPiece(tictactoe.Piece.p2);
								} else {
									boards[megaRowClick][megaColClick].setCurrentPiece(tictactoe.Piece.p1);
								}
						}
					} else {
						if (boards[megaRowClick][megaColClick].getCurrentStatus() == tictactoe.GameStatus.p1Won) {
							megaBoard[megaRowClick][megaColClick] = MegaPiece.p1;
							boards[megaRowClick][megaColClick].clearGame();
							updateGame(MegaPiece.p1, megaRowClick, megaColClick);
						} else if (boards[megaRowClick][megaColClick].getCurrentStatus() == tictactoe.GameStatus.p2Won) {
							megaBoard[megaRowClick][megaColClick] = MegaPiece.p2;
							boards[megaRowClick][megaColClick].clearGame();
							updateGame(MegaPiece.p2, megaRowClick, megaColClick);
						} else if (boards[megaRowClick][megaColClick].getCurrentStatus() == tictactoe.GameStatus.tie) {
							boards[megaRowClick][megaColClick].initGame();
						}
					}
				} else {
					initGame();
					label.setText("  ");
				}
				repaint();
			}
		});
		
		label = new JLabel("  ");
		label.setFont(new Font("Arial", Font.BOLD, 24));
		label.setForeground(new Color(222, 237, 252));
		label.setOpaque(true);
		label.setBackground(new Color(85, 167, 244));
		label.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		
		Container c = getContentPane();
		c.setLayout(new BorderLayout());
		c.add(canvas, BorderLayout.CENTER);
		c.add(label, BorderLayout.PAGE_START);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setTitle("The Best Tic Tac Toe Game Ever");
		setVisible(true);
		
		boards = new tictactoe[boardRows][boardCols];
		for (int i = 0; i < boardRows; i++) {
			for (int j = 0; j < boardCols; j++) {
				boards[i][j] = new tictactoe();
			}
		}
		megaBoard = new MegaPiece[boardRows][boardCols];
		
		initGame();
	}
	
	public void initGame() {
		for (int megaRow = 0; megaRow < boardRows; megaRow++) {
			for (int megaCol = 0; megaCol < boardCols; megaCol++) {
				megaBoard[megaRow][megaCol] = MegaPiece.empty;
				boards[megaRow][megaCol].initGame();
			}
		}
		currentStatus = MegaGameStatus.playing;
	}
	
	public void updateGame(MegaPiece piece, int r, int c) {
		if (hasWon(piece, r, c)) {
			if (piece == MegaPiece.p1) {
				currentStatus = MegaGameStatus.p1Won;
			} else {
				currentStatus = MegaGameStatus.p2Won;
			}
		} else if (isTie()) {
			currentStatus = MegaGameStatus.tie;
		}
	}
	
	public boolean isTie() {
		for (int r = 0; r < boardRows; r++) {
			for (int c = 0; c < boardCols; c++) {
				if (megaBoard[r][c] == MegaPiece.empty) {
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean hasWon(MegaPiece piece, int r, int c) {
		int count = 0;
		for (int i = 0; i < 3; i++) {
			if (megaBoard[i][c] == piece) {
				count++;
			}
		}
		if (count == 3) {
			return true;
		} else {
			count = 0;
		}
		for (int j = 0; j < 3; j++) {
			if (megaBoard[r][j] == piece) {
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
				if (megaBoard[k][k] == piece) {
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
				if (megaBoard[l][2-l] == piece) {
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
			
			g.setColor(new Color(120, 124, 130));
			for (int r = 1; r < boardRows; r++) {
				g.fillRoundRect(canvasPadding, canvasWidth * r + (r - 1) * megaGridWidth, boardWidth - 2 * canvasPadding, megaGridWidth, megaGridWidth, megaGridWidth);
			}
			for (int c = 1; c < boardCols; c++) {
				g.fillRoundRect(canvasHeight * c + (c - 1) * megaGridWidth, canvasPadding, megaGridWidth, boardHeight - 2 * canvasPadding, megaGridWidth, megaGridWidth);
			}
			
			g.setColor(new Color(151, 156, 163));
			for (int r = 1; r < 7; r++) {
				for (int c = 1; c < 7; c++) {
					if (r < 3) {
						g.fillRoundRect(canvasPadding, cellSize * r + canvasPadding + (r - 1) * gridWidth, canvasWidth - 2 * canvasPadding, gridWidth, gridWidth, gridWidth);
						g.fillRoundRect(canvasPadding + canvasWidth + megaGridWidth, cellSize * r + canvasPadding + (r - 1) * gridWidth, canvasWidth - 2 * canvasPadding, gridWidth, gridWidth, gridWidth);
						g.fillRoundRect(canvasPadding + 2 * canvasWidth + 2 * megaGridWidth, cellSize * r + canvasPadding + (r - 1) * gridWidth, canvasWidth - 2 * canvasPadding, gridWidth, gridWidth, gridWidth);
					} else if (r < 5) {
						g.fillRoundRect(canvasPadding, cellSize * (r + 1) + 3 * canvasPadding + (r - 1) * gridWidth + megaGridWidth, canvasWidth - 2 * canvasPadding, gridWidth, gridWidth, gridWidth);
						g.fillRoundRect(canvasPadding + canvasWidth + megaGridWidth, cellSize * (r + 1) + 3 * canvasPadding + (r - 1) * gridWidth + megaGridWidth, canvasWidth - 2 * canvasPadding, gridWidth, gridWidth, gridWidth);
						g.fillRoundRect(canvasPadding + 2 * canvasWidth + 2 * megaGridWidth, cellSize * (r + 1) + 3 * canvasPadding + (r - 1) * gridWidth + megaGridWidth, canvasWidth - 2 * canvasPadding, gridWidth, gridWidth, gridWidth);
					} else {
						g.fillRoundRect(canvasPadding, cellSize * (r + 2) + 5 * canvasPadding + (r - 1) * gridWidth + megaGridWidth * 2, canvasWidth - 2 * canvasPadding, gridWidth, gridWidth, gridWidth);
						g.fillRoundRect(canvasPadding + canvasWidth + megaGridWidth, cellSize * (r + 2) + 5 * canvasPadding + (r - 1) * gridWidth + megaGridWidth * 2, canvasWidth - 2 * canvasPadding, gridWidth, gridWidth, gridWidth);
						g.fillRoundRect(canvasPadding + 2 * canvasWidth + 2 * megaGridWidth, cellSize * (r + 2) + 5 * canvasPadding + (r - 1) * gridWidth + megaGridWidth * 2, canvasWidth - 2 * canvasPadding, gridWidth, gridWidth, gridWidth);
					}
					
					if (c < 3) {
						g.fillRoundRect(cellSize * c + canvasPadding + (c - 1) * gridWidth, canvasPadding, gridWidth, canvasHeight - 2 * canvasPadding, gridWidth, gridWidth);
						g.fillRoundRect(cellSize * c + canvasPadding + (c - 1) * gridWidth, canvasPadding + canvasWidth + megaGridWidth, gridWidth, canvasHeight - 2 * canvasPadding, gridWidth, gridWidth);
						g.fillRoundRect(cellSize * c + canvasPadding + (c - 1) * gridWidth, canvasPadding + 2 * canvasWidth + 2 * megaGridWidth, gridWidth, canvasHeight - 2 * canvasPadding, gridWidth, gridWidth);
					} else if (c < 5) {
						g.fillRoundRect(cellSize * (c + 1) + 3 * canvasPadding + (c - 1) * gridWidth + megaGridWidth, canvasPadding, gridWidth, canvasHeight - 2 * canvasPadding, gridWidth, gridWidth);
						g.fillRoundRect(cellSize * (c + 1) + 3 * canvasPadding + (c - 1) * gridWidth + megaGridWidth, canvasPadding + canvasWidth + megaGridWidth, gridWidth, canvasHeight - 2 * canvasPadding, gridWidth, gridWidth);
						g.fillRoundRect(cellSize * (c + 1) + 3 * canvasPadding + (c - 1) * gridWidth + megaGridWidth, canvasPadding + 2 * canvasWidth + 2 * megaGridWidth, gridWidth, canvasHeight - 2 * canvasPadding, gridWidth, gridWidth);
					} else {
						g.fillRoundRect(cellSize * (c + 2) + 5 * canvasPadding + (c - 1) * gridWidth + megaGridWidth * 2, canvasPadding, gridWidth, canvasHeight - 2 * canvasPadding, gridWidth, gridWidth);
						g.fillRoundRect(cellSize * (c + 2) + 5 * canvasPadding + (c - 1) * gridWidth + megaGridWidth * 2, canvasPadding + canvasWidth + megaGridWidth, gridWidth, canvasHeight - 2 * canvasPadding, gridWidth, gridWidth);
						g.fillRoundRect(cellSize * (c + 2) + 5 * canvasPadding + (c - 1) * gridWidth + megaGridWidth * 2, canvasPadding + 2 * canvasWidth + 2 * megaGridWidth, gridWidth, canvasHeight - 2 * canvasPadding, gridWidth, gridWidth);
					}
				}
			}
			
			Graphics2D g2d = (Graphics2D)g;
			g2d.setStroke(new BasicStroke(pieceStrokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < cols; c++) {
					int boardRow = r / 3;
					int boardCol = c / 3;
					int x1 = (c * cellSize) + (c * cellPadding / 2) + (c / 3) * megaGridWidth + (c / 3) * canvasPadding + canvasPadding + cellPadding;
					int y1 = (r * cellSize) + (r * cellPadding / 2) + (r / 3) * megaGridWidth + (r / 3) * canvasPadding + canvasPadding + cellPadding;
					if (boards[boardRow][boardCol].getPiece(r % 3, c % 3) == tictactoe.Piece.p1) {
						g2d.setColor(new Color(120, 163, 232));
						int x2 = (c + 1) * cellSize + (c * cellPadding / 2) + (c / 3) * megaGridWidth + (c / 3) * canvasPadding - cellPadding + canvasPadding;
						int y2 = (r + 1) * cellSize + (r * cellPadding / 2) + (r / 3) * megaGridWidth + (r / 3) * canvasPadding - cellPadding + canvasPadding;
						g2d.drawLine(x1, y1, x2, y2);
						g2d.drawLine(x2, y1, x1, y2);
					} else if (boards[boardRow][boardCol].getPiece(r % 3, c % 3) == tictactoe.Piece.p2) {
						g2d.setColor(new Color(247, 138, 113));
						g2d.drawOval(x1, y1, pieceSize, pieceSize);
					}
				}
			}
			
			g2d.setStroke(new BasicStroke(pieceStrokeWidth * 2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			for (int r = 0; r < boardRows; r++) {
				for (int c = 0; c < boardCols; c++) {
					int x1 = c * canvasWidth + megaCellPadding + (c % 3) * megaGridWidth;
					int y1 = r * canvasHeight + megaCellPadding + (r % 3) * megaGridWidth;
					if (megaBoard[r][c] == MegaPiece.p1) {
						g2d.setColor(new Color(120, 163, 232));
						int x2 = (c + 1) * canvasWidth + (c % 3) * megaGridWidth - megaCellPadding;
						int y2 = (r + 1) * canvasHeight + (r % 3) * megaGridWidth - megaCellPadding;
						g2d.drawLine(x1, y1, x2, y2);
						g2d.drawLine(x2, y1, x1, y2);
					} else if (megaBoard[r][c] == MegaPiece.p2) {
						g2d.setColor(new Color(247, 138, 113));
						g2d.drawOval(x1, y1, megaPieceSize, megaPieceSize);
					}
				}
			}
			
			if (currentStatus == MegaGameStatus.p1Won) {
				label.setHorizontalTextPosition(JLabel.CENTER);
				label.setText("Player One Won! Click to play again.");
			} else if (currentStatus == MegaGameStatus.p2Won) {
				label.setHorizontalTextPosition(JLabel.CENTER);
				label.setText("Player Two Won! Click to play again.");
			} else if (currentStatus == MegaGameStatus.tie) {
				label.setHorizontalTextPosition(JLabel.CENTER);
				label.setText("It's a tie! Click to play again.");
			}
		}
	}
}