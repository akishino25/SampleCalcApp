package com.example.work.simplecalcapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    //TODO:作成したCalcSetの削除長押し

    //Logcat用タグ文字列（クラス名）
    private final static String TAG = MainActivity.class.getSimpleName();
    private DBAdapter dbAdapter = new DBAdapter(MainActivity.this);

    //ProjectActivityとやり取りするProjectクラス
    private Project project = null;
    //現在編集中の計算セットの配列番号
    private int calcSetEditNo = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //アイコン表示
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        //ProjectsActivityからProjectを渡された時の処理
        this.project = (Project) getIntent().getSerializableExtra("Project");
        //ProjectIDを持つCalcSetで画面再描画
        dbAdapter.open();
        updateMemberAndViewOfCalcSetList(this.project.getProjectId());

        //NewボタンクリックListenr
        findViewById(R.id.newButton).setOnClickListener(
                new View.OnClickListener() {
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

        //プロジェクト保存ボタンクリックListener
        findViewById(R.id.saveButton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Projectクラスを更新する
                        EditText projectNameEdit = (EditText) findViewById(R.id.projectTitle);
                        Log.d(TAG, projectNameEdit.getText().toString());
                        project.setProjectName(projectNameEdit.getText().toString());
                        //ProjectクラスをProjectActivityに戻す
                        Intent intent = new Intent();
                        intent.putExtra("Project", project);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                }
        );


        //ListViewクリックListener
        final ListView listView = (ListView) findViewById(R.id.calcList);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //選択番号のpositionは、calcSetListの配列番号と同じという仮定に基づいて実装
                calcSetEditNo = position;
                Log.d(TAG, "position :" + position);
                Log.d(TAG, "calcSetList size :" + project.getCalcSetList().size());
                CalcSet calcSet = project.getCalcSetList().get(position);
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("calcSet", calcSet);
                int requestCode = 1002; //1002のリクエストコードで戻ってきた場合には、既存の行を更新する
                startActivityForResult(intent, requestCode);
            }
        });
    }

    /**
     * 他のActivityから処理が戻ってきた際の処理
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1001) { //Newボタン押下時の戻り処理
            if (resultCode == Activity.RESULT_OK) {
                CalcSet calcSet = (CalcSet) data.getSerializableExtra("calcSet");
                Log.d(TAG, "calcResult of calcSet:" + calcSet.getCalcResult());

                //CalcSetをメンバprojectのcalcSet配列に入れて、メンバ配列で画面更新（DBは見ない）
                ArrayList<CalcSet> tmpCalcSetList = this.project.getCalcSetList();
                tmpCalcSetList.add(calcSet);
                this.project.setCalcSetList(tmpCalcSetList);
                setListView(this.project.getCalcSetList());
            }
        }

        if (requestCode == 1002) { //既存行クリック時の戻り処理
            if (resultCode == Activity.RESULT_OK) {
                //戻されたcalcSetをメンバ配列として置き換え
                CalcSet calcSet = (CalcSet) data.getSerializableExtra("calcSet");
                Log.d(TAG, "calcResult of calcSet:" + calcSet.getCalcResult());

                //メンバprojectの配列を更新して、メンバ配列で画面更新（DBは見ない）
                ArrayList<CalcSet> tmpCalcSetList = this.project.getCalcSetList();
                tmpCalcSetList.set(calcSetEditNo, calcSet);
                this.project.setCalcSetList(tmpCalcSetList);
                calcSetEditNo = -1; //編集が完了したら、番号をリセットする
                setListView(this.project.getCalcSetList());
            }
        }
    }

    /**
     * DBの値をもとに、メンバおよび画面のCalcSetを更新
     *
     * @param projectId
     */
    private void updateMemberAndViewOfCalcSetList(int projectId) {
        Log.d(TAG, "projectID is " + projectId);
        //DBからClacSetListを取得してprojectメンバに設定
        this.project.setCalcSetList(dbAdapter.getCalcSets(projectId));
        setListView(this.project.getCalcSetList());
    }

    private void setListView(ArrayList<CalcSet> calcSetList) {
        //画面に表示するために、Mapを要素に持つListを作成
        List<Map<String, String>> calcListForListView = new ArrayList<Map<String, String>>();
        for (int i = 0; i < calcSetList.size(); i++) {
            Map<String, String> calcRow = new HashMap<String, String>();
            calcRow.put("Memo", calcSetList.get(i).getMemo());
            if(calcSetList.get(i).enableCalcCheck()){
                calcRow.put("CalcResult", calcSetList.get(i).calcLeft() + "=" + calcSetList.get(i).getCalcResult());
            }else{
                calcRow.put("CalcResult", null);
            }
            calcListForListView.add(calcRow);
        }

        SimpleAdapter adapter = new SimpleAdapter(this, calcListForListView,
                android.R.layout.simple_list_item_2,
                new String[]{"Memo", "CalcResult"},
                new int[]{android.R.id.text1, android.R.id.text2});

        ListView listView = (ListView) findViewById(R.id.calcList);
        listView.setAdapter(adapter);
    }
}
