package ru.tequila.Lab.domain;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

public class Container implements Serializable{
    public long id;
    public String name;
    public String type;
    public String location;
    public ContainerStatus status;
    public int capacity;
    public int occupiedSlots;
    public String ownerUsername;
    public Instant createdAt;
    public Instant updatedAt;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Container container = (Container) o;
        return id == container.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}