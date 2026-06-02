package ru.tequila.Lab.ui;

import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ru.tequila.Lab.domain.Box;
import ru.tequila.Lab.domain.Container;
import ru.tequila.Lab.repository.InMemoryContainerRepository;
import ru.tequila.Lab.service.ContainerService;
import ru.tequila.Lab.service.FileStorageService;
import ru.tequila.Lab.validator.ContainerValidator;
import java.io.File;

public class LabStorageController {
    private final LabStorageView view;
    private final InMemoryContainerRepository repository;
    private final ContainerService containerService;
    private final FileStorageService fileStorageService;
    private Container selectedContainer;

    public LabStorageController(LabStorageView view, InMemoryContainerRepository repository,
                                ContainerService containerService, FileStorageService fileStorageService) {
        this.view = view;
        this.repository = repository;
        this.containerService = containerService;
        this.fileStorageService = fileStorageService;

        initEventHandlers();
        refreshUIData();
    }

    private void initEventHandlers() {
        view.containerTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            selectedContainer = newSel;
            if (newSel != null) {
                updateDetailPanel(newSel);
                view.detailActions.setVisible(true);
            } else {
                clearDetailPanel();
                view.detailActions.setVisible(false);
            }
        });

        view.btnRefresh.setOnAction(e -> refreshUIData());
        view.btnLoad.setOnAction(e -> handleLoadFile());
        view.btnSave.setOnAction(e -> handleSaveFile());
        view.btnAddContainer.setOnAction(e -> handleAddContainer());
        view.btnDeleteContainer.setOnAction(e -> handleDeleteContainer());
        view.btnAddBox.setOnAction(e -> handleAddBox());
        view.btnDeleteBox.setOnAction(e -> handleDeleteBox());
        view.btnToggleOccupy.setOnAction(e -> handleToggleOccupy());
    }

    private void refreshUIData() {
        view.containerTable.setItems(FXCollections.observableArrayList(repository.findAllContainers()));
        if (selectedContainer != null) {
            Container updated = repository.findContainerById(selectedContainer.id);
            if (updated != null) updateDetailPanel(updated);
            else clearDetailPanel();
        }
    }

    private void updateDetailPanel(Container container) {
        view.detailTitleLabel.setText("Контейнер: " + container.name);
        view.containerInfoLabel.setText(String.format("Тип: %s | Локация: %s | Статус: %s | Слоты: %d/%d",
                container.type, container.location, container.status, container.occupiedSlots, container.capacity));
        view.boxTable.setItems(FXCollections.observableArrayList(repository.findBoxesByContainer(container.id)));
    }

    private void clearDetailPanel() {
        selectedContainer = null;
        view.detailTitleLabel.setText("Детализация не выбрана");
        view.boxTable.getItems().clear();
    }

    private void handleAddContainer() {
        ContainerDialog dialog = new ContainerDialog(repository.nextContainerId());
        dialog.showAndWait().ifPresent(container -> {
            try {
                ContainerValidator.validateContainer(container);
                repository.saveContainer(container);
                refreshUIData();
            } catch (Exception ex) {
                showError("Ошибка валидации", ex.getMessage());
            }
        });
    }

    private void handleAddBox() {
        if (selectedContainer == null) return;
        BoxDialog dialog = new BoxDialog(repository.nextBoxId(), selectedContainer.id);
        dialog.showAndWait().ifPresent(box -> {
            try {
                ContainerValidator.validateBox(box);
                repository.saveBox(box);
                selectedContainer.occupiedSlots++;
                refreshUIData();
            } catch (Exception ex) {
                showError("Ошибка создания бокса", ex.getMessage());
            }
        });
    }

    private void handleDeleteContainer() {
        if (selectedContainer == null) return;
        containerService.deleteContainer(selectedContainer.id);
        refreshUIData();
    }

    private void handleDeleteBox() {
        Box box = view.boxTable.getSelectionModel().getSelectedItem();
        if (box == null) return;
        containerService.deleteBox(box.id);
        if (selectedContainer != null && selectedContainer.occupiedSlots > 0) {
            selectedContainer.occupiedSlots--;
        }
        refreshUIData();
    }

    private void handleToggleOccupy() {
        Box box = view.boxTable.getSelectionModel().getSelectedItem();
        if (box == null) return;
        if (box.isOccupied) containerService.freeBox(box.id);
        else containerService.placeSample(box.id);
        refreshUIData();
    }

    private void handleLoadFile() {
        FileChooser chooser = new FileChooser();
        File file = chooser.showOpenDialog(new Stage());
        if (file != null) {
            fileStorageService.loadFromFile(file.getAbsolutePath());
            refreshUIData();
        }
    }

    private void handleSaveFile() {
        FileChooser chooser = new FileChooser();
        File file = chooser.showSaveDialog(new Stage());
        if (file != null) fileStorageService.saveToFile(file.getAbsolutePath());
    }

    private void showError(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}