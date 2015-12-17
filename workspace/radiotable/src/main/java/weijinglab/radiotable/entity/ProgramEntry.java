package weijinglab.radiotable.entity;

import java.util.Date;

/**
 * 番組オブジェクト
 * @author HuangWeijing
 * @version 20151217
 */
public class ProgramEntry {

	/** 番組の開始時間 */
	private Date startTime;
	/** 番組の継続時間（秒単位） */
	private Integer duration;
	/** 番組の名前 */
	private String programName;
	/** 司会者の名前 */
	private String caster;
	/** プログラムのリンクアドレス */
	private String programLink;
	/** 司会者のメールアドレス */
	private String casterMail;
	/**
	 * @return 番組の開始時間
	 */
	public Date getStartTime() {
		return startTime;
	}
	/**
	 * @param startTime 番組の開始時間 to set
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	/**
	 * @return 番組の継続時間（秒単位）
	 */
	public Integer getDuration() {
		return duration;
	}
	/**
	 * @param duration 番組の継続時間（秒単位） to set
	 */
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	/**
	 * @return 番組の名前
	 */
	public String getProgramName() {
		return programName;
	}
	/**
	 * @param programName 番組の名前 to set
	 */
	public void setProgramName(String programName) {
		this.programName = programName;
	}
	/**
	 * @return 司会者の名前
	 */
	public String getCaster() {
		return caster;
	}
	/**
	 * @param caster 司会者の名前 to set
	 */
	public void setCaster(String caster) {
		this.caster = caster;
	}
	/**
	 * @return プログラムのリンクアドレス
	 */
	public String getProgramLink() {
		return programLink;
	}
	/**
	 * @param programLink プログラムのリンクアドレス to set
	 */
	public void setProgramLink(String programLink) {
		this.programLink = programLink;
	}
	/**
	 * @return 司会者のメールアドレス
	 */
	public String getCasterMail() {
		return casterMail;
	}
	/**
	 * @param casterMail 司会者のメールアドレス to set
	 */
	public void setCasterMail(String casterMail) {
		this.casterMail = casterMail;
	}
	
	
}
