package com.dlsc.formsfx.demo.model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.Month;

/**
 * This class serves as the model for the demo application.
 *
 * @author Sacha Schmid
 * @author Rinesch Murugathas
 */
public class Country {

    private StringProperty name = new SimpleStringProperty("Switzerland");
    private StringProperty iso = new SimpleStringProperty("CH");
    private BooleanProperty independence = new SimpleBooleanProperty(true);
    private ObjectProperty<LocalDate> independenceDay = new SimpleObjectProperty<>(LocalDate.of(1648, Month.OCTOBER, 24));

    private StringProperty currencyShort = new SimpleStringProperty("CHF");
    private StringProperty currencyLong = new SimpleStringProperty("Swiss Franc");

    private IntegerProperty population = new SimpleIntegerProperty(8401120);
    private DoubleProperty area = new SimpleDoubleProperty(41285);
    private StringProperty tld = new SimpleStringProperty(".ch");

    private StringProperty dateFormat = new SimpleStringProperty("dd.mm.yyyy");
    private ObjectProperty<String> driverSide = new SimpleObjectProperty<>("Right");
    private StringProperty timeZone = new SimpleStringProperty("CET");
    private StringProperty summerTimeZone = new SimpleStringProperty("CEST");

    private ListProperty<String> allSides = new SimpleListProperty<>(FXCollections.observableArrayList("Right", "Left"));
    private ListProperty<String> allCities = new SimpleListProperty<>(FXCollections.observableArrayList("Zurich (ZH)", "Geneva (GE)", "Basel (BS)", "Lausanne (VD)", "Bern (BE)", "Winterthur (ZH)", "Lucerne (LU)", "St. Gallen (SG)", "Lugano (TI)", "Biel (BE)"));
    private ListProperty<String> allCapitals = new SimpleListProperty<>(FXCollections.observableArrayList("Zurich (ZH)", "Geneva (GE)", "Basel (BS)", "Lausanne (VD)", "Bern (BE)", "Winterthur (ZH)", "Lucerne (LU)", "St. Gallen (SG)", "Lugano (TI)", "Biel (BE)"));
    private ListProperty<String> allContinents = new SimpleListProperty<>(FXCollections.observableArrayList("Africa", "Asia", "Europe", "North America", "South America", "Australia"));

    private ListProperty<String> continents = new SimpleListProperty<>(FXCollections.observableArrayList("Europe"));

    private ObjectProperty<String> capital = new SimpleObjectProperty<>("Bern (BE)");
    private ListProperty<String> germanCities = new SimpleListProperty<>(FXCollections.observableArrayList("Zurich (ZH)", "Basel (BS)", "Bern (BE)", "Winterthur (ZH)", "Lucerne (LU)", "St. Gallen (SG)", "Biel (BE)"));

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getIso() {
        return iso.get();
    }

    public StringProperty isoProperty() {
        return iso;
    }

    public boolean isIndependence() {
        return independence.get();
    }

    public BooleanProperty independenceProperty() {
        return independence;
    }

    public LocalDate getIndependenceDay() {
        return independenceDay.get();
    }

    public ObjectProperty<LocalDate> independenceDayProperty() {
        return independenceDay;
    }

    public String getCurrencyShort() {
        return currencyShort.get();
    }

    public StringProperty currencyShortProperty() {
        return currencyShort;
    }

    public String getCurrencyLong() {
        return currencyLong.get();
    }

    public StringProperty currencyLongProperty() {
        return currencyLong;
    }

    public int getPopulation() {
        return population.get();
    }

    public IntegerProperty populationProperty() {
        return population;
    }

    public double getArea() {
        return area.get();
    }

    public DoubleProperty areaProperty() {
        return area;
    }

    public String getTld() {
        return tld.get();
    }

    public StringProperty tldProperty() {
        return tld;
    }

    public String getDateFormat() {
        return dateFormat.get();
    }

    public StringProperty dateFormatProperty() {
        return dateFormat;
    }

    public String getDriverSide() {
        return driverSide.get();
    }

    public ObjectProperty<String> driverSideProperty() {
        return driverSide;
    }

    public String getTimeZone() {
        return timeZone.get();
    }

    public StringProperty timeZoneProperty() {
        return timeZone;
    }

    public String getSummerTimeZone() {
        return summerTimeZone.get();
    }

    public StringProperty summerTimeZoneProperty() {
        return summerTimeZone;
    }

    public ObservableList<String> getAllSides() {
        return allSides.get();
    }

    public ListProperty<String> allSidesProperty() {
        return allSides;
    }

    public ObservableList<String> getAllCities() {
        return allCities.get();
    }

    public ListProperty<String> allCitiesProperty() {
        return allCities;
    }

    public ObservableList<String> getAllCapitals() {
        return allCapitals.get();
    }

    public ListProperty<String> allCapitalsProperty() {
        return allCapitals;
    }

    public ObservableList<String> getAllContinents() {
        return allContinents.get();
    }

    public ListProperty<String> allContinentsProperty() {
        return allContinents;
    }

    public ObservableList<String> getContinents() {
        return continents.get();
    }

    public ListProperty<String> continentsProperty() {
        return continents;
    }

    public String getCapital() {
        return capital.get();
    }

    public ObjectProperty<String> capitalProperty() {
        return capital;
    }

    public ObservableList<String> getGermanCities() {
        return germanCities.get();
    }

    public ListProperty<String> germanCitiesProperty() {
        return germanCities;
    }
}
