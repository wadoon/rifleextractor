package edu.kit.iti.rifl;

import edu.kit.iti.rifl.annotation.Lattice;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;

/**
 * Created by weigl on 3/24/16.
 */

@SupportedAnnotationTypes({"edu.kit.iti.rifl.annotation.Lattice"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedOptions({"riflspec"})
public class LatticeProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        RIFLSpec spec = StaticFactory.specification;
        for (Element l : roundEnv.getElementsAnnotatedWith(Lattice.class)) {
            Lattice lattice = l.getAnnotation(Lattice.class);
            for (Lattice.Relation r : lattice.value()) {
                spec.allowedFlows.add(
                        new RIFLSpec.FlowRelation(r.from().name(), r.to().name())
                );
                spec.domains.add(r.to().name());
                spec.domains.add(r.from().name());
            }
        }
        return false;
    }
}
