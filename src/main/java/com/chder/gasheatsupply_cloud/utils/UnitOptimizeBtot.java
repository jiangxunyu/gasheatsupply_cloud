package com.chder.gasheatsupply_cloud.utils;

import com.chder.gasheatsupply_cloud.dto.UnitBtotResult;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * 双机组总标煤耗量优化（PSO算法 + 经验公式）
 * 输入：
 *   mm  - 主蒸汽抽汽量 (kg/h)
 *   ml  - 再热蒸汽抽汽量 (kg/h)
 *   Pe  - 电负荷需求 (kW)
 * 输出：
 *   Btot - 总标煤耗量 (t/h)
 */
@Component
public class UnitOptimizeBtot {

    // === 粒子群参数 ===
    static final int SWARM_SIZE = 60;
    static final int MAX_ITER = 1000;
    static final double W = 0.729;
    static final double C1 = 1.49445;
    static final double C2 = 1.49445;
    static final double yb = 0.906;
    static final double yp = 0.99;
    static final double coal = 29307;

    // === 约束条件 ===
    static final double MM_MAX = 200000;
    static final double ML_MAX = 250000;
    static final double PE_MIN = 250025;
    static final double PE_MAX = 660000;

    static class Particle {
        // [mm_one, ml_one, Pe_one]
        double[] x = new double[3];
        double[] v = new double[3];
        double[] pbest = new double[3];
        double pbestVal = Double.POSITIVE_INFINITY;
    }

    /**
     * 总煤耗计算（输出单位：t/h）
     * @param Pe
     * @param mm
     * @param ml
     * @param Pe_one
     * @param mm_one
     * @param ml_one
     * @return
     */
    double bothBtot(double Pe, double mm, double ml,
                              double Pe_one, double mm_one, double ml_one) {
        double btot = -6.49577404e-1
                + 3.70562808e-4 * Pe
                + 1.29735823e-4 * mm
                + 1.34925898e-4 * ml
                - 1.73070283e-10 * (2 * Pe_one * Pe_one - 2 * Pe * Pe_one + Pe * Pe)
                - 1.70373540e-10 * (Pe_one * mm_one + (Pe - Pe_one) * (mm - mm_one))
                - 1.17440652e-10 * (Pe_one * ml_one + (Pe - Pe_one) * (ml - ml_one))
                - 1.17342053e-12 * (2 * mm_one * mm_one - 2 * mm * mm_one + mm * mm)
                - 3.06410660e-11 * ((mm_one * ml_one) + (mm - mm_one) * (ml - ml_one))
                - 3.28522990e-11 * (2 * ml_one * ml_one - 2 * ml * ml_one + ml * ml)
                + 8.58471632e-17 * (Pe_one * Pe_one * Pe_one + (Pe - Pe_one) * (Pe - Pe_one) * (Pe - Pe_one))
                + 1.75254964e-16 * (Pe_one * Pe_one * mm_one + (Pe - Pe_one) * (Pe - Pe_one) * (mm - mm_one))
                + 1.57928628e-16 * (Pe_one * Pe_one * ml_one + (Pe - Pe_one) * (Pe - Pe_one) * (ml - ml_one))
                - 6.98131797e-20 * (Pe_one * mm_one * mm_one + (Pe - Pe_one) * (mm - mm_one) * (mm - mm_one))
                + 2.41124633e-17 * (Pe_one * mm_one * ml_one + (Pe - Pe_one) * (mm - mm_one) * (ml - ml_one))
                + 3.00445549e-17 * (Pe_one * ml_one * ml_one + (Pe - Pe_one) * (ml - ml_one) * (ml - ml_one))
                - 5.6772378e-19 * (mm_one * mm_one * mm_one + (mm - mm_one) * (mm - mm_one) * (mm - mm_one))
                + 4.00166812e-18 * (mm_one * mm_one * ml_one + (mm - mm_one) * (mm - mm_one) * (ml - ml_one))
                + 7.71533930e-18 * (mm_one * ml_one * ml_one + (mm - mm_one) * (ml - ml_one) * (ml - ml_one))
                + 5.72080848e-18 * (ml_one * ml_one * ml_one + (ml - ml_one) * (ml - ml_one) * (ml - ml_one));
        return btot;
    }

    /**
     * 单个机组煤耗计算（输出单位：t/h）
     * @param mm_one
     * @param ml_one
     * @param Pe_one
     * @return
     */
    double singleBtot(double mm_one, double ml_one,double Pe_one) {
        double btot = -3.24788702e-1
                + 3.70562808e-4 * Pe_one
                + 1.29735823e-4 * mm_one
                + 1.34925898e-4 * ml_one
                - 1.73070283e-10 * Math.pow(Pe_one, 2)
                - 1.70373540e-10 * Pe_one * mm_one
                - 1.17440652e-10 * Pe_one * ml_one +
                - 1.17342053e-12 * Math.pow(mm_one, 2)
                - 3.06410660e-11 * mm_one * ml_one
                - 3.28522990e-11 * Math.pow(ml_one, 2)
                + 8.58471632e-17 * Math.pow(Pe_one, 3)
                + 1.75254964e-16 * Math.pow(Pe_one, 2) * mm_one
                + 1.57928628e-16 * Math.pow(Pe_one, 2) * ml_one
                - 6.98131797e-20 * Pe_one * Math.pow(mm_one, 2)
                + 2.41124633e-17 * Pe_one * mm_one * ml_one
                + 3.00445549e-17 * Pe_one * Math.pow(ml_one, 2)
                - 5.67772378e-19 * Math.pow(mm_one, 3)
                + 4.00166812e-18 * Math.pow(mm_one, 2) * ml_one
                + 7.71533930e-18 * mm_one * Math.pow(ml_one, 2)
                + 5.72080848e-18 * Math.pow(ml_one, 3);
        return btot;
    }

