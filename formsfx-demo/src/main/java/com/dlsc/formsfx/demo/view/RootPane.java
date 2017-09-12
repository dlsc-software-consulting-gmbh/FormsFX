package com.dlsc.formsfx.demo.view;

import com.dlsc.formsfx.demo.model.DemoModel;
import com.dlsc.formsfx.model.structure.Section;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.dlsc.formsfx.view.util.ViewMixin;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * This class contains all the nodes and regions needed for the demo
 * application.
 *
 * @author Sacha Schmid
 * @author Rinesch Murugathas
 */
public class RootPane extends BorderPane implements ViewMixin {

    /**
     * - controls holds all the nodes which are rendered on the right side.
     * - scrollContent holds the form.
     * - languageButtons holds both buttons to change language.
     * - statusContent holds all the state change labels.
     * - formButtons holds the save and reset button.
     * - toggleContent holds the buttons to toggle editable and sections.
     * - bindingInfo holds the info of property changes.
     */
    private GridPane controls;
    private ScrollPane scrollContent;
    private HBox languageButtons;
    private VBox statusContent;
    private HBox formButtons;
    private VBox toggleContent;
    private VBox bindingInfo;

    private Button save;
    private Button reset;
    private Button languageDE;
    private Button languageEN;

    private Label validLabel;
    private Label changedLabel;
    private Label persistableLabel;
    private Label countryLabel;
    private Label currencyLabel;
    private Label populationLabel;

    private Button editableToggle;
    private Button sectionToggle;

    private DemoModel model;
    private FormRenderer displayForm;

    /**
     * The constructor to create the nodes and regions.
     *
     * @param model
     *          The model that holds the data.
     */
    public RootPane(DemoModel model) {
        this.model = model;
        init();
    }

    /**
     * This method is used to set up the stylesheets.
     */
    @Override
    public void initializeSelf() {
        getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        getStyleClass().add("root-pane");
    }

    /**
     * This method initializes all nodes and regions.
     */
    @Override
    public void initializeParts() {
        save = new Button("Save");
        reset = new Button("Reset");
        save.getStyleClass().add("save-button");
        reset.getStyleClass().add("reset-button");

        // The language buttons get a picture of the country from the flaticon
        // font in the css.

        languageDE = new Button("\ue001");
        languageEN = new Button("\ue000");

        validLabel = new Label("The form is valid.");
        persistableLabel = new Label("The form is not persistable.");
        changedLabel = new Label("The form has not changed.");
        countryLabel = new Label("Country: " + model.getCountry().getName());
        currencyLabel = new Label("Currency: " + model.getCountry().getCurrencyShort());
        populationLabel = new Label("Population: " + model.getCountry().getPopulation());

        editableToggle = new Button("Toggle Editable");
        sectionToggle = new Button("Toggle Sections");
        editableToggle.getStyleClass().add("toggle-button");
        sectionToggle.getStyleClass().add("toggle-button");

        languageButtons = new HBox();
        statusContent = new VBox();
        formButtons = new HBox();
        toggleContent = new VBox();
        bindingInfo = new VBox();

        controls = new GridPane();
        scrollContent = new ScrollPane();

        displayForm = new FormRenderer(model.getFormInstance());
    }

    /**
     * This method sets up the necessary bindings for the logic of the
     * application.
     */
    @Override
    public void setupBindings() {
        save.disableProperty().bind(model.getFormInstance().persistableProperty().not());
        reset.disableProperty().bind(model.getFormInstance().changedProperty().not());
        displayForm.prefWidthProperty().bind(scrollContent.prefWidthProperty());
    }

