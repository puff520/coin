package com.aqmd.netty.shiro;

import java.io.PrintStream;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;

public class SequenceSessionIdGenerator implements SessionIdGenerator {
   private static final int MIN_SEQ_ID = 536870911;
   private static AtomicInteger idWoker = new AtomicInteger(536870911);

   public Serializable generateId(Session session) {
      int seqId;
      for(seqId = idWoker.getAndIncrement(); seqId < 536870911; seqId = idWoker.addAndGet(536870911)) {
      }

      return (long)seqId;
   }

   public static void main(String[] args) {
      long seqId = (long)idWoker.getAndIncrement();
      PrintStream var10000 = System.out;
      StringBuilder var10001 = (new StringBuilder()).append(seqId).append(":");
      new Long(seqId);
      var10000.println(var10001.append(Long.toBinaryString(seqId)).toString());
      seqId <<= 32;
      var10000 = System.out;
      var10001 = (new StringBuilder()).append(seqId).append(":");
      new Long(seqId);
      var10000.println(var10001.append(Long.toBinaryString(seqId)).toString());
      long time = System.currentTimeMillis();
      var10000 = System.out;
      var10001 = (new StringBuilder()).append(time).append(":");
      new Long(time);
      var10000.println(var10001.append(Long.toBinaryString(time)).toString());
      var10000 = System.out;
      var10001 = (new StringBuilder()).append(time).append(":");
      new Integer((int)time);
      var10000.println(var10001.append(Integer.toBinaryString((int)time)).toString());
      seqId += (long)((int)time);
      var10000 = System.out;
      var10001 = (new StringBuilder()).append(seqId).append(":");
      new Long(seqId);
      var10000.println(var10001.append(Long.toBinaryString(seqId)).toString());

      while(seqId < 536870911L) {
         seqId = (long)idWoker.addAndGet(536870911);
      }

   }
}
