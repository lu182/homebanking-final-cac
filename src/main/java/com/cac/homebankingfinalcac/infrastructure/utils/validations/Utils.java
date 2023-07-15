package com.cac.homebankingfinalcac.infrastructure.utils.validations;

import java.math.BigDecimal;

public class Utils {

    public static boolean isUsernameValid(String value) {
        return value.matches("[a-zA-Z0-9]+"); //acepta una cadena alfanumérica -> p/username
    }

    public static boolean isPassValid(String value) {
        //return value.matches("[a-zA-Z]+"); //solo letras
        return value.matches("^\\d+$"); //acepta una cadena q contiene números -> p/password
    }

    public static boolean isParametersAccountValid(BigDecimal amount){
        //q no sea nulo/q no esté vacío/q no sea negativo
        if (amount != null && !amount.equals("") && amount.compareTo(BigDecimal.ZERO) >= 0
                && amount.scale() >= 0){
            return true;
        }else{
            return false;
        }
    }

    public static boolean isNullOrEmpty(String value1, String value2){
        if ( value1 != null && !value1.isEmpty() && value2 != null && !value2.isEmpty()  ){
            return true;
        }else{
            return false;
        }
    }

}