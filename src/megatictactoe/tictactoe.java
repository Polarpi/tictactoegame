package megatictactoe;

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
	
	public tictactoe() {
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
	
	public void clearGame() {
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				board[r][c] = Piece.empty;
			}
		}
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
	
	public GameStatus getCurrentStatus() {
		return currentStatus;
	}

	public Piece getCurrentPiece() {
		return currentPiece;
	}

	public Piece[][] getBoard() {
		return board;
	}
	
	public Piece getPiece(int r, int c) {
		return board[r][c];
	}
	
	public void setCurrentStatus(GameStatus status) {
		currentStatus = status;
	}
	
	public void setCurrentPiece(Piece piece) {
		currentPiece = piece;
	}
	
	public void setPiece(int r, int c) {
		board[r][c] = currentPiece;
	}
	
	public void setPiece(int r, int c, Piece piece) {
		board[r][c] = piece;
	}

	public void setBoard(Piece[][] board) {
		this.board = board;
	}
}