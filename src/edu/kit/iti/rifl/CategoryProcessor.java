package edu.kit.iti.rifl;

import edu.kit.iti.rifl.annotation.Categories;
import edu.kit.iti.rifl.annotation.Category;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;

/**
 * Created by weigl on 3/24/16.
 */

@SupportedAnnotationTypes({"edu.kit.iti.rifl.annotation.Categories"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedOptions({"riflspec"})
public class CategoryProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        RIFLSpec spec = StaticFactory.specification;
        for (Element l : roundEnv.getElementsAnnotatedWith(Categories.class)) {
            Categories categories = l.getAnnotation(Categories.class);
            for (Category c : categories.value()) {
                spec.domains.add(c.domain().name());
                spec.domainAssignment.put(c.name(), c.domain().name());
            }
        }
        return false;
    }
}
