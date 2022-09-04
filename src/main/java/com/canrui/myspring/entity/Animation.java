package com.canrui.myspring.entity;

import com.canrui.myspring.Autowired;
import com.canrui.myspring.Component;
import com.canrui.myspring.Qualifier;
import com.canrui.myspring.Value;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class Animation {
    @Value("10")
    private Integer id;
    @Value("chainsaw_man")
    private String name;
    @Value("100")
    private Integer score;

    //自动装载
    @Autowired
    //@Qualifier("favorite")
    private Charater charater;
}
