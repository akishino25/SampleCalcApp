package com.example.work.simplecalcapp;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by work on 2017/11/21.
 * 1種類のプロジェクト（複数の計算式を格納）を表すクラス
 */

public class Project  implements Serializable {
    private String projectName = null;
    private ArrayList<CalcSet> calcSetList = null;

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
