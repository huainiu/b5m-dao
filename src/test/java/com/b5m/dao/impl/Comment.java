package com.b5m.dao.impl;

import java.util.Date;

import com.b5m.dao.annotation.Column;
import com.b5m.dao.annotation.Id;
import com.b5m.dao.annotation.NamedQueries;
import com.b5m.dao.annotation.NamedQuery;
import com.b5m.dao.annotation.Table;
import com.b5m.dao.domain.ColType;

@Table("comments")
@NamedQueries({@NamedQuery(name = "querySql", sql = "select createTime from comments where id = ?"), @NamedQuery(name = "querySql1", sql = "select createTime from comments")})
public class Comment {
	@Id
	private Long id;
	@Column
	private String user;
	//0-好评  1-一般 2-很差
	@Column(unsigned = false)
	private int type;
	
	@Column(name = "suppliser_id")
	private Long supplierId;

	// 0--不通过, 1--通过
	@Column(unsigned = false)
	private int oper;
	
	@Column(type = ColType.DATETIME)
	private Date createTime;
	
	@Column(type = ColType.DATETIME)
	private Date updateTime;
	
	@Column(type = ColType.TEXT)
	private String content;
	
	@Column
	private String avatar;
	
	@Column(name = "bak_int_1")
	private int bakInt1;
	
	@Column(name = "bak_int_2")
	private int bakInt2;
	
	@Column(name = "bak_int_3")
	private int bakInt3;
	
	@Column(name = "bak_int_4")
	private int bakInt4;
	
	@Column(name = "bak_str_1")
	private String bakStr1;
	
	@Column(name = "bak_str_2")
	private String bakStr2;
	
	@Column(name = "bak_str_3")
	private String bakStr3;
	
	@Column(name = "bak_str_4")
	private String bakStr4;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getOper() {
		return oper;
	}

	public void setOper(int oper) {
		this.oper = oper;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public int getBakInt1() {
		return bakInt1;
	}

	public void setBakInt1(int bakInt1) {
		this.bakInt1 = bakInt1;
	}

	public int getBakInt2() {
		return bakInt2;
	}

	public void setBakInt2(int bakInt2) {
		this.bakInt2 = bakInt2;
	}

	public int getBakInt3() {
		return bakInt3;
	}

	public void setBakInt3(int bakInt3) {
		this.bakInt3 = bakInt3;
	}

	public int getBakInt4() {
		return bakInt4;
	}

	public void setBakInt4(int bakInt4) {
		this.bakInt4 = bakInt4;
	}

	public String getBakStr1() {
		return bakStr1;
	}

	public void setBakStr1(String bakStr1) {
		this.bakStr1 = bakStr1;
	}

	public String getBakStr2() {
		return bakStr2;
	}

	public void setBakStr2(String bakStr2) {
		this.bakStr2 = bakStr2;
	}

	public String getBakStr3() {
		return bakStr3;
	}

	public void setBakStr3(String bakStr3) {
		this.bakStr3 = bakStr3;
	}

	public String getBakStr4() {
		return bakStr4;
	}

	public void setBakStr4(String bakStr4) {
		this.bakStr4 = bakStr4;
	}

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public String toString() {
		return "Comment [id=" + id + ", user=" + user + ", type=" + type
				+ ", supplierId=" + supplierId + ", oper=" + oper
				+ ", createTime=" + createTime + ", updateTime=" + updateTime
				+ ", content=" + content + ", avatar=" + avatar + ", bakInt1="
				+ bakInt1 + ", bakInt2=" + bakInt2 + ", bakInt3=" + bakInt3
				+ ", bakInt4=" + bakInt4 + ", bakStr1=" + bakStr1
				+ ", bakStr2=" + bakStr2 + ", bakStr3=" + bakStr3
				+ ", bakStr4=" + bakStr4 + "]";
	}
}
