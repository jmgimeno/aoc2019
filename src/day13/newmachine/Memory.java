package day13.newmachine;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class Memory {
    final List<BigInteger> low;
    final Map<BigInteger, BigInteger> high;
    BigInteger base;

    Memory(List<BigInteger> program) {
        low = program;
        high = new HashMap<>();
        base = BigInteger.ZERO;
    }

    private BigInteger innerGet(BigInteger pos) {
        if (pos.compareTo(BigInteger.valueOf(low.size())) < 0)
            return low.get(pos.intValue());
        else
            return high.getOrDefault(pos, BigInteger.ZERO);
    }

    private void innerSet(BigInteger pos, BigInteger value) {
        if (pos.compareTo(BigInteger.valueOf(low.size())) < 0)
            low.set(pos.intValue(), value);
        else
            high.put(pos, value);
    }

    BigInteger getImmediate(BigInteger position) {
        return innerGet(position);
    }

    BigInteger getPosition(BigInteger position) {
        return innerGet(innerGet(position));
    }

    BigInteger getRelative(BigInteger position) {
        return innerGet(base.add(innerGet(position)));
    }

    void moveBase(BigInteger increment) {
        base = base.add(increment);
    }

    public void setPosition(BigInteger pos, BigInteger value) {
        innerSet(innerGet(pos), value);
    }

    public void setImmediate(BigInteger pos, BigInteger value) {
        innerSet(pos, value);
    }

    public void setRelative(BigInteger pos, BigInteger value) {
        innerSet(base.add(innerGet(pos)), value);
    }

    public String dumpLowMemory() {
        return low.stream()
                .map(Object::toString)
                .collect(Collectors.joining(","));
    }
}
