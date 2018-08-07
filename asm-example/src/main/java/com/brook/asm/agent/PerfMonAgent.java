package com.brook.asm.agent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;

/**
 * @author Brook 😈
 * @since 2018/7/24
 */
public class PerfMonAgent {

  private static Instrumentation inst = null;
  public static void premain(String agentArgs, Instrumentation _inst){
    System.out.println("PerfMonAgent.premain() was called.");
    inst = _inst;
    ClassFileTransformer transformer = new ClassTransformerImpl();
    System.out.println("Adding a PerfMonFormer instance to the JVM.");
    inst.addTransformer(transformer);
  }
}
