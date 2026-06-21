package ru.tequila.Lab.ui;

import javafx.application.Platform;
import javafx.scene.control.*;
import ru.tequila.Lab.domain.*;
import ru.tequila.Lab.repository.JdbcContainerRepository;
import ru.tequila.Lab.service.HistoryService;
import ru.tequila.Lab.validator.ContainerValidator;
import ru.tequila.Lab.validator.ValidationException;

public class LabStorageController {
    private final LabStorageView view;
    private final JdbcContainerRepository repo;
    private final String currentUser;
    private final HistoryService history = HistoryService.getInstance();

    public LabStorageController(LabStorageView view, JdbcContainerRepository repo, String currentUser) {
        this.view = view;
        this.repo = repo;
        this.currentUser = currentUser;

        view.containerTable.getSelectionModel().selectedItemProperty().addListener((obs, old, newVal) -> {
            if (newVal != null) refreshBoxes(newVal.id);
            else view.boxTable.getItems().clear();
        });

        view.btnDelete.setOnAction(e -> {
            Box selBox = view.boxTable.getSelectionModel().getSelectedItem();
            Container selCont = view.containerTable.getSelectionModel().getSelectedItem();

            if (selBox != null) {
                if (selBox.ownerUsername.equals(currentUser)) {
                    repo.deleteBoxById(selBox.id);
                    logAction("удален бокс" + selBox.name);
                    refreshBoxes(selBox.containerId);
                } else { showError("не твоё!"); }
            }
            else if (selCont != null) {
                if (selCont.ownerUsername.equals(currentUser)) {
                    repo.deleteContainerById(selCont.id);
                    logAction("удалён контейнер + боксы" + selCont.name);
                    refresh();
                } else { showError("куда? не твоё!"); }
            }
            else {
                showError("ничего не выбрано для удаления");
            }
        });

        view.btnAddContainer.setOnAction(e -> {
            new ContainerDialog(0).showAndWait().ifPresent(c -> {
                try {
                    ContainerValidator.validateContainer(c);
                    c.ownerUsername = currentUser;
                    repo.saveContainer(c);
                    logAction("создан контейнер: " + c.name);
                    refresh();
                } catch (ValidationException ex) {
                    showError(ex.getMessage());
                }
            });
        });

        view.btnStatus.setOnAction(e -> {
            Container sel = view.containerTable.getSelectionModel().getSelectedItem();
            if (sel != null && sel.ownerUsername.equals(currentUser)) {
                new ChoiceDialog<>(sel.status, ContainerStatus.values()).showAndWait().ifPresent(st -> {
                    repo.updateContainerStatus(sel.id, st);
                    logAction("статус " + sel.name + " -> " + st);
                    refresh();
                });
            } else { showError("нет прав. ну куда"); }
        });

        view.btnAddBox.setOnAction(e -> {
            Container selected = view.containerTable.getSelectionModel().getSelectedItem();
            if (selected == null) { showError("выберите контейнер"); return; }
            new BoxDialog(0, selected.id).showAndWait().ifPresent(b -> {
                b.ownerUsername = currentUser;
                repo.saveBox(b);
                logAction("добавлен бокс" + b.name);
                refreshBoxes(selected.id);
            });
        });

        view.btnBoxStatus.setOnAction(e -> {
            Box b = view.boxTable.getSelectionModel().getSelectedItem();
            if (b != null && b.ownerUsername.equals(currentUser)) {
                repo.updateBoxOccupation(b.id, !b.isOccupied);
                logAction("Бокс '" + b.name + "' изменен");
                refreshBoxes(b.containerId);
            } else { showError("Нет прав!"); }
        });

        view.btnLogout.setOnAction(e -> {
            ((javafx.stage.Stage) view.getRoot().getScene().getWindow()).close();
            Platform.runLater(() -> {
                try { new LabStorageApp().start(new javafx.stage.Stage()); } catch (Exception ex) {}
            });
        });

        refresh();
    }

    private void logAction(String msg) {
        history.addCommand(msg);
        Platform.runLater(() -> view.historyList.getItems().setAll(history.getHistory()));
    }

    public void refresh() {
        Platform.runLater(() -> {
            view.containerTable.getItems().setAll(repo.findAllContainers());
            view.boxTable.getItems().clear();
        });
    }

    private void refreshBoxes(long cid) {
        Platform.runLater(() -> view.boxTable.getItems().setAll(repo.findBoxesByContainer(cid)));
    }

    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }
}