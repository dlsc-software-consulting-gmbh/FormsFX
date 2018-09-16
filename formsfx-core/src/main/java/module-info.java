module com.dlsc.formsfx {

    requires javafx.controls;

    exports com.dlsc.formsfx.model.event;
    exports com.dlsc.formsfx.model.structure;
    exports com.dlsc.formsfx.model.util;
    exports com.dlsc.formsfx.model.validators;

    exports com.dlsc.formsfx.view.controls;
    exports com.dlsc.formsfx.view.renderer;
    exports com.dlsc.formsfx.view.util;

    opens com.dlsc.formsfx.view.renderer;
}