package edu.kit.iti.rifl;

import edu.kit.iti.rifl.annotation.*;

import java.lang.ref.WeakReference;
import java.util.List;

import static edu.kit.iti.rifl.annotation.Domain.*;

public class FirstTest {

    @Source(HIGH) int high;

    public void in_secure(@Sink(LOW) List<Integer> i)  {
        i.add(high);
    }


    @Sink(LOW)
    public int leakage() {
        return high;
    }
}
