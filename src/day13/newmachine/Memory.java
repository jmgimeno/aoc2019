package day13.newmachine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class Memory {
    final List<Long> low;
    final Map<Long, Long> high;
    long base;

    Memory(List<Long> program) {
        low = program;
        high = new HashMap<>();
        base = 0L;
    }

    private long innerGet(long pos) {
        if (pos < low.size())
            return low.get((int) pos);
        else
            return high.getOrDefault(pos, 0L);
    }

    private void innerSet(long pos, long value) {
        if (pos < low.size())
            low.set((int) pos, value);
        else
            high.put(pos, value);
    }

    long getImmediate(long position) {
        return innerGet(position);
    }

    long getPosition(long position) {
        return innerGet(innerGet(position));
    }

    long getRelative(long position) {
        return innerGet(base + innerGet(position));
    }

    void moveBase(long increment) {
        base += increment;
    }

    public void setPosition(long pos, long value) {
        innerSet(innerGet(pos), value);
    }

    public void setImmediate(long pos, long value) {
        innerSet(pos, value);
    }

    public void setRelative(long pos, long value) {
        innerSet(base+ innerGet(pos), value);
    }

    public String dumpLowMemory() {
        return low.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }
}
