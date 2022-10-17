package com.coderman.sync.plan.meta;

import ch.qos.logback.classic.layout.TTLLLayout;
import com.coderman.api.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * 同步计划元数据
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PlanMeta extends BaseModel {

    private String uuid;

    private String code;

    private String name;

    private String content;

    private DbMeta dbMeta;

    private ProjectMeta projectMeta;

    private List<TableMeta> tableMetas;

    private List<CallbackMeta> callbackMetas;

    private List<String> tableCodeList = new ArrayList<>();

    private boolean columnConvert;

    public boolean containsCode(String tableCode){

        return this.tableCodeList.contains(tableCode);
    }

    public String getUniqueTypeByCode(String code){

        if(this.getCallbackMetas() !=null && this.getTableMetas().size() >0){

            for (TableMeta tableMeta : this.getTableMetas()) {

                if(tableMeta.getCode().equals(code)){

                    return tableMeta.getUniqueType();
                }

                if(tableMeta.getColumnMetas() !=null && tableMeta.getColumnMetas().size() >0){

                    for (ColumnMeta columnMeta : tableMeta.getColumnMetas()) {

                        if(columnMeta.getSrc().equals(tableMeta.getUnique().getKey()) && columnMeta.getType()!=null){

                            return columnMeta.getType();
                        }
                    }
                }

            }
        }

        return null;
    }

}
