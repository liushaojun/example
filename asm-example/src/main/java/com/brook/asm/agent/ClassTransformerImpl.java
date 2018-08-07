package com.brook.asm.agent;

import com.brook.asm.vistor.DesClassVistor;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

/**
 * @author Brook 😈
 * @since 2018/7/24
 */
public class ClassTransformerImpl implements ClassFileTransformer {

  @Override
  public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
      ProtectionDomain protectionDomain, byte[] classfileBuffer)
      throws IllegalClassFormatException {
//    System.out.printf("Class Name: %s", className + "\n");
    ClassReader reader = new ClassReader(classfileBuffer);
    ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
    ClassVisitor classVisitor = new DesClassVistor(Opcodes.ASM5, classWriter);
    reader.accept(classVisitor, ClassReader.SKIP_DEBUG);
    return classWriter.toByteArray();
  }
}
