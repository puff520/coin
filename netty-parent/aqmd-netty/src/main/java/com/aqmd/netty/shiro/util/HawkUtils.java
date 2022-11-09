package com.aqmd.netty.shiro.util;

import com.aqmd.netty.entity.RequestPacket;
import com.aqmd.netty.entity.ResponsePacket;

public class HawkUtils {
   public static RequestPacket getRequest(Object requestPairSource) {
      return requestPairSource instanceof RequestPairSource ? ((RequestPairSource)requestPairSource).getHawkRequest() : null;
   }

   public static ResponsePacket getResponse(Object requestPairSource) {
      return requestPairSource instanceof RequestPairSource ? ((RequestPairSource)requestPairSource).getHawkResponse() : null;
   }

   public static boolean _isSessionCreationEnabled(Object requestPairSource) {
      if (requestPairSource instanceof RequestPairSource) {
         RequestPairSource source = (RequestPairSource)requestPairSource;
         return _isSessionCreationEnabled(source.getHawkRequest());
      } else {
         return true;
      }
   }
}
