package ru.itmo.robq.comp_math_4.functions;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dot {
    @CsvBindByName(column = "x", required = true)
    private double x;
    @CsvBindByName(column = "y", required = true)
    private double y;
}
