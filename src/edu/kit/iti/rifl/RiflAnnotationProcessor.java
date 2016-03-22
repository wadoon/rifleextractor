package edu.kit.iti.rifl;

import com.sun.tools.javac.code.Symbol;
import edu.kit.iti.rifl.annotation.Sink;
import edu.kit.iti.rifl.annotation.Source;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

/**
 * Created by weigl on 3/22/16.
 */
@SupportedAnnotationTypes({"edu.kit.iti.rifl.annotation.Sink", "edu.kit.iti.rifl.annotation.Sink"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedOptions({"riflspec"})
public class RiflAnnotationProcessor extends AbstractProcessor {
    private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<!DOCTYPE riflspec SYSTEM \"rifl4Java.dtd\">\n" +
            "<riflspec>\n" +
            "  <domains><domain name=\"high\" /><domain name=\"low\" /></domains>\n" +
            "  <flowrelation><flow from=\"low\" to=\"high\" /></flowrelation>\n" +
            "  <domainassignment>\n" +
            "    <assign handle=\"h\" domain=\"high\" />\n" +
            "    <assign handle=\"l\" domain=\"low\" />\n" +
            "  </domainassignment>\n";

    private static final String XML_FOOTER = "</riflspec>\n";

    private String filename;
    private File file;
    private PrintWriter out;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        filename = processingEnv.getOptions().getOrDefault("riflspec", "rifl.xml");

        file = new File(filename);

        try {
            out = new PrintWriter(new FileWriter(this.file));
            out.write(XML_HEADER);
        } catch (IOException e) {
            processingEnv.getMessager().printMessage(
                    Diagnostic.Kind.ERROR,
                    String.format("Could not open %s for writing RIFL specification", file));
        }

    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        out.close();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> sinks = roundEnv.getElementsAnnotatedWith(Sink.class);
        Set<? extends Element> sources = roundEnv.getElementsAnnotatedWith(Source.class);

        for (Element sink : sinks) {
            Sink s = sink.getAnnotation(Sink.class);
            String domain = s.value().name();
            add("sink", domain, sink);
        }

        for (Element source : sources) {
            Source s = source.getAnnotation(Source.class);
            String domain = s.value().name();
            add("source", domain, source);
        }

        out.print(XML_FOOTER);
        out.flush();
        return true;
    }

    private void add(String type, String domain, Element ele) {
        out.format("\t\t<assignable handle=\"%s\">%n", domain);
        out.format("\t\t\t<category name=\"...\">%n");
        out.format("\t\t\t\t<%s>%n", type);

        Symbol.ClassSymbol clazz = null;
        switch (ele.getKind()) {

            case FIELD:
                clazz = (Symbol.ClassSymbol) ele.getEnclosingElement();
                out.format("<field class=\"%s\" name=\"%s\"/>%n",
                        clazz.getQualifiedName(),
                        ele.getSimpleName());
                break;

            case METHOD:
                clazz = (Symbol.ClassSymbol) ele.getEnclosingElement();
                out.format("<returnvalue class=\"%s\" method=\"%s\"/>%n",
                        clazz.getQualifiedName(),
                        ele);
                break;

            case PARAMETER:
                Symbol.MethodSymbol method = (Symbol.MethodSymbol) ele.getEnclosingElement();
                clazz = (Symbol.ClassSymbol) method.getEnclosingElement();
              /*  String parameters_types = method.getParameters().stream()
                        .map((Symbol.VarSymbol a) -> a.asType().toString())
                        .reduce((a, b) -> a + ", " + b)
                        .get();
*/

                out.format("<parameter class=\"%s\" method=\"%s\" parameter=\"%s\"/>%n",
                        clazz.getQualifiedName(),
                        method,
                        ele.getSimpleName());
                break;
        }


        out.format("\t\t\t\t</%s>%n", type);
        out.format("\t\t\t</category>%n");
        out.format("\t\t</assignable>%n");
    }
}
