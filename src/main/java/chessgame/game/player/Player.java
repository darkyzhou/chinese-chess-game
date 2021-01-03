package chessgame.game.player;

import chessgame.game.chess.ChessSide;

import java.util.Objects;

public final class Player {
    private final String name;
    private final String figureName;
    private ChessSide side;

    public Player(String name, String figureName, ChessSide side) {
        this.name = name;
        this.figureName = figureName;
        this.side = side;
    }

    public String getName() {
        return name;
    }

    public String getFigureName() {
        return figureName;
    }

    public ChessSide getSide() {
        return side;
    }

    public void setSide(ChessSide side) {
        this.side = side;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(name, player.name) &&
                side == player.side;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", figureName='" + figureName + '\'' +
                ", side=" + side +
                '}';
    }
}
