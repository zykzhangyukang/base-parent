package com.coderman.sync.plan.meta;

import com.coderman.api.model.BaseModel;
import com.coderman.sync.pair.SyncPair;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class TableMeta extends BaseModel {

    private String code;

    private String src;

    private String dest;

    private String type;

    private SyncPair<String,String> unique;

    private SyncPair<String,String> relate;

    private List<ColumnMeta> columnMetas;

    private String uniqueType;


    public ColumnMeta addColumnMeta(String src,String dest){


        if(CollectionUtils.isEmpty(columnMetas)){

            this.setColumnMetas(new ArrayList<>());
        }

        ColumnMeta columnMeta = new ColumnMeta();
        columnMeta.setDest(dest);
        columnMeta.setSrc(src);

        this.getColumnMetas().add(columnMeta);
        return columnMeta;
    }
}
