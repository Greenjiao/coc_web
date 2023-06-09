package com.greenjiao.coc.common.bean;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @Author Yan
 * @Date 2023/06/09 0:25
 * @Version 1.0
 */
@Data
public class PageParam {
    private static Integer PAGE_NO = 1;
    private static Integer PAGE_SIZE = 10;

    @NotNull(message = "页码不能为空")
    @Min(value = 1, message = "页码最小值为 1")
    private Integer page = PAGE_NO;

    @NotNull(message = "每页条数不能为空")
    @Min(value = 1, message = "每页条数最小值为 1")
    @Max(value = 100, message = "每页条数最大值为 100")
    private Integer limit = PAGE_SIZE;
}
