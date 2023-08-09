import java.util.Objects;

public class Value implements Comparable<Value> {
    int val;
    int usage;
    Value(int val) {
        this.val = val;
        this.usage = 0;
    }

    Value(Value other) {
        this.val = other.val;
        this.usage = other.usage;
    }


    @Override
    public int compareTo(Value other) {
        if (this.usage < other.usage) return -1;
        if (this.usage > other.usage) return 1;
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Value value = (Value) o;
        return val == value.val;
    }

    @Override
    public int hashCode() {
        return Objects.hash(val, usage);
    }
}
