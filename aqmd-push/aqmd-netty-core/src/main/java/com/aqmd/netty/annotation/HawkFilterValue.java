package com.aqmd.netty.annotation;

import com.aqmd.netty.filter.HFilter;

public class HawkFilterValue implements Comparable<HawkFilterValue> {
   private int order;
   private int[] cmds;
   private int[] ignoreCmds;
   private HFilter hfilter;

   public HawkFilterValue(int order, int[] cmds, int[] ignoreCmds, HFilter hfilter) {
      this.order = order;
      this.cmds = cmds;
      this.ignoreCmds = ignoreCmds;
      this.hfilter = hfilter;
   }

   public int getOrder() {
      return this.order;
   }

   public void setOrder(int order) {
      this.order = order;
   }

   public int[] getCmds() {
      return this.cmds;
   }

   public void setCmds(int[] cmds) {
      this.cmds = cmds;
   }

   public int[] getIgnoreCmds() {
      return this.ignoreCmds;
   }

   public void setIgnoreCmds(int[] ignoreCmds) {
      this.ignoreCmds = ignoreCmds;
   }

   public HFilter getHfilter() {
      return this.hfilter;
   }

   public void setHfilter(HFilter hfilter) {
      this.hfilter = hfilter;
   }

   public int compareTo(HawkFilterValue another) {
      return Integer.compare(this.order, another.order);
   }
}
