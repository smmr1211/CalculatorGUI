package Calcurator;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayDeque;
import java.util.Stack;
import java.math.BigDecimal;

import javax.swing.*;

public class Calc extends JFrame {
	// フィールド
	//private boolean isNewInput = false; // 演算子の後の数値入力を判断
	Stack<String> stack_op = new Stack<String>(); // 演算子を格納するstack
	Stack<Double> stack_num = new Stack<Double>(); // 数字を格納するstack
	Stack<String> stack_op_retain = new Stack<String>();
	Stack<Double> stack_num_retain = new Stack<Double>();
	private boolean isNewInput = false; // 演算子の後の数値入力を判断
	JTextField field = new JTextField(); // 入力、計算結果を表示するフィールドを作成
	private boolean isOperatorJustPressed = false;
	private boolean isEroor = false;
	private boolean isEqual = false;

	public Calc() {
		super("Calcurator");
		//部品の追加
		JPanel pane = (JPanel) getContentPane();
		pane.add(field, BorderLayout.NORTH);
		JPanel keyPanel = new JPanel(new GridLayout(5, 5));

		pane.add(keyPanel, BorderLayout.CENTER);

		// NumkeyとFunckeyを設置
		String[] keys = {"C", "+/-", "%", "÷",
				"7", "8", "9", "×",
				"4", "5", "6", "-",
				"1", "2", "3", "+",
				"0", "AC", ".", "="};

		for (String key : keys) {
			if (key.matches("[0-9]")) {
                keyPanel.add(new JButton(new Numkey(key, field, this)));
			}else{
				//System.out.println(key);
				keyPanel.add(new JButton(new Funckey(key, field, this)));
			}
		}
	}

	// getter & setter

	public Stack<String> getStack_op() {
		return stack_op;
	}

	public void setStack_op(Stack<String> stack_op) {
		this.stack_op = stack_op;
	}

	public Stack<Double> getStack_num() {
		return stack_num;
	}

	public void setStack_num(Stack<Double> stack_num) {
		this.stack_num = stack_num;
	}

	public JTextField getField() {
		return field;
	}

	public void setField(JTextField field) {
		this.field = field;
	}

	public boolean getIsNewInput() {
		return isNewInput;
	}

	public void setIsNewInput(boolean newInput) {
		isNewInput = newInput;
	}

	public boolean isOperatorJustPressed() {
		return isOperatorJustPressed;
	}

	public void setOperatorJustPressed(boolean operatorJustPressed) {
		isOperatorJustPressed = operatorJustPressed;
	}

	public boolean isEroor() {
		return isEroor;
	}

	public void setEroor(boolean eroor) {
		isEroor = eroor;
	}

	public boolean isEqual() {
		return isEqual;
	}

	public void setEqual(boolean equal) {
		isEqual = equal;
	}

	public Stack<Double> getStack_num_retain() {
		return stack_num_retain;
	}

	public void setStack_num_retain(Stack<Double> stack_num_retain) {
		this.stack_num_retain = stack_num_retain;
	}

	public Stack<String> getStack_op_retain() {
		return stack_op_retain;
	}

	public void setStack_op_retain(Stack<String> stack_op_retain) {
		this.stack_op_retain = stack_op_retain;
	}
}
