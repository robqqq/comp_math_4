package ru.itmo.robq.comp_math_4.approximation;

import com.opencsv.bean.CsvBindByName;
import lombok.*;
import ru.itmo.robq.comp_math_4.functions.Function;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApproximationResults {
    @CsvBindByName(column = "function", required = true)
    private Function function;
    @CsvBindByName(column = "deviation", required = true)
    private double deviation;
    @CsvBindByName(column = "correlation", required = true)
    private Double correlation;
    @CsvBindByName(column = "r2")
    private double r2;
}
