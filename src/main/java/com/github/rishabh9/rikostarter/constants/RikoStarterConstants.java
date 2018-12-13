package com.github.rishabh9.rikostarter.constants;

import java.util.Set;

public class RikoStarterConstants {
    public static final String GRANT_TYPE = "authorization_code";
    public static final String API_KEY = "PUT_YOUR_API_KEY_HERE";
    public static final String API_SECRET = "PUT_YOUR_API_KEY_SECRET";
    public static final String REDIRECT_URI = "http://localhost:8080/callback";
    public static final String API_CRED = "API_CRED";
    public static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    public static final String TOKEN = "PUT_YOUR_TOKEN_HERE"; // Only obtained during log in
    public static final String TOKEN_TYPE = "Bearer";
    public static final long TOKEN_EXPIRY = 86400000L;

    public static final Set<String> SYMBOLS = Set.of(
            "ADANIENT", "BPCL",
            "EICHERMOT", "ENGINERSIN",
            "GODREJIND", "GRANULES", "GRASIM",
            "HEROMOTOCO", "HEXAWARE",
            "ICICIBANK", "ICICIPRULI",
            "INDIANB", "INDIGO", "INDUSINDBK",
            "JINDALSTEL", "JISLJALEQS",
            "JUSTDIAL", "KAJARIACER", "KOTAKBANK",
            "MOTHERSUMI", "MRF", "MRPL",
            "OIL", "ONGC", "ORIENTBANK",
            "PAGEIND", "PCJEWELLER", "PEL",
            "RAMCOCEM", "RAYMOND", "RBLBANK",
            "RCOM", "RECLTD", "RELCAPITAL",
            "RELIANCE", "RELINFRA", "SREINFRA",
            "SUNPHARMA", "SUNTV", "SUZLON",
            "TATAMOTORS", "TVSMOTOR", "YESBANK"
    );
}
