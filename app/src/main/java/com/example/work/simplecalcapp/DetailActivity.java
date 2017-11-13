package com.example.work.simplecalcapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    //Logcat用タグ文字列（クラス名）
    private final static String TAG = DetailActivity.class.getSimpleName();

    private CalcSet calcSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //渡されたCalcSetをメンバに設定
        this.calcSet = (CalcSet) getIntent().getSerializableExtra("calcSet");

        //CalcSetを展開してViewに表示
        setViewFromCalcSet(calcSet);

        //確定ボタンクリックListener
        Button confirmButton = (Button) findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //メモ、入力1,2、演算子1の値を取得
                //TODO:詳細画面で値を入力せずに確定するとアプリが落ちる
                EditText inputMemoEdit = (EditText) findViewById(R.id.inputMemo);
                String inputMemo = inputMemoEdit.getText().toString();
                EditText firstInput = (EditText) findViewById(R.id.firstInputNum);
                int inputNum1 = Integer.parseInt(firstInput.getText().toString());
                EditText secondInput = (EditText) findViewById(R.id.secondInputNum);
                int inputNum2 = Integer.parseInt(secondInput.getText().toString());
                Spinner firstInputSymbol = (Spinner) findViewById(R.id.symbol_spinner);
                String inputSym1 = (String) firstInputSymbol.getSelectedItem();

                //メンバのCalcSetに改めて設定
                calcSet.setMemo(inputMemo);
                ArrayList<Integer> inputNums = new ArrayList<Integer>();
                inputNums.add(inputNum1);
                inputNums.add(inputNum2);
                calcSet.setInputNums(inputNums);
                ArrayList<String> inputSyms = new ArrayList<String>();
                inputSyms.add(inputSym1);
                calcSet.setInputSyms(inputSyms);

                //計算処理して計算結果を設定
                calcSet.calcExec();

                //IntentでcalcSetをMainActivityに戻す
                Intent intent = new Intent();
                Log.d(TAG, "calcResult of calcSet:" +calcSet.getCalcResult());
                intent.putExtra("calcSet", calcSet);
                //intent.putExtra("testIntent", "test");
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    /**
     * CalcSetの中身を展開して画面に表示
     *
     * @param calcSet
     */
    private void setViewFromCalcSet(CalcSet calcSet) {
        //メモ表示
        if (calcSet.getMemo() != null) {
            EditText inputMemo = (EditText) findViewById(R.id.inputMemo);
            inputMemo.setText(calcSet.getMemo());
        }

        //入力数値表示
        if (calcSet.getInputNums() != null) {
            //TODO:固定で2つのEditしか対応していないので、3つ以上入力数値が存在する場合に未対応
            EditText firstInputNum = (EditText) findViewById(R.id.firstInputNum);
            EditText secondInputNum = (EditText) findViewById(R.id.secondInputNum);
            firstInputNum.setText(calcSet.getInputNums().get(0).toString());
            secondInputNum.setText(calcSet.getInputNums().get(1).toString());
        }

        //入力演算子
        if (calcSet.getInputSyms() != null) {
            Spinner firstInputSymbol = (Spinner) findViewById(R.id.symbol_spinner);
            //TODO: Spinnerの値の変更方法が分からん
        }

    }
}