    /**
     * This method sets up listeners and sets the text of the state change
     * labels.
     */
    @Override
    public void setupValueChangedListeners() {
        model.getFormInstance().changedProperty().addListener((observable, oldValue, newValue) -> changedLabel.setText("The form has " + (newValue ? "" : "not ") + "changed."));
        model.getFormInstance().validProperty().addListener((observable, oldValue, newValue) -> validLabel.setText("The form is " + (newValue ? "" : "not ") + "valid."));
        model.getFormInstance().persistableProperty().addListener((observable, oldValue, newValue) -> persistableLabel.setText("The form is " + (newValue ? "" : "not ") + "persistable."));

        model.getCountry().nameProperty().addListener((observable, oldValue, newValue) -> countryLabel.setText("Country: " + newValue));
        model.getCountry().currencyShortProperty().addListener((observable, oldValue, newValue) -> currencyLabel.setText("Currency: " + newValue));
        model.getCountry().populationProperty().addListener((observable, oldValue, newValue) -> populationLabel.setText("Population: " + newValue));
    }

    /**
     * This method sets up the handling for all the button clicks.
     */
    @Override
    public void setupEventHandlers() {
        reset.setOnAction(event -> model.getFormInstance().reset());
        save.setOnAction(event -> model.getFormInstance().persist());

        languageDE.setOnAction(event -> {
            model.translate("DE");
            languageDE.setDisable(true);
            languageEN.setDisable(false);
        });

        languageEN.setOnAction(event -> {
            model.translate("EN");
            languageEN.setDisable(true);
            languageDE.setDisable(false);
        });

        sectionToggle.setOnAction(event -> model.getFormInstance().getGroups().stream().filter(s -> s instanceof Section).forEach(s -> {
            Section sec = (Section) s;
            sec.collapse(!sec.isCollapsed());
        }));
        
        editableToggle.setOnAction(event -> model.getFormInstance().getFields().forEach(s -> s.editable(!s.isEditable())));
    }

    /**
     * This method is used to layout the nodes and regions properly.
     */
    @Override
    public void layoutParts() {
        scrollContent.setContent(displayForm);
        scrollContent.setFitToWidth(true);
        scrollContent.getStyleClass().add("scroll-pane");

        languageDE.getStyleClass().addAll("flaticon", "lang-button", "lang-button--left");
        languageEN.getStyleClass().addAll("flaticon", "lang-button", "lang-button--right");

        languageEN.setDisable(true);

        languageDE.setMaxWidth(Double.MAX_VALUE);
        languageEN.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(languageDE, Priority.ALWAYS);
        HBox.setHgrow(languageEN, Priority.ALWAYS);
        languageButtons.getChildren().addAll(languageDE, languageEN);
        languageButtons.setPadding(new Insets(10));
        controls.add(languageButtons, 0, 0);

        statusContent.setPadding(new Insets(10));
        statusContent.getChildren().addAll(validLabel, changedLabel, persistableLabel);
        statusContent.setSpacing(10);
        statusContent.setPrefWidth(200);
        statusContent.getStyleClass().add("bordered");
        controls.add(statusContent, 0, 1);

        save.setMaxWidth(Double.MAX_VALUE);
        reset.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(save, Priority.ALWAYS);
        HBox.setHgrow(reset, Priority.ALWAYS);
        formButtons.getChildren().addAll(reset, save);
        formButtons.setPadding(new Insets(10));
        formButtons.setSpacing(10);
        formButtons.setPrefWidth(200);
        formButtons.getStyleClass().add("bordered");
        controls.add(formButtons, 0, 2);

        bindingInfo.setPadding(new Insets(10));
        bindingInfo.getChildren().addAll(countryLabel, currencyLabel, populationLabel);
        bindingInfo.setSpacing(10);
        bindingInfo.setPrefWidth(200);
        bindingInfo.getStyleClass().add("bordered");
        controls.add(bindingInfo, 0, 3);

        editableToggle.setMaxWidth(Double.MAX_VALUE);
        sectionToggle.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(editableToggle, Priority.ALWAYS);
        HBox.setHgrow(sectionToggle, Priority.ALWAYS);
        toggleContent.setPadding(new Insets(10));
        toggleContent.getChildren().addAll(editableToggle, sectionToggle);
        toggleContent.setSpacing(10);
        toggleContent.setPrefWidth(200);
        toggleContent.getStyleClass().add("bordered");
        controls.add(toggleContent, 0, 4);

        controls.setPrefWidth(200);
        controls.getStyleClass().add("controls");

        setCenter(scrollContent);
        setRight(controls);
    }

}
