package com.aghosn.craigsapp.utils;

public class ApplicationResolver {

	public static String resolveCategory(String cat){
		switch (cat) {
		case "All Community":
			return "ccc/index.rss";
		case "Activities":
			return "act/index.rss";
		case "Childcare":
			return "kid/index.rss";
		case "General":
			return "com/index.rss";
		case "Groups":
			return "grp/index.rss";
		case "Pets":
			return "pet/index.rss";
		case "Events":
			return "eve/index.rss";
		case "Lost + Found":
			return "laf/index.rss";
		case "Musicians":
			return "muc/index.rss";
		case "Local News":
			return "vnn/index.rss";
		case "Politics":
			return "pol/index.rss";
		case "Rideshare":
			return "rid/index.rss";
		case "Volunteers":
			return "vol/index.rss";
		case "Classes":
			return "cls/index.rss";
		case "All Housing":
			return "hhh/index.rss";
		case "Apts./Housing (by owner)":
			return "abo/index.rss";
		case "Apts./Housing (no broker fee)":
			return "nfb/index.rss";
		case "Apts./Housing (broker fee)":
			return "fee/index.rss";
		case "Apts./Housing (reg. fee)":
			return "aiv/index.rss";
		case "Rooms/Shared":
			return "roo/index.rss";
		case "Sublets/Temporary":
			return "sub/index.rss";
		case "Housing Wanted":
			return "hsw/index.rss";
		case "Housing Swap":
			return "swp/index.rss";
		case "Vacation rentals":
			return "vac/index.rss";
		case "Parking/Storage":
			return "prk/index.rss";
		case "Office/Commercial":
			return "off/index.rss";
		case "Real estate for sale":
			return "rea/index.rss";
		case"All Jobs":
			return "jjj/index.rss";
		case "Accounting + Finance":
			return "acc/index.rss";
		case "Admin/Office":
			return "ofc/index.rss";
		case "Architecture/Engineering":
			return "egr/index.rss";
		case "Art/Media/Design":
			return "med/index.rss";
		case "Biotech/Science":
			return "sci/index.rss";
		case "Business/Management":
			return "bus/index.rss";
		case "Customer Service":
			return "csr/index.rss";
		case "Education":
			return "edu/index.rss";
		case "Food/Bev./Hosp.":
			return "fbh/index.rss";
		case "General Labor":
			return "lab/index.rss";
		case "Government":
			return "gov/index.rss";
		case "HR":
			return "hum/index.rss";
		case "Internet Engineers":
			return "eng/index.rss";
		case "Legal/Paralegal":
			return "lgl/index.rss";
		case "Manufacturing":
			return "mnu/index.rss";
		case "Marketing/PR/Ad.":
			return "mar/index.rss";
		case "Medical/Health":
			return "hea/index.rss";
		case "Nonprofit Sector":
			return "npo/index.rss";
		case "Real Estate":
			return "rej/index.rss";
		case "Retail/Wholesale":
			return "ret/index.rss";
		case "Sales/Bus. Dev.":
			return "sls/index.rss";
		case "Salon/Spa/Fitness":
			return "spa/index.rss";
		case "Security":
			return "sec/index.rss";
		case "Skilled Trade/Craft":
			return "trd/index.rss";
		case "Software/QA/DBA":
			return "sof/index.rss";
		case "Systems/Network":
			return "sad/index.rss";
		case "Technical Support":
			return "tch/index.rss";
		case "Transport":
			return "trp/index.rss";
		case "TV/Film/Video":
			return "tfr/indes.rss";
		case "Web/Info. Design":
			return "web/index.rss";
		case "Writing/Editing":
			return "wri/index.rss";
		case "[ETC]":
			return "etc/index.rss";
		case "[Part-time]":
			return "jjj?is_parttime=1";
		default:
			return "";
		}
	}
	
	public static String resolveLocation(String loc){
		switch (loc) {
		case "queens":
			return "http://newyork.craigslist.org/que/";
		case "manhattan":
			return "http://newyork.craigslist.org/mnh/";
		case "staten":
			return "http://newyork.craigslist.org/stn/";
		case "bronx":
			return "http://newyork.craigslist.org/brx/";
		case "brooklyn":
			return "http://newyork.craigslist.org/brk/";
		default:
			return "";
		}
	}
}
