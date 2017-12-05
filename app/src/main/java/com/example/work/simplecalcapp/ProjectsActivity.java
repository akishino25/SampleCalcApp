package com.example.work.simplecalcapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
        dbAdapter.open();
        updateMemberAndViewOfProjectList();

        //プロジェクト新規作成ボタンクリックListener
        findViewById(R.id.newProjectButton).setOnClickListener(
                new View.OnClickListener() {
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

        ListView listView = (ListView) findViewById(R.id.projectList);
        //フローティングContextMenuを表示するViewを登録
        registerForContextMenu(listView);
        //ListViewクリックListener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //選択されたProjectのpositionはprojectListの配列番号と同じという仮定に基づいて実装
                Project project = projectList.get(position);
                Intent intent = new Intent(ProjectsActivity.this, MainActivity.class);
                intent.putExtra("Project", project);
                int requestCode = 1001;
                startActivityForResult(intent, requestCode);
            }
        });
    }

    /**
     * 他のActivityから処理が戻ってきた場合の処理
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1001) { //プロジェクト作成の戻り処理
            Project project = (Project) data.getSerializableExtra("Project");
            Log.d(TAG, project.getProjectName());

            //Project、およびCalcSetをDBに格納して、DB情報を画面に再描画
            dbAdapter.open();

            //Projectのidが付与されていなければProject新規登録、付与されていれば更新
            if (project.getProjectId() == 0) {
                double projectId = dbAdapter.insertProject(project);
                project.setProjectId((int) projectId);
                Log.d(TAG, "Id of new Project is " + projectId);
            } else {
                double projectId = dbAdapter.updateProject(project);
                Log.d(TAG, "Id of update Project is "+projectId);
            }

            //ClacSetsのDB処理
            for (CalcSet calcSet : project.getCalcSetList()) {
                if (calcSet.getCalcSetId() == 0) {
                    Log.d(TAG, "Paranet's Id of Project is " + project.getProjectId());
                    calcSet.setProjectId(project.getProjectId());
                    dbAdapter.insertCalcSet(calcSet);
                } else {
                    dbAdapter.updateCalcSet(calcSet);
                }
            }
            updateMemberAndViewOfProjectList();

            //TODO:リストビュー押下時の処理を追加
        }
    }

    /**
     * ContextMenu表示を登録されたViewが長押しクリックを検知した場合
     *
     * @param menu
     * @param v
     * @param menuInfo
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.projects_menu, menu);
    }

    /**
     * ContextMenu内のItemが選択された時
     *
     * @param item
     * @return
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.deleteProject:
                //選択されたListViewの行はinfo.positionで取得
                Log.d(TAG, "Target of delete operation is " + info.position);
                //選択された行番号から、ProjectIDを取得
                Project project = projectList.get(info.position);
                int projectId = project.getProjectId();
                dbAdapter.deleteProject(projectId);
                //TODO:削除Projectに関連するCalcSetも削除
                //削除後に画面再描画
                updateMemberAndViewOfProjectList();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    /**
     * メンバのprojectList　および　画面　をDBの値で更新
     */
    private void updateMemberAndViewOfProjectList() {
        this.projectList = dbAdapter.getAllProjects();
        setListView(this.projectList);
    }


    /**
     * ProjectのArrayListを画面に再描画
     *
     * @param projectList
     */
    private void setListView(ArrayList<Project> projectList) {
        List<String> projectNameList = new ArrayList<String>();
        for (Project p : projectList) {
            String pName = p.getProjectName();
            projectNameList.add(pName);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, projectNameList);

        ListView listView = (ListView) findViewById(R.id.projectList);
        listView.setAdapter(adapter);
    }
}
