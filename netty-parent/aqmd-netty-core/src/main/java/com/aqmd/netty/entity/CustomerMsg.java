package com.aqmd.netty.entity;

import java.io.Serializable;
import java.util.Date;

public class CustomerMsg implements Serializable {
   protected static final long serialVersionUID = 1L;
   protected Long id;
   protected Date updateTime;
   protected String createdBy;
   protected Date creationTime;
   protected String customerName;
   protected String email;
   protected Integer cardType;
   protected String idCardNo;
   protected Long bankDetailId;
   protected Integer locked;
   protected String nickName;
   protected String password;
   protected String phone;
   protected String salt;
   protected String sex;
   protected Long agentId;
   protected Long organId;
   protected String opNote;

   public Long getId() {
      return this.id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public Date getUpdateTime() {
      return this.updateTime;
   }

   public void setUpdateTime(Date updateTime) {
      this.updateTime = updateTime;
   }

   public String getCreatedBy() {
      return this.createdBy;
   }

   public void setCreatedBy(String createdBy) {
      this.createdBy = createdBy;
   }

   public Date getCreationTime() {
      return this.creationTime;
   }

   public void setCreationTime(Date creationTime) {
      this.creationTime = creationTime;
   }

   public String getCustomerName() {
      return this.customerName;
   }

   public void setCustomerName(String customerName) {
      this.customerName = customerName;
   }

   public String getEmail() {
      return this.email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public Integer getCardType() {
      return this.cardType;
   }

   public void setCardType(Integer cardType) {
      this.cardType = cardType;
   }

   public String getIdCardNo() {
      return this.idCardNo;
   }

   public void setIdCardNo(String idCardNo) {
      this.idCardNo = idCardNo;
   }

   public Integer getLocked() {
      return this.locked;
   }

   public void setLocked(Integer locked) {
      this.locked = locked;
   }

   public String getPassword() {
      return this.password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public String getPhone() {
      return this.phone;
   }

   public void setPhone(String phone) {
      this.phone = phone;
   }

   public String getSalt() {
      return this.salt;
   }

   public void setSalt(String salt) {
      this.salt = salt;
   }

   public String getSex() {
      return this.sex;
   }

   public void setSex(String sex) {
      this.sex = sex;
   }

   public String getOpNote() {
      return this.opNote;
   }

   public void setOpNote(String opNote) {
      this.opNote = opNote;
   }

   public String getNickName() {
      return this.nickName;
   }

   public void setNickName(String nickName) {
      this.nickName = nickName;
   }

   public Long getBankDetailId() {
      return this.bankDetailId;
   }

   public void setBankDetailId(Long bankDetailId) {
      this.bankDetailId = bankDetailId;
   }

   public Long getAgentId() {
      return this.agentId;
   }

   public void setAgentId(Long agentId) {
      this.agentId = agentId;
   }

   public Long getOrganId() {
      return this.organId;
   }

   public void setOrganId(Long organId) {
      this.organId = organId;
   }

   public String toString() {
      return "CustomerMsg{, id=" + this.id + ", updateTime=" + this.updateTime + ", createdBy=" + this.createdBy + ", creationTime=" + this.creationTime + ", customerName=" + this.customerName + ", email=" + this.email + ", cardType=" + this.cardType + ", idCardNo=" + this.idCardNo + ", locked=" + this.locked + ", nickName=" + this.nickName + ", password=" + this.password + ", phone=" + this.phone + ", salt=" + this.salt + ", sex=" + this.sex + ", opNote=" + this.opNote + "}";
   }
}
