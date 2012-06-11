<div class="t">
<form action="" method="post">
收件人：<input type="text" name="orderPerson" value="" />
手机号码/订单号：<input type="text" name="orderTelcode" value="{tostring bean=$orderTelcode}" />
<select name="orderStatus" style="font-size: 12px;">
<option value="">所有状态</option>
<option value="0" {if $orderStatus == '0'}selected{/if}>已下单(未付款)</option>
<option value="1" {if $orderStatus == '1'}selected{/if}>已付款(未发货)</option>
<option value="2" {if $orderStatus == '2'}selected{/if}>已发货(待收货)</option>
<option value="3" {if $orderStatus == '3'}selected{/if}>交易成功</option>
<option value="9" {if $orderStatus == '9'}selected{/if}>交易结束</option>
<option value="19" {if $orderStatus == '19'}selected{/if}>交易关闭</option>
</select>
<input type="submit" value="查询我的订单" style="cursor:pointer;" title="输入收件人和手机号码查询，可以查看完整收件人信息；只输入手机号码或订单号可以查看订单信息，不能查看完整收件人信息" />
<input type="reset" style="cursor:pointer;" onclick="this.form.elements['orderTelcode'].value='';this.form.elements['orderPerson'].value='';this.form.elements['orderStatus'].value='';return false;" value="重置查询条件" />
</form>
</div>
<div class="d">
    <form id="orderform_{$ui.uiid}" action="" onsubmit="return false;">
        <table width=100% class="cart">
            <tr>
                <th>商品名称</th>
                <th>单价(元)</th>
                <th>数量</th>
                <th width="150px" align="center">金额(元)</th>
                <th width="80px" align="right"></th>
            </tr>
            {foreach from=$data.items item="order" key="idx"}
                <tr class="order">
                    <td colspan=2>
                        <table>
                        <tr>
                        <td class="fld">订&nbsp;&nbsp;单&nbsp;&nbsp;号：</td><td>{tostring bean=$order.obj prop="timeID"}</td>
                        <td class="fld">下单时间：</td><td>{tostring bean=$order.obj prop="created" pattern="yyyy-MM-dd HH:mm"}</td>
                        </tr>
                        <tr>
                        <td class="fld">交&nbsp;&nbsp;易&nbsp;&nbsp;号：</td><td>{tostring bean=$order.obj prop="tradeID"}</td>
                        <td class="fld">付款时间：</td><td>{tostring bean=$order.obj prop="payTime" pattern="yyyy-MM-dd HH:mm"}</td>
                        </tr>
                        <tr>
                        <td class="fld">物流单号：</td><td width="300px">{tostring bean=$order.obj prop="logisticsID"}{if $order.obj.logisticsName != null}({tostring bean=$order.obj prop="logisticsName"}){/if}</td>
                        <td class="fld">发货时间：</td><td>{tostring bean=$order.obj prop="logisticsTime" pattern="yyyy-MM-dd HH:mm"}</td>
                        </tr>
                        <tr>
                        <td class="fld">收件地址：</td><td width="300px">
                            {if $orderPerson == null}{tostring bean=$order.obj prop="buyerHideInfo"}{else}{tostring bean=$order.obj prop="buyerInfo"}{/if}
                        </td>
                        <td class="fld">邮政编码：</td><td>{tostring bean=$order.obj prop="postcode"}</td>
                        </tr>
                        </table>
                    </td>
                    <td>
                        {tostring bean=$order.obj prop="itemsAmount"}
                    </td>
                    <td width="150px" align="center" nowrap>
                        <span class="totalprice">{tostring bean=$order.obj prop="totalCost" pattern="#,##0.00"}</span>
                    </td>
                    <td width="60px" align="right">
                        <span class="status">
                            {if $order.obj.status==0}<a class="btn" href="{$contextPath}/ui/{$block.titleLink.id}/{$orderModule.id}:{$order.obj.id}">付款</a>
                            {elseif $order.obj.status==1}已付款
                            {elseif $order.obj.status==2}已发货
                            {elseif $order.obj.status==3}交易成功
                            {elseif $order.obj.status==9}交易结束
                            {elseif $order.obj.status==19}交易关闭
                            {else}$order.obj.status
                            {/if}
                        </span>
                    </td>
                </tr>
                {foreach from=$order.items item="item" key="idx"}
                <tr class="item item_{$idx}">
                    <td class="name">
                        <div class="img">{import file="a.st" href=$item.href target=$item.target title=$item.name img=$item.img}</div>
                        {import file="a.st" href=$item.href target=$item.target title=$item.name name=$item.name}
                        ({tostring bean=$item.obj prop="code"})
                    </td>
                    <td class="price">{tostring bean=$item.obj prop="price" pattern="#,##0.00"}</td>
                    <td class="amount">{tostring bean=$item.obj prop="amount"}</td>
                    <td class="subtotal" width="150px" align="center">{tostring bean=$item.obj prop="subtotal" pattern="#,##0.00"}</td>
                    <td class="delete" width="60px" align="right"></td>
                </tr>
                {/foreach}
            {/foreach}
        </table>
    </form>
</div>
<div class="b"></div>
<script language="javascript">
</script>
