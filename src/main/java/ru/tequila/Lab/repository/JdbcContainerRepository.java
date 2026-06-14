package ru.tequila.Lab.repository;

import ru.tequila.Lab.domain.Box;
import ru.tequila.Lab.domain.Container;
import ru.tequila.Lab.domain.ContainerStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcContainerRepository implements ContainerRepository {

    public JdbcContainerRepository() {
        initDatabase();
    }

    public void initDatabase() {
        String createContainersTable = "CREATE TABLE IF NOT EXISTS containers (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "type TEXT NOT NULL, " +
                "location TEXT, " +
                "status TEXT, " +
                "capacity INTEGER, " +
                "occupied_slots INTEGER DEFAULT 0" +
                ");";

        String createBoxesTable = "CREATE TABLE IF NOT EXISTS boxes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "container_id INTEGER, " +
                "name TEXT NOT NULL, " +
                "slot_number INTEGER, " +
                "is_occupied INTEGER DEFAULT 0, " +
                "FOREIGN KEY(container_id) REFERENCES containers(id) ON DELETE CASCADE" +
                ");";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:lab_storage.db");
             Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON;"); // Включаем каскадное удаление для SQLite
            stmt.execute(createContainersTable);
            stmt.execute(createBoxesTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveContainer(Container c) {
        if (c.id > 0) {
            String sql = "UPDATE containers SET name = ?, type = ?, location = ?, status = ?, capacity = ?, occupied_slots = ? WHERE id = ?";
            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:lab_storage.db");
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, c.name);
                pstmt.setString(2, c.type);
                pstmt.setString(3, c.location);
                pstmt.setString(4, c.status.name());
                pstmt.setInt(5, c.capacity);
                pstmt.setInt(6, c.occupiedSlots);
                pstmt.setLong(7, c.id);
                pstmt.executeUpdate();
            } catch (SQLException e) { e.printStackTrace(); }
            return;
        }

        String sql = "INSERT INTO containers (name, type, location, status, capacity, occupied_slots) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:lab_storage.db");
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, c.name);
            pstmt.setString(2, c.type);
            pstmt.setString(3, c.location);
            pstmt.setString(4, c.status.name());
            pstmt.setInt(5, c.capacity);
            pstmt.setInt(6, c.occupiedSlots);
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    c.id = generatedKeys.getLong(1);
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void saveBox(Box b) {
        if (b.id > 0) {
            String sql = "UPDATE boxes SET container_id = ?, name = ?, slot_number = ?, is_occupied = ? WHERE id = ?";
            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:lab_storage.db");
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setLong(1, b.containerId);
                pstmt.setString(2, b.name);
                pstmt.setInt(3, b.slotNumber);
                pstmt.setInt(4, b.isOccupied ? 1 : 0);
                pstmt.setLong(5, b.id);
                pstmt.executeUpdate();
            } catch (SQLException e) { e.printStackTrace(); }
            return;
        }

        String sql = "INSERT INTO boxes (container_id, name, slot_number, is_occupied) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:lab_storage.db");
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setLong(1, b.containerId);
            pstmt.setString(2, b.name);
            pstmt.setInt(3, b.slotNumber);
            pstmt.setInt(4, b.isOccupied ? 1 : 0);
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    b.id = generatedKeys.getLong(1);
                }
            }
            updateOccupiedSlots(b.containerId);
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public Container findContainerById(long id) {
        String sql = "SELECT * FROM containers WHERE id = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:lab_storage.db");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Container c = new Container();
                    c.id = rs.getLong("id");
                    c.name = rs.getString("name");
                    c.type = rs.getString("type");
                    c.location = rs.getString("location");
                    c.status = ContainerStatus.valueOf(rs.getString("status"));
                    c.capacity = rs.getInt("capacity");
                    c.occupiedSlots = rs.getInt("occupied_slots");
                    return c;
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public Box findBoxById(long id) {
        String sql = "SELECT * FROM boxes WHERE id = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:lab_storage.db");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Box b = new Box();
                    b.id = rs.getLong("id");
                    b.containerId = rs.getLong("container_id");
                    b.name = rs.getString("name");
                    b.slotNumber = rs.getInt("slot_number");
                    b.isOccupied = rs.getInt("is_occupied") == 1;
                    return b;
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public List<Container> findAllContainers() {
        List<Container> list = new ArrayList<>();
        String sql = "SELECT * FROM containers";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:lab_storage.db");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Container c = new Container();
                c.id = rs.getLong("id");
                c.name = rs.getString("name");
                c.type = rs.getString("type");
                c.location = rs.getString("location");
                c.status = ContainerStatus.valueOf(rs.getString("status"));
                c.capacity = rs.getInt("capacity");
                c.occupiedSlots = rs.getInt("occupied_slots");
                list.add(c);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    @Override
    public List<Box> findAllBoxes() {
        List<Box> list = new ArrayList<>();
        String sql = "SELECT * FROM boxes";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:lab_storage.db");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Box b = new Box();
                b.id = rs.getLong("id");
                b.containerId = rs.getLong("container_id");
                b.name = rs.getString("name");
                b.slotNumber = rs.getInt("slot_number");
                b.isOccupied = rs.getInt("is_occupied") == 1;
                list.add(b);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    @Override
    public List<Box> findBoxesByContainer(long containerId) {
        List<Box> list = new ArrayList<>();
        String sql = "SELECT * FROM boxes WHERE container_id = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:lab_storage.db");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, containerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Box b = new Box();
                    b.id = rs.getLong("id");
                    b.containerId = rs.getLong("container_id");
                    b.name = rs.getString("name");
                    b.slotNumber = rs.getInt("slot_number");
                    b.isOccupied = rs.getInt("is_occupied") == 1;
                    list.add(b);
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    @Override
    public void deleteContainerById(long id) {
        String sql = "DELETE FROM containers WHERE id = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:lab_storage.db");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void deleteBoxById(long id) {
        Box b = findBoxById(id);
        String sql = "DELETE FROM boxes WHERE id = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:lab_storage.db");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
            if (b != null) {
                updateOccupiedSlots(b.containerId);
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private void updateOccupiedSlots(long containerId) {
        String sql = "UPDATE containers SET occupied_slots = (SELECT COUNT(*) FROM boxes WHERE container_id = ?) WHERE id = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:lab_storage.db");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, containerId);
            pstmt.setLong(2, containerId);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}