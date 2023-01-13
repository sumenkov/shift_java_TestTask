package ru.sumenkov.msf.model;

import java.util.Arrays;

public class OutputArray {
    private String[] outArrayS;
    private int[] outArrayI;

    public int[] getOutArrayI() {
        return outArrayI;
    }

    public void setOutArrayI(int[] outArrayI) {
        this.outArrayI = outArrayI;
    }

    public String[] getOutArrayS() {
        return outArrayS;
    }

    public void setOutArrayS(String[] outArrayS) {
        this.outArrayS = outArrayS;
    }

    @Override
    public String toString() {
        return "OutputArray{" +
                "outArrayI=" + Arrays.toString(outArrayI) +
                ", outArrayO=" + Arrays.toString(outArrayS) +
                '}';
    }
}
