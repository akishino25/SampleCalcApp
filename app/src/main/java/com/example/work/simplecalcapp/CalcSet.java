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
        //TODO:演算子が2つ以上存在する場合の計算は未考慮
        if(inputNums.size()>1 && inputSyms.size()>0){
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
        if(inputNums.size()>1 && inputSyms.size()>0) {
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
        ArrayList<Integer> convertedList = new ArrayList<Integer>();
        String[] l = stringList.split(",", 0);
        for(int i=0; i<l.length; i++){
            convertedList.add(Integer.valueOf(l[i]));
        }
        return convertedList;
    }

    /**
     * inputSyms のStringをArrayListに変換
     * @return
     */
    public ArrayList<String> convertFromStringToInputSyms(String stringList){
        ArrayList<String> convertedList = new ArrayList<String>();
        String[] l = stringList.split(",", 0);
        for(int i=0; i<l.length; i++){
            convertedList.add(l[i]);
        }
        return convertedList;
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
