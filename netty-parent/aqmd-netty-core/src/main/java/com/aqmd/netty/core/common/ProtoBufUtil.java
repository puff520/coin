package com.aqmd.netty.core.common;

import com.aqmd.netty.entity.RequestPacket;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ProtoBufUtil {
   public static <T> byte[] serializer(T o) {
      Schema schema = RuntimeSchema.getSchema(o.getClass());
      return ProtobufIOUtil.toByteArray(o, schema, LinkedBuffer.allocate(256));
   }

   public static <T> T deserializer(byte[] bytes, Class<T> clazz) {
      T obj = null;

      try {
         obj = clazz.newInstance();
         Schema schema = RuntimeSchema.getSchema(obj.getClass());
         ProtostuffIOUtil.mergeFrom(bytes, obj, schema);
      } catch (InstantiationException var4) {
         var4.printStackTrace();
      } catch (IllegalAccessException var5) {
         var5.printStackTrace();
      }

      return obj;
   }

   public static byte[] buildRequestBytes(String terminal, short cmd, int version, long sequenceId, byte[] body) throws IOException {
      RequestPacket packet = new RequestPacket();
      packet.setCmd(cmd);
      packet.setVersion(version);
      packet.setBody(body);
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      DataOutputStream dos = new DataOutputStream(bos);
      int length = packet.getLength();
      dos.writeInt(length);
      dos.writeLong(sequenceId);
      dos.writeShort(cmd);
      dos.writeInt(version);
      byte[] terminalBytes = terminal.getBytes();
      dos.write(terminalBytes);
      dos.writeInt(0);
      if (body != null) {
         dos.write(body);
      }

      return bos.toByteArray();
   }
}
