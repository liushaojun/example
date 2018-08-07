package com.brook.asm.vistor;

import com.brook.asm.FiledInfo;
import com.brook.asm.filter.DesFilter;
import com.brook.asm.filter.DesFilterMap;
import com.brook.asm.utils.ASMUtil;
import java.util.Objects;
import org.objectweb.asm.AnnotationVisitor;

public class DesTypeAnnotationAdapter extends AnnotationVisitor {
    private static final String VALUE = "value";
    private FiledInfo filedInfo;
    public DesTypeAnnotationAdapter(int api) {
        super(api);
    }

    public DesTypeAnnotationAdapter(int api, AnnotationVisitor av) {
        super(api, av);
    }

    public DesTypeAnnotationAdapter(int api, AnnotationVisitor av, FiledInfo filedInfo) {
        super(api, av);
        this.filedInfo = filedInfo;
    }

    @Override
    public void visit(String name, Object value) {
        super.visit(name, value);
        if (VALUE.equals(name)){
            try {
                Class fliterClass = ASMUtil.getClassByASMDesc(value.toString());
                DesFilter desFilter = DesFilterMap.get(fliterClass);
                //如果为空就新生成一个到map里面
                if (Objects.isNull(desFilter)){
                    DesFilterMap.put(fliterClass, (DesFilter) fliterClass.newInstance());
                }
                filedInfo.setFilterClass(fliterClass);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}