package com.africastalking.chat;

import java.util.List;
import java.util.Map;

public class InteractiveMessageBody extends MessageBody {

    public InteractiveAction action;
    public InteractiveText body = null;
    public InteractiveText header = null;
    public InteractiveText footer = null;

    public static class InteractiveText {
        public String text;
        public InteractiveText(String text) {
            this.text = text;
        }
    }

    public  interface InteractiveAction {}

    public static class InteractiveList implements InteractiveAction {
        public String button;
        public List<Section> sections;

        public static class Section {
            public String title;
            public List<SectionRow> rows;
            public List<SectionProductItem> product_items;

            public static class SectionRow {
                public String id;
                public String title;
                public String description = null;
            }

            public static class SectionProductItem {
                public String product_retailer_id;
            }
        }
    }

    public static class ReplyButtons implements InteractiveAction {
        public List<ReplyButton> buttons;

        public static class ReplyButton {
            public String id;
            public String title;
        }
    }

    public static class InteractiveProduct implements InteractiveAction {
        public String catalog_id;
        public String product_retailer_id;
    }

    public static class InteractiveProductList implements InteractiveAction {
        public String catalog_id;
        public String product_retailer_id;
        public List<InteractiveList.Section> sections;
    }

    public static class InteractiveFlow implements  InteractiveAction {
        public String flow_message_version;
        public String flow_token;
        public String flow_id;
        public String flow_cta;
        public String flow_action = null;
        public FlowMode mode;
        public FlowActionPayload flow_action_payload = null;

        public enum FlowMode{
            Draft, Published
        }
        public static class FlowActionPayload {
            public String screen;
            public Map<String, String> data = null;
        }
    }

    public InteractiveMessageBody(InteractiveAction action) {
        this.action = action;
    }

    public InteractiveMessageBody(InteractiveAction action, InteractiveText body) {
        this(action);
        this.body = body;
    }

    public InteractiveMessageBody(InteractiveAction action, InteractiveText body, InteractiveText header) {
        this(action, body);
        this.header = header;
    }

    public InteractiveMessageBody(InteractiveAction action, InteractiveText body, InteractiveText header, InteractiveText footer) {
        this(action, body, header);
        this.footer = footer;
    }
}
