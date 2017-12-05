package com.example.work.simplecalcapp;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by work on 2017/11/21.
 * 1種類のプロジェクト（複数の計算式を格納）を表すクラス
 */

public class Project  implements Serializable {
    private int projectId = 0;
    private String projectName = null;
    private ArrayList<CalcSet> calcSetList = new ArrayList<CalcSet>();

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }
    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public ArrayList<CalcSet> getCalcSetList() {
        return calcSetList;
    }

    public void setCalcSetList(ArrayList<CalcSet> calcSetList) {
        this.calcSetList = calcSetList;
    }
}
