module lab {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens ru.tequila.Lab.ui to javafx.graphics, javafx.fxml;
    opens ru.tequila.Lab.domain to javafx.base;
}