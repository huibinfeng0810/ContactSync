package com.snail.contact.sync.contact;

import java.util.List;

/**
 * Created by fenghb on 4/3/14.
 */
public class CloudContact {
    private String status;
    private String nUserId;
    private String nImei;
    private String cUuid;
    private String dLastModifyTime;
    private String sContactLastName;
    private String sContactFirstName;
    private String sContactFullName;
    private String sContactMiddleName;
    private String sContactData;
    private List<Phone> phones;
    private String dCreateTime;
    private String sNickname;
    private String sComment;
    private String birthday;
    private String iOperateStatus;
    private List<Email> emails;
    private List<Address> addresses;
    private List<Website> websites;
    private List<Group> groups;

    public List<Group> getGroups() {
        return groups;
    }

    public List<Website> getWebsites() {
        return websites;
    }

    public List<Address> getAddresses() {
        return addresses;
    }


    public String getStatus() {
        return status;
    }

    public String getnUserId() {
        return nUserId;
    }

    public String getnImei() {
        return nImei;
    }

    public String getdLastModifyTime() {
        return dLastModifyTime;
    }

    public String getsContactLastName() {
        return sContactLastName;
    }

    public String getsContactFirstName() {
        return sContactFirstName;
    }

    public String getsContactFullName() {
        return sContactFullName;
    }

    public String getsContactMiddleName() {
        return sContactMiddleName;
    }

    public String getsContactData() {
        return sContactData;
    }


    public String getdCreateTime() {
        return dCreateTime;
    }


    public String getsNickname() {
        return sNickname;
    }

    public String getsComment() {
        return sComment;
    }


    public String getBirthday() {
        return birthday;
    }

    public String getcUuid() {
        return cUuid;
    }

    public String getiOperateStatus() {
        return iOperateStatus;
    }

    public List<Email> getEmails() {
        return emails;
    }

    public List<Phone> getPhones() {
        return phones;
    }

    /**
     * Phone
     */
    public static class Phone {
        private String sPhoneType;
        private String nPhoneNumber;
        private String sPhoneTypeId;

        public Phone(String sPhoneType, String nPhoneNumber, String sPhoneTypeId) {
            this.sPhoneType = sPhoneType;
            this.nPhoneNumber = nPhoneNumber;
            this.sPhoneTypeId = sPhoneTypeId;
        }

        public String getsPhoneType() {
            return sPhoneType;
        }

        public String getnPhoneNumber() {
            return nPhoneNumber;
        }

        public String getsPhoneTypeId() {
            return sPhoneTypeId;
        }
    }

    /**
     * Email
     */
    public static class Email {
        private String sEmailTypeId;
        private String sEmailType;
        private String sEmailAddress;

        public Email(String sEmailTypeId, String sEmailType, String sEmailAddress) {
            this.sEmailTypeId = sEmailTypeId;
            this.sEmailType = sEmailType;
            this.sEmailAddress = sEmailAddress;
        }

        public String getsEmailTypeId() {
            return sEmailTypeId;
        }

        public String getsEmailType() {
            return sEmailType;
        }

        public String getsEmailAddress() {
            return sEmailAddress;
        }
    }

    /**
     * Group
     */
    public static class Group {
        private String sGroupName;

        public Group(String sGroupName) {
            this.sGroupName = sGroupName;
        }

        public String getsGroupName() {
            return sGroupName;
        }
    }

    /**
     * Website
     */
    public static class Website {
        private String sSite;

        public Website(String sSite) {
            this.sSite = sSite;
        }

        public String getsSite() {
            return sSite;
        }
    }

    /**
     * Address
     */
    public static class Address {
        private String sAddress;
        private String sAddressType;
        private String sAddressTypeId;
        private String sAddressCode;

        public Address(String sAddress, String sAddressType, String sAddressTypeId, String sAddressCode) {
            this.sAddress = sAddress;
            this.sAddressType = sAddressType;
            this.sAddressTypeId = sAddressTypeId;
            this.sAddressCode = sAddressCode;
        }

        public String getsAddress() {
            return sAddress;
        }

        public String getsAddressType() {
            return sAddressType;
        }

        public String getsAddressTypeId() {
            return sAddressTypeId;
        }

        public String getsAddressCode() {
            return sAddressCode;
        }
    }


}
