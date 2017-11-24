package com.example.work.simplecalcapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * SQLite接続用クラス
 * Created by work on 2017/11/22.
 */

public class DBAdapter {
    //Logcat用タグ文字列（クラス名）
    private final static String TAG = DBAdapter.class.getSimpleName();

    static final String DATABASE_NAME = "simpleCalc.db";
    static final int DATABASE_VERSION = 1;

    //Projectテーブル
    public static final String PROJECT_TABLE_NAME = "projectsTable";
    public static final String COL_PROJECT_ID = "id";
    public static final String COL_PROJECT_NAME = "name";

    //CalcSetテーブル
    public static final String CALCSET_TABLE_NAME = "calcSetsTable";
    public static final String COL_CAlCSET_ID = "id";
    public static final String COL_CALCSET_PROJECTID = "projectId";
    public static final String COL_CAlCSET_MEMO = "memo";
    public static final String COL_CAlCSET_INPUTNUMS = "inputNums";
    public static final String COL_CAlCSET_INPUTSYMS = "inputSyms";
    public static final String COL_CAlCSET_CALCRESULT = "calcResult";

    protected final Context context;
    protected DatabaseHelper dbHelper;
    protected SQLiteDatabase db;

    public DBAdapter(Context context) {
        this.context = context;
        this.dbHelper = new DatabaseHelper(context);
    }

    //内部クラス
    private static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        //DBに初めて接続した際
        public void onCreate(SQLiteDatabase db) {
            //Tableを作成
            db.execSQL("CREATE TABLE " + PROJECT_TABLE_NAME + " ("
                    + COL_PROJECT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COL_PROJECT_NAME + " TEXT" + ");"
            );
            db.execSQL("CREATE TABLE " + CALCSET_TABLE_NAME + " ("
                    + COL_CAlCSET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COL_CALCSET_PROJECTID + " INTEGER NOT NULL, "
                    + COL_CAlCSET_MEMO + " TEXT, "
                    + COL_CAlCSET_INPUTNUMS + " TEXT, "
                    + COL_CAlCSET_INPUTSYMS + " TEXT, "
                    + COL_CAlCSET_CALCRESULT + " REAL, "
                    + "FOREIGN KEY(" + COL_CALCSET_PROJECTID + ") REFERENCES " + PROJECT_TABLE_NAME + "(" + COL_PROJECT_ID + "));"
            );

            //外部キー制約有効化
            db.execSQL("PRAGMA foreign_key=true");
        }

        @Override
        //DBの構造が更新された際
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //TODO:DB構造変更時の処理追加
        }
    }

    public DBAdapter open() {
        this.db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        this.dbHelper.close();
    }

    /**
     * Project新規登録
     *
     * @return 処理失敗時には負の値を返す
     */
    public boolean insertProject(Project project) {
        ContentValues values = new ContentValues();
        values.put(COL_PROJECT_NAME, project.getProjectName());
        return db.insert(PROJECT_TABLE_NAME, null, values) > 0;
    }

    /**
     * Project削除
     *
     * @return
     */
    public int deleteProject(int projectId) {
        int rowId = db.delete(PROJECT_TABLE_NAME,
                COL_PROJECT_ID + "=?",
                new String[]{String.valueOf(projectId)}
        );
        return rowId;
    }

    /**
     * Project更新
     *
     * @return
     */
    public boolean updateProject() {
        //TODO:Project更新SQL処理を実装
        return true;
    }

    /**
     * Project全件取得
     *
     * @return
     */
    public ArrayList<Project> getAllProjects() {
        ArrayList<Project> projectList = new ArrayList<Project>();
        Cursor cursor = null;
        try {
            cursor = db.query(PROJECT_TABLE_NAME,
                    new String[]{COL_PROJECT_ID, COL_PROJECT_NAME},
                    null, null, null, null,
                    COL_PROJECT_ID + " ASC");
        } catch (NullPointerException e) {
            //TODO:DBが空の時NULLが返る？
            Log.d(TAG, "getAllProjectsError", e);
        }
        //cursorの参照先を先頭にする
        boolean isEof = cursor.moveToFirst();
        while (isEof) {
            int projectId = cursor.getInt(cursor.getColumnIndex(COL_PROJECT_ID));
            String projectName = cursor.getString(cursor.getColumnIndex(COL_PROJECT_NAME));
            Project project = new Project();
            project.setProjectId(projectId);
            project.setProjectName(projectName);
            projectList.add(project);
            isEof = cursor.moveToNext();
        }
        cursor.close();
        return projectList;
    }

    /**
     * CalcSet新規登録
     * @param calcSet
     * @return
     */
    public boolean insertCalcSet(CalcSet calcSet){
        //TODO:CalcSet新規登録処理実装
        return true;
    }

    /**
     * CalcSet削除
     * @param calcSetId
     * @return
     */
    public int deleteCalcSet(int calcSetId){
        //TODO:CalcSet削除処理実装
        return 0;
    }

    /**
     * CalcSet更新
     * @param calcSet
     * @return
     */
    public boolean updateCalcSet(CalcSet calcSet){
        //TODO:CalcSet更新処理実装
        return true;
    }

    public Cursor getAllCalcSets(){
        //TODO:CalcSet全件取得処理実装
        return null;
    }

}