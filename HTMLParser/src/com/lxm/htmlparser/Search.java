package com.lxm.htmlparser;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.util.Log;

import com.lxm.htmlparser.ReadUrlThread.SearchRet;

public class Search {
	int index;
	String searchNum; // 搜索结果
	String CurrPageUrl; // 当前页
	String NextPageUrl; // 下一页
	int CurrPageNum; // 本页有几个
	int CurrSearchNum; // 本页当前搜索位置
	Document Doc;
	String SearchStr; // 搜索字串
	String Searchpre; // 搜索原始字串
	String baseUrl = "http://www.i7wu.cn/ebook/1/"; // 基地址

	public Search() {
		// TODO Auto-generated constructor stub
		index = 0;
		searchNum = "";
		CurrPageUrl = null;
		NextPageUrl = null;
		CurrSearchNum = 0;
		Doc = null;
		SearchStr = null;
		CurrPageNum = 0;
	}

	public void init(String SearchStr) {
		// TODO Auto-generated constructor stub
		index = 0;
		searchNum = "";
		CurrPageUrl = null;
		NextPageUrl = null;
		CurrSearchNum = 0;
		Doc = null;
		CurrPageNum = 0;
		this.SearchStr = SearchStr;
		Searchpre = SearchStr;

	}

	public SearchRet initSql() {

		SearchRet Ret = new SearchRet();
		try {
			SearchStr = MyUtil.encode(SearchStr, "UTF-8");
			Log.i("xxxxx", SearchStr);
		} catch (Exception e) {
			// TODO: handle exception
			Ret.bCanLoading = true;
			Ret.bIserror = true;

			return Ret;
		}
		CurrPageUrl = baseUrl + SearchStr + "/";
		Doc = null;
		try {

			Doc = null;
			Connection conn = Jsoup.connect(CurrPageUrl).timeout(0);
			Doc = conn.get();

		} catch (Exception e) {
			e.printStackTrace();
			Ret.bCanLoading = true;
			Ret.bIserror = true;

			return Ret;
		} finally {
			if (Doc == null) {
				Ret.bCanLoading = true;
				Ret.bIserror = true;

				return Ret;
			}
			Element Elem2 = Doc.select("div.xstitle").first();
			searchNum = (Elem2.getElementsByTag("b").first().text());
			if (Integer.parseInt(searchNum) == 0) {
				Ret.bIsend = true;
				Ret.bCanLoading = false;
			}
			GetNextPage();
			Ret.al.add("搜索：" + Searchpre + "\n结果：" + searchNum);
			Ret.al.add(searchNum);

		}
		return Ret;
	}

	private void GetNextPage() {
		NextPageUrl = null;
		Element Elem2 = Doc.select("td.pageBar").first();
		if (Elem2 == null) {
			NextPageUrl = null;

		} else {
			Elements Elem3 = Elem2.select("a[href]");
			if (Elem3 != null) {
				for (Element tmp : Elem3) {

					if (tmp.text().matches("下一页")) {
						NextPageUrl = tmp.getElementsByAttribute("href")
								.first().attr("abs:href");
						// /Log.i("lxmlxm", Searchpre +":"+SearchStr);
						NextPageUrl = NextPageUrl.replace(Searchpre, SearchStr);
						// Log.i("rtrtrtr", NextPageUrl);
						break;
					}
				}

			}
		}

	}

	public SearchRet Searching() {
		SearchRet Ret = new SearchRet();
		if (Integer.parseInt(searchNum) == 0 || CurrPageUrl == null) {
			Ret.bIsend = true;
			Ret.bCanLoading = false;
			Log.i("lxm1", "333");
			return Ret;
		}
		try {

			if (CurrSearchNum == 0) {
				Doc = null;
				Connection conn = Jsoup.connect(CurrPageUrl);
				Doc = Jsoup.connect(CurrPageUrl).get();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Ret.bCanLoading = true;
			Ret.bIserror = true;
			return Ret;
		} finally {
			if (Doc == null) {
				Ret.bCanLoading = true;
				Ret.bIserror = true;

				return Ret;
			}
			Elements Elem2 = Doc.select("table.xsinfo");
			CurrPageNum = Elem2.size(); // 当前页获取信息大小
			Log.i("lxm", "" + CurrPageNum + ":" + CurrSearchNum);
			if (CurrSearchNum < CurrPageNum) {
				// 获取到小说名字，下载地址，以及详情介绍
				Element tmp = Elem2.get(CurrSearchNum);
				CurrSearchNum++;
				Element tmp1 = tmp.select("a[href]").first();
				Ret.al.add(tmp1.text()); // 获取到小说名字
				tmp1 = tmp.select("td.xsintroduce").first();
				Ret.al.add(tmp1.text()); // 获取到小说详情
				tmp1 = tmp.select("a[href]").first();
				Ret.al.add(tmp1.attr("abs:href")); // 获取到小说下载地址
				tmp1 = tmp.select("a[href]").get(1);
				Ret.al.add(tmp1.text());// 获取到小说作者
				tmp1 = tmp.select("a[href]").get(2);
				Ret.al.add(tmp1.text());// 获取到小说类别

			} else {
				GetNextPage();
				CurrSearchNum = 0;
				CurrPageUrl = NextPageUrl;
				if (CurrPageUrl == null) {
					Ret.bIsend = true;
					Ret.bCanLoading = false;
					Log.i("lxm1", "222");
					return Ret;
				} else {
					Log.i("lxm1", "5555+" + CurrPageUrl);
					Ret = Searching();
					Log.i("lxm1", "111");
				}
			}

		}
		return Ret;
	}

}
