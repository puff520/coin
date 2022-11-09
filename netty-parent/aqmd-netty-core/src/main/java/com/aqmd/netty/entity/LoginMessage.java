package com.aqmd.netty.entity;

import com.google.protobuf.AbstractParser;
import com.google.protobuf.ByteString;
import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.Descriptors;
import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.ExtensionRegistryLite;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.Parser;
import com.google.protobuf.UnknownFieldSet;
import com.google.protobuf.Descriptors.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public final class LoginMessage {
   private static final Descriptors.Descriptor internal_static_tutorial_LoginUser_descriptor;
   private static final GeneratedMessageV3.FieldAccessorTable internal_static_tutorial_LoginUser_fieldAccessorTable;
   private static Descriptors.FileDescriptor descriptor;

   private LoginMessage() {
   }

   public static void registerAllExtensions(ExtensionRegistryLite registry) {
   }

   public static void registerAllExtensions(ExtensionRegistry registry) {
      registerAllExtensions((ExtensionRegistryLite)registry);
   }

   public static Descriptors.FileDescriptor getDescriptor() {
      return descriptor;
   }

   static {
      String[] descriptorData = new String[]{"\n\u000bLogin.proto\u0012\btutorial\"A\n\tLoginUser\u0012\u0010\n\busername\u0018\u0001 \u0001(\t\u0012\u000e\n\u0006passwd\u0018\u0002 \u0001(\t\u0012\u0012\n\nverificode\u0018\u0003 \u0001(\tB%\n\u0015com.spark.hawk.entityB\fLoginMessageb\u0006proto3"};
      Descriptors.FileDescriptor.InternalDescriptorAssigner assigner = new Descriptors.FileDescriptor.InternalDescriptorAssigner() {
         public ExtensionRegistry assignDescriptors(Descriptors.FileDescriptor root) {
            LoginMessage.descriptor = root;
            return null;
         }
      };
      FileDescriptor.internalBuildGeneratedFileFrom(descriptorData, new Descriptors.FileDescriptor[0], assigner);
      internal_static_tutorial_LoginUser_descriptor = (Descriptors.Descriptor)getDescriptor().getMessageTypes().get(0);
      internal_static_tutorial_LoginUser_fieldAccessorTable = new GeneratedMessageV3.FieldAccessorTable(internal_static_tutorial_LoginUser_descriptor, new String[]{"Username", "Passwd", "Verificode"});
   }

   public static final class LoginUser extends GeneratedMessageV3 implements LoginUserOrBuilder {
      public static final int USERNAME_FIELD_NUMBER = 1;
      private volatile Object username_;
      public static final int PASSWD_FIELD_NUMBER = 2;
      private volatile Object passwd_;
      public static final int VERIFICODE_FIELD_NUMBER = 3;
      private volatile Object verificode_;
      private byte memoizedIsInitialized;
      private static final long serialVersionUID = 0L;
      private static final LoginUser DEFAULT_INSTANCE = new LoginUser();
      private static final Parser<LoginUser> PARSER = new AbstractParser<LoginUser>() {
         public LoginUser parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return new LoginUser(input, extensionRegistry);
         }
      };

      private LoginUser(GeneratedMessageV3.Builder<?> builder) {
         super(builder);
         this.memoizedIsInitialized = -1;
      }

      private LoginUser() {
         this.memoizedIsInitialized = -1;
         this.username_ = "";
         this.passwd_ = "";
         this.verificode_ = "";
      }

      public final UnknownFieldSet getUnknownFields() {
         return UnknownFieldSet.getDefaultInstance();
      }

      private LoginUser(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
         this();
//         int mutable_bitField0_ = false;

         try {
            boolean done = false;

            while(!done) {
               int tag = input.readTag();
               String s;
               switch (tag) {
                  case 0:
                     done = true;
                     break;
                  case 10:
                     s = input.readStringRequireUtf8();
                     this.username_ = s;
                     break;
                  case 18:
                     s = input.readStringRequireUtf8();
                     this.passwd_ = s;
                     break;
                  case 26:
                     s = input.readStringRequireUtf8();
                     this.verificode_ = s;
                     break;
                  default:
                     if (!input.skipField(tag)) {
                        done = true;
                     }
               }
            }
         } catch (InvalidProtocolBufferException var11) {
            throw var11.setUnfinishedMessage(this);
         } catch (IOException var12) {
            throw (new InvalidProtocolBufferException(var12)).setUnfinishedMessage(this);
         } finally {
            this.makeExtensionsImmutable();
         }

      }

      public static final Descriptors.Descriptor getDescriptor() {
         return LoginMessage.internal_static_tutorial_LoginUser_descriptor;
      }

      protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
         return LoginMessage.internal_static_tutorial_LoginUser_fieldAccessorTable.ensureFieldAccessorsInitialized(LoginUser.class, Builder.class);
      }

      public String getUsername() {
         Object ref = this.username_;
         if (ref instanceof String) {
            return (String)ref;
         } else {
            ByteString bs = (ByteString)ref;
            String s = bs.toStringUtf8();
            this.username_ = s;
            return s;
         }
      }

      public ByteString getUsernameBytes() {
         Object ref = this.username_;
         if (ref instanceof String) {
            ByteString b = ByteString.copyFromUtf8((String)ref);
            this.username_ = b;
            return b;
         } else {
            return (ByteString)ref;
         }
      }

      public String getPasswd() {
         Object ref = this.passwd_;
         if (ref instanceof String) {
            return (String)ref;
         } else {
            ByteString bs = (ByteString)ref;
            String s = bs.toStringUtf8();
            this.passwd_ = s;
            return s;
         }
      }

      public ByteString getPasswdBytes() {
         Object ref = this.passwd_;
         if (ref instanceof String) {
            ByteString b = ByteString.copyFromUtf8((String)ref);
            this.passwd_ = b;
            return b;
         } else {
            return (ByteString)ref;
         }
      }

      public String getVerificode() {
         Object ref = this.verificode_;
         if (ref instanceof String) {
            return (String)ref;
         } else {
            ByteString bs = (ByteString)ref;
            String s = bs.toStringUtf8();
            this.verificode_ = s;
            return s;
         }
      }

      public ByteString getVerificodeBytes() {
         Object ref = this.verificode_;
         if (ref instanceof String) {
            ByteString b = ByteString.copyFromUtf8((String)ref);
            this.verificode_ = b;
            return b;
         } else {
            return (ByteString)ref;
         }
      }

      public final boolean isInitialized() {
         byte isInitialized = this.memoizedIsInitialized;
         if (isInitialized == 1) {
            return true;
         } else if (isInitialized == 0) {
            return false;
         } else {
            this.memoizedIsInitialized = 1;
            return true;
         }
      }

      public void writeTo(CodedOutputStream output) throws IOException {
         if (!this.getUsernameBytes().isEmpty()) {
            GeneratedMessageV3.writeString(output, 1, this.username_);
         }

         if (!this.getPasswdBytes().isEmpty()) {
            GeneratedMessageV3.writeString(output, 2, this.passwd_);
         }

         if (!this.getVerificodeBytes().isEmpty()) {
            GeneratedMessageV3.writeString(output, 3, this.verificode_);
         }

      }

      public int getSerializedSize() {
         int size = this.memoizedSize;
         if (size != -1) {
            return size;
         } else {
            size = 0;
            if (!this.getUsernameBytes().isEmpty()) {
               size += GeneratedMessageV3.computeStringSize(1, this.username_);
            }

            if (!this.getPasswdBytes().isEmpty()) {
               size += GeneratedMessageV3.computeStringSize(2, this.passwd_);
            }

            if (!this.getVerificodeBytes().isEmpty()) {
               size += GeneratedMessageV3.computeStringSize(3, this.verificode_);
            }

            this.memoizedSize = size;
            return size;
         }
      }

      public boolean equals(final Object obj) {
         if (obj == this) {
            return true;
         } else if (!(obj instanceof LoginUser)) {
            return super.equals(obj);
         } else {
            LoginUser other = (LoginUser)obj;
            boolean result = true;
            result = result && this.getUsername().equals(other.getUsername());
            result = result && this.getPasswd().equals(other.getPasswd());
            result = result && this.getVerificode().equals(other.getVerificode());
            return result;
         }
      }

      public int hashCode() {
         if (this.memoizedHashCode != 0) {
            return this.memoizedHashCode;
         } else {
            int hash = 41;
            hash = 19 * hash + getDescriptor().hashCode();
            hash = 37 * hash + 1;
            hash = 53 * hash + this.getUsername().hashCode();
            hash = 37 * hash + 2;
            hash = 53 * hash + this.getPasswd().hashCode();
            hash = 37 * hash + 3;
            hash = 53 * hash + this.getVerificode().hashCode();
            hash = 29 * hash + this.unknownFields.hashCode();
            this.memoizedHashCode = hash;
            return hash;
         }
      }

      public static LoginUser parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
         return (LoginUser)PARSER.parseFrom(data);
      }

      public static LoginUser parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
         return (LoginUser)PARSER.parseFrom(data, extensionRegistry);
      }

      public static LoginUser parseFrom(ByteString data) throws InvalidProtocolBufferException {
         return (LoginUser)PARSER.parseFrom(data);
      }

      public static LoginUser parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
         return (LoginUser)PARSER.parseFrom(data, extensionRegistry);
      }

      public static LoginUser parseFrom(byte[] data) throws InvalidProtocolBufferException {
         return (LoginUser)PARSER.parseFrom(data);
      }

      public static LoginUser parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
         return (LoginUser)PARSER.parseFrom(data, extensionRegistry);
      }

      public static LoginUser parseFrom(InputStream input) throws IOException {
         return (LoginUser)GeneratedMessageV3.parseWithIOException(PARSER, input);
      }

      public static LoginUser parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
         return (LoginUser)GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
      }

      public static LoginUser parseDelimitedFrom(InputStream input) throws IOException {
         return (LoginUser)GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
      }

      public static LoginUser parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
         return (LoginUser)GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input, extensionRegistry);
      }

      public static LoginUser parseFrom(CodedInputStream input) throws IOException {
         return (LoginUser)GeneratedMessageV3.parseWithIOException(PARSER, input);
      }

      public static LoginUser parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
         return (LoginUser)GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
      }

      public Builder newBuilderForType() {
         return newBuilder();
      }

      public static Builder newBuilder() {
         return DEFAULT_INSTANCE.toBuilder();
      }

      public static Builder newBuilder(LoginUser prototype) {
         return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
      }

      public Builder toBuilder() {
         return this == DEFAULT_INSTANCE ? new Builder() : (new Builder()).mergeFrom(this);
      }

      protected Builder newBuilderForType(GeneratedMessageV3.BuilderParent parent) {
         Builder builder = new Builder(parent);
         return builder;
      }

      public static LoginUser getDefaultInstance() {
         return DEFAULT_INSTANCE;
      }

      public static Parser<LoginUser> parser() {
         return PARSER;
      }

      public Parser<LoginUser> getParserForType() {
         return PARSER;
      }

      public LoginUser getDefaultInstanceForType() {
         return DEFAULT_INSTANCE;
      }

      // $FF: synthetic method
      LoginUser(GeneratedMessageV3.Builder x0, Object x1) {
         this(x0);
      }

      // $FF: synthetic method
      LoginUser(CodedInputStream x0, ExtensionRegistryLite x1, Object x2) throws InvalidProtocolBufferException {
         this(x0, x1);
      }

      public static final class Builder extends GeneratedMessageV3.Builder<Builder> implements LoginUserOrBuilder {
         private Object username_;
         private Object passwd_;
         private Object verificode_;

         public static final Descriptors.Descriptor getDescriptor() {
            return LoginMessage.internal_static_tutorial_LoginUser_descriptor;
         }

         protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
            return LoginMessage.internal_static_tutorial_LoginUser_fieldAccessorTable.ensureFieldAccessorsInitialized(LoginUser.class, Builder.class);
         }

         private Builder() {
            this.username_ = "";
            this.passwd_ = "";
            this.verificode_ = "";
            this.maybeForceBuilderInitialization();
         }

         private Builder(GeneratedMessageV3.BuilderParent parent) {
            super(parent);
            this.username_ = "";
            this.passwd_ = "";
            this.verificode_ = "";
            this.maybeForceBuilderInitialization();
         }

         private void maybeForceBuilderInitialization() {
            if (LoginMessage.LoginUser.alwaysUseFieldBuilders) {
            }

         }

         public Builder clear() {
            super.clear();
            this.username_ = "";
            this.passwd_ = "";
            this.verificode_ = "";
            return this;
         }

         public Descriptors.Descriptor getDescriptorForType() {
            return LoginMessage.internal_static_tutorial_LoginUser_descriptor;
         }

         public LoginUser getDefaultInstanceForType() {
            return LoginMessage.LoginUser.getDefaultInstance();
         }

         public LoginUser build() {
            LoginUser result = this.buildPartial();
            if (!result.isInitialized()) {
               throw newUninitializedMessageException(result);
            } else {
               return result;
            }
         }

         public LoginUser buildPartial() {
            LoginUser result = new LoginUser(this);
            result.username_ = this.username_;
            result.passwd_ = this.passwd_;
            result.verificode_ = this.verificode_;
            this.onBuilt();
            return result;
         }

         public Builder clone() {
            return (Builder)super.clone();
         }

         public Builder setField(Descriptors.FieldDescriptor field, Object value) {
            return (Builder)super.setField(field, value);
         }

         public Builder clearField(Descriptors.FieldDescriptor field) {
            return (Builder)super.clearField(field);
         }

         public Builder clearOneof(Descriptors.OneofDescriptor oneof) {
            return (Builder)super.clearOneof(oneof);
         }

         public Builder setRepeatedField(Descriptors.FieldDescriptor field, int index, Object value) {
            return (Builder)super.setRepeatedField(field, index, value);
         }

         public Builder addRepeatedField(Descriptors.FieldDescriptor field, Object value) {
            return (Builder)super.addRepeatedField(field, value);
         }

         public Builder mergeFrom(Message other) {
            if (other instanceof LoginUser) {
               return this.mergeFrom((LoginUser)other);
            } else {
               super.mergeFrom(other);
               return this;
            }
         }

         public Builder mergeFrom(LoginUser other) {
            if (other == LoginMessage.LoginUser.getDefaultInstance()) {
               return this;
            } else {
               if (!other.getUsername().isEmpty()) {
                  this.username_ = other.username_;
                  this.onChanged();
               }

               if (!other.getPasswd().isEmpty()) {
                  this.passwd_ = other.passwd_;
                  this.onChanged();
               }

               if (!other.getVerificode().isEmpty()) {
                  this.verificode_ = other.verificode_;
                  this.onChanged();
               }

               this.onChanged();
               return this;
            }
         }

         public final boolean isInitialized() {
            return true;
         }

         public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            LoginUser parsedMessage = null;

            try {
               parsedMessage = (LoginUser)LoginMessage.LoginUser.PARSER.parsePartialFrom(input, extensionRegistry);
            } catch (InvalidProtocolBufferException var8) {
               parsedMessage = (LoginUser)var8.getUnfinishedMessage();
               throw var8.unwrapIOException();
            } finally {
               if (parsedMessage != null) {
                  this.mergeFrom(parsedMessage);
               }

            }

            return this;
         }

         public String getUsername() {
            Object ref = this.username_;
            if (!(ref instanceof String)) {
               ByteString bs = (ByteString)ref;
               String s = bs.toStringUtf8();
               this.username_ = s;
               return s;
            } else {
               return (String)ref;
            }
         }

         public ByteString getUsernameBytes() {
            Object ref = this.username_;
            if (ref instanceof String) {
               ByteString b = ByteString.copyFromUtf8((String)ref);
               this.username_ = b;
               return b;
            } else {
               return (ByteString)ref;
            }
         }

         public Builder setUsername(String value) {
            if (value == null) {
               throw new NullPointerException();
            } else {
               this.username_ = value;
               this.onChanged();
               return this;
            }
         }

         public Builder clearUsername() {
            this.username_ = LoginMessage.LoginUser.getDefaultInstance().getUsername();
            this.onChanged();
            return this;
         }

         public Builder setUsernameBytes(ByteString value) {
            if (value == null) {
               throw new NullPointerException();
            } else {
               LoginMessage.LoginUser.checkByteStringIsUtf8(value);
               this.username_ = value;
               this.onChanged();
               return this;
            }
         }

         public String getPasswd() {
            Object ref = this.passwd_;
            if (!(ref instanceof String)) {
               ByteString bs = (ByteString)ref;
               String s = bs.toStringUtf8();
               this.passwd_ = s;
               return s;
            } else {
               return (String)ref;
            }
         }

         public ByteString getPasswdBytes() {
            Object ref = this.passwd_;
            if (ref instanceof String) {
               ByteString b = ByteString.copyFromUtf8((String)ref);
               this.passwd_ = b;
               return b;
            } else {
               return (ByteString)ref;
            }
         }

         public Builder setPasswd(String value) {
            if (value == null) {
               throw new NullPointerException();
            } else {
               this.passwd_ = value;
               this.onChanged();
               return this;
            }
         }

         public Builder clearPasswd() {
            this.passwd_ = LoginMessage.LoginUser.getDefaultInstance().getPasswd();
            this.onChanged();
            return this;
         }

         public Builder setPasswdBytes(ByteString value) {
            if (value == null) {
               throw new NullPointerException();
            } else {
               LoginMessage.LoginUser.checkByteStringIsUtf8(value);
               this.passwd_ = value;
               this.onChanged();
               return this;
            }
         }

         public String getVerificode() {
            Object ref = this.verificode_;
            if (!(ref instanceof String)) {
               ByteString bs = (ByteString)ref;
               String s = bs.toStringUtf8();
               this.verificode_ = s;
               return s;
            } else {
               return (String)ref;
            }
         }

         public ByteString getVerificodeBytes() {
            Object ref = this.verificode_;
            if (ref instanceof String) {
               ByteString b = ByteString.copyFromUtf8((String)ref);
               this.verificode_ = b;
               return b;
            } else {
               return (ByteString)ref;
            }
         }

         public Builder setVerificode(String value) {
            if (value == null) {
               throw new NullPointerException();
            } else {
               this.verificode_ = value;
               this.onChanged();
               return this;
            }
         }

         public Builder clearVerificode() {
            this.verificode_ = LoginMessage.LoginUser.getDefaultInstance().getVerificode();
            this.onChanged();
            return this;
         }

         public Builder setVerificodeBytes(ByteString value) {
            if (value == null) {
               throw new NullPointerException();
            } else {
               LoginMessage.LoginUser.checkByteStringIsUtf8(value);
               this.verificode_ = value;
               this.onChanged();
               return this;
            }
         }

         public final Builder setUnknownFields(final UnknownFieldSet unknownFields) {
            return this;
         }

         public final Builder mergeUnknownFields(final UnknownFieldSet unknownFields) {
            return this;
         }

         // $FF: synthetic method
         Builder(Object x0) {
            this();
         }

         // $FF: synthetic method
         Builder(GeneratedMessageV3.BuilderParent x0, Object x1) {
            this(x0);
         }
      }
   }

   public interface LoginUserOrBuilder extends MessageOrBuilder {
      String getUsername();

      ByteString getUsernameBytes();

      String getPasswd();

      ByteString getPasswdBytes();

      String getVerificode();

      ByteString getVerificodeBytes();
   }
}
