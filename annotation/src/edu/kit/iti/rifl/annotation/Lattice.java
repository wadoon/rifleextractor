package edu.kit.iti.rifl.annotation;

/**
 * Created by weigl on 3/23/16.
 */
public @interface Lattice {
    Relation[] value();

    public @interface Relation {
        Domain from();
        Domain to();
    }

}
