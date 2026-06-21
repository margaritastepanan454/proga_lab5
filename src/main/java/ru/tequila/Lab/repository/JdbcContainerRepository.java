package ru.tequila.Lab.repository;

import ru.tequila.Lab.domain.*;
import java.sql.*;
import java.util.*;

public class JdbcContainerRepository {
    private Connection conn;

    public JdbcContainerRepository() {
        try {
            this.conn = DriverManager.getConnection("jdbc:sqlite:lab_storage.db");
            Statement stmt = conn.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS containers (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, type TEXT, location TEXT, capacity INTEGER, status TEXT, owner_username TEXT)");
            stmt.execute("CREATE TABLE IF NOT EXISTS boxes (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, isOccupied INTEGER, container_id INTEGER, slotNumber INTEGER, owner_username TEXT)");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveContainer(Container c) {
        String sql = "INSERT INTO containers(name, type, location, capacity, status, owner_username) VALUES(?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.name); ps.setString(2, c.type); ps.setString(3, c.location);
            ps.setInt(4, c.capacity); ps.setString(5, c.status.name()); ps.setString(6, c.ownerUsername);
            ps.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateContainerStatus(long id, ContainerStatus status) {
        try (PreparedStatement ps = conn.prepareStatement("UPDATE containers SET status = ? WHERE id = ?")) {
            ps.setString(1, status.name());
            ps.setLong(2, id);
            ps.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveBox(Box b) {
        String sql = "INSERT INTO boxes(name, isOccupied, container_id, slotNumber, owner_username) VALUES(?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, b.name); ps.setInt(2, b.isOccupied ? 1 : 0);
            ps.setLong(3, b.containerId); ps.setInt(4, b.slotNumber); ps.setString(5, b.ownerUsername);
            ps.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateBoxOccupation(long id, boolean occupied) {
        try (PreparedStatement ps = conn.prepareStatement("UPDATE boxes SET isOccupied = ? WHERE id = ?")) {
            ps.setInt(1, occupied ? 1 : 0);
            ps.setLong(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteContainerById(long id) {
        try {
            try (PreparedStatement ps1 = conn.prepareStatement("DELETE FROM boxes WHERE container_id = ?")) {
                ps1.setLong(1, id);
                ps1.executeUpdate();
            }
            try (PreparedStatement ps2 = conn.prepareStatement("DELETE FROM containers WHERE id = ?")) {
                ps2.setLong(1, id);
                ps2.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteBoxById(long id) {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM boxes WHERE id = ?")) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Container> findAllContainers() {
        List<Container> list = new ArrayList<>();
        try (ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM containers")) {
            while (rs.next()) {
                Container c = new Container();
                c.id = rs.getLong("id"); c.name = rs.getString("name");
                c.status = ContainerStatus.valueOf(rs.getString("status"));
                c.ownerUsername = rs.getString("owner_username");
                list.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Box> findBoxesByContainer(long cid) {
        List<Box> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM boxes WHERE container_id = ?")) {
            ps.setLong(1, cid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Box b = new Box();
                b.id = rs.getLong("id"); b.name = rs.getString("name");
                b.slotNumber = rs.getInt("slotNumber");
                b.isOccupied = rs.getInt("isOccupied") == 1;
                b.ownerUsername = rs.getString("owner_username");
                b.containerId = cid;
                list.add(b);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
}