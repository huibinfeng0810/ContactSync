package com.snail.contact.sync.sms;

/**
 * Created by fenghb on 2014/4/27.
 */
public class RawSms {

    private final class Sms {
        private long _id;
        private long thread_id;
        private String address;
        private long person;
        private long date;
        private long date_sent;
        private long protocol;
        private int read;
        private int status;
        private int type;
        private long reply_path_present;
        private String subject;
        private String body;
        private String service_center;
        private boolean locked;
        private long error_code;
        private boolean seen;
    }

    private final class Threads {
        private long _id;
        private long date;
        private long message_count;
        private long recipient_ids;
        private String snippet;
        private long snippet_cs;
        private boolean read;
        private long type;
        private long error;
        private String has_attachment;
    }


    private final class Words {
        private long _id;
        private String index_text;
        private long source_id;
        private long talbe_to_use;
    }
}
