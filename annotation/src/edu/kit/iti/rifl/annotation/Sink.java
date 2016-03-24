package edu.kit.iti.rifl.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a sink.
 *
 * A sink is assigned to an category and a handle.
 * Created by weigl on 3/22/16.
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.PARAMETER, ElementType.FIELD, ElementType.LOCAL_VARIABLE, ElementType.METHOD})
public @interface Sink  {
    /**
     * handle
     * @return
     */
    String value();

    /**
     * category name
     */

    String category() default "notavailable";
}
