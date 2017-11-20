package com.example.work.simplecalcapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

public class ProjectsActivity extends AppCompatActivity {

    //Logcat用タグ文字列（クラス名）
    private final static String TAG = ProjectsActivity.class.getSimpleName();

    private ArrayList<Project> projectList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects);

        //プロジェクト新規作成ボタンクリックListener
        findViewById(R.id.newProjectButton).setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        //空のProjectを作成してMainActivityに渡す
                        Project project = new Project();
                        Intent intent = new Intent(ProjectsActivity.this, MainActivity.class);
                        intent.putExtra("Project", project);
                        int requestCode = 1001;
                        startActivityForResult(intent, requestCode);
                    }
                }
        );
    }

    /**
     * 他のActivityから処理が戻ってきた場合の処理
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1001){ //プロジェクト作成の戻り処理
            Project project = (Project) data.getSerializableExtra("Project");
            Log.d(TAG, project.getProjectName());
            //TODO 戻されたプロジェクトをListViewとして表示する
        }
    }
}
