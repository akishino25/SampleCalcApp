package com.example.work.simplecalcapp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {
    //TODO:計算式は左から順に実施していることが分かるように工夫する→あるいは計算できるように関数実装
    //TODO:追加した計算セットの削除ボタン
    //一度入力すると小数表示されるが、小数点を入力できない

    //Logcat用タグ文字列（クラス名）
    private final static String TAG = DetailActivity.class.getSimpleName();

    private CalcSet calcSet;

    //動的に作成したInputNum用EditTextのリスト
    ArrayList<EditText> inputNumEditList = new ArrayList<EditText>();

    //動的に作成したInputSym用Spinnerのリスト
    ArrayList<Spinner> inputSymSpinnerList = new ArrayList<Spinner>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //アイコン表示
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        //渡されたCalcSetをメンバに設定
        this.calcSet = (CalcSet) getIntent().getSerializableExtra("calcSet");

        //CalcSetを展開してViewに表示
        setView(calcSet);

        //追加ボタンクリックListener
        Button addPartButton = (Button) findViewById(R.id.addPartButton);
        addPartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addInputSymView();
                addInputNumberView();
            }
        });


        //確定ボタンクリックListener
        Button confirmButton = (Button) findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validate() == false){
                    //入力に不正がある
                    new AlertDialog.Builder(DetailActivity.this)
                            .setTitle(R.string.detailValidateTitle)
                            .setMessage(R.string.detailValidateMessage)
                            .setPositiveButton("OK", null)
                            .show();
                    return;
                }

                Log.d(TAG, Boolean.toString(validate()));

                //memoメンバ更新
                EditText inputMemoEdit = (EditText) findViewById(R.id.inputMemo);
                String inputMemo = inputMemoEdit.getText().toString();
                if(!TextUtils.isEmpty(inputMemo)){
                    calcSet.setMemo(inputMemo);
                }
                //iputNumsメンバ更新
                ArrayList<Double> l = new ArrayList<Double>();
                for(EditText inputNumberEdit :inputNumEditList){
                    l.add(Double.parseDouble(inputNumberEdit.getText().toString()));
                }
                calcSet.setInputNums(l);

                //inputSymsメンバ更新
                ArrayList<String> m = new ArrayList<String>();
                for(Spinner inputSymSpinner :inputSymSpinnerList){
                    m.add(inputSymSpinner.getSelectedItem().toString());
                }
                calcSet.setInputSyms(m);

                //計算処理して計算結果を設定
                calcSet.calcExec();

                for(String sym : calcSet.getInputSyms()){
                    Log.d(TAG, "sym:" +sym);
                }

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
     * InputNumber用のTextViewおよびEditTextを作成
     */
    private void addInputNumberView(){
        LinearLayout detailLinerLayout = findViewById(R.id.detailLinearLayout);
        Log.d(TAG, "LinerLayout size is " +detailLinerLayout.getChildCount());

        //inputNumber の 説明文用TextViewを追加
        TextView inputNumText = new TextView(DetailActivity.this);
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        llp.setMargins(Util.convertDpToPx(DetailActivity.this, 8), Util.convertDpToPx(DetailActivity.this, 8),8,0);
        //inputNumText.setLayoutParams(llp);
        inputNumText.setText(R.string.inputNumber);
        detailLinerLayout.addView(inputNumText, detailLinerLayout.getChildCount(), llp);

        //inputNumber の EditTextを追加
        EditText inputNumberEdit = new EditText(DetailActivity.this);
        llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        llp.setMargins(Util.convertDpToPx(DetailActivity.this, 8), Util.convertDpToPx(DetailActivity.this, 8),8,Util.convertDpToPx(DetailActivity.this, 8));
        llp.gravity = Gravity.CENTER;
        inputNumberEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
        inputNumberEdit.setEms(10);
        detailLinerLayout.addView(inputNumberEdit, detailLinerLayout.getChildCount(), llp);

        //作成したEditTextはメンバ配列に追加
        inputNumEditList.add(inputNumberEdit);
    }

    /**
     * InputSym用のTextViewおよびSpinnerを作成
     */
    private void addInputSymView(){
        String spinnerItems[] = getResources().getStringArray(R.array.symbol_items);

        LinearLayout detailLinerLayout = findViewById(R.id.detailLinearLayout);
        Log.d(TAG, "LinerLayout size is " +detailLinerLayout.getChildCount());

        //inputSym の 説明文用TextViewを追加
        TextView inputSymText = new TextView(DetailActivity.this);
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        llp.setMargins(Util.convertDpToPx(DetailActivity.this, 8), Util.convertDpToPx(DetailActivity.this, 8),8,0);
        inputSymText.setText(R.string.inputSymbol);
        detailLinerLayout.addView(inputSymText, detailLinerLayout.getChildCount(), llp);

        //inputSym の Spinnerを追加
        Spinner inputSymSpinner = new Spinner(DetailActivity.this);
        llp = new LinearLayout.LayoutParams(Util.convertDpToPx(DetailActivity.this, 80), Util.convertDpToPx(DetailActivity.this, 46));
        llp.setMargins(0, Util.convertDpToPx(DetailActivity.this, 8),0,Util.convertDpToPx(DetailActivity.this, 8));
        llp.gravity = Gravity.CENTER;
        inputSymSpinner.setBackgroundResource(R.drawable.spinner_drawable);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputSymSpinner.setAdapter(adapter);
        detailLinerLayout.addView(inputSymSpinner, detailLinerLayout.getChildCount(), llp);

        //作成したEditTextはメンバ配列に追加
        inputSymSpinnerList.add(inputSymSpinner);
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

        /*動的に計算部表示*/

        if(calcSet.getInputNums() == null || calcSet.getInputSyms() == null){ //CalcSetが空の場合
            addInputNumberView();
            addInputSymView();
            addInputNumberView();
        }else if(calcSet.enableCalcCheck()){ //ClacSetに計算可能な情報が入っている場合
            //1つ目のInputNumberViewは固定で出力
            addInputNumberView();
            //以降は演算子の数分、InputSymとInputNumber用のViewを表示
            for(int i=0; i<calcSet.getInputSyms().size(); i++){
                addInputSymView();
                addInputNumberView();
            }
            //InputNumberを画面にセット
            for (int i=0; i<calcSet.getInputNums().size(); i++){
                EditText inputNumberEdit = inputNumEditList.get(i);
                inputNumberEdit.setText(calcSet.getInputNums().get(i).toString());
            }
            //InputSymsを画面にセット
            for(int i=0; i<calcSet.getInputSyms().size(); i++){
                Spinner inputSymsSpinner = inputSymSpinnerList.get(i);
                ArrayAdapter<String> adapter = (ArrayAdapter<String>)inputSymsSpinner.getAdapter();
                int position = adapter.getPosition(calcSet.getInputSyms().get(i));
                inputSymsSpinner.setSelection(position);            }
        }
    }

    /**
     * 確定ボタン押下時に、入力に問題無いかチェックする
     * @return
     */
    private boolean validate(){
        //数値入力欄が全て入力されていること
        for(EditText inputNumberEdit :inputNumEditList){
            if(TextUtils.isEmpty(inputNumberEdit.getText().toString())){
                return false;
            }
        }
        return true;
    }

}