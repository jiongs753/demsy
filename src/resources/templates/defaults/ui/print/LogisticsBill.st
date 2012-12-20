<!DOCTYPE style PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script src="{$scriptPath}/jquery.js" type="text/javascript"></script>
<script src="{$scriptPath}/demsy.utils.js" type="text/javascript"></script>
<script language="javascript" src="/scripts2/plugins/LodopFuncs.js"></script>
<style>
#body {
    _display: inline-block;
    margin: 0 auto;
    position: relative;
    text-align: left;
    width: 1000px; /*20.4cm/2.54*96px*/
    font-size:12px;
}
.printStatus{
	color: red;
}
</style>
</head>
<body>
<div id="body">
    <div style="padding: 5px;">
            物流公司：<select class="logisticsName">
            <option value="">--物流公司--</option>
            <option value="EMS">EMS</option>
            <option value="顺丰快递">顺丰快递</option>
            <option value="申通E物流">申通E物流</option>
            <option value="圆通速递">圆通速递</option>
            <option value="中通速递">中通速递</option>
            <option value="宅急送">宅急送</option>
            <option value="韵达快运">韵达快运</option>
            <option value="天天快递">天天快递</option>
            <option value="联邦快递">联邦快递</option>
            <option value="汇通快运">汇通快运</option>
            <option value="天天快递">天天快递</option>
            <option value="华强物流">华强物流</option>
            <option value="快捷快递">快捷快递</option>
            <option value="其他">其它</option>
            </select>
            起始快递单号：<input class="startExpressNo" type="input" style="font-size: 18px;" value="">
    </div>
    <div id="orderslist">
    <table width="100%" cellpadding="0" cellspacing="0" border=0 class="orderslist" style="font-size: 12px;border: 1px solid #000000;BORDER-COLLAPSE: collapse;">
	    <tr>
            <th style="display: none;"></th>
		    <th style="border: 1px solid gray; padding:2px;height: 25px;text-align:center;">订单号</th>
		    <th style="border: 1px solid gray; padding:2px;text-align:center;">物流单号</th>
		    <th style="border: 1px solid gray; padding:2px;text-align:center;">收件人</th>
		    <th style="border: 1px solid gray; padding:2px;text-align:center;">商品清单【名称、编码、数量】</th>
            <th class="noprint" style="border: 1px solid gray; padding:2px;text-align:center;">打印状态</th>
            <th class="noprint" style="border: 1px solid gray; padding:2px;text-align:center;">操作</th>
	    </tr>
    {foreach from=$data item="order"}
        {if $startExpressNo == null}{assign var="startExpressNo" value=$order.deliver.logisticsNo}{/if}
        {if $logisticsName == null}{assign var="logisticsName" value=$order.deliver.logisticsName}{/if}
        <tr class="bill">
            <td style="display: none;">
                <div class="s_user">{tostring bean=$order.deliver prop="personName" pattern="*"}</div>
                <div class="s_tel">{tostring bean=$order.deliver prop="personTel" pattern="*"}</div>
                <div class="s_corp">{tostring bean=$order.deliver prop="corpName"}</div>
                <div class="s_addr">{tostring bean=$order.deliver prop="address"}</div>
                <div class="s_postcode">{tostring bean=$order.deliver prop="postcode"}</div>
                <div class="s_date">{tostring bean=$order prop="currentDate" pattern="yyyy-MM-dd HH:mm"}</div>
                <div class="s_list">
                            {foreach from=$order.items item="item" key="idx"}
                                {tostring bean=$item prop="name"}[{tostring bean=$item prop="amount"}]
                            {/foreach}
                </div>
                <input type="hidden" class="express_id" value='{tostring bean=$order prop="id"}' />
                <input type="hidden" class="order_no" name="orderID" value="{$order.orderID}" readonly="true" />
                <input type="hidden" class="express_no" value='{tostring bean=$order prop="code"}'/>
            </td>
        	<td style="border: 1px solid gray; padding:5px; width: 130px; white-space:nowrap;font-size: 14px;"  valign="top">
                {tostring bean=$order prop="orderID"}
            </td>
            <td class="express_no_label" style="border: 1px solid gray; padding:5px; width: 105px; white-space:nowrap;font-size: 14px;"  valign="top">
                {tostring bean=$order prop="code"}
            </td>
		    <td style="border: 1px solid gray; padding:5px;width:300px;"  valign="top">
                <table width="300px" style="font-size: 12px;">
                    <tr><td class="r_addr" style="padding:2px;width:300px;" colspan=4>{tostring bean=$order prop="address" pattern="*"}</td></tr>
                    <tr>
                        <td class="r_user" style="padding:2px;width:60px;">{tostring bean=$order prop="name" pattern="*"}</td>
                        <td class="r_tel" style="padding:2px;width:80px;">{tostring bean=$order prop="telcode" pattern="*"}</td>
                        <td class="r_postcode" style="padding:2px;width:60px;">{tostring bean=$order prop="postcode"}</td>
                        <td>&nbsp;</td>
                    </tr>
                </table>
            </td>
		    <td style="border: 1px solid gray; padding:5px;width:310px;" valign="top">
                <table width='100%' class="itemslist" border="0" cellpadding="0" cellspacing="0"  style="font-size: 12px;width:310px;">{foreach from=$order.items item="item" key="idx"}
                    <tr>
		            	<td style="padding: 2px 5px 2px 5px;width:210px;" valign="top">{tostring bean=$item prop="name"}</td>
                        <td style="padding: 2px 5px 2px 5px;width:80px;" valign="top">{tostring bean=$item prop="code"}</td>
		                <td style="padding: 2px 5px 2px 5px;width:10px; font-size: 14px;" valign="top" align="right">{tostring bean=$item prop="amount"}</td>
		            </tr>{/foreach}
                </table>
		    </td>
            <td class="printStatus noprint" style="border: 1px solid gray; padding:5px;width:120px;" valign="top">
                {if $order.printNum > 0}已打印【{$order.printNum}】次{/if}
            </td>
            <td class="op noprint" style="border: 1px solid gray; padding:5px 1px; width:20px; font-size: 12px;" valign="top">
                <input class="btnSendGoods" style="font-size: 12px;" type="submit" value="发货" />
            </td>
        </tr>
        {/foreach}
    </table>
    </div>
    <div id="readyGoodsBills" style="display: none;">
    </div>
    <div id="sendGoodsBills" style="display: block;">{foreach from=$data item="order"}
    	<div class="logisticsBill" style="margin-top:10px;">
	    <table width="100%" cellpadding="0" cellspacing="0" border="0" style="font-size: 12px;border: 1px solid #000000;BORDER-COLLAPSE: collapse;text-align:left;">
	    	<tr>
		    	<td rowspan=6 style="padding: 5px;"><img src='{thumbs src=$order.deliver.billImage width=320 height=180}' border=0 /></td>
		    	<th style="padding:2px;height: 25px;">订单号：</th>
		    	<td colspan=4>{$order.orderID}</td>
	    	</tr>
	    	<tr>
		    	<th style="padding:2px;height: 25px;">物流单号：</th>
		    	<td colspan=4 class="express_no_label">{tostring bean=$order prop="code"}</td>
	    	</tr>
	    	<tr>
		    	<th style="padding:2px;height: 25px;">收货人姓名：</th>
		    	<td colspan=4>{$order.name}</td>
	    	</tr>
	    	<tr>
		    	<th style="padding:2px;height: 25px;">收货人电话：</th>
		    	<td colspan=4>{$order.telcode}</td>
	    	</tr>
	    	<tr>
		    	<th style="padding:2px;height: 25px;">收货人邮编：</th>
		    	<td colspan=4>{$order.postcode}</td>
	    	</tr>
	    	<tr>
		    	<th style="padding:2px;height: 25px;">收货人地址：</th>
		    	<td colspan=4>{$order.address}</td>
	    	</tr>
	    	<tr>
		    	<td style="padding:2px; border-bottom: 1px solid #000000;height: 25px;" colspan=6></td>
	    	</tr>
		    <tr>
			    <th style="padding:2px 5px 2px 5px; border-bottom: 1px solid #000000; height: 25px; text-align: left; width: 340px;">名称</th>
			    <th style="padding:2px 5px 2px 5px; border-bottom: 1px solid #000000; text-align: left; width: 100px;">编码</th>
                <th style="padding:2px 5px 2px 5px; border-bottom: 1px solid #000000;">&nbsp;</th>
			    <th style="padding:2px 5px 2px 5px; border-bottom: 1px solid #000000; text-align: right; width: 60px;" align="right">单价(元)</th>
			    <th style="padding:2px 5px 2px 5px; border-bottom: 1px solid #000000; text-align: right; width: 60px;" align="right">数量</th>
			    <th style="padding:2px 5px 2px 5px; border-bottom: 1px solid #000000; text-align: right; width: 60px;" align="right">金额(元)</th>
		    </tr>{foreach from=$order.items item="item" key="idx"}
	        <tr>
				<td style="padding: 2px 5px 2px 5px; height:25px; text-align: left; width: 340px;">{tostring bean=$item prop="name"}</td>
				<td style="padding: 2px 5px 2px 5px; font-size: 14px; text-align: left; width: 100px;white-space: nowrap;">{tostring bean=$item prop="code"}</td>
                <td style="padding: 2px 5px 2px 5px; font-size: 14px;">&nbsp;</td>
				<td style="padding: 2px 5px 2px 5px; font-size: 14px; text-align: right; width: 60px;" align="right">{tostring bean=$item prop="price" pattern="#,##0.00"}</td>
				<td style="padding: 2px 5px 2px 5px; font-size: 14px; text-align: right; width: 60px;" align="right">{tostring bean=$item prop="amount"}</td>
				<td style="padding: 2px 5px 2px 5px; font-size: 14px; text-align: right; width: 60px;" align="right">{tostring bean=$item prop="subtotal" pattern="#,##0.00"}</td>
			</tr>{/foreach}
	    	<tr>
		    	<td style="padding:2px;height: 25px;border-bottom: 1px solid #000000;" colspan=6></td>
	    	</tr>
	        <tr>
				<th style="padding: 2px 5px 2px 5px; height:25px;">合计：</th>
                <td style="padding: 2px 5px 2px 5px; font-size: 14px; text-align: left; width: 100px;">&nbsp;</td>
                <td style="padding: 2px 5px 2px 5px; font-size: 14px;">&nbsp;</td>
                <td style="padding: 2px 5px 2px 5px; font-size: 14px; text-align: right; width: 60px;" align="right">&nbsp;</td>
				<td style="padding: 2px 5px 2px 5px; font-size: 14px; text-align: right; width: 60px;" align="right">{tostring bean=$order prop="itemsAmount"}</td>
				<td style="padding: 2px 5px 2px 5px; font-size: 14px; text-align: right; width: 60px;" align="right">{tostring bean=$order prop="totalCost" pattern="#,##0.00"}</td>
			</tr>
            <tr>
                <th style="padding: 2px 5px 2px 5px; border-top: 1px solid #000000; height:25px;" colspan=6>发货说明</th>
            </tr>
	    	<tr>
		    	<td style="padding:2px; height:25px;border-top: 1px solid #000000;text-align: left;" colspan=6>
                {tohtml bean=$order prop="note2"}
                </td>
	    	</tr>
	    </table></div>{/foreach}
    </div>
    <div style="text-align: center; height: 30px; padding: 10px 0 10px 0; border-top: 1px solid gray; width:100%;margin-top: 10px;">
            <input type="submit" value="浏览快递单" class="viewExpressBills" />
            <input type="submit" value="打印快递单" class="printExpressBills" />
            <input type="submit" value="打印发货单" class="viewSendGoodsBills" />
            <!-- 
            <input type="submit" value="打印备货单" class="viewReadyGoodsBills" />
            <input type="submit" value="维护快递单" class="manageExpressBill" />
            <input type="submit" value="设计快递单" class="designExpressBill" />
             -->
        </div>
    <object id="LODOP" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0>
        <embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 pluginspage="/scripts2/plugins/install_lodop.exe">
        </embed>
    </object>
