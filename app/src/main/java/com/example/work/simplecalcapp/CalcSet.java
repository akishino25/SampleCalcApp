package com.example.work.simplecalcapp;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by work on 2017/11/10.
 * 単一の計算式要素群クラス
 */

public class CalcSet implements Serializable {
    private int calcSetId = 0;
    private int projectId = 0;
    private String memo = null;
    private ArrayList<Integer> inputNums = null;
    private ArrayList<String> inputSyms = null;
    private double calcResult = 0;

    /**
     * 入力された数値、演算子から計算を実行
     */
    public void calcExec(){
        //if(inputNums!=null && inputNums.size()>1 && inputSyms!=null && inputSyms.size()>0){
        //TODO:演算子が2つ以上の場合は未考慮
        if(enableCalcCheck()){
            switch (inputSyms.get(0)){
                case "＋":
                    setCalcResult(inputNums.get(0) + inputNums.get(1));
                    break;
                case "－":
                    setCalcResult(inputNums.get(0) - inputNums.get(1));
                    break;
                case "×":
                    setCalcResult(inputNums.get(0) * inputNums.get(1));
                    break;
                case "÷":
                    setCalcResult((double)inputNums.get(0) / (double)inputNums.get(1));
                    break;
            }
        }
    }

    /**
     * 計算式の左側を文字列として返却
     * @return
     */
    public String calcLeft(){
        if(enableCalcCheck()) {
            String calcLeftString = inputNums.get(0).toString();
            for (int i = 0; i < inputSyms.size(); i++) {
                calcLeftString = calcLeftString + inputSyms.get(i);
                calcLeftString = calcLeftString + inputNums.get(i + 1).toString();
            }
            return calcLeftString;
        }else{
            return null;
        }
    }

    /**
     * 計算可能な情報がそろっているか判定
     */
    public boolean enableCalcCheck(){
        if(inputNums!=null && inputSyms!=null){
            if(inputNums.size()==2 && inputSyms.size()==1){
                //TODO:入力値×2、入力値×1以外は非対応
                return true;
            }
        }
        return false;
    }

    /**
     * inputNums のArrayListをStringに変換
     * @return
     */
    public String convertFromInputNumsToString(){
        String c = null;
        for(int i=0; i<this.inputNums.size(); i++ ){
            String d = null;
            if(i != this.inputNums.size()-1){
                d = String.valueOf(inputNums.get(i)) + ",";
            }else{
                d = String.valueOf(inputNums.get(i));
            }
            if(c == null){
                c = d;
            }else{
                c = c + d;
            }
        }
        return c;
    }

    /**
     * inputSyms のArrayListをStringに変換
     * @return
     */
    public String convertFromInputSymsToString(){
        String c = null;
        for(int i=0; i<this.inputSyms.size(); i++ ){
            String d = null;
            if(i != this.inputSyms.size()-1){
                d = inputSyms.get(i) + ",";
            }else{
                d = inputSyms.get(i);
            }
            if(c == null){
                c = d;
            }else{
                c = c + d;
            }
        }
        return c;
    }

    /**
     * inputNums のStringをArrayListに変換
     * @return
     */
    public ArrayList<Integer> convertFromStringToInputNums(String stringList){
        if(stringList != null){
            ArrayList<Integer> convertedList = new ArrayList<Integer>();
            String[] l = stringList.split(",", 0);
            for(int i=0; i<l.length; i++){
                convertedList.add(Integer.valueOf(l[i]));
            }
            return convertedList;
        }
        return null;
    }

    /**
     * inputSyms のStringをArrayListに変換
     * @return
     */
    public ArrayList<String> convertFromStringToInputSyms(String stringList){
        if(stringList!=null){
            ArrayList<String> convertedList = new ArrayList<String>();
            String[] l = stringList.split(",", 0);
            for(int i=0; i<l.length; i++){
                convertedList.add(l[i]);
            }
            return convertedList;
        }
        return null;
    }

    public int getCalcSetId() {
        return calcSetId;
    }

    public void setCalcSetId(int calcSetId) {
        this.calcSetId = calcSetId;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public ArrayList<Integer> getInputNums() {
        return inputNums;
    }

    public void setInputNums(ArrayList<Integer> inputNums) {
        this.inputNums = inputNums;
    }

    public ArrayList<String> getInputSyms() {
        return inputSyms;
    }

    public void setInputSyms(ArrayList<String> inputSyms) {
        this.inputSyms = inputSyms;
    }

    public double getCalcResult() {
        return calcResult;
    }

    public void setCalcResult(double calcResult) {
        this.calcResult = calcResult;
    }
}
