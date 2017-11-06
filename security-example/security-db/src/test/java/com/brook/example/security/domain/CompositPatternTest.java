package com.brook.example.security.domain;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

/**
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2017/8/30
 */
public class CompositPatternTest {
  @Test
  public void test(){
    TextTagComposit composite = new PTag();
    composite.addTag(new SpanTag());
    composite.addTag(new DivTag());

    // sample client code
    composite.startTag();
    for (TextTag leaf : composite.getTags()) {
      leaf.startTag();
      leaf.endTag();
    }
    composite.endTag();

  }
}
interface TextTag{
  void startTag();
  void endTag();
}
interface TextTagComposit extends TextTag{
  List<TextTag> getTags();
  void addTag(TextTag tag);
}

class PTag implements TextTagComposit{

  private List<TextTag> tags = new ArrayList<TextTag>();

  @Override
  public void startTag() {
    System.out.println("<p>");
  }

  @Override
  public void endTag() {
    System.out.println("</p>");

  }

  @Override
  public List<TextTag> getTags() {
    return tags;
  }

  @Override
  public void addTag(TextTag tag) {
    tags.add(tag);
  }
}

class SpanTag implements TextTag {

  @Override
  public void startTag() {
    System.out.println("<span>");
  }

  @Override
  public void endTag() {
    System.out.println("</span>");
  }
}

class DivTag implements TextTag {

  @Override
  public void startTag() {
    System.out.println("<div>");
  }

  @Override
  public void endTag() {
    System.out.println("</div>");
  }

}