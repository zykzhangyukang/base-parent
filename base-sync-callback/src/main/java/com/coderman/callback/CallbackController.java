package com.coderman.callback;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.coderman.api.util.ResultUtil;
import com.coderman.api.vo.ResultVO;
import com.coderman.service.util.SpringContextUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 回调控制器，处理同步消息回调
 * 优化后的代码
 */
@RestController
@RequestMapping(value = "/${domain}/callback")
public class CallbackController {

    private static final Logger logger = LoggerFactory.getLogger(CallbackController.class);

    @PostMapping(value = "/notify")
    @SuppressWarnings("unchecked")
    public ResultVO<Void> callback(String msg, HttpServletRequest request) {
        try {

            // TODO 回调权限校验

            if (StringUtils.equals(msg, "ping")) {
                return ResultUtil.getSuccess();
            }

            // 构建消息参数
            SyncMsg syncMsg = this.parseMsg(msg);

            // 获取回调扫描服务
            CallbackScannerService service = SpringContextUtil.getBean(CallbackScannerService.class);

            // 获取回调元信息
            CallbackMeta callbackMeta = Optional.ofNullable(service.getCallbackMeta(syncMsg.getPlanCode()))
                    .orElseThrow(() -> new IllegalArgumentException("回调方法未指定,planCode:" + syncMsg.getPlanCode()));

            // 获取回调方法
            Method method = callbackMeta.getInstantClass().getMethod(callbackMeta.getMethodName(), SyncMsg.class);

            // 调用回调方法并返回结果
            return (ResultVO<Void>) method.invoke(SpringContextUtil.getBean(callbackMeta.getInstantClass()), syncMsg);

        } catch (NoSuchMethodException e) {
            logger.error("回调方法不存在: ", e);
            return ResultUtil.getFail("回调方法不存在");
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.error("回调方法执行失败: ", e);
            return ResultUtil.getFail("回调方法执行失败");
        } catch (Exception e) {
            logger.error("回调处理异常: ", e);
            return ResultUtil.getFail("回调处理异常");
        }
    }

    /**
     * 解析消息，将JSON字符串转化为SyncMsg对象
     *
     * @param msg 接收到的JSON消息
     * @return SyncMsg对象
     */
    public SyncMsg parseMsg(String msg) {
        JSONObject jsonObject = (JSONObject) JSONObject.parse(msg, Feature.OrderedField);
        SyncMsg syncMsg = new SyncMsg();
        syncMsg.setPlanCode(jsonObject.getString("plan"));
        syncMsg.setMsgId(jsonObject.getString("msgId"));
        syncMsg.setMsg(msg);

        List<SyncMsgItem> itemList = new ArrayList<>();
        JSONArray jsonArray = jsonObject.getJSONArray("tables");

        // 使用forEach和lambda简化循环处理
        jsonArray.forEach(item -> {
            JSONObject json = (JSONObject) item;
            List<String> uniqueList = Optional.ofNullable(json.getJSONArray("unique"))
                    .map(arr -> arr.toJavaList(String.class))
                    .orElse(new ArrayList<>());

            itemList.add(new SyncMsgItem(json.getString("code"), uniqueList));
        });

        syncMsg.setMsgItemList(itemList);
        return syncMsg;
    }
}
