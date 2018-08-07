import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 *
 * <pr>
 *
  public class Demo{
    public void run(){
      System.out.println("Hello, ASM!");
  }

 * </pr>
 * @author Brook 😈
 * @since 2018/7/25
 */
public class AsmDemo extends ClassLoader implements Opcodes {


  /**
   * 动态创建一个类，有一个无参数的构造函数
   */
  static ClassWriter createClassWriter(String className) {
    ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
    //声明一个类，使用JDK1.8版本，public的类，父类是java.lang.Object，没有实现任何接口
    cw.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, className, null, "java/lang/Object", null);

    //初始化一个无参的构造函数
    MethodVisitor constructor = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
    //这里请看截图
    constructor.visitVarInsn(Opcodes.ALOAD, 0);
    //执行父类的init初始化
    constructor.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
    //从当前方法返回void
    constructor.visitInsn(Opcodes.RETURN);
    constructor.visitMaxs(1, 1);
    constructor.visitEnd();
    return cw;
  }

  /**
   * 创建一个run方法，里面只有一个输出
   * public void run()
   * {
   * System.out.println(message);
   * }
   */
  static byte[] createVoidMethod(String className, String message) throws Exception {
    //注意，这里需要把classname里面的.改成/，如com.asm.Test改成com/asm/Test
    ClassWriter cw = createClassWriter(className.replace('.', '/'));

    //创建run方法
    //()V表示函数，无参数，无返回值
    MethodVisitor runMethod = cw.visitMethod(Opcodes.ACC_PUBLIC, "run", "()V", null, null);
    //先获取一个java.io.PrintStream对象
    runMethod.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
    //将int, float或String型常量值从常量池中推送至栈顶  (此处将message字符串从常量池中推送至栈顶[输出的内容])
    runMethod.visitLdcInsn(message);
    //执行println方法（执行的是参数为字符串，无返回值的println函数）
    runMethod.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println",
        "(Ljava/lang/String;)V", false);
    runMethod.visitInsn(Opcodes.RETURN);
    runMethod.visitMaxs(1, 1);
    runMethod.visitEnd();

    return cw.toByteArray();
  }

  public Class<?> loadClass(String clazzName , byte[] data){
    return this.defineClass(clazzName, data, 0, data.length);
  }

  public static void main(String[] args) throws Exception {

    String className = "com.brook.asm.Demo";
    byte[] classData = createVoidMethod(className, "Hello, ASM!");
    AsmDemo demo = new AsmDemo();
    Class<?> clazz = demo.loadClass(className, classData);
    clazz.getMethods()[0].invoke(clazz.newInstance());
  }
}
