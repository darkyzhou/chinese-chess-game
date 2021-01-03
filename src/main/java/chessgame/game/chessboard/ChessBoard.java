package chessgame.game.chessboard;

import chessgame.game.chess.*;
import chessgame.util.Position;

import java.util.*;

public class ChessBoard {
    public static final Chess CHESS_ADVISOR_HAN = new ChessAdvisor(ChessSide.HAN);
    public static final Chess CHESS_ADVISOR_CHU = new ChessAdvisor(ChessSide.CHU);
    public static final Chess CHESS_CANNON_HAN = new ChessCannon(ChessSide.HAN);
    public static final Chess CHESS_CANNON_CHU = new ChessCannon(ChessSide.CHU);
    public static final Chess CHESS_CHARIOT_HAN = new ChessChariot(ChessSide.HAN);
    public static final Chess CHESS_CHARIOT_CHU = new ChessChariot(ChessSide.CHU);
    public static final Chess CHESS_ELEPHANT_HAN = new ChessElephant(ChessSide.HAN);
    public static final Chess CHESS_ELEPHANT_CHU = new ChessElephant(ChessSide.CHU);
    public static final Chess CHESS_GENERAL_HAN = new ChessGeneral(ChessSide.HAN);
    public static final Chess CHESS_GENERAL_CHU = new ChessGeneral(ChessSide.CHU);
    public static final Chess CHESS_HORSE_HAN = new ChessHorse(ChessSide.HAN);
    public static final Chess CHESS_HORSE_CHU = new ChessHorse(ChessSide.CHU);
    public static final Chess CHESS_SOLDIER_HAN = new ChessSoldier(ChessSide.HAN);
    public static final Chess CHESS_SOLDIER_CHU = new ChessSoldier(ChessSide.CHU);

    private static final Chess[][] INITIAL_CHESS_BOARD = new Chess[][]{
            {CHESS_CHARIOT_CHU, CHESS_HORSE_CHU, CHESS_ELEPHANT_CHU, CHESS_ADVISOR_CHU, CHESS_GENERAL_CHU, CHESS_ADVISOR_CHU, CHESS_ELEPHANT_CHU, CHESS_HORSE_CHU, CHESS_CHARIOT_CHU},
            {null, null, null, null, null, null, null, null, null},
            {null, CHESS_CANNON_CHU, null, null, null, null, null, CHESS_CANNON_CHU, null},
            {CHESS_SOLDIER_CHU, null, CHESS_SOLDIER_CHU, null, CHESS_SOLDIER_CHU, null, CHESS_SOLDIER_CHU, null, CHESS_SOLDIER_CHU},
            {null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null},
            {CHESS_SOLDIER_HAN, null, CHESS_SOLDIER_HAN, null, CHESS_SOLDIER_HAN, null, CHESS_SOLDIER_HAN, null, CHESS_SOLDIER_HAN},
            {null, CHESS_CANNON_HAN, null, null, null, null, null, CHESS_CANNON_HAN, null},
            {null, null, null, null, null, null, null, null, null},
            {CHESS_CHARIOT_HAN, CHESS_HORSE_HAN, CHESS_ELEPHANT_HAN, CHESS_ADVISOR_HAN, CHESS_GENERAL_HAN, CHESS_ADVISOR_HAN, CHESS_ELEPHANT_HAN, CHESS_HORSE_HAN, CHESS_CHARIOT_HAN}
    };

    public static final int BOARD_WIDTH = 9;
    public static final int BOARD_LENGTH = 10;

    private final List<Chess> downChessList;
    private final Deque<MoveChessAction> actions;
    private final Chess[][] chessBoard;

    private ChessSide currentSide;

    public ChessBoard(ChessSide currentSide) {
        this.currentSide = currentSide;
        this.downChessList = new ArrayList<>();
        this.actions = new LinkedList<>();
        this.chessBoard = new Chess[BOARD_LENGTH][BOARD_WIDTH];
        reset(currentSide);
    }

    public void reset(ChessSide newSide) {
        for (int i = 0; i < BOARD_LENGTH; i++) {
            System.arraycopy(INITIAL_CHESS_BOARD[i], 0, chessBoard[i], 0, BOARD_WIDTH);
        }
        currentSide = newSide;
        actions.clear();
        downChessList.clear();
    }

    public Chess getChessAt(Position position) {
        if (position.getX() < 0 || position.getX() > 8 || position.getY() < 0 || position.getY() > 9) {
            throw new IllegalArgumentException("invalid position: " + position);
        }
        return chessBoard[position.getY()][position.getX()];
    }

