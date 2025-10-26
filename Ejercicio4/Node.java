package Ejercicio4;

import java.util.Objects;

public class Node {
    private String name;
    private int id;
    
    public Node(String name, int id) {
        this.name = name;
        this.id = id;
        
    }
    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    @Override 
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return name.equals(node.name); // Comparamos por nombre
    }

    @Override
    public int hashCode() {
        return Objects.hash(name); // Usamos el hash del nombre
    }
}