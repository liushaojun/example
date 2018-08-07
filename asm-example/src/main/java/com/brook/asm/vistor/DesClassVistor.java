package com.brook.asm.vistor;

import com.brook.asm.Desensitized;
import com.brook.asm.FiledInfo;
import java.util.HashMap;
import java.util.Map;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * @author Brook 😈
 * @since 2018/7/24
 */
public class DesClassVistor extends ClassVisitor implements Opcodes {

  private static final String classAnnotationType =
      "L" + Desensitized.class.getName().replaceAll("\\.", "/") + ";";

  private String className;
  private boolean enabled;

  public DesClassVistor(int version){
    super(version);
  }
  public DesClassVistor(int version, ClassWriter classWriter) {
    super(version,classWriter);
  }

  private Map<String, FiledInfo> filedMap = new HashMap<>();

  @Override
  public void visit(int version, int acc
      , String className, String generic, String superClass, String[]
      superInterfaces) {
    this.className = className;

    super.visit(version, acc,className, generic, superClass, superInterfaces);
  }

  /**
   * @param type 注解类型
   * @param seeing 可见性
   */
  @Override
  public AnnotationVisitor visitAnnotation(String type, boolean seeing) {
    if (classAnnotationType.equals(type)) {
      this.enabled = true;
    }
    return super.visitAnnotation(type, seeing);
  }
  /**
   * @param acc 访问权限
   * @param name 字段名字
   * @param type 类型
   * @param generic 泛型
   * @param defaultValue 默认值
   */
  @Override
  public FieldVisitor visitField(int acc, String name, String type, String generic,
      Object defaultValue) {
    FieldVisitor fv = super.visitField(acc, name, type, generic, defaultValue);
    if (!enabled || acc >= Opcodes.ACC_STATIC) {
      return fv;
    }
    FiledInfo filedInfo = new FiledInfo(acc, name, type, generic, defaultValue);
    filedMap.put(name, filedInfo);
    FieldVisitor testFieldVisitor = new DesFieldVisitor(filedInfo, fv);
    return testFieldVisitor;
  }

  @Override
  public MethodVisitor visitMethod(int access, String name, String desc, String signature,
      String[] exceptions) {
    MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
    if (!enabled || !"toString".equals(name)) {
      return mv;
    }
    MethodVisitor testMethodVistor = new DesMethodVistor(mv, filedMap);
    return testMethodVistor;
  }

}
