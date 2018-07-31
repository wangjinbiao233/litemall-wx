package org.linlinjava.litemall.db.domain;


public class LitemallFaceFourdata {
    private Integer id;

    private Integer facetype;

    private Integer percentageStart;

    private Integer percentageEnd;

    private String levelDescribe;

    private String skinProblems;

    private String suggest;
    
    private String explainname;
    

    public String getExplainname() {
		return explainname;
	}

	public void setExplainname(String explainname) {
		this.explainname = explainname;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFacetype() {
        return facetype;
    }

    public void setFacetype(Integer facetype) {
        this.facetype = facetype;
    }

    public Integer getPercentageStart() {
        return percentageStart;
    }

    public void setPercentageStart(Integer percentageStart) {
        this.percentageStart = percentageStart;
    }

    public Integer getPercentageEnd() {
        return percentageEnd;
    }

    public void setPercentageEnd(Integer percentageEnd) {
        this.percentageEnd = percentageEnd;
    }

    public String getLevelDescribe() {
        return levelDescribe;
    }

    public void setLevelDescribe(String levelDescribe) {
        this.levelDescribe = levelDescribe == null ? null : levelDescribe.trim();
    }

    public String getSkinProblems() {
        return skinProblems;
    }

    public void setSkinProblems(String skinProblems) {
        this.skinProblems = skinProblems == null ? null : skinProblems.trim();
    }

    public String getSuggest() {
        return suggest;
    }

    public void setSuggest(String suggest) {
        this.suggest = suggest == null ? null : suggest.trim();
    }
}