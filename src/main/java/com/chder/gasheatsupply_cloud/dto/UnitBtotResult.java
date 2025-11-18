package com.chder.gasheatsupply_cloud.dto;

/**
 * 煤耗量计算返回结果
 */
public class UnitBtotResult {
    double mm_one;
    double ml_one;
    double Pe_one;
    double mm_two;
    double ml_two;
    double Pe_two;
    double Btot_one;
    double Btot_two ;
    double Btot;
    double Pr;
    double b;
    public UnitBtotResult(double mm1, double ml1, double Pe1,
                          double mm2, double ml2, double Pe2,
                          double Btot1, double Btot2, double Btot,
                          double Pr, double b) {
        this.mm_one = mm1;
        this.ml_one = ml1;
        this.Pe_one = Pe1;
        this.mm_two = mm2;
        this.ml_two = ml2;
        this.Pe_two = Pe2;
        this.Btot_one = Btot1;
        this.Btot_two = Btot2;
        this.Btot = Btot;
        this.Pr = Pr;
        this.b = b;
    }

    public double getMm_one() {
        return mm_one;
    }

    public void setMm_one(double mm_one) {
        this.mm_one = mm_one;
    }

    public double getMl_one() {
        return ml_one;
    }

    public void setMl_one(double ml_one) {
        this.ml_one = ml_one;
    }

    public double getPe_one() {
        return Pe_one;
    }

    public void setPe_one(double pe_one) {
        Pe_one = pe_one;
    }

    public double getMm_two() {
        return mm_two;
    }

    public void setMm_two(double mm_two) {
        this.mm_two = mm_two;
    }

    public double getMl_two() {
        return ml_two;
    }

    public void setMl_two(double ml_two) {
        this.ml_two = ml_two;
    }

    public double getPe_two() {
        return Pe_two;
    }

    public void setPe_two(double pe_two) {
        Pe_two = pe_two;
    }

    public double getBtot_one() {
        return Btot_one;
    }

    public void setBtot_one(double btot_one) {
        Btot_one = btot_one;
    }

    public double getBtot_two() {
        return Btot_two;
    }

    public void setBtot_two(double btot_two) {
        Btot_two = btot_two;
    }

    public double getBtot() {
        return Btot;
    }

    public void setBtot(double btot) {
        Btot = btot;
    }

    public double getPr() {
        return Pr;
    }

    public void setPr(double pr) {
        Pr = pr;
    }

    public double getB() {
        return b;
    }

    public void setB(double b) {
        this.b = b;
    }

    public String toString() {
        return String.format(
                "最优结果 (单位: mm/ml=kg/h, Pe=kW, Btot=t/h):\n" +
                        "机组1 -> mm1=%.2f, ml1=%.2f, Pe1=%.2f, Btot1=%.2f\n" +
                        "机组2 -> mm2=%.2f, ml2=%.2f, Pe2=%.2f, Btot2=%.2f\n" +
                        "总标煤耗 Btot=%.2f t/h\n" +
                        "总供汽煤耗成本 Pr=%.2f t/h\n" +
                        "平均发电煤耗率 b=%.2f t/h\n",
                mm_one, ml_one, Pe_one, Btot_one, mm_two, ml_two, Pe_two, Btot_two, Btot, Pr, b);
    }
}
