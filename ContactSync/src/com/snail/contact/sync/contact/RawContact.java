package com.snail.contact.sync.contact;

import android.text.TextUtils;

import java.util.List;

/**
 * Created by fenghb on 3/31/14.
 */
final public class RawContact {
    private static final String TAG = "RawContact";

    private static final boolean DUG = true;
    /**
     * --------------------------------------------------
     */

    private final String cUuid;
    private final String sContactFirstName;
    private final String sContactLastName;
    private final String dLastModifyTime;
    private final List<RawContact.Phone> phones;
    private final List<Group> groups;
    private final List<Email> emails;
    private final String sContactData;
    /**
     * status 1, delete
     */
    private final String status;
    private final String sContactFullName;
    private final String sComment;
    private final String sNickname;
    private final String dBirthday;
    private final List<Address> addresses;
    private final List<Website> websites;

    /**
     * --------------------------------------------------
     */
    public RawContact(String cUuid, String sContactFirstName, String sContactLastName,
                      String dLastModifyTime, List<Phone> phones,
                      List<Group> groups, List<Email> emails, String sContactData,
                      String status, String sContactFullName, String sComment, String sNickname,
                      String dBirthday, List<Address> addresses, List<Website> websites) {
        this.cUuid = cUuid;
        this.sContactFirstName = sContactFirstName;
        this.sContactLastName = sContactLastName;
        this.dLastModifyTime = dLastModifyTime;
        this.phones = phones;
        this.groups = groups;
        this.emails = emails;
        this.sContactData = sContactData;
        this.status = status;
        this.sContactFullName = sContactFullName;
        this.sComment = sComment;
        this.sNickname = sNickname;
        this.dBirthday = dBirthday;
        this.addresses = addresses;
        this.websites = websites;
    }

    /**
     * --------------------------------------------------
     */
    public static RawContact create(String cUuid, String sContactFirstName, String sContactLastName,

                                    String dLastModifyTime, List<Phone> phones, List<Group> groups, List<Email> emails,

                                    String sContactData, String status, String sContactFullName,

                                    String sComment, String sNickname, String getdBirthday,

                                    List<Address> addresses, List<Website> websites) {

        return new RawContact(cUuid, sContactFirstName, sContactLastName, dLastModifyTime, phones, groups,

                emails, sContactData, status, sContactFullName, sComment, sNickname, getdBirthday, addresses, websites);
    }

    public static RawContact createDeletedContact(String cUuid) {

        return new RawContact(cUuid, null, null, null, null, null,

                null, null, "1", null, null, null, null, null, null);

    }

    /**
     * --------------------------------------------------
     */

    public String getcUuid() {
        return cUuid;
    }

    public String getsContactFirstName() {
        return sContactFirstName;
    }

    public String getsContactLastName() {
        return sContactLastName;
    }

    public String getdLastModifyTime() {
        return dLastModifyTime;
    }


    public String getsContactData() {
        return sContactData;
    }

    public String getStatus() {
        return status;
    }

    public String getsContactFullName() {
        return sContactFullName;
    }

    public String getsComment() {
        return sComment;
    }

    public String getsNickname() {
        return sNickname;
    }

    public String getdBirthday() {
        return dBirthday;
    }

    public List<Phone> getPhones() {
        return phones;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public List<Email> getEmails() {
        return emails;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public List<Website> getWebsites() {
        return websites;
    }

    public String getBestName() {
        if (!TextUtils.isEmpty(sContactFullName)) {
            return sContactFullName;
        } else if (TextUtils.isEmpty(sContactFirstName)) {
            return sContactLastName;
        } else {
            return sContactFirstName;
        }
    }



    /**
     * --------------------------------------------------
     */
    public interface Constant {
        String cUuid = "cUuid";

        String sContactFirstName = "sContactFirstName";

        String sContactLastName = "sContactLastName";

        String dLastModifyTime = "dLastModifyTime";

        String phone = "phone";

        String group = "group";

        String email = "email";

        String sContactData = "sContactData";

        String status = "status";

        String sContactFullName = "sContactFullName";

        String sComment = "sComment";

        String sNickname = "sNickname";

        String dBirthday = "dBirthday";

        String address = "address";

        String website = "website";


    }

    /**
     * phone
     */

    public static class Phone {
        private String nPhoneNumber;
        private String sPhoneType;
        private String sPhoneTypeId;

        public Phone(String nPhoneNumber, String sPhoneType, String sPhoneTypeId) {
            this.nPhoneNumber = nPhoneNumber;
            this.sPhoneType = sPhoneType;
            this.sPhoneTypeId = sPhoneTypeId;
        }

        public String getnPhoneNumber() {
            return nPhoneNumber;
        }

        public String getsPhoneType() {
            return sPhoneType;
        }

        public String getsPhoneTypeId() {
            return sPhoneTypeId;
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
     * Email
     */
    public static class Email {
        private String sEmailAddress;
        /**
         * "家里","公司"
         */
        private String sEmailType;

        public Email(String sEmailAddress, String sEmailType) {
            this.sEmailAddress = sEmailAddress;
            this.sEmailType = sEmailType;
        }

        public String getsEmailAddress() {
            return sEmailAddress;
        }

        public String getsEmailType() {
            return sEmailType;
        }
    }

    /**
     * Address
     */
    public static class Address {
        /**
         * post code
         */
        private String SAddressCode;

        /**
         * detail address, such as "苏州市仓颉114号"
         */
        private String SAddress;

        /**
         * "家里", "公司"
         */
        private String SAddressType;

        /**
         * "家里" -> "12", "公司" -> "13"
         */
        private String SAddressTypeId;

        public Address(String SAddressCode, String SAddress, String SAddressType, String SAddressTypeId) {
            this.SAddressCode = SAddressCode;
            this.SAddress = SAddress;
            this.SAddressType = SAddressType;
            this.SAddressTypeId = SAddressTypeId;
        }

        public String getSAddressCode() {
            return SAddressCode;
        }

        public String getSAddress() {
            return SAddress;
        }

        public String getSAddressType() {
            return SAddressType;
        }

        public String getSAddressTypeId() {
            return SAddressTypeId;
        }
    }

    /**
     * WebsiteF
     */
    public static class Website {
        private final String SSite;

        public Website(String SSite) {
            this.SSite = SSite;
        }

        public String getSSite() {
            return SSite;
        }
    }
}
