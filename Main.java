package Calcurator;

import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.time.chrono.IsoChronology;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Main {
    public static void main(String[] args){
        // インスタンス化
        Calc calc = new Calc();
        calc.setDefaultCloseOperation(EXIT_ON_CLOSE);

        // 計算機の大きさ
        calc.setSize(200, 200);

        //  可視化
        calc.setVisible(true);
    }
}
