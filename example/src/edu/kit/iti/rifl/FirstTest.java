package edu.kit.iti.rifl;

import edu.kit.iti.rifl.annotation.Category;
import edu.kit.iti.rifl.annotation.Lattice;
import edu.kit.iti.rifl.annotation.Sink;
import edu.kit.iti.rifl.annotation.Source;

import java.util.List;

import static edu.kit.iti.rifl.annotation.Domain.HIGH;
import static edu.kit.iti.rifl.annotation.Domain.LOW;

@Lattice({@Lattice.Relation(from = HIGH, to = LOW)})
@Category(name = "H", domain = HIGH)
@Category(name = "L", domain = LOW)
public class FirstTest {

    @Source("H")
    int high;

    public void in_secure(@Sink("L") List<Integer> i) {
        i.add(high);
    }


    @Sink("L")
    public int leakage() {
        return high;
    }
}
