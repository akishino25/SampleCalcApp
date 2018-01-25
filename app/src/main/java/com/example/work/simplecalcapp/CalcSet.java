package com.example.work.simplecalcapp;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by work on 2017/11/10.
 * 単一の計算式要素群クラス
 */

public class CalcSet implements Serializable {

    //Logcat用タグ文字列（クラス名）
    private final static String TAG = CalcSet.class.getSimpleName();

    private int calcSetId = 0;
    private int projectId = 0;
    private String memo = null;
    private ArrayList<Double> inputNums = null;
    private ArrayList<String> inputSyms = null;
    private double calcResult = 0;

    /**
     * 四則演算順序考慮版
     */
    public void calcExec() {
        //Doubleの配列をBigDecimalの配列に変換
        ArrayList<BigDecimal> tmpInputNums = new ArrayList<BigDecimal>();
        for (int i = 0; i < this.inputNums.size(); i++) {
            System.out.println(i);
            tmpInputNums.add(BigDecimal.valueOf(this.inputNums.get(i)));
        }

        //Stringの演算子配列を計算用にディープコピー
        ArrayList<String> tmpInputSyms = new ArrayList<String>(this.inputSyms);

        //演算子リストに積または商がなくなるまでループ
        while (tmpInputSyms.indexOf("×") != -1 || tmpInputSyms.indexOf("÷") != -1) {
            //演算子リストの先頭から、積または商を検索して、項番取得
            int multiSymNo = tmpInputSyms.indexOf("×");
            int divSymNo = tmpInputSyms.indexOf("÷");
            int symNo = 0;
            if (multiSymNo == -1) {
                symNo = divSymNo;
            } else if (divSymNo == -1) {
                symNo = multiSymNo;
            } else if (multiSymNo < divSymNo) {
                symNo = multiSymNo;
            } else if (multiSymNo > divSymNo) {
                symNo = divSymNo;
            }

            //演算子の前後の数値を取得（同項番、同項番+1）
            int numNo = symNo;

            //部分計算
            BigDecimal partRes = null;
            switch (tmpInputSyms.get(symNo)) {
                case "×":
                    partRes = tmpInputNums.get(numNo).multiply(tmpInputNums.get(numNo + 1));
                    break;
                case "÷":
                    partRes = tmpInputNums.get(numNo).divide(tmpInputNums.get(numNo + 1), 5, RoundingMode.HALF_UP);
                    break;
            }

            //演算子は削除
            tmpInputSyms.remove(symNo);
            //同項番の数値は計算結果に置き換え、同項番+1の数値は削除
            tmpInputNums.set(numNo, partRes);
            tmpInputNums.remove(numNo + 1);
        }

        //ループから抜けたらあとは左から順番に加算もしくは減算する
        BigDecimal res = tmpInputNums.get(0);
        for (int i = 0; i < tmpInputSyms.size(); i++) {
            switch (tmpInputSyms.get(i)) {
                case "＋":
                    res = res.add(tmpInputNums.get(i + 1));
                    break;
                case "－":
                    res = res.subtract(tmpInputNums.get(i + 1));
                    break;
            }
        }
        setCalcResult(res.doubleValue());

    }

