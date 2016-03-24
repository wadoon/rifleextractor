package edu.kit.iti.rifl;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.*;

/**
 * Created by weigl on 3/23/16.
 */
public class RIFLSpec {
    public HashMap<String, String> domainAssignment = new HashMap<>();
    public Set<FlowRelation> allowedFlows = new HashSet<>();
    public Set<String> domains = new HashSet<>();

    public Map<String, Map<String, List<Place>>> assignables = new HashMap<>();
    public File saveTo = new File("default.rifl.xml");


    public void save() {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            doc.setXmlStandalone(true);

            // root element
            Element riflspec = doc.createElement("riflspec");
            doc.appendChild(riflspec);

            {
                // Domains
                Element edomains = doc.createElement("domains");
                riflspec.appendChild(edomains);
                for (String s : domains) {
                    Element d = doc.createElement("domain");
                    d.setAttribute("name", s);
                    edomains.appendChild(d);
                }
            }

            {
                // Flow Relation
                Element efr = doc.createElement("flowrelation");
                riflspec.appendChild(efr);
                for (FlowRelation s : allowedFlows) {
                    Element d = doc.createElement("flow");
                    d.setAttribute("from", s.from);
                    d.setAttribute("to", s.to);
                    efr.appendChild(d);
                }
            }

            {
                //Domain Assignments
                Element da = doc.createElement("domainassigment");
                riflspec.appendChild(da);
                for (Map.Entry<String, String> s : domainAssignment.entrySet()) {
                    Element d = doc.createElement("assign");
                    d.setAttribute("handle", s.getKey());
                    d.setAttribute("domain", s.getValue());
                    da.appendChild(d);
                }
            }

            {
                //Assignable
                for (Map.Entry<String, Map<String, List<Place>>> a : assignables.entrySet()) {
                    Element eassign = doc.createElement("assignable");
                    riflspec.appendChild(eassign);

                    eassign.setAttribute("handle", a.getKey());

                    for (Map.Entry<String, List<Place>> c : a.getValue().entrySet()) {
                        Element cat = doc.createElement("category");
                        eassign.appendChild(cat);

                        cat.setAttribute("name", c.getKey());

                        for (Place p : c.getValue()) {
                            cat.appendChild(p.getElement(doc));
                        }
                    }
                }
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setAttribute("indent-number", 4);
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "rifl4Java.dtd");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            DOMSource source = new DOMSource(doc);

            StreamResult result = new StreamResult(saveTo);
            transformer.transform(source, result);

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }

    }

    public void add(String handle, String category, Place p) {
        if (!assignables.containsKey(handle)) {
            assignables.put(handle, new HashMap<>());
        }

        if (!assignables.get(handle).containsKey(category)) {
            assignables.get(handle).put(category, new ArrayList<>());
        }

        assignables.get(handle).get(category).add(p);
    }

    public static class FlowRelation {
        String from;
        String to;

        public FlowRelation(String from, String to) {
            this.from = from;
            this.to = to;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            FlowRelation that = (FlowRelation) o;

            if (from != null ? !from.equals(that.from) : that.from != null) return false;
            return to != null ? to.equals(that.to) : that.to == null;

        }

        @Override
        public int hashCode() {
            int result = from != null ? from.hashCode() : 0;
            result = 31 * result + (to != null ? to.hashCode() : 0);
            return result;
        }
    }

    class Assignable {
        String handle;
        Map<String, List<Place>> categories = new HashMap<>();

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Assignable that = (Assignable) o;

            if (handle != null ? !handle.equals(that.handle) : that.handle != null) return false;
            return categories != null ? categories.equals(that.categories) : that.categories == null;

        }

        @Override
        public int hashCode() {
            int result = handle != null ? handle.hashCode() : 0;
            result = 31 * result + (categories != null ? categories.hashCode() : 0);
            return result;
        }
    }


    static class Place {
        /**
         * returnvalue, field, parameter
         */
        String kind;

        /**
         * sink or source
         */
        String type,
        /**
         *
         */
        clazz, method, name, parameter;

        public Node getElement(Document doc) {
            Element top = doc.createElement(type);
            Element sub = doc.createElement(kind);
            sub.setAttribute("class", clazz);

            switch (kind) {
                case "parameter":
                    sub.setAttribute("parameter", parameter);
                    //Fallthrough
                case "returnvalue":
                    sub.setAttribute("method", method);
                    break;
                case "field":
                    sub.setAttribute("name", name);
                    break;
            }


            top.appendChild(sub);
            return top;
        }
    }

}
