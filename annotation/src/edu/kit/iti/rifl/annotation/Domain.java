package edu.kit.iti.rifl.annotation;

/**
 * Created by weigl on 3/22/16.
 */
public enum Domain {
    LOW, HIGH;

    public static int compare(Domain l1, Domain l2) {
        return l1.ordinal() - l2.ordinal();
    }
}
