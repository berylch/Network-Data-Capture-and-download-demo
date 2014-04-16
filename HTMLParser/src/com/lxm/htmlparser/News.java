package com.lxm.htmlparser;

/**
 * 新闻实体类
 * 
 * @author Andy.Chen
 * @mail Chenjunjun.ZJ@gmail.com
 * 
 */
public class News {

	private String title = ""; // 标题
	private String content = ""; // 内容
	private String downloadurl = ""; // 下载解析地址
	private boolean click = false;
	private boolean isshowdownload = true;
	private boolean isdownloading = false;
	private String zuozhe = ""; // 作者
	private String zuopin_leibie = ""; // 作品类别

	public String getTitle() {
		return title;
	}

	public String getzuozhe() {
		return zuozhe;
	}

	public void setzuozhe(String zuozhe_in) {
		zuozhe = zuozhe_in;
	}

	public String getzuopin_leibie() {
		return zuopin_leibie;
	}

	public void setzuopin_leibie(String zuopin_leibie_in) {
		zuopin_leibie = zuopin_leibie_in;
	}

	public void setdownloadurl(String url) {
		downloadurl = url;
	}

	public void setdownloading(boolean is) {
		isdownloading = is;
	}

	public boolean Getdownloading() {
		return isdownloading;
	}

	public String getdownloadurl() {
		return downloadurl;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public boolean getclick() {
		return click;
	}

	public void Setclick(boolean click) {
		this.click = click;
	}

	public void isshowdownload(boolean show) {
		this.isshowdownload = show;
	}

	public boolean showdownload() {
		return this.isshowdownload;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
