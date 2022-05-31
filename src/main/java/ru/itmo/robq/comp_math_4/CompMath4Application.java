package ru.itmo.robq.comp_math_4;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import ru.itmo.robq.comp_math_4.gui.MainFrame;

import javax.swing.*;
import java.awt.*;

@SpringBootApplication
public class CompMath4Application {

    public static void main(String[] args) {
        ApplicationContext ctx = new SpringApplicationBuilder(CompMath4Application.class)
                .headless(false).run(args);

        EventQueue.invokeLater(() -> {
            JFrame mainFrame = ctx.getBean(MainFrame.class);
            mainFrame.setVisible(true);
        });
    }

}
