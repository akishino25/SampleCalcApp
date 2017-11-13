package com.example.work.simplecalcapp;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by work on 2017/11/10.
 * 単一の計算式要素群クラス
 */

public class CalcSet implements Serializable {
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
