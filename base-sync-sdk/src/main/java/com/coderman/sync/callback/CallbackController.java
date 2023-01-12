package com.coderman.sync.callback;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.coderman.api.util.ResultUtil;
import com.coderman.api.vo.ResultVO;
import com.coderman.service.util.SpringContextUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/${domain}/callback")
public class CallbackController {


    @PostMapping(value = "/notify")
    @SuppressWarnings("all")
    public ResultVO<Void> callback(String msg, HttpServletRequest request) throws Exception {

        CallbackScannerService service = SpringContextUtil.getBean(CallbackScannerService.class);

        // 构建消息参数
        SyncMsg syncMsg = this.parseMsg(msg);

        // 执行回调方法
        CallbackMeta callbackMeta = service.getCallbackMeta(syncMsg.getPlanCode());

        if (callbackMeta == null) {

            return ResultUtil.getFail("回调方法未指定,planCode:" + syncMsg.getPlanCode());
        }

        Method method = callbackMeta.getInstantClass().getMethod(callbackMeta.getMethodName(), SyncMsg.class);

        return (ResultVO<Void>) method.invoke(SpringContextUtil.getBean(callbackMeta.getInstantClass()), syncMsg);
    }


    public SyncMsg parseMsg(String msg) {

        JSONObject jsonObject = (JSONObject) JSONObject.parse(msg, Feature.OrderedField);

        List<SyncMsgItem> itemList = new ArrayList<>();

        SyncMsg syncMsg = new SyncMsg();
        syncMsg.setPlanCode(jsonObject.getString("plan"));
        syncMsg.setMsgId(jsonObject.getString("msgId"));
        syncMsg.setMsgItemList(itemList);
        syncMsg.setMsg(msg);

        JSONArray jsonArray = jsonObject.getJSONArray("tables");

        for (int i = 0; i < jsonArray.size(); i++) {

            JSONObject json = jsonArray.getJSONObject(i);

            List<String> uniqueList = new ArrayList<>();

            for (int j = 0; j < ((JSONArray) json.get("unique")).size(); j++) {
                uniqueList.add(((JSONArray) json.get("unique")).getString(j));
            }

            itemList.add(new SyncMsgItem(json.getString("code"), uniqueList));
        }

        return syncMsg;
    }
}
