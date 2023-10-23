package org.modellwerkstatt.desktopgriddemo;

import java.math.BigDecimal;

public class SomeDto {

    public int posNum;
    public String name;
    public String article;
    public BigDecimal value;

    public SomeDto(int posNum, String name, String article, BigDecimal value) {
        this.posNum = posNum;
        this.name = name;
        this.article = article;
        this.value = value;
    }

}
