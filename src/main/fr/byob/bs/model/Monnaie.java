package fr.byob.bs.model;

import java.util.HashMap;
import java.util.Map;


public enum Monnaie {
	AFN(1L, "AFN", "afghani"), ZAR(2L, "ZAR", "rand"), ALL(3L, "ALL", "lek"), DZD(4L, "DZD", "dinar"), EUR(5L, "EUR", "euro"), AON(7L, "AON", "kwanza"), XCD(8L, "XCD", "dollar"), ANG(10L, "ANG",
			"florin"), SAR(11L, "SAR", "ryal"), ARS(12L, "ARS", "peso"), AMD(13L, "AMD", "dram"), AWG(14L, "AWG", "florin"), SHP(15L, "SHP", "livre"), AUD(16L, "AUD", "dollar"), ILS(17L, "ILS",
			"shekel"), AZN(19L, "AZN", "manat"), BSD(20L, "BSD", "dollar"), BHD(21L, "BHD", "dinar"), BDT(22L, "BDT", "taka"), BBD(23L, "BBD", "dollar"), BYR(24L, "BYR", "rouble"), BZD(26L, "BZD",
			"dollar"), XOF(27L, "XOF", "franc CFA"), BMD(28L, "BMD", "dollar"), BTN(29L, "BTN", "ngultrum"), BOB(30L, "BOB", "boliviano"), BAM(31L, "BAM", "mark convertible"), BWP(32L, "BWP", "pula"), BRL(
			33L, "BRL", "réal"), BND(34L, "BND", "dollar"), BGN(35L, "BGN", "lev"), BIF(37L, "BIF", "franc"), KHR(38L, "KHR", "riel"), XAF(39L, "XAF", "franc CFA"), CAD(40L, "CAD", "dollar"), CVE(
			41L, "CVE", "escudo"), KYD(42L, "KYD", "dollar"), CLP(44L, "CLP", "peso"), CNY(45L, "CNY", "yuan"), COP(49L, "COP", "peso"), KMF(50L, "KMF", "franc"), CDF(51L, "CDF", "franc"), NZD(53L,
			"NZD", "dollar"), KPW(54L, "KPW", "won"), KRW(55L, "KRW", "won"), CRC(56L, "CRC", "colon"), HRK(58L, "HRK", "kuna"), CUP(59L, "CUP", "peso"), DKK(60L, "DKK", "couronne"), USD(61L, "USD",
			"dollar"), DJF(62L, "DJF", "franc"), DOP(63L, "DOP", "peso"), EGP(65L, "EGP", "livre"), AED(66L, "AED", "dirham"), ERN(68L, "ERN", "nafka"), EKK(70L, "EKK", "couronne"), ETB(72L, "ETB",
			"birr"), FJD(74L, "FJD", "dollar"), GMD(78L, "GMD", "dalasi"), GEL(79L, "GEL", "lari"), GHS(80L, "GHS", "cedi"), GIP(81L, "GIP", "livre"), GTQ(87L, "GTQ", "quetzal"), GNF(88L, "GNF",
			"franc"), GYD(91L, "GYD", "dollar"), HTG(93L, "HTG", "gourde"), HNL(95L, "HNL", "lempira"), HKD(96L, "HKD", "dollar"), HUF(97L, "HUF", "forint"), INR(98L, "INR", "roupie"), IDR(99L,
			"IDR", "rupiah"), IQD(100L, "IQD", "dinar"), IRR(101L, "IRR", "rial"), ISK(103L, "ISK", "krona"), NIS(104L, "NIS", "shekel"), JMD(106L, "JMD", "dollar"), JPY(107L, "JPY", "yen"), JOD(
			108L, "JOD", "dinar"), KZT(109L, "KZT", "tengue"), KES(110L, "KES", "shiling"), KGS(111L, "KGS", "som"), KWD(113L, "KWD", "dinar"), LAK(114L, "LAK", "kip"), LSL(115L, "LSL", "loti"), LVL(
			116L, "LVL", "lats"), LBP(117L, "LBP", "livre"), LRD(118L, "LRD", "dollar"), LYD(119L, "LYD", "dinar"), CHF(120L, "CHF", "franc"), LTL(121L, "LTL", "litas"), MOP(123L, "MOP", "pataca"), MKD(
			124L, "MKD", "denar"), MGA(125L, "MGA", "ariary"), MYR(126L, "MYR", "ringgit"), MKW(127L, "MKW", "kwacha"), MVR(128L, "MVR", "rufiyaa"), FKP(130L, "FKP", "livre"), MAD(133L, "MAD",
			"dirham"), MUR(136L, "MUR", "roupie"), MRO(137L, "MRO", "ouguiya"), MXN(140L, "MXN", "peso"), MDL(142L, "MDL", "leu"), MNT(144L, "MNT", "tugrik"), MZM(147L, "MZM", "metical"), MKK(148L,
			"MKK", "kyat"), NAD(149L, "NAD", "dollar"), NPR(151L, "NPR", "roupie"), NIO(152L, "NIO", "cordoba"), NGN(154L, "NGN", "naira"), NOK(156L, "NOK", "couronne"), XPF(157L, "XPF", "franc CFP"), OMR(
			159L, "OMR", "rial"), UGX(160L, "UGX", "shiling"), UKS(161L, "UKS", "sum"), PKR(162L, "PKR", "roupie"), PGK(165L, "PGK", "kina"), PYG(166L, "PYG", "guarani"), PEN(168L, "PEN", "nuevo sol"), PHP(
			169L, "PHP", "peso"), PLN(170L, "PLN", "zloty"), QAR(174L, "QAR", "rial"), Ron(176L, "Ron", "leu"), GBP(177L, "GBP", "pound"), RUB(178L, "RUB", "rouble"), RWF(179L, "RWF", "franc"), SBD(
			186L, "SBD", "dollar"), WST(188L, "WST", "tala"), STD(190L, "STD", "dobra"), RSD(192L, "RSD", "dinar"), SCR(193L, "SCR", "roupie"), SLL(194L, "SLL", "leone"), SGD(195L, "SGD", "dollar"), SOS(
			198L, "SOS", "shiling"), SDD(199L, "SDD", "dinar"), LKR(200L, "LKR", "roupie"), SEK(201L, "SEK", "couronne"), SRD(203L, "SRD", "dollar"), SZL(204L, "SZL", "lilangeni"), SYP(205L, "SYP",
			"livre"), TJS(206L, "TJS", "somoni"), TZS(207L, "TZS", "shiling"), TWD(208L, "TWD", "dollar"), CZK(210L, "CZK", "couronne"), THB(211L, "THB", "baht"), TOP(215L, "TOP", "pa’anga"), TTD(
			216L, "TTD", "dollar"), TND(217L, "TND", "dinar"), TMM(218L, "TMM", "manat"), TRY(220L, "TRY", "livre"), UAH(222L, "UAH", "hryvnia"), UYU(223L, "UYU", "peso"), VUV(224L, "VUV", "vatu"), VEB(
			226L, "VEB", "bolivar"), VND(229L, "VND", "dong"), YER(231L, "YER", "riyal"), ZMK(232L, "ZMK", "kwacha"), ZWD(233L, "ZWD", "dollar");

	private Long idMonnaie;

	private String code;

	private String libelle;

	private Monnaie(final Long idMonnaie, final String code, final String libelle) {
		this.idMonnaie = idMonnaie;
		this.code = code;
		this.libelle = libelle;
	}

	public Long getIdMonnaie() {
		return this.idMonnaie;
	}

	/**
	 * Warning : this label is not internationalized
	 * 
	 * @return
	 */
	public String getLibelle() {
		return this.libelle;
	}

	public String getCode() {
		return this.code;
	}

	private final static Map<Long, Monnaie> monnaies = new HashMap<Long, Monnaie>(Monnaie.values().length);
	
	static {
		for (Monnaie monnaie : Monnaie.values()) {
			monnaies.put(monnaie.idMonnaie, monnaie);
		
		}
	}
	
	public static Monnaie get(Long id) {
		return monnaies.get(id);
	}
}
