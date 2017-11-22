package com.example.work.simplecalcapp;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ProjectsActivity extends AppCompatActivity {

    //Logcat用タグ文字列（クラス名）
    private final static String TAG = ProjectsActivity.class.getSimpleName();

    private ArrayList<Project> projectList = new ArrayList<Project>();

    private DBAdapter dbAdapter = new DBAdapter(ProjectsActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects);

        //DBからProjectListを取得し、画面再描画
        updateProjectListFromProjectTable();
        setListViewFromProjectList(this.projectList);

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
            /*
            //Projectをリストに格納して、画面に再描画
            this.projectList.add(project);
            setListViewFromProjectList(this.projectList);
            */

            //ProjectをDBに格納して、DB情報を画面に再描画
            dbAdapter.open();
            dbAdapter.insertProject(project);
            updateProjectListFromProjectTable();
            setListViewFromProjectList(this.projectList);


            //TODO:リストビュー押下時の処理を追加
        }
    }

    /**
     * メンバのProjectListをProjectTableの内容で更新
     */
    private void updateProjectListFromProjectTable(){
        ArrayList<Project> projectList = new ArrayList<Project>();
        Cursor cursor = dbAdapter.getAllProjects();
        if(cursor != null){
            //cursorの参照先を先頭にする
            boolean isEof = cursor.moveToFirst();
            while(isEof){
                String projectName = cursor.getString(cursor.getColumnIndex(dbAdapter.COL_PROJECT_NAME));
                Project project = new Project();
                project.setProjectName(projectName);
                projectList.add(project);
                isEof = cursor.moveToNext();
            }
            cursor.close();
            this.projectList = projectList;
        }
    }

    /**
     * ProjectのArrayListを画面に再描画
     * @param projectList
     */
    private void setListViewFromProjectList(ArrayList<Project> projectList){
        List<String> projectNameList= new ArrayList<String>();
        for(Project p: projectList){
            String pName = p.getProjectName();
            projectNameList.add(pName);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, projectNameList);

        ListView listView = (ListView)findViewById(R.id.projectList);
        listView.setAdapter(adapter);
    }
}
