package com.brook.asm.vistor;

import com.brook.asm.FiledInfo;
import com.brook.asm.filter.DesFilter;
import com.brook.asm.filter.DesFilterMap;
import com.brook.asm.utils.ASMUtil;
import java.util.Map;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * @author Brook 😈
 * @since 2018/7/24
 */
public class DesMethodVistor extends MethodVisitor implements Opcodes{

  Map<String, FiledInfo> filedMap;

  public DesMethodVistor(int i) {
    super(i);
  }

  public DesMethodVistor(int i, MethodVisitor methodVisitor) {
    super(i, methodVisitor);
  }

  public DesMethodVistor(MethodVisitor mv, Map<String, FiledInfo> filedMap) {
    super(ASM5, mv);
    this.filedMap = filedMap;
  }

  @Override
  public void visitVarInsn(int opcode, int var) {
    if (!(opcode == Opcodes.ALOAD && var == 0)) {
      super.visitVarInsn(opcode, var);
    }
  }

  /**
   * 添加过滤逻辑
   */
  @Override
  public void visitFieldInsn(int opcode, String owner, String name, String desc) {
    FiledInfo filedInfo = filedMap.get(name);

    if (!filedInfo.isEnabled()) {
      super.visitVarInsn(Opcodes.ALOAD, 0);
      super.visitFieldInsn(opcode, owner, name, desc);
      return;
    }

    mv.visitLdcInsn(filedInfo.getFilterClass().getName());
    mv.visitMethodInsn(INVOKESTATIC, ASMUtil.getASMOwnerByClass(DesFilterMap.class),
        "getByClassName", "(Ljava/lang/String;)Lcom/brook/asm/filter/DesFilter;", false);
    super.visitVarInsn(ALOAD, 0);
    super.visitFieldInsn(opcode, owner, name, desc);
    mv.visitMethodInsn(INVOKEINTERFACE, ASMUtil.getASMOwnerByClass(DesFilter.class), "desc",
        "(Ljava/lang/Object;)Ljava/lang/Object;", true);
    mv.visitMethodInsn(INVOKESTATIC, ASMUtil.getASMOwnerByClass(String.class), "valueOf",
        "(Ljava/lang/Object;)Ljava/lang/String;", true);
  }
}
