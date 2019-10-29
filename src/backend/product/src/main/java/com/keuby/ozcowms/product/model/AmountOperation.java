package com.keuby.ozcowms.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AmountOperation {

    /**
     * 操作对象的id， 优先于操作对象
     */
    private Long operatorId;

    /**
     * 操作对象的名字， 含有此字段时会创建操作联系人
     */
    private String operatorName;

    /**
     * 操作备注
     */
    private String remark;

    /**
     * 商品ID
     */
    @Positive(message = "必须大于0")
    private long productId;

    /**
     * 操作数量
     */
    private List<Amount> amounts;

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Amount {

        /**
         * 单位ID
         */
        @Positive(message = "必须大于0")
        @NotNull
        private Long unitId;

        /**
         * 数量
         */
        @PositiveOrZero(message = "必须大于等于0")
        @NotNull
        private Long count;
    }
}
