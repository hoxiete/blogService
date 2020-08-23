package com.project.config;




public class regit {
//    public static final String PW_PATTERN = "^(?:(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9]))(?!.*(\\d)\\1{2}).{10,}$";
    public static final String PW_PATTERN = "^(?![A-Za-z0-9]+$)(?![a-z0-9\\W]+$)(?![A-Za-z\\W]+$)(?![A-Z0-9\\W]+$)(?!.*(\\d)\\1{2})[a-zA-Z0-9\\W]{10,}$";
    public static void main(String[] args) {
        // 全部大写，不通过
        String pw1 = "Sada12121#2122";
        // 全部小写，不通过
        String pw2 = "Ssadas#121";
        // 全部数字，不通过
        String pw3 = "Sada1#212111";
        // 全部字符，不通过
        String pw4 = "Sada121212121";
//        String pw5 = "Sada121212122";
        String pw5 = "S#ada121212122";

        System.out.println(pw1.matches(PW_PATTERN));
        System.out.println(pw2.matches(PW_PATTERN));
        System.out.println(pw3.matches(PW_PATTERN));
        System.out.println(pw4.matches(PW_PATTERN));
        System.out.println(pw5.matches(PW_PATTERN));
    }
}
