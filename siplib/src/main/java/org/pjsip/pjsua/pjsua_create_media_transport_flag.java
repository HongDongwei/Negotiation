/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.9
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package org.pjsip.pjsua;

public enum pjsua_create_media_transport_flag {
  PJSUA_MED_TP_CLOSE_MEMBER(pjsuaJNI.PJSUA_MED_TP_CLOSE_MEMBER_get());

  public final int swigValue() {
    return swigValue;
  }

  public static pjsua_create_media_transport_flag swigToEnum(int swigValue) {
    pjsua_create_media_transport_flag[] swigValues = pjsua_create_media_transport_flag.class.getEnumConstants();
    if (swigValue < swigValues.length && swigValue >= 0 && swigValues[swigValue].swigValue == swigValue)
      return swigValues[swigValue];
    for (pjsua_create_media_transport_flag swigEnum : swigValues)
      if (swigEnum.swigValue == swigValue)
        return swigEnum;
    throw new IllegalArgumentException("No enum " + pjsua_create_media_transport_flag.class + " with value " + swigValue);
  }

  @SuppressWarnings("unused")
  private pjsua_create_media_transport_flag() {
    this.swigValue = SwigNext.next++;
  }

  @SuppressWarnings("unused")
  private pjsua_create_media_transport_flag(int swigValue) {
    this.swigValue = swigValue;
    SwigNext.next = swigValue+1;
  }

  @SuppressWarnings("unused")
  private pjsua_create_media_transport_flag(pjsua_create_media_transport_flag swigEnum) {
    this.swigValue = swigEnum.swigValue;
    SwigNext.next = this.swigValue+1;
  }

  private final int swigValue;

  private static class SwigNext {
    private static int next = 0;
  }
}

