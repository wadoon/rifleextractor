package edu.kit.iti.rifl;

import com.sun.tools.javac.code.Symbol;
import edu.kit.iti.rifl.annotation.Sink;
import edu.kit.iti.rifl.annotation.Source;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.io.File;
import java.io.PrintWriter;
import java.util.Set;

/**
 * Created by weigl on 3/22/16.
 */
@SupportedAnnotationTypes({"edu.kit.iti.rifl.annotation.Sink", "edu.kit.iti.rifl.annotation.Sink"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedOptions({"riflspec"})
public class SinkSourceAnnotationProcessor extends AbstractProcessor {
    private String filename;
    private File file;
    private PrintWriter out;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        filename = processingEnv.getOptions().getOrDefault("riflspec", "rifl.xml");
        StaticFactory.specification.saveTo = new File(filename);

    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> sinks = roundEnv.getElementsAnnotatedWith(Sink.class);
        Set<? extends Element> sources = roundEnv.getElementsAnnotatedWith(Source.class);

        for (Element sink : sinks) {
            Sink s = sink.getAnnotation(Sink.class);
            add("sink", s.value(), s.category(), sink);
        }

        for (Element source : sources) {
            Source s = source.getAnnotation(Source.class);
            add("source", s.value(), s.category(), source);
        }

        StaticFactory.specification.save();

        return true;

    }

    private void add(String type, String handle, String category, Element ele) {
        Symbol.ClassSymbol clazz = null;

        RIFLSpec.Place p = new RIFLSpec.Place();
        p.type = type;
        switch (ele.getKind()) {
            case FIELD:
                p.kind = "field";
                clazz = (Symbol.ClassSymbol) ele.getEnclosingElement();
                p.clazz = clazz.getQualifiedName().toString();
                p.name = ele.getSimpleName().toString();
                break;

            case METHOD:
                clazz = (Symbol.ClassSymbol) ele.getEnclosingElement();
                p.kind = "returnvalue";
                p.clazz = clazz.getQualifiedName().toString();
                p.name = ele.toString();
                break;

            case PARAMETER:
                Symbol.MethodSymbol method = (Symbol.MethodSymbol) ele.getEnclosingElement();
                clazz = (Symbol.ClassSymbol) method.getEnclosingElement();
                p.kind = "parameter";
                p.clazz = clazz.getQualifiedName().toString();
                p.method = method.toString();
                p.parameter = ele.getSimpleName().toString();
                break;
        }


        StaticFactory.specification.add(handle, category, p);
    }
}
