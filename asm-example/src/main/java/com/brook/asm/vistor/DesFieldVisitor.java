package com.brook.asm.vistor;

import com.brook.asm.DesField;
import com.brook.asm.FiledInfo;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;

/**
 * @author Brook 😈
 * @since 2018/7/24
 */
public class DesFieldVisitor extends FieldVisitor {

  private static final String desFieldAnnotationType =
      "L" + DesField.class.getName().replaceAll("\\.", "/") + ";";
  private FiledInfo info;

  public DesFieldVisitor(int i) {
    super(i);
  }

  public DesFieldVisitor(int i, FieldVisitor fieldVisitor) {
    super(i, fieldVisitor);
  }

  public DesFieldVisitor(FiledInfo filedInfo, FieldVisitor fv) {
    super(Opcodes.ASM5, fv);
    this.info = filedInfo;
  }

  @Override
  public AnnotationVisitor visitAnnotation(String s, boolean b) {
    AnnotationVisitor av = super.visitAnnotation(s, b);
    if (!desFieldAnnotationType.equals(s)) {
      return av;
    }
    info.setEnabled(true);
    AnnotationVisitor avAdapter = new DesTypeAnnotationAdapter(Opcodes.ASM5, av, this.info);
    return avAdapter;
  }
}
