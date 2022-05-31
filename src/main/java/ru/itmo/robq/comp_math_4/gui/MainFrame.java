package ru.itmo.robq.comp_math_4.gui;


import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.itmo.robq.comp_math_4.approximation.*;
import ru.itmo.robq.comp_math_4.functions.Dot;
import ru.itmo.robq.comp_math_4.functions.LinearFunction;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.JTableHeader;
import java.util.List;

@Component
public class MainFrame extends CustomFrame {
    private List<JTextField> xFields;
    private List<JTextField> yFields;
    private JTable resultTable;
    private ResultTableModel tableModel;
    private ChartPanel chartPanel;
    private final int fieldsCount = 12;
    private List<Approximation> approximationList;

    private List<ApproximationResults> results;

    private Integer best = null;



    @Autowired
    public MainFrame(LinearApproximation linearApproximation,
                     Polynomial2Approximation polynomial2Approximation,
                     Polynomial3Approximation polynomial3Approximation,
                     LogarithmicApproximation logarithmicApproximation,
                     ExponentialApproximation exponentialApproximation,
                     PowerApproximation powerApproximation) {
        super();
        approximationList = new ArrayList<>();
        approximationList.add(linearApproximation);
        approximationList.add(polynomial2Approximation);
        approximationList.add(polynomial3Approximation);
        approximationList.add(logarithmicApproximation);
        approximationList.add(exponentialApproximation);
        approximationList.add(powerApproximation);
        initUI();
    }

