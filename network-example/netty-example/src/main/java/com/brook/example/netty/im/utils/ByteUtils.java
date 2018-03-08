package com.brook.example.netty.im.utils;

import io.netty.buffer.ByteBuf;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2018/3/7
 */
public final class ByteUtils {

  public static byte[] read(ByteBuf data) {
    byte[] bytes = new byte[data.readableBytes()];
    data.readBytes(bytes);
    return bytes;
  }

  public static Object byteToObject(byte[] bytes) {
    Object obj = null;

    try (ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
        ObjectInputStream oi = new ObjectInputStream(bi)) {
       obj = oi.readObject();

    } catch (Exception e) {
      e.printStackTrace();
    }
    return obj;
  }

  public static byte[] objectToByte(Object obj) {
    byte[] bytes = null;
    try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);) {
      oos.writeObject(obj);
      bytes = bos.toByteArray();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return bytes;
  }


}