    /**
     * 约束条件判断
     * @param mm_one
     * @param ml_one
     * @param Pe_one
     * @return
     */
    boolean feasible(double pe,double mm, double ml,double mm_one, double ml_one, double Pe_one) {
        if (mm_one < 0 || mm_one > MM_MAX || mm_one> mm) {
            return false;
        }
        if (ml_one < 0 || ml_one > ML_MAX || ml_one > ml) {
            return false;
        }
        if (Pe_one < PE_MIN || Pe_one > PE_MAX || Pe_one > pe) {
            return false;
        }
        if (mm-mm_one < 0 || mm-mm_one > MM_MAX ) {
            return false;
        }
        if (ml-ml_one < 0 || ml-ml_one > ML_MAX ) {
            return false;
        }
        if (pe-Pe_one < PE_MIN || pe-Pe_one > PE_MAX) {
            return false;
        }

        double maxBound = Pe_one + 0.60152 * mm_one + 0.683025 * ml_one - 712944;
        double minBound = -Pe_one - 0.0932 * mm_one - 0.1736 * ml_one + 264004;
        return (maxBound <= 0 && minBound <= 0);
    }

    /**
     * 粒子群优化算法
     * @param mm
     * @param ml
     * @param pe
     * @param scenario  再热抽气来源 1：机组1 2：机组2 3：同时
     * @return
     */
    public UnitBtotResult runPSO(double mm, double ml, double pe, double pCoal, int scenario) {
        double mm_max = MM_MAX;
        double ml_max = ML_MAX;
        if (mm < MM_MAX){
            mm_max = mm;
        }
        if (ml < ML_MAX){
            ml_max = ml;
        }
        Particle[] swarm = new Particle[SWARM_SIZE];
        Particle gbest = new Particle();
        double gbestVal = Double.POSITIVE_INFINITY;
        // 初始化
        for (int i = 0; i < SWARM_SIZE; i++) {
            Particle p = new Particle();
            switch (scenario) {
                case 1:
                    // 低压抽汽只来自1号机组
                    p.x[1] = ml;
                    break;
                case 2:
                    // 低压抽汽只来自2号机组
                    p.x[1] = 0;
                    break;
                case 3:
                    // 低压抽汽来自1号和2号机组
                    p.x[1] = getRandomPe(0, ml_max, ml);
                    break;
            }
            p.x[0] = getRandomPe(0, mm_max, mm);
            p.x[2] = getRandomPe(PE_MIN, PE_MAX, pe);
            for (int d = 0; d < 3; d++) {
                p.v[d] = (Math.random() - 0.5) * 1000;
            }
            double val = bothBtot(pe, mm, ml, p.x[2], p.x[0], p.x[1]);
            p.pbestVal = val;
            System.arraycopy(p.x, 0, p.pbest, 0, 3);
            if (val < gbestVal) {
                gbestVal = val;
                gbest = copyParticle(p);
            }
            swarm[i] = p;
        }
        // 迭代
        for (int iter = 0; iter < MAX_ITER; iter++) {
            for (Particle p : swarm) {
//                for (int d = 0; d < 3; d++) {
//                    double r1 = Math.random(), r2 = Math.random();
//                    p.v[d] = W * p.v[d]
//                            + C1 * r1 * (p.pbest[d] - p.x[d])
//                            + C2 * r2 * (gbest.x[d] - p.x[d]);
//                    p.x[d] += p.v[d];
//                }

                if (!feasible(pe, mm, ml,p.x[0], p.x[1], p.x[2])) {
                    continue;
                }
                double val = bothBtot(pe, mm, ml, p.x[2], p.x[0], p.x[1]);
                if (val < p.pbestVal) {
                    p.pbestVal = val;
                    System.arraycopy(p.x, 0, p.pbest, 0, 3);
                }
                if (val < gbestVal) {
                    gbestVal = val;
                    gbest = copyParticle(p);
                }
            }
        }

        double mm1 = gbest.x[0], ml1 = gbest.x[1], Pe1 = gbest.x[2];
        double mm2 = mm - mm1, ml2 = ml - ml1, Pe2 = pe - Pe1;
        double btot1 = singleBtot(mm1, ml1, Pe1);
        double btot2 = singleBtot(mm2, ml2, Pe2);
        double Btot = gbestVal;
        double qm_one = 1.4210854715202004e-13
                -1.2489541079603903e-19*Pe1
                +0.0031462236754466037*mm1
                -4.2924198563833916e-21*ml1;
        double ql_one = 0.9236142825959774
                -1.912787144883469e-6*Pe1
                -6.397491915774607e-7*mm1
                +0.003347196387710836*ml1;
        double qm_two = 1.4210854715202004e-13
                -1.2489541079603903e-19*Pe2
                +0.0031462236754466037*mm2
                -4.2924198563833916e-21*ml2;
        double ql_two = 0.9236142825959774
                -1.912787144883469e-6*Pe2
                -6.397491915774607e-7*mm2
                +0.003347196387710836*ml2;
        double m_one = (qm_one+ql_one)*1000/coal/yp/yb;
        double m_two = (qm_two+ql_two)*1000/coal/yp/yb;
        double m = m_one+m_two;
        double pr = m*pCoal;
        double b = (Btot-m)*1000000/pe;
        return new UnitBtotResult(mm1, ml1, Pe1, mm2, ml2, Pe2,btot1,btot2, Btot,pr,b);
    }

