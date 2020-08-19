package com.temur.myword.model;

import java.util.List;

public class ProverbList {

    private List<Proverb> proverbList;

    public List<Proverb> getProverbList() {
        return proverbList;
    }

    public class Proverb{
        private String proverb;
        private String explanation;
        private String equivalent;
        private String footnote;

        public String getProverb() {
            return proverb;
        }

        public String getExplanation() {
            return explanation;
        }

        public String getEquivalent() {
            return equivalent;
        }

        public String getFootnote() {
            return footnote;
        }
    }

}
