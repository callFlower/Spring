package com.canrui.myspring.entity;

import com.canrui.myspring.Component;
import com.canrui.myspring.Value;
import lombok.Data;

@Data
//"favorite"
@Component()
public class Charater {
    @Value("1")
    private String peopleId;
    @Value("2.3")
    private Float presence;
}
