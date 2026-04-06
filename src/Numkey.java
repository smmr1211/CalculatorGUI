package Calcurator;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JTextField;

//
public class Numkey extends AbstractAction{
	// フィールド
	Calc calc;
	JTextField field;

	// コンストラクタ
	Numkey(String key, JTextField field, Calc calc){
		putValue(Action.NAME, key);
		this.field = field;
		this.calc = calc; // これでcalcのなかのdequeにアクセスできるようになった！
	}

	// イベントの処理
	public void actionPerformed(ActionEvent e) {
		calc.setOperatorJustPressed(false);
		calc.setEqual(false);
		if (calc.getIsNewInput() && !calc.isEroor()) {
			field.setText("");
			calc.setIsNewInput(false);// 直前は演算子であったのでフィールドをリセット
		}
		if(!calc.isEroor()) {
			String input = (String) getValue(Action.NAME);
			field.setText(field.getText() + input);
		}
	}
}
