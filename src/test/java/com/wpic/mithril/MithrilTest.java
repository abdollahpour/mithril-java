package com.wpic.mithril;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import static com.wpic.mithril.Mithril.m;
import static java.util.Collections.singletonMap;

import static org.junit.Assert.*;

public class MithrilTest {

    @Test
    public void test() {
        final String inner = "this is mithril , .> #$# <<<";

        final Mithril mithril = m("div.row", singletonMap("class", "mithril"),
                "inner text",
                m("p", inner),
                m("span.fa.fa-sample")
        );

        final String html = mithril.toHtml();
        final Document doc = Jsoup.parse(html);

        assertEquals(html, "<div class=\"mithril row\">inner text<p>this is mithril , .&gt; #$# &lt;&lt;&lt;</p><span class=\"fa fa-sample\"/></div>");
        assertEquals(doc.getElementsByClass("row").size(), 1);
        assertEquals(doc.getElementsByClass("mithril").size(), 1);
    }

}
