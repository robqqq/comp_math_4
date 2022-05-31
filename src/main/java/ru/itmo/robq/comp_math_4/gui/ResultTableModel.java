package ru.itmo.robq.comp_math_4.gui;

import lombok.Getter;
import lombok.Setter;
import ru.itmo.robq.comp_math_4.approximation.ApproximationResults;

import javax.swing.table.AbstractTableModel;

@Getter
@Setter
public class ResultTableModel extends AbstractTableModel {
    private ApproximationResults[] results = new ApproximationResults[0];

    @Override
    public int getRowCount() {
        return results.length;
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int row, int column) {
        switch (column) {
            case 0:
                return results[row].getFunction();
            case 1:
                return results[row].getDeviation();
            case 2:
                return results[row].getR2();
            case 3:
                return results[row].getCorrelation();
            default:
                return 0;
        }
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "function";
            case 1:
                return "δ";
            case 2:
                return "r²";
            case 3:
                return "r";
            default:
                return "";
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 0) {
            return String.class;
        }
        return double.class;
    }
}
