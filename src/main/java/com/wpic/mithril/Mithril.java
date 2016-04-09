package com.wpic.mithril;

import org.apache.commons.lang.StringEscapeUtils;

import java.util.*;

/**
 * Simple Mithril implementation for Java.
 *
 * @Vertion v1.0-SNAPSHOT
 */
public class Mithril {

    private String tag;

    private Map<String, String> attributes;

    private Object[] children;

    private String html;

    private Mithril() {

    }

    /**
     * Create empty tag.
     * @param tag Tag name
     * @return Mithril object
     */
    public static Mithril m(final String tag) {
        return m(tag, null, null);
    }

    /**
     * Create tag with attribute and inner list of objects.
     * @param tag Tag name
     * @param children Attribute map and sub-nodes. If the first param in the array be instance of Map, it will use as
     *                 attributes.
     * @return Mithril object
     */
    public static Mithril m(final String tag, final Object... children) {
        if (tag == null && tag.length() > 0) {
            throw new IllegalArgumentException("Illegal tag: " + tag);
        }

        final String[] tagParts = tag.split("\\.");
        final String tagName = tagParts[0];

        final String[] classes = Arrays.copyOfRange(tagParts, 1, tagParts.length);

        final Mithril m = new Mithril();
        m.attributes = new HashMap();

        // add classes
        m.attributes.put("class", String.join(" ", classes));

        if (children != null && children.length > 0 && children[0] instanceof Map) {
            System.out.println("found");
            final Map<String, String> addedAttributes = (Map) children[0];

            for (Map.Entry e:addedAttributes.entrySet()) {
                final String k = e.getKey().toString();
                final String v = e.getValue().toString();

                if (m.attributes.containsKey(k)) {
                    m.attributes.put(k, v + " " + m.attributes.get(k));
                }
                else {
                    m.attributes.put(k, v);
                }
            }

            m.children = Arrays.copyOfRange(children, 1, children.length);
        }
        else {
            m.children = children;
        }

        m.tag = tagName;

        return m;
    }

    /**
     * This method flags a string as trusted HTML
     * @param html HTML string
     * @return Mithril object
     */
    public static Mithril trust(final String html) {
        final Mithril m = new Mithril();
        m.html = html;
        return m;
    }

    public String toHtml() {
        return toHtml(this);
    }

    @Override
    public String toString() {
        return toHtml();
    }

    private String toHtml(final Object o) {
        if (o instanceof Mithril) {
            final Mithril m = (Mithril) o;

            if (m.html != null) {
                return m.html;
            }
            else {
                final StringBuilder s = new StringBuilder();

                // Start tag
                s.append('<');
                s.append(m.tag);

                for (Object e : m.attributes.entrySet()) {
                    final Map.Entry entry = (Map.Entry) e;
                    final String k = entry.getKey().toString();
                    final String v = entry.getValue().toString();

                    if (v.length() > 0) {
                        s.append(' ').append(k).append("=\"")
                                .append(StringEscapeUtils.escapeHtml(v))
                                .append("\"");
                    }
                }

                boolean hasChildren = false;
                // Inline nodes
                for (Object child : m.children) {
                    hasChildren = hasChildren | (child != null);
                }

                if (hasChildren) {
                    s.append('>');

                    // Inline nodes
                    for (Object child : m.children) {
                        if (child != null) {
                            s.append(toHtml(child));
                        }
                    }

                    // End tag
                    s.append("</");
                    s.append(m.tag);
                    s.append('>');
                } else {
                    s.append("/>");
                }

                return s.toString();
            }
        }
        return StringEscapeUtils.escapeHtml(o.toString());
    }

}
