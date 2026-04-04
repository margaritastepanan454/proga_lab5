package ru.tequila.Lab.domain;

import java.time.Instant;
import java.util.Objects;

public class Box {
    public long id;
    public long containerId;
    public String name;
    public int slotNumber;
    public boolean isOccupied;
    public Instant occupiedSince;
    public String ownerUsername;
    public Instant createdAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Box box = (Box) o;
        return id == box.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}