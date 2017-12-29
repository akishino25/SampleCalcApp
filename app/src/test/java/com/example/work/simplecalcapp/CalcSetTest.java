package com.example.work.simplecalcapp;

import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by work on 2017/12/30.
 */
public class CalcSetTest {

    /**
     * enableClacCheckのテスト
     * 入力値を2つ、入力演算子を1つ設定して、計算可能チェックがtrueになることを確認
     */
    @Test
    public void testEnableCalcCheck() {
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
    public void testEnableCalcCheck_ng_inputNums() {
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
    public void testEnableCalcCheck_ng_inputSyms() {
        CalcSet calcSet = new CalcSet();
        ArrayList<Integer> inputNums = new ArrayList<Integer>();
        inputNums.add(1);
        inputNums.add(2);
        calcSet.setInputNums(inputNums);
        ArrayList<String> inputSyms = new ArrayList<String>();
        calcSet.setInputSyms(inputSyms);

        assertFalse(calcSet.enableCalcCheck());
    }

    @Test
    public void testConvertFormInputNumsToString() {
        CalcSet calcSet = new CalcSet();
        ArrayList<Integer> inputNums = new ArrayList<Integer>();
        inputNums.add(1);
        inputNums.add(2);
        calcSet.setInputNums(inputNums);
        assertEquals(calcSet.convertFromInputNumsToString(), "1,2");
    }

    @Test
    public void testConvertFormInputSymsToString() {
        CalcSet calcSet = new CalcSet();
        ArrayList<String> inputSyms = new ArrayList<String>();
        inputSyms.add("＋");
        inputSyms.add("－");
        calcSet.setInputSyms(inputSyms);
        assertEquals(calcSet.convertFromInputSymsToString(), "＋,－");
    }

    @Test
    public void testConvertFromStringToInputNums(){
        CalcSet calcSet = new CalcSet();
        ArrayList<Integer> expect = new ArrayList<Integer>();
        expect.add(1);
        expect.add(2);
        ArrayList<Integer> actual = calcSet.convertFromStringToInputNums("1,2");
        assertEquals(expect, actual);
    }

    @Test
    public void testConvertFromStringToInputSyms(){
        CalcSet calcSet = new CalcSet();
        ArrayList<String> expect = new ArrayList<>();
        expect.add("＋");
        expect.add("－");
        ArrayList<String> actual = calcSet.convertFromStringToInputSyms("＋,－");
        assertEquals(expect, actual);
    }

}