    private void initUI() {
        JLabel tableLabel = new JLabel("Таблица значений:");
        System.out.println(approximationList);
        List<JLabel> xLabels = new ArrayList<>();
        List<JLabel> yLabels = new ArrayList<>();
        for (int i = 1; i <= fieldsCount; i++) {
            xLabels.add(new JLabel("x" + i));
            yLabels.add(new JLabel("y" + i));
        }
        xFields = new ArrayList<>();
        yFields = new ArrayList<>();
        for (int i = 0; i < fieldsCount; i++) {
            xFields.add(new JTextField());
            yFields.add(new JTextField());
        }
        tableModel = new ResultTableModel();
        resultTable = new JTable(tableModel) {
            @Override
            public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend) {
                super.changeSelection(best, 1, false, false);
            }
        };
        resultTable.setBackground(new Color(238, 238, 238));
        JButton chooseFileButton = new JButton("Выбрать файл");
        JButton approximateButton = new JButton("Аппроксимировать");
        JButton saveToFileButton = new JButton("Сохранить в файл");
        JButton exitButton = new JButton("Выход");
        chooseFileButton.addActionListener((ActionEvent) -> onChooseFileButtonClick());
        approximateButton.addActionListener((ActionEvent) -> onApproximateButtonClick());
        saveToFileButton.addActionListener((ActionEvent) -> onSaveToFileButtonClick());
        exitButton.addActionListener((ActionEvent) -> System.exit(0));
        JFreeChart chart = ChartFactory.createXYLineChart(
                "График аппроксимации",
                "X",
                "Y",
                new XYSeriesCollection(),
                PlotOrientation.VERTICAL,
                false,
                true, false
        );
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.white);
        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        plot.setDomainGridlinePaint(Color.lightGray);
        plot.setRangeGridlinePaint(Color.lightGray);
        chart.setBackgroundPaint(new Color(238, 238, 238));

        addComponent(tableLabel);
        createInputTable(xLabels, xFields);
        createInputTable(yLabels, yFields);
        addHorizontalComponents(40, approximateButton, chooseFileButton, saveToFileButton, exitButton);
        addComponent(resultTable.getTableHeader());
        addComponent(resultTable);
        chartPanel = new ChartPanel(chart);
        addComponent(chartPanel);
    }

    private void createInputTable(List<JLabel> labels, List<JTextField> fields) {
        addHorizontalComponents(20, labels.get(0), fields.get(0),
                labels.get(1), fields.get(1),
                labels.get(2), fields.get(2),
                labels.get(3), fields.get(3),
                labels.get(4), fields.get(4),
                labels.get(5), fields.get(5),
                labels.get(6), fields.get(6),
                labels.get(7), fields.get(7),
                labels.get(8), fields.get(8),
                labels.get(9), fields.get(9),
                labels.get(10), fields.get(10),
                labels.get(11), fields.get(11));
    }

    private void onApproximateButtonClick() {
        List<Double> xValues = new ArrayList<>();
        List<Double> yValues = new ArrayList<>();
        for (int i = 0; i < fieldsCount; i++) {
            String xText = xFields.get(i).getText();
            String yText = yFields.get(i).getText();
            if ((xText.equals("") || yText.equals("")) && i < 10) {
                JOptionPane.showMessageDialog(this,
                        "Необходимо указать не менее 10 пар значений x, y",
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                xValues.add(Double.parseDouble(xText.replace(',', '.').trim()));
                yValues.add(Double.parseDouble(yText.replace(',', '.').trim()));
            } catch (NumberFormatException e) {
                if (i < 10) {
                    JOptionPane.showMessageDialog(this,
                            "Значения x и y должны быть числами",
                            "Ошибка",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

        }
        results = new ArrayList<>();
        int n = Math.min(xValues.size(), yValues.size());
        double[] xValuesArray = new double[n];
        double[] yValuesArray = new double[n];
        for (int i = 0; i < n; i++) {
            xValuesArray[i] = xValues.get(i);
            yValuesArray[i] = yValues.get(i);
        }
        for (Approximation approximation: approximationList) {
            results.add(approximation.approximate(xValuesArray, yValuesArray));
        }
        double bestDeviation = Double.MAX_VALUE;
        best = 0;
        for (int i = 0; i < results.size(); i++) {
            if (results.get(i).getDeviation() < bestDeviation) {
                best = i;
                bestDeviation = results.get(i).getDeviation();
            }
        }

        tableModel.setResults(results.toArray(ApproximationResults[]::new));
        tableModel.fireTableDataChanged();
        resultTable.changeSelection(best, 1, false, false);

        var dotDataset = new XYSeriesCollection();
        var dataset = new XYSeriesCollection();
        var dotSeries = new XYSeries("dots");
        for (int i = 0; i < n; i++) {
            dotSeries.add(xValuesArray[i], yValuesArray[i]);
        }
        double left = xValues.stream().min(Double::compareTo).get();
        double right = xValues.stream().max(Double::compareTo).get();
        double wide = right - left;
        left = left - wide / 10;
        right = right + wide / 10;
        dotDataset.addSeries(dotSeries);
        for (int i = 0; i < results.size(); i++) {
            var series = new XYSeries(results.get(i).getFunction().toString());
            for (double x = left; x <= right; x += 0.1) {
                series.add(x, results.get(i).getFunction().apply(x));
            }
            dataset.addSeries(series);
        }

        JFreeChart chart = ChartFactory.createXYLineChart(
                "График аппроксимации",
                "X",
                "Y",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true, false
        );
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setDataset(1, dotDataset);
        plot.setBackgroundPaint(Color.white);
        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        plot.setDomainGridlinePaint(Color.lightGray);
        plot.setRangeGridlinePaint(Color.lightGray);
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setDefaultShapesVisible(false);

        XYLineAndShapeRenderer renderer2 = new XYLineAndShapeRenderer(false, true);
        plot.setRenderer(1, renderer2);

        chart.setBackgroundPaint(new Color(238, 238, 238));
        chartPanel.setChart(chart);
    }

    private void onChooseFileButtonClick() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV File", "csv"));
        int ret = fileChooser.showOpenDialog(this);
        if (ret == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                List<Dot> inputData = new CsvToBeanBuilder<Dot>(new FileReader(file))
                        .withType(Dot.class)
                        .build()
                        .parse();
                for (int i = 0; i < inputData.size() && i < fieldsCount; i++) {
                    xFields.get(i).setText(String.valueOf(inputData.get(i).getX()));
                    yFields.get(i).setText(String.valueOf(inputData.get(i).getY()));
                }
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(this,
                        "Файл не найден",
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
            } catch (RuntimeException e) {
                JOptionPane.showMessageDialog(this,
                        "Неверный CSV формат",
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void onSaveToFileButtonClick() {
        if (Objects.isNull(results)) {
            JOptionPane.showMessageDialog(this,
                    "Нет результатов для сохранения",
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("CSV File", "csv"));
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                if (!filePath.substring(filePath.lastIndexOf(".") + 1).equals("csv"))
                    filePath += ".csv";
                try {
                    FileWriter writer = new FileWriter(filePath);
                    HeaderColumnNameMappingStrategy<ApproximationResults> strategy = new HeaderColumnNameMappingStrategy<>();
                    strategy.setType(ApproximationResults.class);
                    new StatefulBeanToCsvBuilder<ApproximationResults>(writer)
                            .withMappingStrategy(strategy)
                            .withApplyQuotesToAll(false)
                            .build()
                            .write(results);
                    writer.close();
                } catch (IOException | CsvRequiredFieldEmptyException | CsvDataTypeMismatchException e) {
                    JOptionPane.showMessageDialog(this,
                            "Не удалось записать в файл",
                            "Ошибка",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}
