package entity;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 数据库中的<cost>表,用于此项目中的<资费管理>业务
 * 
 * @author Martin
 *
 */
public class Cost implements Serializable {
	private static final long serialVersionUID = 310050620210009279L;
	// 套餐id
	private Integer costId; // number(4) primary key,
	// 套餐名称
	private String name;// varchar(50) not null,
	// 基本时长
	private Integer baseDuration; // number(11),
	// 基本费用
	private Double baseCost; // number(7,2),
	// 单位费用,多少钱每分钟
	private Double unitCost; // number(7,4),
	// 套餐状态(枚举):0-开通,1-暂停
	private String status; // char(1),
	// 资费说明
	private String descr; // varchar2(100),
	// 数据创建时间
	private Timestamp creatime; // date default sysdate ,
	// 套餐开通时间
	private Timestamp startime; // date,
	// 资费类型(枚举):1-包月,2-套餐,3-计时
	private String costType; // char(1)

	public Integer getCostId() {
		return costId;
	}

	public void setCostId(Integer costId) {
		this.costId = costId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getBaseDuration() {
		return baseDuration;
	}

	public void setBaseDuration(Integer baseDuration) {
		this.baseDuration = baseDuration;
	}

	public Double getBaseCost() {
		return baseCost;
	}

	public void setBaseCost(Double baseCost) {
		this.baseCost = baseCost;
	}

	public Double getUnitCost() {
		return unitCost;
	}

	public void setUnitCost(Double unitCost) {
		this.unitCost = unitCost;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public Timestamp getCreatime() {
		return creatime;
	}

	public void setCreatime(Timestamp creatime) {
		this.creatime = creatime;
	}

	public Timestamp getStartime() {
		return startime;
	}

	public void setStartime(Timestamp startime) {
		this.startime = startime;
	}

	public String getCostType() {
		return costType;
	}

	public void setCostType(String costType) {
		this.costType = costType;
	}

	@Override
	public String toString() {
		return "Cost [costId=" + costId + ", name=" + name + ", baseDuration=" + baseDuration + ", baseCost=" + baseCost
				+ ", unitCost=" + unitCost + ", status=" + status + ", descr=" + descr + ", creatime=" + creatime
				+ ", startime=" + startime + ", costType=" + costType + "]";
	}
	
	
}
