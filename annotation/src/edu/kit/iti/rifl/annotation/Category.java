package edu.kit.iti.rifl.annotation;

import java.lang.annotation.*;

/**
 * Created by weigl on 3/23/16.
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE})
@Repeatable(Categories.class)
public @interface Category {
    Domain domain();
    String name();
}
