package com.example.work.simplecalcapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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
        setView(calcSet);

        //確定ボタンクリックListener
        Button confirmButton = (Button) findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //memoメンバ更新
                EditText inputMemoEdit = (EditText) findViewById(R.id.inputMemo);
                String inputMemo = inputMemoEdit.getText().toString();
                if(!TextUtils.isEmpty(inputMemo)){
                    calcSet.setMemo(inputMemo);
                }
                //iputNumsメンバ更新
                EditText firstInput = (EditText) findViewById(R.id.firstInputNum);
                String firstInputStr = firstInput.getText().toString();
                EditText secondInput = (EditText) findViewById(R.id.secondInputNum);
                String secondInputStr = secondInput.getText().toString();
                if(!TextUtils.isEmpty(firstInputStr) && !TextUtils.isEmpty(secondInputStr)){
                    int inputNum1 = Integer.parseInt(firstInputStr);
                    int inputNum2 = Integer.parseInt(secondInputStr);
                    ArrayList<Integer> inputNums = new ArrayList<Integer>();
                    inputNums.add(inputNum1);
                    inputNums.add(inputNum2);
                    calcSet.setInputNums(inputNums);
                }
                //inputSymsメンバ更新
                Spinner firstInputSymbol = (Spinner) findViewById(R.id.symbol_spinner);
                String inputSym1 = (String) firstInputSymbol.getSelectedItem();
                if(!TextUtils.isEmpty(inputSym1)){
                    ArrayList<String> inputSyms = new ArrayList<String>();
                    inputSyms.add(inputSym1);
                    calcSet.setInputSyms(inputSyms);
                }
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
    private void setView(CalcSet calcSet) {
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
            ArrayAdapter<String> adapter = (ArrayAdapter<String>)firstInputSymbol.getAdapter();
            Log.d(TAG, calcSet.getInputSyms().get(0));
            int position = adapter.getPosition(calcSet.getInputSyms().get(0));
            firstInputSymbol.setSelection(position);
        }
    }
}