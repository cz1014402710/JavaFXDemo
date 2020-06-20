package sample.controller;

import java.text.DateFormatSymbols;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import sample.model.Person;

/**
 * description: <一句话功能简述>
 *
 * @author Chenz
 * @date 2020/6/15
 */
public class BirthdayStatisticsController {

    @FXML
    private BarChart<String, Integer> barChart;
    @FXML
    private CategoryAxis              xAxis;

    private ObservableList<String> monthNames = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        String[] months = DateFormatSymbols.getInstance(Locale.CHINESE).getMonths();
        monthNames.addAll(Arrays.asList(months));
        xAxis.setCategories(monthNames);
    }

    public void setPersonData(List<Person> persons) {
        int[] monthCounter = new int[12];
        for (Person p : persons) {
            int month = p.getBirthday().getMonthValue() - 1;
            monthCounter[month]++;
        }

        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        for (int i = 0; i < monthCounter.length; i++) {
            series.getData().add(new XYChart.Data<>(monthNames.get(i), monthCounter[i]));

        }
        barChart.getData().add(series);
    }

}
