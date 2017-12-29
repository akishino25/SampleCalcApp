package com.example.work.simplecalcapp;

import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by work on 2017/12/30.
 */
public class CalcSetTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    /**
     * enableClacCheckのテスト
     * 入力値を2つ、入力演算子を1つ設定して、計算可能チェックがtrueになることを確認
     */
    @Test
    public void testEnableCalcCheck(){
        CalcSet calcSet = new CalcSet();
        ArrayList<Integer> inputNums = new ArrayList<Integer>();
        inputNums.add(1);
        inputNums.add(2);
        calcSet.setInputNums(inputNums);
        ArrayList<String> inputSyms = new ArrayList<String>();
        inputSyms.add("＋");
        calcSet.setInputSyms(inputSyms);

        assertTrue(calcSet.enableCalcCheck());
    }

    /**
     * enableCalcCheckのテスト
     * inputNumsが1つしか設定されていないため計算不可となることを確認
     */
    @Test
    public void testEnableCalcCheck_ng_inputNums(){
        CalcSet calcSet = new CalcSet();
        ArrayList<Integer> inputNums = new ArrayList<Integer>();
        inputNums.add(1);
        calcSet.setInputNums(inputNums);
        ArrayList<String> inputSyms = new ArrayList<String>();
        inputSyms.add("＋");
        calcSet.setInputSyms(inputSyms);

        assertFalse(calcSet.enableCalcCheck());
    }

    /**
     * enableClacCheckのテスト
     * inputSymsが0つしか設定されていないため計算不可となることを確認
     */
    @Test
    public void testEnableCalcCheck_ng_inputSyms(){
        CalcSet calcSet = new CalcSet();
        ArrayList<Integer> inputNums = new ArrayList<Integer>();
        inputNums.add(1);
        inputNums.add(2);
        calcSet.setInputNums(inputNums);
        ArrayList<String> inputSyms = new ArrayList<String>();
        calcSet.setInputSyms(inputSyms);

        assertFalse(calcSet.enableCalcCheck());
    }

}