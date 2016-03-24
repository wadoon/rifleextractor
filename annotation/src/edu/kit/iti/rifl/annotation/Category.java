package edu.kit.iti.rifl.annotation;

import java.lang.annotation.*;

/**
 * This annoation maps to a RIFL category.
 *
 * A category consists of an unique {@see name} and a assigned {@see domain}
 *
 * Created by weigl on 3/23/16.
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE})
@Repeatable(Categories.class)
public @interface Category {
    /**
     * The assigned domain
     * @return
     */
    Domain domain();

    /**
     * Unique name
     * @return
     */
    String name();
}
