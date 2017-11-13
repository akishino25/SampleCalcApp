package com.example.work.simplecalcapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //Logcat用タグ文字列（クラス名）
    private final static String TAG = MainActivity.class.getSimpleName();

    //画面に表示する計算セットを保持する配列
    private ArrayList<CalcSet> calcSetList = new ArrayList<CalcSet>();
    //現在編集中の計算セットの配列番号
    private int calcSetEditNo = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //NewボタンクリックListenr
        findViewById(R.id.newButton).setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        //空のClacSetを作成して、DetailActivityに渡す
                        CalcSet calcSet = new CalcSet();
                        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                        intent.putExtra("calcSet", calcSet);
                        int requestCode = 1001;
                        startActivityForResult(intent, requestCode);

                    }
            }
        );

        //ListViewクリックListener
        final ListView listView = (ListView)findViewById(R.id.calcList);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //選択番号のpositionは、calcSetListの配列番号と同じという仮定に基づいて実装
                calcSetEditNo = position;
                CalcSet calcSet = calcSetList.get(position);
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("calcSet", calcSet);
                int requestCode = 1002; //1002のリクエストコードで戻ってきた場合には、既存の行を更新する
                startActivityForResult(intent, requestCode);
            }
        });

    }

    /**
     * 他のActivityから処理が戻ってきた際の処理
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1001){ //Newボタン押下時の戻り処理
            if(resultCode == Activity.RESULT_OK){
                //戻されたcalcSetをメンバ配列に格納
                //Log.d(TAG, "Intent Test:" +data.getStringExtra("testIntent"));
                CalcSet calcSet = (CalcSet)data.getSerializableExtra("calcSet");
                Log.d(TAG, "calcResult of calcSet:" +calcSet.getCalcResult());
                this.calcSetList.add(calcSet);

                //画面にcalcSetListを再描画
                setLisViewFromCalcSetList(this.calcSetList);
            }
        }

        if(requestCode == 1002) { //既存行クリック時の戻り処理
            if(resultCode == Activity.RESULT_OK) {
                //戻されたcalcSetをメンバ配列として置き換え
                CalcSet calcSet = (CalcSet)data.getSerializableExtra("calcSet");
                Log.d(TAG, "calcResult of calcSet:" +calcSet.getCalcResult());
                this.calcSetList.set(calcSetEditNo, calcSet);
                calcSetEditNo = -1; //編集が完了したら、番号をリセットする

                //画面にcalcSetListを再描画
                setLisViewFromCalcSetList(this.calcSetList);
            }
        }
    }

    private void setLisViewFromCalcSetList(ArrayList<CalcSet> calcSetList){
        //画面に表示するために、Mapを要素に持つListを作成
        List<Map<String, String>> calcListForListView = new ArrayList<Map<String, String>>();
        for(int i=0; i<calcSetList.size(); i++){
            Map<String, String> calcRow = new HashMap<String, String>();
            calcRow.put("Memo", calcSetList.get(i).getMemo());
            calcRow.put("CalcResult", calcSetList.get(i).calcLeft() +"=" +calcSetList.get(i).getCalcResult());
            calcListForListView.add(calcRow);
        }

        SimpleAdapter adapter = new SimpleAdapter(this, calcListForListView,
                android.R.layout.simple_list_item_2,
                new String[]{"Memo", "CalcResult"},
                new int[]{android.R.id.text1, android.R.id.text2});

        ListView listView = (ListView)findViewById(R.id.calcList);
        listView.setAdapter(adapter);
    }

}