    /**
     * 計算式の左側を文字列として返却
     *
     * @return
     */
    public String calcLeft() {
        //本処理用にDoubleを文字列に変換
        ArrayList<String> tmpInputNums = new ArrayList<String>();
        for (int i = 0; i < inputNums.size(); i++) {
            tmpInputNums.add(inputNums.get(i).toString());
        }
        //本処理用にディープコピー
        ArrayList<String> tmpInputSyms = new ArrayList<String>(this.inputSyms);

        //キーワード一覧用スタック
        Deque<HashMap<String, String>> stack = new ArrayDeque<HashMap<String, String>>();

        //演算子リストに積または商がなくなるまでループ
        int count = 0;
        while (tmpInputSyms.indexOf("×") != -1 || tmpInputSyms.indexOf("÷") != -1) {
            //演算子リストの先頭から、積または商を検索して、項番取得
            int multiSymNo = tmpInputSyms.indexOf("×");
            int divSymNo = tmpInputSyms.indexOf("÷");
            int symNo = 0;
            if (multiSymNo == -1) {
                symNo = divSymNo;
            } else if (divSymNo == -1) {
                symNo = multiSymNo;
            } else if (multiSymNo < divSymNo) {
                symNo = multiSymNo;
            } else if (multiSymNo > divSymNo) {
                symNo = divSymNo;
            }

            //演算子の前後の数値を取得（同項番、同項番+1）
            int numNo = symNo;

            //keyword:部分計算式のマップをスタックに詰める
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("key", "part" + count);
            map.put("calc", tmpInputNums.get(numNo) + tmpInputSyms.get(symNo) + tmpInputNums.get(numNo + 1));
            stack.push(map);

            //部分計算式をpart+iのキーワードで置き換える
            tmpInputNums.set(numNo, "part" + count);
            tmpInputNums.remove(numNo + 1);
            tmpInputSyms.remove(symNo);

            count++;
        }

        //置換後の計算式文字列を生成
        String calcLeft = tmpInputNums.get(0);
        for (int i = 0; i < tmpInputSyms.size(); i++) {
            calcLeft = calcLeft + tmpInputSyms.get(i);
            calcLeft = calcLeft + tmpInputNums.get(i + 1);
        }

        //ループから抜けたらスタックの先頭からキーワードを戻す、戻すときにカッコを前後につける
        while (true) {
            HashMap<String, String> map = stack.poll();
            if (map == null) {
                break;
            }
            calcLeft = calcLeft.replace(map.get("key"), "(" + map.get("calc") + ")");
        }
        return calcLeft;
    }

    /**
     * 計算可能な情報がそろっているか判定
     */
    public boolean enableCalcCheck() {
        if (inputNums != null || inputSyms != null) {
            if (inputNums.size() - inputSyms.size() == 1) {
                //inputSymsはinputNumsより1少ない
                return true;
            }
        }
        return false;
    }

    /**
     * inputNums のArrayListをStringに変換
     *
     * @return
     */
    public String convertFromInputNumsToString() {
        String c = null;
        for (int i = 0; i < this.inputNums.size(); i++) {
            String d = null;
            if (i != this.inputNums.size() - 1) {
                d = String.valueOf(inputNums.get(i)) + ",";
            } else {
                d = String.valueOf(inputNums.get(i));
            }
            if (c == null) {
                c = d;
            } else {
                c = c + d;
            }
        }
        return c;
    }

    /**
     * inputSyms のArrayListをStringに変換
     *
     * @return
     */
    public String convertFromInputSymsToString() {
        String c = null;
        for (int i = 0; i < this.inputSyms.size(); i++) {
            String d = null;
            if (i != this.inputSyms.size() - 1) {
                d = inputSyms.get(i) + ",";
            } else {
                d = inputSyms.get(i);
            }
            if (c == null) {
                c = d;
            } else {
                c = c + d;
            }
        }
        return c;
    }

    /**
     * inputNums のStringをArrayListに変換
     *
     * @return
     */
    public ArrayList<Double> convertFromStringToInputNums(String stringList) {
        if (stringList != null) {
            ArrayList<Double> convertedList = new ArrayList<>();
            String[] l = stringList.split(",", 0);
            for (int i = 0; i < l.length; i++) {
                convertedList.add(Double.valueOf(l[i]));
            }
            return convertedList;
        }
        return null;
    }

    /**
     * inputSyms のStringをArrayListに変換
     *
     * @return
     */
    public ArrayList<String> convertFromStringToInputSyms(String stringList) {
        if (stringList != null) {
            ArrayList<String> convertedList = new ArrayList<String>();
            String[] l = stringList.split(",", 0);
            for (int i = 0; i < l.length; i++) {
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
