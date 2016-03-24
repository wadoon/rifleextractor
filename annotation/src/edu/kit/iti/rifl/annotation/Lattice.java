package edu.kit.iti.rifl.annotation;

/**
 * Defines the <b>FlowRelation</b> of a RIFL Specification.
 *
 * A flow relation is a set of pairs of domains, allowing a flow between them.
 *
 * Created by weigl on 3/23/16.
 */
public @interface Lattice {
    Relation[] value();

    /**
     * Currently unsupported.
     * Activates the calculation of the transitive hull for the given relations.
     * @return
     */
    boolean transitive_hull() default false;

    public @interface Relation {
        Domain from();
        Domain to();
    }

}
