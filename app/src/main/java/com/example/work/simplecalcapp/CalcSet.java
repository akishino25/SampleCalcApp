package com.example.work.simplecalcapp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

/**
 * Created by work on 2017/11/10.
 * 単一の計算式要素群クラス
 */

public class CalcSet implements Serializable {
    private int calcSetId = 0;
    private int projectId = 0;
    private String memo = null;
    private ArrayList<Double> inputNums = null;
    private ArrayList<String> inputSyms = null;
    private double calcResult = 0;

    /**
     * 入力された数値、演算子から計算を実行
     */
    /*
    public void calcExec(){
        if(enableCalcCheck()){
            double res = inputNums.get(0);
            for(int i =0 ; i<inputSyms.size(); i++){
                switch (inputSyms.get(i)){
                    case "＋":
                        res = res + inputNums.get(i+1);
                        break;
                    case "－":
                        res = res - inputNums.get(i+1);
                        break;
                    case "×":
                        res = res * inputNums.get(i+1);
                        break;
                    case "÷":
                        res = res / (double)inputNums.get(i+1);
                        break;
                }
            }
            setCalcResult((double)res);
        }
    }
    */

    /**
     * 四則演算順序考慮版
     *
     */
    public void calcExec(){
        /**
         * 1+2-3*4/5 = 1+2-((3*4)/5) = 0.6
         * inputNums = 1,2,3,4,5
         * inputSyms = +,-,*,/
         */
        //Doubleの配列をBigDecimalの配列に変換
        ArrayList<BigDecimal> tmpInputNums = new ArrayList<BigDecimal>();
        for(int i=0; i<this.inputNums.size(); i++){
            System.out.println(i);
            tmpInputNums.add(BigDecimal.valueOf(this.inputNums.get(i)));
        }



        System.out.println("start");
        //演算子リストに積または商がなくなるまでループ
        while(this.inputSyms.indexOf("×") != -1 || this.inputSyms.indexOf("÷") != -1){
            //演算子リストの先頭から、積または商を検索して、項番取得
            int multiSymNo = this.inputSyms.indexOf("×");
            int divSymNo = this.inputSyms.indexOf("÷");
            int symNo = 0;
            if(multiSymNo == -1){
                symNo = divSymNo;
            }else if(divSymNo == -1){
                symNo = multiSymNo;
            }else if(multiSymNo < divSymNo){
                symNo = multiSymNo;
            }else if(multiSymNo > divSymNo){
                symNo = divSymNo;
            }
            System.out.println("symNo:" +symNo);

            //演算子の前後の数値を取得（同項番、同項番+1）
            int numNo = symNo;

            //部分計算
            BigDecimal partRes = null;
            switch (inputSyms.get(symNo)){
                case "×":
                    partRes = tmpInputNums.get(numNo).multiply(tmpInputNums.get(numNo+1));
                    break;
                case "÷":
                    partRes = tmpInputNums.get(numNo).divide(tmpInputNums.get(numNo+1),5, RoundingMode.HALF_UP);
                    break;
            }
            System.out.println("partRes:" +partRes);

            //演算子は削除
            this.inputSyms.remove(symNo);
            //同項番の数値は計算結果に置き換え、同項番+1の数値は削除
            tmpInputNums.set(numNo, partRes);
            tmpInputNums.remove(numNo+1);

        }

        //ループから抜けたらあとは左から順番に加算もしくは減算する
        BigDecimal res = tmpInputNums.get(0);
        for(int i =0 ; i<inputSyms.size(); i++) {
            switch (inputSyms.get(i)) {
                case "＋":
                    res = res.add(tmpInputNums.get(i+1));
                    break;
                case "－":
                    res = res.subtract(tmpInputNums.get(i+1));
                    break;
            }
        }
        setCalcResult(res.doubleValue());

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
        if(inputNums!=null || inputSyms!=null){
            if(inputNums.size() - inputSyms.size() ==  1){
                //inputSymsはinputNumsより1少ない
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
    public ArrayList<Double> convertFromStringToInputNums(String stringList){
        if(stringList != null){
            ArrayList<Double> convertedList = new ArrayList<>();
            String[] l = stringList.split(",", 0);
            for(int i=0; i<l.length; i++){
                convertedList.add(Double.valueOf(l[i]));
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

    public ArrayList<Double> getInputNums() {
        return inputNums;
    }

    public void setInputNums(ArrayList<Double> inputNums) {
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
