package edu.kit.iti.rifl;

/**
 * Created by weigl on 3/23/16.
 */
public class StaticFactory {
    public static final RIFLSpec specification = new RIFLSpec();


    static {
        Runnable r = specification::save;
        Runtime.getRuntime().addShutdownHook(
                new Thread(r)
        );

    }
}