    private void setChessAt(Position position, Chess chess) {
        chessBoard[position.getY()][position.getX()] = chess;
    }

    public Iterable<Chess> getDownChessList() {
        return downChessList;
    }

    public Deque<MoveChessAction> getActions() {
        return actions;
    }

    public boolean canMoveChess(Position from, Position to) {
        Chess sourceChess = getChessAt(from);
        Chess targetChess = getChessAt(to);
        if (targetChess != null && targetChess.getSide() == sourceChess.getSide()) {
            return false;
        }
        return sourceChess.canMove(currentSide, this, from, to);
    }

    public MoveResult moveChess(Position from, Position to) {
        Chess moved = getChessAt(from);
        Chess target = getChessAt(to);
        ChessSide winSide = null;
        if (target != null) {
            downChessList.add(target);
            if (target == CHESS_GENERAL_CHU) {
                winSide = ChessSide.HAN;
            }
            if (target == CHESS_GENERAL_HAN) {
                winSide = ChessSide.CHU;
            }
        }
        actions.addLast(new MoveChessAction(from, to, moved, target));
        setChessAt(from, null);
        setChessAt(to, moved);
        return new MoveResult(winSide, moved, target);
    }

    public void undoLatestTwoAction() {
        if (actions.size() < 2) {
            throw new IllegalStateException("the count of all actions must be over 2");
        }

        MoveChessAction action = actions.pollLast();
        Position from = action.getFrom();
        Position to = action.getTo();
        setChessAt(to, action.getTargetChess());
        setChessAt(from, action.getMovedChess());

        System.out.println("xxx: " + action);

        action = actions.pollLast();
        from = action.getFrom();
        to = action.getTo();
        setChessAt(to, action.getTargetChess());
        setChessAt(from, action.getMovedChess());

        System.out.println("xxx: " + action);
    }

    public boolean isPositionInPalace(Position position) {
        int x = position.getX();
        int y = position.getY();
        return 3 <= x && x <= 5 && ((0 <= y && y <= 2) || (7 <= y && y <= 9));
    }

    public boolean isValidMoveInPalace(Position from, Position to, ChessSide side) {
        int fromXRelative = from.getX() - 3;
        int toXRelative = to.getX() - 3;
        int fromYRelative = from.getY() - (side == ChessSide.CHU ? 0 : 7);
        int toYRelative = to.getY() - (side == ChessSide.CHU ? 0 : 7);

        if (fromXRelative == 1 && fromYRelative == 1) {
            if (from.distanceToInSquare(to) > 2) {
                return false;
            }
        }

        if ((fromXRelative == 0 && fromYRelative == 1) || (fromXRelative == 2 && fromYRelative == 1)) {
            if (toXRelative == 1 && toYRelative != 1) {
                return false;
            }
        }

        if ((fromXRelative == 1 && fromYRelative == 0) || (fromXRelative == 1 && fromYRelative == 2)) {
            if (toXRelative != 1 && toYRelative == 1) {
                return false;
            }
        }

        return from.distanceToInSquare(to) <= 2;
    }

    public boolean isPositionInOwnArea(Position position, ChessSide side) {
        int y = position.getY();
        if (side == ChessSide.CHU) {
            return y <= 4;
        } else {
            return y >= 5;
        }
    }

    public List<Chess> getChessAlongSide(Position begin, Position end, boolean ignoreBegin) {
        if (begin.getX() != end.getX() && begin.getY() != end.getY()) {
            throw new IllegalArgumentException("invalid begin / end");
        }

        List<Chess> chessList = new ArrayList<>();
        int beginX = begin.getX();
        int beginY = begin.getY();
        int endX = end.getX();
        int endY = end.getY();
        if (beginX == endX) {
            for (int i = Math.min(beginY, endY); i <= Math.max(beginY, endY); i++) {
                Chess chess = getChessAt(new Position(beginX, i));
                if (chess != null) {
                    if (ignoreBegin && i == beginY) {
                        continue;
                    }
                    chessList.add(chess);
                }
            }
        } else {
            for (int i = Math.min(beginX, endX); i <= Math.max(beginX, endX); i++) {
                Chess chess = getChessAt(new Position(i, beginY));
                if (chess != null) {
                    if (ignoreBegin && i == beginX) {
                        continue;
                    }
                    chessList.add(chess);
                }
            }
        }

        return chessList;
    }

    public static Position transformRelativeToChuSide(Position relative) {
        return new Position(ChessBoard.BOARD_WIDTH - relative.getX() - 1, ChessBoard.BOARD_LENGTH - relative.getY() - 1);
    }
}
