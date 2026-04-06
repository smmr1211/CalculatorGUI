package Calcurator;

import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.plaf.synth.SynthTextAreaUI;
import java.math.RoundingMode;
import java.sql.Array;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Set;

import java.text.DecimalFormat;
import java.math.BigDecimal;

public class Funckey extends AbstractAction {
    // フィールド
    private String operator;
    JTextField field;
    Calc calc;

    // コンストラクタ
    public Funckey(String operator, JTextField field, Calc calc) {
        putValue(Action.NAME, operator);
        this.operator = operator;
        this.field = field;
        this.calc = calc;
    }

    // メソッド stackを用いた計算
    public void surfaceCalc() {
        // num1:最表層、num2:num1の下　※ popしてるので出した中身はなくなる！
        double num1 = calc.getStack_num().pop();
        double num2 = calc.getStack_num().pop();
        double result = 0;

        // 演算子を取得
        String op = calc.getStack_op().pop();

        // =が連打されたときのために計算を記録

        while (calc.getStack_num_retain().size() != 0) {
            calc.getStack_num_retain().pop();
        }
        while (calc.getStack_op_retain().size() != 0) {
            calc.getStack_op_retain().pop();
        }
        calc.getStack_num_retain().push(num1);
        calc.getStack_op_retain().push(op);

        // 演算子に応じて計算を実行
        switch (op) {
            case "+":
                result = num2 + num1;
                break;
            case "-":
                result = num2 - num1;
                break;
            case "×":
                result = num2 * num1;
                break;
            case "÷":
                if (num1 == 0) {
                    field.setText("エラー");
                    calc.setEroor(true);
                    return;
                }
                result = num2 / num1;
                break;
        }

        // 計算結果を数字stackにpush
        calc.getStack_num().push(result);
    }

    // 演算子に優先順位のラベル付けをする関数
    private int getOperatorPriority(String tmp_op) {
        switch (tmp_op) {
            case "+":
            case "-":
                return 0;
            case "×":
            case "÷":
                return 1;
            default:
                return 0;
        }
    }

    // 出力を整える関数
    private String formatNumber(double num) {
        final double MIN_THRESHOLD = Math.pow(10, -8);
        final double MAX_THRESHOLD = Math.pow(10, 125);

        // 最小値を下回ったら0を出力
        if (Math.abs(num) < MIN_THRESHOLD) return "0";

        // 最大値を上回ったらエラーを出力
        if (Math.abs(num) > MAX_THRESHOLD) {
            field.setText("エラー");
            calc.setEroor(true);
            return "エラー";
        }

        BigDecimal bd = BigDecimal.valueOf(num).stripTrailingZeros();

        // 桁数を制御、有効数字9桁で丸め
        int precision = bd.precision();
        int scale = bd.scale();

        // 有効数字9桁になるように調整
        if (precision > 9) {
            int newScale = scale - (precision - 9);
            if (newScale < 0) newScale = 0;
            bd = bd.setScale(newScale, RoundingMode.HALF_UP);
        }

        bd = bd.stripTrailingZeros();

        // 桁数が多い場合は指数表示に切り替える
        if (Math.abs(num) >= 1e9 || Math.abs(num) < 1e-4) {
            // 指数表示
            DecimalFormat expFormat = new DecimalFormat("0.########E0");
            expFormat.setRoundingMode(RoundingMode.HALF_UP);
            return expFormat.format(bd);
        }

        // default表示
        return bd.toPlainString();
    }