</div>

<script type="text/javascript">
$(function(){ 
    var evalExpressNo=function(){
        var nextExpressNo = $(".startExpressNo").val();
        var start="";
        var end="";
        var numstr="";
        for(var i = nextExpressNo.length-1; i >=0 ; i--){
            var c = nextExpressNo.substring(i, i+1);
            if(parseInt(c)>=0){
            	numstr = c + numstr;
            }else{
                if(numstr.length>0)
                    break;
                
                end = c + end;
            }
        }
        while(numstr.length>0){
            var c = numstr.substring(0, 1);
            if(parseInt(c)==0){
                numstr = numstr.substring(1);
            }else{
                break;
            }
        }
        var num = parseInt(numstr);
        var numlen = numstr.length;
        var len=nextExpressNo.length;
        //物流单
        var sendGoodsBills = $(".express_no_label",$("#sendGoodsBills"));
        //备货单
        $(".express_no").each(function(i){
            if(nextExpressNo != null && nextExpressNo.length > 0 && ""+nextExpressNo != "NaN"){
                var $this=$(this);
                $this.val(nextExpressNo);
                $(".express_no_label",$this.parent().parent()).html(nextExpressNo);
                $(sendGoodsBills.get(i)).html(nextExpressNo);
                
                num = num + 1;
                numstr=num+end;
                nextExpressNo=nextExpressNo.substring(0,len-numstr.length)+numstr; 
            }
        });
    }
    $(".startExpressNo").val("{tostring bean=$startExpressNo}");
    $(".logisticsName").val("{tostring bean=$logisticsName}");
    evalExpressNo();
    $(".startExpressNo").bind("keyup",function(){
    	evalExpressNo();
    })
    var LODOP=null;
    var nextToken="{$nextToken}";
    
    /**
     * 打印快递单 
     */
    var createExpressItem = function($this,isPrint){
        LODOP.ADD_PRINT_TEXT(57,251,153,20,$(".s_date",$this).html());
        LODOP.SET_PRINT_STYLEA(0,"FontSize",13);
        LODOP.SET_PRINT_STYLEA(0,"FontColor","#0000FF");
        LODOP.ADD_PRINT_TEXT(85,119,150,20,$(".s_user",$this).html());
        LODOP.SET_PRINT_STYLEA(0,"FontSize",12);
        LODOP.SET_PRINT_STYLEA(0,"FontColor","#0000FF");
        LODOP.ADD_PRINT_TEXT(83,276,125,20,$(".s_tel",$this).html());
        LODOP.SET_PRINT_STYLEA(0,"FontSize",14);
        LODOP.SET_PRINT_STYLEA(0,"FontColor","#0000FF");
        LODOP.ADD_PRINT_TEXT(112,178,218,20,$(".s_corp",$this).html());
        LODOP.ADD_PRINT_TEXT(142,120,270,65,$(".s_addr",$this).html());
        LODOP.SET_PRINT_STYLEA(0,"FontSize",12);
        LODOP.SET_PRINT_STYLEA(0,"FontColor","#0000FF");
        LODOP.ADD_PRINT_TEXT(208,315,100,20,$(".s_postcode",$this).html());
        LODOP.SET_PRINT_STYLEA(0,"FontName","黑体");
        LODOP.SET_PRINT_STYLEA(0,"FontSize",16);
        LODOP.SET_PRINT_STYLEA(0,"LetterSpacing",4.5);
        LODOP.SET_PRINT_STYLEA(0,"FontColor","#0000FF");
        LODOP.SET_PRINT_STYLEA(0,"Italic",1);
        //LODOP.ADD_PRINT_TEXT(270,55,353,56,$(".s_list",$this).html());
        //LODOP.SET_PRINT_STYLEA(0,"FontColor","#0000FF");
        LODOP.ADD_PRINT_TEXT(85,483,100,20,$(".r_user",$this).html());
        LODOP.SET_PRINT_STYLEA(0,"FontSize",12);
        LODOP.SET_PRINT_STYLEA(0,"FontColor","#0000FF");
        LODOP.ADD_PRINT_TEXT(83,650,125,20,$(".r_tel",$this).html());
        LODOP.SET_PRINT_STYLEA(0,"FontSize",14);
        LODOP.SET_PRINT_STYLEA(0,"FontColor","#0000FF");
        LODOP.ADD_PRINT_TEXT(142,497,270,65,$(".r_addr",$this).html());
        LODOP.SET_PRINT_STYLEA(0,"FontSize",12);
        LODOP.SET_PRINT_STYLEA(0,"FontColor","#0000FF");
        LODOP.ADD_PRINT_TEXT(208,681,100,20,$(".r_postcode",$this).html());
        LODOP.SET_PRINT_STYLEA(0,"FontName","黑体");
        LODOP.SET_PRINT_STYLEA(0,"FontColor","#0000FF");
        LODOP.SET_PRINT_STYLEA(0,"LetterSpacing",4.5);
        LODOP.SET_PRINT_STYLEA(0,"FontSize",16);
        LODOP.SET_PRINT_STYLEA(0,"Italic",1);

        if(!isPrint){
            LODOP.ADD_PRINT_TEXT(45,499,235,25,$(".express_no",$this).val());
            LODOP.SET_PRINT_STYLEA(0,"FontName","黑体");
            LODOP.SET_PRINT_STYLEA(0,"FontSize",22);
            LODOP.SET_PRINT_STYLEA(0,"FontColor","#FF0000");
            LODOP.SET_PRINT_STYLEA(0,"LetterSpacing",4);
            LODOP.ADD_PRINT_TEXT(387,145,220,25,$(".express_no",$this).val());
            LODOP.SET_PRINT_STYLEA(0,"FontName","黑体");
            LODOP.SET_PRINT_STYLEA(0,"FontSize",20);
            LODOP.SET_PRINT_STYLEA(0,"FontColor","#FF0000");
            LODOP.SET_PRINT_STYLEA(0,"LetterSpacing",2);
        }
    }

    var createExpressPage = function(orderNo){
        LODOP=getLodop(document.getElementById('LODOP'),document.getElementById('LODOP_EM'));
        LODOP.PRINT_INIT("快递单("+orderNo+")");
        LODOP.SET_PRINT_STYLE("FontSize", 10);
        LODOP.SET_PRINT_STYLE("Bold", 0);
		LODOP.SET_PRINT_STYLE("FontColor","#0000FF");
        //LODOP.PRINT_INITA(0,0,771,480,"快递单("+orderNo+")");
        LODOP.SET_PRINT_PAGESIZE(1,2400,1330,"");
        //LODOP.ADD_PRINT_SETUP_BKIMG("<img src='/themes2/images/express/EMS.jpg'>");		
        //LODOP.SET_SHOW_MODE("BKIMG_WIDTH","240mm");
		//LODOP.SET_SHOW_MODE("BKIMG_HEIGHT","133mm");
        //LODOP.SET_SHOW_MODE("BKIMG_IN_PREVIEW",true);
        //LODOP.SET_SHOW_MODE("PREVIEW_IN_BROWSE",1);
        //LODOP.SET_SHOW_MODE("HIDE_PAPER_BOARD",1);
        LODOP.SET_PREVIEW_WINDOW(2,0,1,1000,660,"快递单("+orderNo+").开始打印");
        LODOP.SET_PRINT_MODE("AUTO_CLOSE_PREWINDOW",1);
		LODOP.SET_SHOW_MODE("HIDE_PBUTTIN_PREVIEW",1);
		//LODOP.SET_SHOW_MODE("HIDE_SBUTTIN_PREVIEW",1);
		//LODOP.SET_SHOW_MODE("HIDE_QBUTTIN_PREVIEW",1);
    }
    $(".btnSendGoods").click(function(){
        processSendGoods($(this).parent().parent());
    });
    var processSendGoods=function($this){
        if(!$this){
            return;
        }
    	var express_no=$(".express_no",$this).val(); 
		var $status=$(".printStatus",$this);
		var url="{$submitUrl}"+$(".express_id",$this).val()+"/"+nextToken;
		var data="data.code="+express_no+"&data.logisticsName="+$(".logisticsName").val();
        $.post(url, data, function(json) {
            $status.html(json.message);
            if(json.success){
            	var btn=$(".btnSendGoods", $this);
                btn.attr("disabled", true);
				clearTimeout(btn.attr("t"));
            	nextToken=json.nextToken;
            }
        }, "json");
    }
	var waitPrint=function(P, P_ID,$this){
		var t=setInterval(function(){
			var ok= P.GET_VALUE("PRINT_STATUS_OK",P_ID); 
			var pool= P.GET_VALUE("PRINT_STATUS_EXIST",P_ID); 
			var code= P.GET_VALUE("PRINT_STATUS_ID",P_ID); 
			var seconds= P.GET_VALUE("PRINT_STATUS_SECONDS",P_ID); 
			var busy= P.GET_VALUE("PRINT_STATUS_BUSY",P_ID); 
			var msg="(CODE="+code+",POOL="+pool+",busy="+busy+",seconds="+seconds+")";
			var $status=$(".printStatus",$this);
	        if(ok){
				clearTimeout(t);
	    		$status.html("打印成功");
	    		processSendGoods($this);
	    	}else{
	    		$status.html(P.GET_VALUE("PRINT_STATUS_TEXT",P_ID)+msg);
	    	}
		},1000);
		$(".btnSendGoods",$this).attr("t",t);
	}
    $(".viewExpressBills").click(function(){
    	createExpressPage("");
        var $bill=$(".bill");
        var pages=$bill.length;
        $bill.each(function(idx){
            createExpressItem($(this),false);
            if(idx+1 < pages){
              LODOP.NewPage();
            }
        });
    	LODOP.PREVIEW(); 
    });
    $(".printExpressBills").click(function(){
    	$(".bill").each(function(idx){
        	var $this=$(this);
        	var express_no=$(".express_no",$this).val(); 
    		var $status=$(".printStatus",$this);
        	if(express_no!=null&&express_no.length>0){
	            createExpressPage($(".order_no",$this).val());
	            createExpressItem($this,true);
	            LODOP.SET_PRINT_MODE("CATCH_PRINT_STATUS",true);
	            waitPrint(LODOP,LODOP.PRINT(),$this);
        	}else{
        		$status.html("未知快递单号！打印失败！");
        	}
        });
    });
    $(".manageExpressBill").click(function(){
        createExpressPage("");
        createExpressItem($(".bill"),false);
        LODOP.PRINT_SETUP(); 
    });
    $(".designExpressBill").click(function(){
        createExpressPage("");
        createExpressItem($(".bill"),false);
    	LODOP.PRINT_DESIGN(); 
    });

    /*
     * 打印备货单
     */
    var createReadyGoodsBillsPage = function(orderNo){
        LODOP=getLodop(document.getElementById('LODOP'),document.getElementById('LODOP_EM'));
        LODOP.PRINT_INIT("备货单");
        LODOP.SET_PRINT_STYLE("FontSize", 10);
        LODOP.SET_PRINT_STYLE("Bold", 0);
        var $table=$("#readyGoodsBills");
        $table.html($("#orderslist").html());
        $(".noprint",$table).remove();
        LODOP.ADD_PRINT_TABLE("2%","1%","96%","98%",$table.html());
    }
	$(".viewReadyGoodsBills").click(function(){
		createReadyGoodsBillsPage();
    	LODOP.PREVIEW(); 
	});
	$(".printReadyGoodsBills").click(function(){
		createReadyGoodsBillsPage();
    	LODOP.PRINT(); 
	});
	
    /*
     * 打印物流单
     */
    var createSendGoodsBillsPage = function(orderNo){
        LODOP=getLodop(document.getElementById('LODOP'),document.getElementById('LODOP_EM'));
        LODOP.PRINT_INIT("物流单");
        LODOP.SET_PRINT_STYLE("FontSize", 10);
        LODOP.SET_PRINT_STYLE("Bold", 0);
        var $bills=$(".logisticsBill",$("#sendGoodsBills"));
        var pages=$bills.length;
        $bills.each(function(idx){
            LODOP.ADD_PRINT_TABLE("2%","1%","96%","98%",$(this).html());
            if(idx+1 < pages){
              LODOP.NewPage();
            }
        });
    }
	$(".viewSendGoodsBills").click(function(){
		createSendGoodsBillsPage();
    	LODOP.PREVIEW(); 
	});
	$(".printSendGoodsBills").click(function(){
		createSendGoodsBillsPage();
    	LODOP.PRINT(); 
	});
});
</script>

</body>
</html>
