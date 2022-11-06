package com.BDacy.A_Shifting_BloomFilter;

public enum ElementBelong {
    S1_diff_S2,         //belongs to S1 − S2
    S1_and_S2,          //belongs to S1 ∩ S2
    S2_diff_S1,         //belongs to S2 − S1
    S1_UnSureS2,        //belongs to S1 but is unsure whether or not it belongs to S2.
    UnSureS1_S2,        //belongs to S2 but is unsure whether or not it belongs to S1.
    S1_diff_S2_or_S2_diff_S1, //belongs to S1 − S2 ∪ S2 − S1.
    S1_or_S2,           //belongs to S1 U S2
    Not_S1_or_S2

}