    /**
     * 机组1运行煤耗
     * @param mm
     * @param ml
     * @param pe
     * @param pCoal
     * @return
     */
    public UnitBtotResult unitOneBtot(double mm, double ml, double pe,double pCoal){
        //乘以1000
        double mm_one = mm;
        double ml_one = ml;
        double pe_one = pe;
        double btot_one = singleBtot(mm_one, ml_one, pe_one);
        double qm_one = 1.4210854715202004e-13
                -1.2489541079603903e-19*pe_one
                +0.0031462236754466037*mm_one
                -4.2924198563833916e-21*ml_one;
        double ql_one = 0.9236142825959774
                -1.912787144883469e-6*pe_one
                -6.397491915774607e-7*mm_one
                +0.003347196387710836*ml_one;
        double m_one = (qm_one + ql_one)*1000/coal/yp/yb;
        double pr = m_one*pCoal;
        double b = (btot_one-m_one)*1000000/pe_one;
        return new UnitBtotResult(mm_one, ml_one, pe_one, 0, 0, 0,btot_one,0, btot_one,pr,b);
    }

    /**
     * 机组1运行煤耗
     * @param mm
     * @param ml
     * @param pe
     * @param pCoal
     * @return
     */
    public UnitBtotResult unitTwoBtot(double mm, double ml, double pe,double pCoal){
        //乘以1000
        double mm_two = mm;
        double ml_two = ml;
        double pe_two = pe;
        double btot_two = singleBtot(mm_two, ml_two, pe_two);
        double qm_two = 1.4210854715202004e-13
                -1.2489541079603903e-19*pe_two
                +0.0031462236754466037*mm_two
                -4.2924198563833916e-21*ml_two;
        double ql_one = 0.9236142825959774
                -1.912787144883469e-6*pe_two
                -6.397491915774607e-7*mm_two
                +0.003347196387710836*ml_two;
        double m_two = (qm_two + ql_one)*1000/coal/yp/yb;
        double pr = m_two*pCoal;
        double b = (btot_two-m_two)*1000000/pe_two;
        return new UnitBtotResult(0, 0, 0, mm_two, ml_two, pe_two,0,btot_two, btot_two,pr,b);
    }

    Particle copyParticle(Particle p) {
        Particle q = new Particle();
        System.arraycopy(p.x, 0, q.x, 0, 3);
        System.arraycopy(p.v, 0, q.v, 0, 3);
        System.arraycopy(p.pbest, 0, q.pbest, 0, 3);
        q.pbestVal = p.pbestVal;
        return q;
    }

    /**
     * 生成限制电负荷值
     * @param min
     * @param max
     * @param sumLimit
     * @return
     */
    public double getRandomPe(double min, double max, double sumLimit) {
        Random random = new Random();
        double num1, num2;
        // 最大尝试次数，防止死循环
        int maxAttempts = 1000;
        int attempts = 0;

        do {
            // 步骤1：随机生成num1（闭区间 [min, max]）
            num1 = min + (max - min) * random.nextDouble();

            // 步骤2：强制num2 = targetSum - num1（确保和等于targetSum）
            num2 = sumLimit - num1;

            attempts++;
            // 极端情况：超过最大尝试次数，返回边界组合
            if (attempts >= maxAttempts) {
                num1 = Math.max(min, Math.min(max, sumLimit - min)); // 确保num1在区间内
                break;
            }
        } while (num2 < min || num2 > max); // 校验num2是否在闭区间 [min, max] 内

        return num1;
    }

    public static void main(String[] args) {
        UnitOptimizeBtot unitOptimizeBtot = new UnitOptimizeBtot();
        double[] vars = new double[60];
        for (int i = 0; i < 60; i++) {
            double randomPe = unitOptimizeBtot.getRandomPe(0, 60000, 90000);
            vars[i] = randomPe;
        }
        for (double var : vars) {
            System.out.println(var);
        }
    }
}
