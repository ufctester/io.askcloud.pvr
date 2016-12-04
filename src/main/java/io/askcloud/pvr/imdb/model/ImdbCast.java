package io.askcloud.pvr.imdb.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;

/**
 * JSON Wrapper class for the response from the API
 *
 * Not intended for use outside of the API
 *
 * @author stuart.boston
 */
public class ImdbCast extends AbstractJsonMapping {

    @JsonProperty("name")
    private ImdbPerson person = new ImdbPerson();
    @JsonProperty("attr")
    private String attr = "";
    @JsonProperty("char")
    private String character = "";
    @JsonProperty("job")
    private String job = "";
    @JsonProperty("as")
    private String roleAs = "";
    @JsonProperty("label")
    private String label = "";

    @JsonProperty("rewrite")
    private List<List<ImdbCast>> rewrite = Collections.emptyList();

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getRoleAs() {
        return roleAs;
    }

    public void setRoleAs(String roleAs) {
        this.roleAs = roleAs;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public ImdbPerson getPerson() {
        return person;
    }

    public void setPerson(ImdbPerson person) {
        this.person = person;
    }

    public String getAttr() {
        return attr;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }

    public List<List<ImdbCast>> getRewrite() {
        return rewrite;
    }

    public void setRewrite(List<List<ImdbCast>> rewrite) {
        this.rewrite = rewrite;
    }
}
