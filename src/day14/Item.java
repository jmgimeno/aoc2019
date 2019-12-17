package day14;

import java.util.Objects;

public class Item {
    final String name;
    final int quantity;

    public Item(int quantity, String name) {
        this.name = name;
        this.quantity = quantity;
    }

    public static Item parseItem(String str) {
        // 10 ORE
        var parts = str.split(" ");
        return new Item(Integer.parseInt(parts[0]), parts[1]);
    }

    public Item multiply(int factor) {
        return new Item(quantity * factor, name);
    }

    public Item mergeWith(Item other) {
        assert name.equals(other.name);
        return new Item(quantity + other.quantity, name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return quantity == item.quantity &&
                name.equals(item.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, quantity);
    }

    @Override
    public String toString() {
        return "day14.Item{" +
                "name='" + name + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
