package day14;

import java.util.*;

public class State {

    final List<Item> objectives;

    public State(List<Item> objectives) {
        this.objectives = objectives;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return objectives.equals(state.objectives);
    }

    @Override
    public int hashCode() {
        return Objects.hash(objectives);
    }

    public boolean isFinal() {
        return false;
    }
}