    // メソッド イベントの処理
    public void actionPerformed(ActionEvent e) {

        // 押下された演算子を取得
        String new_op = (String) getValue(Action.NAME);

        // = 連打対策
        if(!new_op.equals("=")){
            calc.setEqual(false);
        }

        // もし=が連打されていた場合
        if (new_op.equals("=") && calc.isEqual()) {
            // フィールド上の数字をとってくる
            double result = Double.parseDouble(field.getText());
            // 直前の計算を再現する
            double n = calc.getStack_num_retain().peek();
            String op = calc.getStack_op_retain().peek();

            // 演算子に応じた演算を実行
            switch (op) {
                case "+":
                    result = result + n;
                    break;
                case "-":
                    result = result - n;
                    break;
                case "×":
                    result = result * n;
                    break;
                case "÷":
                    if (n == 0) {
                        field.setText("エラー");
                        calc.setEroor(true);
                        return;
                    }
                    result = result / n;
                    break;
            }
            field.setText(formatNumber(result));
            return;
        }

        if (!new_op.equals("=") && calc.isEqual()) {
            calc.setEqual(false);
        }

        // 直前に不適切な計算を行なってエラーを出していた場合はAC以外受け付けない
        if (calc.isEroor()) {
            if (new_op.equals("AC")) {
                field.setText("0");
                while (calc.getStack_num().size() != 0) {
                    calc.getStack_num().pop();
                }
                while (calc.getStack_op().size() != 0) {
                    calc.getStack_op().pop();
                }
                calc.setEroor(false);
            }
            return;
        }

        // +/-, ., %, C, ACのとき -> field上での表示を切り替えるだけ
        calc.setIsNewInput(false);
        switch (new_op) {
            case "+/-":
                field.setText(formatNumber((-1) * Double.parseDouble(field.getText())));
                return;
            case ".":
                if (field.getText().equals("0")) {
                    field.setText("0.");
                    return;
                } else if (!field.getText().contains(".")) {
                    field.setText(field.getText() + ".");
                    return;
                } else {
                    return;
                }
            case "%": // フィールド上の数字を0.01倍
                field.setText(formatNumber((0.01) * Double.parseDouble(field.getText())));
                return;
            case "C": // フィールドを空にする
                field.setText("0");
                calc.setIsNewInput(true);
                return;
            case "AC": // フィールドクリア + スタック空にする
                field.setText("0");
                while (calc.getStack_num().size() != 0) {
                    calc.getStack_num().pop();
                }
                while (calc.getStack_op().size() != 0) {
                    calc.getStack_op().pop();
                }
                calc.setIsNewInput(true);
                return;
        }

        // 次のNumkey入力時にフィールドをリセットさせたい！
        calc.setIsNewInput(true);

        // フィールドに表示されている数字をpush
        if (!calc.isOperatorJustPressed()) {
            calc.getStack_num().push((Double.parseDouble(field.getText())));

        } else {
            // 直前も演算子だったということ！
            // 演算子を上書きしたい、opスタックに何か入っていれば追い出しておく
            if (!calc.getStack_op().isEmpty()) {
                calc.getStack_op().pop();
            }
        }

        // 何も計算すべき演算子がない
        if (calc.getStack_op().isEmpty()) {
            // 演算子をpush
            calc.getStack_op().push(new_op);

            // stack_numのpeekをフィールドに表示
            field.setText(formatNumber(calc.getStack_num().peek()));

            // 演算子受け付けた！
            calc.setOperatorJustPressed(true);

            // =連打用に記録
            calc.getStack_num_retain().push(Double.parseDouble(field.getText()));
            calc.getStack_op_retain().push(new_op);
            calc.setEqual(true);
            return;
        }

        // 演算子が"="だった場合
        if (new_op.equals("=")) {
            if (!calc.isEqual()) {
                while (!calc.getStack_op().isEmpty()) {
                    surfaceCalc();
                }
                field.setText(formatNumber(calc.getStack_num().peek()));
                calc.getStack_num().pop();
                calc.setOperatorJustPressed(false); // reset
                calc.setEqual(true);
                System.out.println(calc.isEqual());
                return;
            }
        }
        calc.setOperatorJustPressed(true); // 演算子がおされた！

        // 演算子stackに乗除が残っているとき
        if (getOperatorPriority(calc.getStack_op().peek()) == 1) {
            while (getOperatorPriority(calc.getStack_op().peek()) != 0) {
                surfaceCalc();
            }

            calc.getStack_op().push(new_op);

            // stack_numのpeekをフィールドに表示
            field.setText(formatNumber(calc.getStack_num().peek()));
            return;
        }

        // 演算子stackに和差が残っているとき
        if (getOperatorPriority(calc.getStack_op().peek()) == 0) {
            // case1) new_opも和差
            if (getOperatorPriority(new_op) == 0) {
                surfaceCalc();
                calc.getStack_op().push(new_op);

                // stack_numのpeekをフィールドに表示
                field.setText(formatNumber(calc.getStack_num().peek()));
                return;
            }
            // case2) new_opが乗除
            else {
                calc.getStack_op().push(new_op);
                // fieldに対しては沈黙
            }
        }
    }
}
