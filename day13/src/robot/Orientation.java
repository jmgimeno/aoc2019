package robot;

public enum Orientation {
    RIGHT {
        @Override
        public Orientation turn(Turn turn) {
            return switch (turn) {
                case LEFT -> UP;
                case RIGHT -> DOWN;
            };
        }
    },
    DOWN {
        @Override
        public Orientation turn(Turn turn) {
            return switch (turn) {
                case LEFT -> RIGHT;
                case RIGHT -> LEFT;
            };
        }
    },
    LEFT {
        @Override
        public Orientation turn(Turn turn) {
            return switch (turn) {
                case LEFT -> DOWN;
                case RIGHT -> UP;
            };
        }
    },
    UP {
        @Override
        public Orientation turn(Turn turn) {
            return switch (turn) {
                case LEFT -> LEFT;
                case RIGHT -> RIGHT;
            };
        }
    };

    public abstract Orientation turn(Turn turn);
}
